package Tracker;
import java.util.ArrayList;
import Utils.*;

/**
 * Provides a central hub for accessing information relating to a specific player as well as static methods for accessing any existing Player and creating new Players from Riot servers.&nbsp;
 * @author Zach Greenhalge
 *
 */
public class Player {

	private static transient ArrayList<Player> players = new ArrayList<Player>();
	private static transient ArrayList<Player> tracked = new ArrayList<Player>();
	public String name;
	public int level;
	public int id;
	private String note;
	private transient ArrayList<RecentGame> recent = new ArrayList<RecentGame>();	//serialized separately
	private transient ArrayList<GameStat> games = new ArrayList<GameStat>(); 	//serialized separately
	
	/**
	 * Create a new summoner by name
	 * @param s - summoner name
	 * @throws Exception when there are issues with accessing the server
	 */
    public Player(String s) throws Exception{
        this(RiotAPI.getSummoner(s));
    }
        
    /**
     * Used by RiotAPI to create new Players based on return values from Riot servers
     * @param base
     */
	public Player(RiotAPI.PlayerReturn base){
			name = base.name;
			id = base.id;
			level = base.summonerLevel;
			note = "";
	}
		
	/**
	 * Get the minimum information about the Player
	 * @return PlayerMini struct
	 */
	public PlayerMini getMini(){
		PlayerMini miniMe = new PlayerMini();
		miniMe.summonerId = id;
		miniMe.name = name;
		return miniMe;
	}
	
	/**
	 * Set a personalized note for the Player
	 * @param s - the note to save
	 */
	public void setNote(String s){note = s;}
	
	/**
	 * Returns the saved note
	 * @return
	 */
	public String getNote(){return note;}
	
	public RecentGame findGame(int id){
		for(RecentGame rg: recent)
			if(rg.gameId == id)
				return rg;
		return null;
	}
	
	/**
	 * Returns all tracked Players.&nbsp; Should only be accessed in a static context.
	 * @return ArrayList of all tracked Players
	 */
	public static ArrayList<Player> getTracked(){
		cleanse();
		return tracked;
	}
	
	/**
	 * Returns a list of all Players in the program
	 * @return ArrayList of all Players created
	 */
	public static ArrayList<Player> getAll(){
		cleanse();
		return players;
	}
	
	/**
	 * Returns the Player's RecentGames
	 * @return ArrayList of Player's RecentGames
	 */
	public ArrayList<RecentGame> getRecentGames(){
		return recent;
	}
	
	/**
	 * Returns an ArrayList of all GameStat associated with this Player.&nbsp; 
	 * If none exist, returns an empty ArrayList.
	 * @return ArrayList of all associated GameStat
	 */
	public ArrayList<GameStat> getGames(){
		if(games == null)
			games = new ArrayList<GameStat>();
		return games;
	}
	
	/**
	 * Add a game to a Player
	 * @param game - the GameStat to add
	 */
	public synchronized void addGame(GameStat game){
		if(game.incomplete){
			ArrayList<Long> incGames = ((ArrayList<Long>)ConfigVars.get(ConfigVars.INCOMPLETE_GAMES));
			if(!incGames.contains(game.gameId))
				incGames.add(game.gameId);
		}
		if(games == null)
			games = new ArrayList<GameStat>();
		games.add(game);
		ArrayFunctions.sort(games);
		if(!GameStat.getGames().contains(game)){
			ArrayList<GameStat> allGames = GameStat.getGames();
			allGames.add(game);
			ArrayFunctions.sort(allGames);
		}
	}
	
        @SuppressWarnings("empty-statement")
	private static void cleanse(){
		while(tracked.remove(null));
		while(players.remove(null));
	}
        
