package Tracker;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;


import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import Utils.Time;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class IOHandler {

	public static boolean canWrite = true;
	public static Object sync = new Object();
	private InputHandler ih;
	private BufferedWriter bw;
	private BufferedReader br;
	private File playerDir;
	private File logDir;
	private File root;
	private File resources;
	private File championData;
	private File itemData;
	private File configVars;
	public static Gson gson;
	private Type championList;
	private Type itemList;
	private List<File> toBeDeleted;
	
	public IOHandler() throws Exception{
		root = Main.root;
		playerDir = new File(root, "players");
		logDir = new File(root, "logs");
		resources = new File(root, "resources");
		championData = new File(resources, "champions.txt");
		itemData = new File(resources, "items.txt");
		configVars = new File(resources, "config.txt");
		resources.mkdir();
		playerDir.mkdir();
		logDir.mkdir();
		configVars.createNewFile();
		gson = new Gson();
		toBeDeleted = new ArrayList<File>();
		ih = new InputHandler();
		championList = new TypeToken<ArrayList<Champion>>(){}.getType();
		itemList = new TypeToken<ArrayList<Item>>(){}.getType();
	}
	
	public void init() throws Exception{
		synchronized(sync){
			if(championData.exists()){
				br = new BufferedReader(new FileReader(championData));
				String in = br.readLine();
				if(in != null && !in.equals("null"))
					Champion.champions = gson.fromJson(in, championList);
				else
					Champion.init();
			}
			else
				Champion.init();
			if(itemData.exists()){
				br = new BufferedReader(new FileReader(itemData));
				String in = br.readLine();
				if(in != null && !in.equals("null"))
					Item.items = gson.fromJson(in, itemList);
				else
					Item.init();
			}
			else
				Item.init();
			ConfigVars vars = null;
			if(configVars.exists()){
				br = new BufferedReader(new FileReader(configVars));
				String in = br.readLine();
				if(in != null && !in.equals("null"))
					vars = gson.fromJson(in, ConfigVars.class);
			}
			ConfigVars.set(vars);
			for(File f: playerDir.listFiles())
				loadPlayer(f);
		}
		if(br != null)
			br.close();
		ih.start();
	}
	
	public static void updateData() throws Exception{
		Champion.init();
		Item.init();
	}
	
	//TODO add auto-update in InputHandler, addPlayer
	public void matchDiskToLive(){
		synchronized(sync){	
			if(!canWrite){
				return;
			}
			try{
				bw = new BufferedWriter(new FileWriter(championData));
				bw.write(gson.toJson(Champion.champions));
				bw.flush();
				bw = new BufferedWriter(new FileWriter(itemData));
				bw.write(gson.toJson(Item.items));
				bw.flush();
				bw = new BufferedWriter(new FileWriter(configVars));
				bw.write(gson.toJson(ConfigVars.getVars()));
				bw.flush();
			}catch(Exception e){
				Logger.loudLog(e);
			}
			List<File> indvPlayerDirs = Arrays.asList(playerDir.listFiles());
			for(Player p: Player.getTracked()){
				File personalDir = new File(playerDir, ""+p.id);
				File personalGames = new File(personalDir, "games");
				File playerDat = new File(personalDir, p.id + ".txt");
				File tempGame;
				personalGames.mkdirs();
				if(playerDat.exists()){
					playerDat.delete();
				}
				try{
					bw = new BufferedWriter(new FileWriter(playerDat));
					bw.write(gson.toJson(p));
					bw.flush();
					for(GameStat g: p.getGames()){
						tempGame = new File(personalGames, Time.setDate(g.gameDate).fileDate() + "_" + g.gameId + ".txt");
						if(!tempGame.exists()){
							bw = new BufferedWriter(new FileWriter(tempGame));
							bw.write(gson.toJson(g));
							bw.flush();
						}
					}
					bw.close();
				}catch(Exception e){
					Logger.loudLogLine("***Error logging player " + p.id + "***");
					Logger.loudLog(e);
				}	
			}
			for(File f: indvPlayerDirs){
				if(Player.isTracked(Integer.parseInt(f.getName()))){ //if player is being tracked
					markSafe(f);
					for(File g: (new File(f, "games")).listFiles()){
						if(!GameStat.contains(g.getName().substring(11, g.getName().length()-4)))
							recursiveMarkDelete(g);
					}
				}
				else
					recursiveMarkDelete(f);
			}
		}
	}
	
	private void loadPlayer(File playerFolder) throws Exception{
		if(playerFolder.isDirectory()){
			File gameDirectory = null;
			Player created = null;
			for(File g: playerFolder.listFiles()){	//for all files per folder
				if(g.getAbsolutePath().endsWith(".txt")){	//if player data
					br = new BufferedReader(new FileReader(g));
					String in = br.readLine();
					if(in != null && !in.equals("null")){
						created = gson.fromJson(in, Player.class);
						created.updateRecent();
						Player.addTracked(created);
					}
				}
				else if(g.isDirectory()){
					gameDirectory = g;
				}
			}
			if(created != null){
				loadGames(created, gameDirectory);
				System.out.println("  " + created.summaryShort() + " loaded.");
				if(!((Boolean) ConfigVars.get(ConfigVars.COMPLETE_ONLY)))
					created.update();
			}
			if(br != null)
				br.close();
		}
	}
	
	private void loadGames(Player created, File gameDirectory) throws Exception{
		if(gameDirectory == null)
			return;
		for(File gameFile: gameDirectory.listFiles()){
			if(gameFile.toString().endsWith(".txt")){
				br = new BufferedReader(new FileReader(gameFile));
				String in = br.readLine();
				if(in != null && !in.equals("null")){
					GameStat temp = gson.fromJson(in, GameStat.class);
					temp.load();
					created.addGame(temp);
				}
				continue;
			}
		}
	}
	
	public void matchLiveToDisk() throws Exception{
		synchronized(sync){
			List<File> indvPlayerDirs = Arrays.asList(playerDir.listFiles());
			Player.resetTracked();
			for(File f: indvPlayerDirs){
				if(toBeDeleted.contains(f))
					continue;
				else
					loadPlayer(f);
			}
		}
	}
	
	private void markSafe(File f){
		if(f.isDirectory())
			for(File g: f.listFiles())
				markSafe(g);
		toBeDeleted.remove(f);
	}
	
	private void recursiveMarkDelete(File f){
		if(f.isDirectory())
			for(File g: f.listFiles())
				recursiveMarkDelete(g);
		toBeDeleted.add(f);
	}
	
	private void recursiveDelete(File f){
		if(f.isDirectory())
			for(File g: f.listFiles())
				recursiveDelete(g);
		f.delete();
	}
	
	public void cleanFiles(){
		for(File f: toBeDeleted)
			if(f.exists())
				recursiveDelete(f);
	}
	
	//TODO fork a new thread for every call to this - should write while you interact with the program
	public void cleanUp(){
		synchronized(sync){
			if(!canWrite)
				return;
			matchDiskToLive();
			cleanFiles();
			try{Logger.writeOut();}
			catch(Exception e){e.printStackTrace();}
		}
	}
	
}
