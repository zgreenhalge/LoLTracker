import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;


import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

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
	public static Gson gson;
	private Type championList;
	private Type itemList;
	private List<File> deleteOnExit;
	
	public IOHandler() throws Exception{
		root = Main.root;
		playerDir = new File(root, "players");
		logDir = new File(root, "logs");
		resources = new File(root, "resources");
		championData = new File(resources, "champions.txt");
		itemData = new File(resources, "items.txt");
		resources.mkdir();
		playerDir.mkdir();
		logDir.mkdir();
		gson = new Gson();
		deleteOnExit = new ArrayList<File>();
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
				br.close();
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
				br.close();
			}
			else
				Item.init();
			for(File f: playerDir.listFiles()){ //for all player folders - 1
				//System.out.println("Found player folder " + f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf(File.separator)));
				if(f.isDirectory()){ // 2
					Player created = null;
					for(File g: f.listFiles()){	//for all files per folder - 3
						if(g.getAbsolutePath().endsWith(".txt")){	//if player data - 4
							//System.out.println(g.getAbsolutePath());
							br = new BufferedReader(new FileReader(g));
							String in = br.readLine();
							if(in != null && !in.equals("null")){ // 5
								created = gson.fromJson(in, Player.class);
								created.updateRecent();
								Player.getTracked().add(created);
							}// 5
							br.close();
							continue; //this line written by hallie
						} // 4
						else if(g.isDirectory()){ //if game directory - 5.5
							for(File h: g.listFiles()){ // 6
								if(h.toString().endsWith(".txt")){ // 7
									br = new BufferedReader(new FileReader(h));
									String in = br.readLine();
									if(in != null && !in.equals("null")){ // 8
										GameStat temp = gson.fromJson(in, GameStat.class);
										temp.load();
										created.addGame(temp);
									} // 8
									br.close();
									continue;
								} // 7
							}//for all files inside // 6
						}//if isDirectory - 5.5
						if(created != null){ // 10
							System.out.println("Loaded: " + created.summaryShort());
							created.update();
						} // 10
					}//for all files inside - 3
				}//if is folder - 2
			}//for all player folders - 1
		}
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
				bw.close();
				bw = new BufferedWriter(new FileWriter(itemData));
				bw.write(gson.toJson(Item.items));
				bw.close();
			}catch(Exception e){
				Logger.loudLog(e);
			}
			List<File> indvPlayerDirs = Arrays.asList(playerDir.listFiles());
			String[] tokens;
			for(File f: indvPlayerDirs){
				tokens = f.getAbsolutePath().split(File.separator);
				if(Player.getTracked().contains(tokens[tokens.length-1])) //if player is being tracked
					markSafe(f);
				else
					recursiveDelete(f);
			}
		}
	}
	
	public void matchLiveToDisk() throws Exception{
		synchronized(sync){
			List<File> indvPlayerDirs = Arrays.asList(playerDir.listFiles());
			String[] tokens;
			ArrayList<Integer> idsToSave = new ArrayList<Integer>();
			for(File f: indvPlayerDirs){
				if(deleteOnExit.contains(f))
					continue;
				markSafe(f);
				tokens = f.getAbsolutePath().split(File.separator);
				idsToSave.add(Integer.parseInt(tokens[tokens.length-1]));
				if(!Player.getTracked().contains(tokens[tokens.length-1])) //if player is not being tracked
					br = new BufferedReader(new FileReader(f));
					Player.getTracked().add(gson.fromJson(br.readLine(), Player.class));
					br.close();
				}
			Player.getTracked().retainAll(idsToSave);
		}
	}
	
	private void markSafe(File f){
		if(f.isDirectory())
			for(File g: f.listFiles())
				markSafe(g);
		deleteOnExit.remove(f);
	}
	
	private void recursiveDelete(File f){
		if(f.isDirectory())
			for(File g: f.listFiles())
				recursiveDelete(g);
		else
			deleteOnExit.add(f);
	}
	
	public void cleanUp(){
		synchronized(sync){
			if(!canWrite) return;
			matchDiskToLive();
			for(Player p: Player.getTracked()){
				File personalDir = new File(playerDir, ""+p.id);
				File personalGames = new File(personalDir, "games");
				File playerDat = new File(personalDir, p.id + ".txt");
				File tempGame;
				personalGames.mkdirs();
				try{
					bw = new BufferedWriter(new FileWriter(playerDat));
					bw.write(gson.toJson(p));
					bw.flush();
					bw.close();
					for(GameStat g: p.getGames()){
						tempGame = new File(personalGames, Time.setDate(g.gameDate).fileDate() + "_" + g.gameId + ".txt");
						bw = new BufferedWriter(new FileWriter(tempGame));
						bw.write(gson.toJson(g));
						bw.flush();
						bw.close();
					}
				}catch(Exception e){
					Logger.loudLogLine("***Error logging player " + p.id + "***");
					Logger.loudLog(e);
					e.printStackTrace();
				}	
			}
			try{
			Logger.writeOut();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
}