    //TODO make every call to update a new thread
	/**
	 * Query Riot servers for this player's RecentGames, and generate any new GameStat needed
	 */
	public void update(){
		cleanse();
	    try{
	        recent = RiotAPI.getRecentGames(id);
	    }
	    catch(Exception e){
	        Logger.loudLog(e);
	        if(recent == null)
	        	recent = new ArrayList<RecentGame>();
	    }
	    for(RecentGame rg: recent){
	    	try{
	        	if(!GameStat.contains(new Long(rg.gameId)))
	        		addGame(new GameStat(rg, this));
	        }
        	catch(Exception e){
        		games = new ArrayList<GameStat>();
    			addGame(new GameStat(rg, this));
        	}
        }
	    IOHandler.savePlayer(this);
	}
	
	/**
	 * Query the Riot servers for any new RecentGames
	 */
	public void updateRecent(){
		try{
	        recent = RiotAPI.getRecentGames(id);
	    }
	    catch(Exception e){
	        Logger.loudLog(e);
	    }
	}

	/**
	 * A static way to update a tracked Player.&nbsp;
	 * Functionally equivalent to Player.&nbsp;findPlayer(string).&nbsp;update()  
	 * @param string - the name of the Player to update
	 */
	public static void update(String string) {
		for(Player p: players){
			if(p.name.equalsIgnoreCase(string)){
				p.update();
				break;
			}
		}
	}
	
	/**
	 * A convenience method to update all tracked Players
	 */
	public static void updateAll(){
		for(Player p: tracked)
			p.update();
		Logger.logLine("All players are up-to-date with Riot servers");
	}	
	
	/**
	 * Finds the Player for the name given.&nbsp; If no Player of that name exists in the program, Riot servers will be queried and a Player will be generated.
	 * 
	 * @param summonerName - the name of the Player to find
	 * @return the Player with a matching name
	 * @throws Exception from RiotAPI
	 */
	public static Player findPlayer(String summonerName) throws Exception{ 
		for(Player p: players){
			if(p.name.equals(summonerName))
				return p;
		}
		Player temp = new Player(RiotAPI.getSummoner(summonerName));
		players.add(temp);
		temp.updateRecent();
		return temp;
	}
	
	/**
	 * Finds the Player for the id given.&nbsp; If no Player of that name exists in the program, Riot servers will be queried and a Player will be generated.
	 * 
	 * @param id of the Player to be found
	 * @return the desired Player
	 * @throws Exception from RiotAPI
	 */
	public static Player findPlayerById(int id) throws Exception{
		for(Player p: players)
			if(p.id == id)
				return p;
		Player temp = new Player(RiotAPI.getSummonerById(id));
		players.add(temp);
		temp.updateRecent();
		return temp;
	}
	
	/**
	 * Returns whether or not the Player is being tracked
	 * @param summonerName - name of the Player to check
	 * @return true if the Player is tracked, false otherwise
	 */
	public static boolean isTracked(String summonerName){
		for(Player p: tracked)
			if(StringFunctions.normEq(p.name, summonerName))
				return true;
		return false;
	}
	
	/**
	 * Returns whether or not the Player is being tracked
	 * @param id of the Player to check
	 * @return true if the Player is tracked, false otherwise
	 */
	public static boolean isTracked(int id){
		for(Player p: tracked)
			if(id == p.id)
				return true;
		return false;
	}
	
	
	//throws exception when player does not exist
	/**
	 * Adds a Player to the tracked list.
	 * @param summonerName - name of Player to add
	 * @return the Player added
	 * @throws Exception when the Player cannot be found
	 */
	public static Player addPlayer(String summonerName) throws Exception{
		Player temp = null;
		for(Player p: tracked)
			if(p.equals(summonerName)){
				temp = p;
                                Logger.logLine(p.name + " found in the system!");
				return temp;
			}
		if(temp == null){
			for(Player p: players)
				if(p.equals(summonerName)){
					temp = p;
                                        Logger.logLine(p.name + " was not being tracked.");
					break;
			}
			try{
				if(temp == null)
					temp = new Player(RiotAPI.getSummoner(summonerName));
				tracked.add(temp);
				Logger.loudLogLine("Now tracking " + temp.name + "(ID: " + temp.id + ")");
				temp.update();
				return temp;
			}catch(Exception e){
				System.err.println(e.getMessage());
			}
		}
		throw new Exception();
	}
	
