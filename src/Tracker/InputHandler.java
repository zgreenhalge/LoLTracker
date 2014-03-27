package Tracker;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import Utils.Time;


public class InputHandler {
	
	private BufferedReader br;
	private String input;
	private String temp = "";
	private boolean loop;
	
	public InputHandler(){
		br = new BufferedReader(new InputStreamReader(System.in));
		loop = true;
	}
	
	public void start(){
		welcome();
		while(loop){
			takeInput();
		}
	}
	
	private void welcome() {
		synchronized(IOHandler.sync){}
		Logger.loudLogLine("\nWelcome to the League of Legends Statistical Database and Player Tracker!\n"  
				+ "-------------------------" + Time.updateCal().dateTime() + "--------------------------\n"
				+ " (This product is not endorsed, certified or otherwise approved)\n    (in any way by Riot Games, Inc. or any of its affiliates.)\n");
		if(Player.getTracked().size() == 0){
			/*Logger.loudLogLine("Start tracking players with 'add [summonerName]'!\n"
					+ "View all tracked players with 'list summoners'\n"
					+ "View all logged games with 'list games'\n"
					+ "Look up stats with 'query'\n"
					+ "Type 'help' or '?' for more commands.");*/
		}
		else{
			Logger.loudLogLine("Currently tracking: " + Player.getTracked().size());
			for(Player p: Player.getTracked()){
				Logger.loudLogLine(" " + p.summaryMini() + " - " + p.getGames().size() + " games");
			}
		}
		//Logger.loudLog("####IF A GAME IS INCOMPLETE YOU WILL ONLY SEE THE STATS OF THE TRACKED PLAYER#####");
		//Logger.loudLog("##THIS IS CAUSED BY THE GAME BEING FLUSHED OUT OF ANOTHER PLAYER'S RECENT GAMES###");
		System.out.flush();
		initialize();
	}
	
	private void initialize(){
		Logger.loudLogLine(ConfigVars.listVars());
		Logger.setDev((Boolean)(ConfigVars.get(ConfigVars.DEV_MODE)));
		
	}

	private void takeInput(){
		try {
			System.out.print("\n>");
			input = br.readLine();
		} catch (IOException e) {
			Logger.loudLog(e);
			takeInput();
		}
		input = input.toLowerCase();
		handleInput();
	}
	
	private void handleInput(){
		System.out.flush();
		System.err.flush();
		Logger.logInput("\n>" + input);
		String[] element = input.split(" ");
		try{
			if(element[0].equals("riot")){
				/** HERE THERE BE API CALLS */
				if(element[1].equals("check"))
					riotCheck(element);
				else if(element[1].equals("stats"))
					riotStats(element);
				else if(element[1].equals("teams"))
					riotTeam(element);
				else if(element[1].equals("runes"))
					riotRunes(element);
				else if(element[1].equals("masteries"))
					riotMasteries(element);
				else{
					Logger.loudLogLine("Proper calls:\n" +
							" riot check [summonerName]\n" +
							" riot stats [summonerName]\n" +
							" riot teams [summonerName]\n" +
							" riot runes [summonerName]\n" +
							" riot masteries[summonerName].");
				}
			}
			else if(element[0].equals("update")){
				if(element.length == 1 || element[1].equals("all"))
					Player.updateAll();
				else if(element[1].equals("data"))
					IOHandler.updateData();
				else if(element.length > 2){
					for(int i=3; i<element.length; i++)
						temp = temp.concat(element[i]);
					Player.update(temp);
				}
				else Player.update(element[1]);
			}
			else if(element[0].equals("add")){
				if(element.length>=2){
					String temp = "";
					for(int i=1; i<element.length; i++)
						if(element[i].endsWith(",")){
							temp = temp.concat(element[i].substring(0, element[i].toCharArray().length-1));
							addPlayer(temp);
							temp = "";
						}
						else
							temp = temp.concat(element[i]);
					addPlayer(temp);
				}
				else if(element.length<2){
					Logger.loudLogLine("Proper syntax: add [summonerName], [summonerName],...");
				}
			}
			else if(element[0].equals("list")){
				if(element[1].equals("summoners") || element[1].equals("players")){
					listTrackedSummoners();
				}
				else if(element[1].equals("games")){
					if(element.length==2)
						listGames(null);
					else{
						listGames(Arrays.copyOfRange(element, 2, element.length));
					}
				}
				else if (element[1].equals("settings"))
					Logger.loudLogLine(ConfigVars.listVars());
				else{
					Logger.loudLogLine("Proper syntax: list [summoners || games] -c -r");
				}
			}
			else if(element[0].equals("summary")){
				try{
					if(element[1].equals("-g")){
						gameSummary(element[2]);
					}else if(element[1].equals("-s")){
						String name ="";
						for(int i=2; i<element.length; i++)
							name = name.concat(element[i]);
						playerSummary(name);
					}
				}
				catch(IndexOutOfBoundsException e){
					System.out.println("Proper syntax: summary -s [summonerName]\n" +
							"                   -or-\n" +
							"               summary -g [gameId]");
				}
			}
			else if(element[0].equals("help") || element[0].equals("?")){
				listCommands();
			}
			else if(element[0].equals("save")){
				Logger.loudLog("Saving data.");
				Main.getIOHandler().matchDiskToLive();
				Logger.loudLog(".");
				Main.getIOHandler().cleanFiles();
				Logger.loudLog(".");
				Logger.writeOut();
				Logger.loudLogLine(" Saved!");
			}
			else if(element[0].equals("delete")){
				if(element.length == 1){
					Player.resetTracked();
					GameStat.setGames(new ArrayList<GameStat>());
				}
				else if(element[1].equals("games")){
					if(element.length==2)
						GameStat.setGames(new ArrayList<GameStat>());
					else if(element[2].equals("-i"))
						deleteGames(false);
					else if(element[2].equals("-c"))
						deleteGames(true);
				}
				else{
					String name ="";
					for(int i=1; i<element.length; i++)
						name = name.concat(element[i]);
					Player.removeTracked(name);
				}
			}
			else if(element[0].equals("settings")){
				if(element[1].equals("-c")){
					boolean current = (Boolean)(ConfigVars.get(ConfigVars.COMPLETE_ONLY));
					ConfigVars.set(ConfigVars.COMPLETE_ONLY, (current = !current));
					Logger.loudLogLine("Complete games only: " + !current + " -> " + current);
				}
			}
			else if(element[0].equals("quit") || element[0].equals("q")){
					shutdown();
			}
			else if(element[0].equals("note")){
				//figure this out
			}
			else if(element[0].equals("dev")){
				Logger.switchDevMode();
				//do other things here?
				//maybe dev options menu
			}
			else{
				Logger.loudLogLine("Command not recognized! Type 'help' or '?' for commands.");
			}
		} catch(Exception e){
			System.out.println("\nERROR: " + e.getMessage());
			Logger.log(e);
		}
		
	}

