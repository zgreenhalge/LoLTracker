import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


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
			/*System.out.println("Start tracking players with 'add [summonerName]'!\n"
					+ "View all tracked players with 'list summoners'\n"
					+ "View all catalogued games with 'list games'\n"
					+ "Look up stats with 'query'\n"
					+ "Type 'help' or '?' for more commands.");*/
		}
		else{
			Logger.loudLogLine("Currently tracking:" + Player.getTracked().size());
			for(Player p: Player.getTracked()){
				if(p != null)
					if(p.getGames() == null)
						Logger.loudLogLine(" " + p.header() + " - 0 games");
					else
						Logger.loudLogLine(" " + p.header() + " - " + p.getGames().size() + " games");
			}
		}
		//Logger.loudLog("####IF A GAME IS INCOMPLETE YOU WILL ONLY SEE THE STATS OF THE TRACKED PLAYER#####");
		//Logger.loudLog("##THIS IS CAUSED BY THE GAME BEING FLUSHED OUT OF ANOTHER PLAYER'S RECENT GAMES###");
		System.out.flush();
	}
	
	private void takeInput(){
		try {
			System.out.print(">");
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
		Logger.logLine(">" + input);
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
				if(element.length<2){
					Logger.loudLogLine("Proper syntax: add [summonerName],[summonerName],...");
				}
				else if(element.length>=2){
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
			}
			else if(element[0].equals("list")){
				if(element.length != 2)
					Logger.loudLogLine("Proper syntax: list summoners || games");
				else if(element[1].equals("summoners") || element[1].equals("players"))
					listTrackedSummoners();
				else if(element[1].equals("games"))
					listGames();
				else{
					Logger.loudLogLine("Proper syntax: list summoners || games");
				}
			}
			else if(element[0].equals("summary")){
				if(element[1].equals("-g")){
					gameSummary(element[2]);
				}else if(element[1].equals("-s")){
					playerSummary(element[2]);
				}
			}
			else if(element[0].equals("help") || element[0].equals("?")){
				Logger.loudLogLine("Valid commands (#commands are not yet implemented):\n" +
						"#riot [command] [summonerName]\n" +
						" update -a -s [summonerName]\n" +
						" add [summonerName],[summonerName],[summonerName]... \n" +
						" list (summoners)(games)\n" +
						"#summary -v -g [gameID] -s [summonerName]\n" +
						"#history -v -r -p [timePeriod]\n" +
						"#query"
						);
			}
			else if(element[0].equals("quit") || element[0].equals("exit")){
					shutdown();
			}
			else if(element[0].equals("note")){
				//figure this out
			}
			else{
				Logger.loudLogLine("Command not recognized! Type 'help' or '?' for commands.");
			}
		} catch(Exception e){
			Logger.loudLog(e);
		}
		
	}

	private void playerSummary(String string) {
		// TODO Auto-generated method stub
		
	}

	private void gameSummary(String idStr) throws Exception{
		GameStat temp = null;
		for(GameStat g: GameStat.allGames)
			if(g.equals(idStr)){
				temp = g;
				break;
			}
		if(temp == null) throw new Exception("Game " + idStr + " is not a valid game!");
		Logger.loudLogLine(temp.summary());
	}
	
	private void listGames(){
		if(GameStat.allGames == null || GameStat.allGames.size() == 0)
			Logger.loudLogLine("No games have been loaded");
		else
			for(GameStat gs: GameStat.allGames)
				Logger.loudLogLine(gs.summaryShort());
	}
	
	private void listTrackedSummoners(){
		if(Player.getTracked().size() == 0)
			System.out.println("No players are being tracked");
		else
			for(Player p: Player.getTracked())
				Logger.loudLogLine(p.summaryShort());
	}
	
	private void shutdown(){
		Logger.loudLogLine("Goodbye!");
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