	/**
	 * Adds a Player to the tracked list.
	 * @param id of Player to add
	 * @return the Player added
	 * @throws Exception when the Player cannot be found
	 */
	public static Player addPlayerById(int id) throws Exception{
		Player temp = null;
		for(Player p: tracked)
			if(p.equals(id)){
				temp = p;
				break;
			}
		if( temp == null){
			for(Player p: players)
				if(p.equals(id)){
					temp = p;
					break;
			}
			if(temp == null)
				temp = new Player(RiotAPI.getSummonerById(id));
			tracked.add(temp);
		}
		temp.update();
		return temp;
	}
	
	/**
	 * Clears the list of tracked Players
	 */
	public static void resetTracked(){
		tracked = new ArrayList<Player>();
	}

	/**
	 * Returns true if the Object passed is a Player with an identical id as this Player, or if it is a String or Integer representation of this Player's id. 
	 */
	@Override
	public boolean equals(Object obj){
		if(obj instanceof Player)
			return ((Player)obj).id == id;
		if(obj instanceof String)
			return Utils.StringFunctions.normEq((String)obj,name);
		if(obj instanceof Integer)
			return ((Integer)obj) == id;
		return false;
	}
	
	/**
	 * Creates a summary of all the Queues this Player has played in, as well as the games this program has tracked of the Player's.
	 * @return String summary of this Player's queues
	 */
	public String summary(){
		if(!isTracked(name)){
			return NTSummary();
		}
		ArrayList<QueueSummary> summaries;
		try{
			summaries = RiotAPI.getSummary(id);
		}
		catch(Exception e){
			summaries = new ArrayList<QueueSummary>();
		}
		int wins = 0, streak = 1;
		int kills = 0, deaths = 0, assists = 0, streakK = 0, streakD = 0, streakA = 0;
		boolean winStreak = recent.get(0).stats.isWin(), broken = false;
		String firstGame = Time.setDate(games.get(0).gameDate).date();
		String lastGame = Time.setDate(recent.get(0).createDate).date();
		PlayerStats stats;
		for(int i = games.size()-1; i>0; i--){
			stats = games.get(i).playedAsSummoner.stats;
			int gameKills = stats.getChampionsKilled();
			int gameDeaths = stats.getNumDeaths();
			int gameAssists = stats.getAssists();
			kills += gameKills;
			deaths += gameDeaths;
			assists += gameAssists;
			if(!broken)
				if(stats.isWin() == winStreak){
					streak++;
					streakK += stats.getChampionsKilled();
					streakD += stats.getNumDeaths();
					streakA += stats.getAssists();
				}
				else broken = true;
			if(stats.isWin())
				wins++;
		}
		String streakStr = winStreak ? "win" : "loss";
		String ret = String.format(" %s:%d from %s - %s\n     %dW-%dL %1.2f%% (%d game %s streak)", name, id, firstGame, lastGame, 
										wins, games.size()-wins, (double)wins/(double)(games.size()), streak, streakStr);
		for(QueueSummary qs: summaries){
			String in;
			if(!(in = qs.summary()).equals(""))
					ret += "\n" + in;
		}
		ret += "\n" + String.format("      TOTALS - %02d/%02d/%02d %2.2fKDA", kills, deaths, assists, 
				((double)(kills+assists)/(double)deaths));
		ret += "\n" + String.format("       AVGS  - %02d/%02d/%02d %2.2fKDA", kills/=games.size(), deaths/=games.size(), assists/=games.size(), 
				((double)(kills+assists)/(double)deaths));
		ret += "\n" + String.format("      STREAK - %02d/%02d/%02d %2.2fKDA", streakK/=streak, streakD/=streak, streakA/=streak,
				((double)(streakK+streakA)/(double)streakD));
		ret += "\n";
		return ret;
	}
	