	private void listCommands(){
		Logger.loudLogLine("Valid commands (#commands are not yet implemented):\n" +
				"#riot [command] [summonerName]\n" +
				" update -s [summonerName]\n" +
				" add [summonerName],[summonerName],[summonerName]... \n" +
				" list -c -r [summoners || games]\n" +
				" summary -g [gameID] -s [summonerName]\n" +
				"#history -v -r -p [timePeriod]\n" +
				"#query\n" +
				" settings -c " +
				" save  ~to save all data\n" +
				" quit  ~(or q) to save and the program"
				);
	}
	
	private void deleteGames(boolean incomplete){
		ArrayList<GameStat> games = GameStat.getGames();
		long[] ids = new long[games.size()];
		int index = 0;
		for(GameStat g: games)
			if(g.incomplete != incomplete)
				ids[index++] = g.gameId;
		for(int i=0; i< ids.length; i++){
			if(ids[i] != 0)
				GameStat.delete(ids[i]);
		}
	}
	
	private void playerSummary(String string) throws Exception{
		Player temp = Player.find(string);
		System.out.println(temp.summary());
		
	}

	private void gameSummary(String idStr) throws Exception{
		GameStat temp = null;
		for(GameStat g: GameStat.getGames())
			if(g.equals(idStr)){
				temp = g;
				break;
			}
		if(temp == null) throw new Exception("Game " + idStr + " does not exist");
		Logger.loudLogLine(temp.summary());
	}
		
	private void listGames(String[] input){
		if(GameStat.getGames() == null || GameStat.getGames().size() == 0)
			Logger.loudLogLine("No games have been loaded");
		else{
			boolean complete = false;
			if((Boolean)ConfigVars.get(ConfigVars.COMPLETE_ONLY))
				complete = true;
			boolean ranked = false;
			String summoner = "";
			ArrayList<GameStat> games;
			Player temp;
			if(input != null){
				for(int i=0; i<input.length; i++){
					if(input[i].startsWith("-")){
						if(input[i].equals("-c"))
							complete = true;
						else if(input[i].equals("-r"))
							ranked = true;
						continue;
					}
					else 
						summoner = summoner.concat(input[i]);
				}
			}
			temp = Player.getTracked(summoner);
			if(temp != null){
				games = temp.getGames();
			}
			else{
				games = GameStat.getGames();
			}
			int listed = 1;
			for(GameStat g: games){
				if(complete){
					if(ranked){
						if(!g.incomplete && g.ranked)
							Logger.loudLogLine("\n(" + (listed++) + ") " + g.summaryShort());
					}
					else if(!g.incomplete)
						Logger.loudLogLine("\n(" + (listed++) + ") " + g.summaryShort());
				}
				else if(ranked){
					if(g.ranked)
						Logger.loudLogLine("\n(" + (listed++) + ") " + g.summaryShort());
				}
				else
					Logger.loudLogLine("\n(" + (listed++) + ") " + g.summaryShort());
			}
		}
	}
	
	private void listTrackedSummoners(){
		if(Player.getTracked().size() == 0)
			System.out.println("No players are being tracked");
		else
			for(Player p: Player.getTracked())
				Logger.loudLogLine(p.summaryShort());
	}
	
	private void shutdown(){
		Logger.loudLogLine("Cleaning up...");
		loop = false;
	}

	private void riotMasteries(String[] element) {
		// TODO Auto-generated method stub
		
	}


	private void riotRunes(String[] element) {
		// TODO Auto-generated method stub
		
	}


	private void riotTeam(String[] element) {
		// TODO Auto-generated method stub
		
	}


	private void riotStats(String[] element) {
		// TODO Auto-generated method stub
		
	}


	private void addPlayer(String string) {
		if(Player.isTracked(string)){
			Logger.loudLogLine("Player is already being tracked");
			return;
		}
		try {
			Player.addPlayer(string);
		} catch (Exception e) {
			Logger.loudLog(e);
			return;
		}
	}

	private void riotCheck(String[] element) {
		
	}
}
