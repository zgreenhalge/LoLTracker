package Tracker;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import Utils.Time;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Handles all reading and writing that interacts with the disk.&nbsp;
 * Has methods to save and load players, update all static data, save and load state from disk, and clean the disk to match current state.
 * 
 * @author Zach Greenhalge
 *
 */
public class IOHandler {

	public static boolean canWrite = true;
	public static final Object sync = new Object();
	private BufferedWriter bw;
	private BufferedReader br;
	private static File playerDir;
	private static File logDir;
	private static File root;
	private static File resources;
	private static File championData;
	private static File itemData;
	private static File configVars;
	public static Gson gson;
	private final Type championList;
	private final Type itemList;
	private final List<File> toBeDeleted;
	
	/**
	 * Creates a new IOHandler with the given File as the root directory. 
	 * 
	 * @param root - File representation of the desired root path
	 * @throws IOException when there are issues writing to disk
	 */
	public IOHandler(File root) throws IOException{
		IOHandler.root = root;
		playerDir = new File(root, "players");
		logDir = new File(root, "logs");
		resources = new File(root, "resources");
                resources.mkdir();
		championData = new File(resources, "champions.txt");
		itemData = new File(resources, "items.txt");
		configVars = new File(resources, "config.txt");
		playerDir.mkdir();
		logDir.mkdir();
		configVars.createNewFile();
		gson = new Gson();
		toBeDeleted = new ArrayList<File>();
		championList = new TypeToken<ArrayList<Champion>>(){}.getType();
		itemList = new TypeToken<ArrayList<Item>>(){}.getType();
	}
        
	/**
	 * Loads all static data, as well as all players and the config file.
	 * This class can throw exceptions after a patch due to Champion.&nbsp;init() and Item.&nbsp;init()
	 * 
	 * @throws IOException when there are IO problems
	 * @throws Exception when a Champion/Item cannot be found.
	 */
	public void init() throws IOException, Exception{
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
                Logger.loudLogLine("Initialization completed!");
	}
	
	/**
	 * Updates all static data.
	 * @throws Exception when a Champion or Item cannot be found.
	 * @see Champion
	 * @see Item
	 */
	public static void updateData() throws Exception{
		Champion.init();
		Item.init();
	}
	
	/**
	 * Saves the given Player and all their associated games to disk.
	 * 
	 * @param p - the Player to be saved
	 */
	public static void savePlayer(Player p){
		File personalDir = new File(playerDir, ""+p.id);
		File personalGames = new File(personalDir, "games");
		File playerDat = new File(personalDir, p.id + ".txt");
		File tempGame;
		personalGames.mkdirs();
		if(playerDat.exists()){
			playerDat.delete();
		}
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(playerDat));
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
		}catch(IOException e){
			Logger.loudLogLine("***Error logging player " + p.id + "***");
			Logger.loudLog(e);
		}	
	}
        
	//TODO add auto-update in InputHandler, addPlayer
	/**
	 * Writes all data to disk.&nbsp; 
	 * If there is data on disk that no longer exists in the running environment, it is marked for deletion.
	 * 
	 */
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
			}catch(IOException e){
				Logger.loudLog(e);
			}
			List<File> indvPlayerDirs = Arrays.asList(playerDir.listFiles());
			for(Player p: Player.getTracked()){
				savePlayer(p);
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
	
	/**
	 * Loads a Player into the system.
	 * 
	 * @param playerFolder - File representation of the player's folder path
	 * @throws IOException when there are IO errors
	 * @throws Exception when there is a problem loading a game
	 */
	private void loadPlayer(File playerFolder) throws IOException, Exception{
		if(playerFolder.isDirectory()){
			File gameDirectory = null;
			Player created = null;
			for(File g: playerFolder.listFiles()){	//for all files per folder
				if(g.getAbsolutePath().endsWith(".txt")){	//if player data
                                    Logger.loudLogLine("Loading data for " + g.getName().substring(0, g.getName().length()-4));
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
				Logger.loudLogLine("  " + created.summaryShort() + " loaded.");
				if(!((Boolean) ConfigVars.get(ConfigVars.COMPLETE_ONLY)))
					created.update();
			}
			if(br != null)
				br.close();
		}
	}
	
	/**
	 * Loads all games for the specified player.
	 * 
	 * @param created - the player who should be associated with the games
	 * @param gameDirectory - File representation of the player's game folder path
	 * @throws IOException when there are IO errors
	 * @throws Exception when there is an issue loading a game
	 */
	private void loadGames(Player created, File gameDirectory) throws IOException, Exception{
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
			}
		}
	}
	
	/**
	 * Resets all data in the program, then loads all Players not marked for deletion.
	 * 
	 * @throws Exception when there is an error loading a Player
	 */
	public void matchLiveToDisk() throws Exception{
		synchronized(sync){
			List<File> indvPlayerDirs = Arrays.asList(playerDir.listFiles());
			Player.resetTracked();
			for(File f: indvPlayerDirs){
				if(!toBeDeleted.contains(f))
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
	/**
	 * Cleans up all responsibilities.&nbsp;
	 * More specifically, calls matchDiskToLive(), cleanFiles, and then writes out the Logger.
	 */
	public void cleanUp(){
		if(!canWrite)
			return;
		Logger.logLine("Writing files to disk...");
		matchDiskToLive();
		Logger.logLine("Cleaning up deleted files...");
		cleanFiles();
		Logger.logLine("Writing logs...");
		try{
			Logger.writeOut();
		}
		catch(IOException e){
                        Logger.log(e);
                    }
	}
	
}