	/**
	 * Creates a summary for a Player who is not currently being tracked.&nbsp;
	 * Will return summaries of each queue typethis Player has playerd.
	 * @return String summary of every queue for this Player
	 */
	public String NTSummary(){
		int wins = 0, streak = 1;
		int kills = 0, deaths = 0, assists = 0, streakK = 0, streakD = 0, streakA = 0;
		boolean winStreak = recent.get(0).stats.isWin(), broken = false;
		String firstGame = Time.setDate(recent.get(0).createDate).date();
		String lastGame = Time.setDate(recent.get(0).createDate).date();
		PlayerStats stats;
		for(int i = recent.size()-1; i>0; i--){
			stats = recent.get(i).stats;
			int gameKills = stats.getChampionsKilled();
			int gameDeaths = stats.getNumDeaths();
			int gameAssists = stats.getAssists();
			kills += gameKills;
			deaths += gameDeaths;
			assists += gameAssists;
			if(!broken)
				if(stats.isWin() == winStreak){
					streak++;
					streakK += stats.getChampionsKilled();
					streakD += stats.getNumDeaths();
					streakA += stats.getAssists();
				}
				else broken = true;
			if(stats.isWin())
				wins++;
		}
		String streakStr = winStreak ? "win" : "loss";
		String ret = String.format(" %s:%d from %s - %s\n     %dW-%dL %1.2f%% (%d game %s streak)", name, id, firstGame, lastGame, 
										wins, recent.size()-wins, (double)wins/(double)(recent.size()), streak, streakStr);
		ret += "\n" + String.format("     TOTALS - %02d/%02d/%02d %2.2fKDA", kills, deaths, assists, 
											((double)(kills+assists)/(double)deaths));
		ret += "\n" + String.format("      AVGS  - %02d/%02d/%02d %2.2fKDA", kills/=recent.size(), deaths/=recent.size(), assists/=recent.size(), 
											((double)(kills+assists)/(double)deaths));
		ret += "\n" + String.format("     STREAK - %02d/%02d/%02d %2.2fKDA ", streakK/=streak, streakD/=streak, streakA/=streak,
											((double)(streakK+streakA)/(double)streakD));
		return ret;
	}
	
	/**
	 * A convenience method for getting the Player's name, id, level, and number of games tracked.
	 * @return String summary of this Player in the program
	 */
	public String summaryShort() {
		Debug.checkCall(getGames());
		return  summaryMini() + " - " + getGames().size() + " games";	
	}
	
	/**
	 * A convenience method for getting the Player's name, id, and level.
	 * @return String summary of this Player's metadata
	 */
	public String summaryMini() {
		return name + ":" + id + " (Lvl " + level + ")";
	}

	/**
	 * Removes a game from the Player's list of tracked games.
	 * @param the game to be removed
	 */
	public void removeGame(GameStat g){
		games.remove(g);
	}
	
	/**
	 * A static method for adding a Player to the tracked list.
	 * @param the Player to be tracked
	 */
	public static void addTracked(Player p){
		if(tracked == null)
			tracked = new ArrayList<Player>();
		tracked.add(p);
	}
	
	/**
	 * Get the tracked Player by summoner name.&nbsp; Will return null if the Player is not being tracked.
	 * @param name of the Player to be retrieve
	 * @return the tracked Player with the given name
	 */
	public static Player getTracked(String name){
		if(name == null)
			return null;
		Player temp = null;
		for(Player p: tracked){
			if(StringFunctions.normEq(p.name, name))
				temp = p;
		}
		return temp;
	}
	
	/**
	 * Removes a specific Player from the tracked list.&nbsp;
	 * This method is thorough in that it also removes all games played by this Player from the program as well
	 * @param name of the Player to be removed
	 */
	public static void removeTracked(String name){
		Logger.loudLog(".");
		Player temp;
		if((temp=getTracked(name)) == null)
			return;
		Logger.loudLog(".");
		ArrayList<GameStat> allGames = GameStat.getGames();
		for(GameStat g: temp.getGames())
			allGames.remove(g);
		Logger.loudLog(".");
		tracked.remove(temp);
		Logger.loudLog(". ");
		Logger.loudLogLine(name + " has been deleted");
	}
	
}
