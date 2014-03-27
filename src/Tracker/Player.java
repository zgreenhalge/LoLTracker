package Tracker;
import java.util.ArrayList;
import Utils.*;

public class Player {

	private static transient ArrayList<Player> players = new ArrayList<Player>();
	private static transient ArrayList<Player> tracked = new ArrayList<Player>();
	public String name;
	public int level;
	public int id;
	private String note;
	private transient ArrayList<RecentGame> recent = new ArrayList<RecentGame>();	//serialized separately
	private transient ArrayList<GameStat> games = new ArrayList<GameStat>(); 	//serialized separately
	
	public Player(RiotAPI.PlayerReturn base){
			name = base.name;
			id = base.id;
			level = base.summonerLevel;
	}
		
	public PlayerMini getMini(){
		PlayerMini miniMe = new PlayerMini();
		miniMe.summonerId = id;
		miniMe.name = name;
		return miniMe;
	}
	
	public void setNote(String s){note = s;}
	
	public String getNote(){return note;}
	
	public RecentGame findGame(int id){
		for(RecentGame rg: recent)
			if(rg.gameId == id)
				return rg;
		return null;
	}
	
	public static ArrayList<Player> getTracked(){
		cleanse();
		return tracked;
	}
	
	public static ArrayList<Player> getAll(){
		cleanse();
		return players;
	}
	
	public ArrayList<RecentGame> getRecentGames(){
		return recent;
	}
	
	public ArrayList<GameStat> getGames(){
		if(games == null)
			games = new ArrayList<GameStat>();
		return games;
	}
	
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
	
	private static void cleanse(){
		while(tracked.remove(null));
		while(players.remove(null));
	}
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
	}
	
	public void updateRecent(){
		try{
	        recent = RiotAPI.getRecentGames(id);
	    }
	    catch(Exception e){
	        Logger.loudLog(e);
	    }
	}

	public static void update(String string) {
		for(Player p: players){
			if(p.name.equalsIgnoreCase(string)){
				p.update();
				break;
			}
		}
	}
	
	public static void updateAll(){
		for(Player p: tracked)
			p.update();
		Logger.loudLogLine("All players are up-to-date with Riot servers");
	}	
	
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
	
	public static Player findPlayerById(int id) throws Exception{
		for(Player p: players)
			if(p.id == id)
				return p;
		Player temp = new Player(RiotAPI.getSummonerById(id));
		players.add(temp);
		temp.updateRecent();
		return temp;
	}
	
	public static boolean isTracked(String summonerName){
		for(Player p: tracked)
			if(StringFunctions.normEq(p.name, summonerName))
				return true;
		return false;
	}
	
	public static boolean isTracked(int id){
		for(Player p: tracked)
			if(id == p.id)
				return true;
		return false;
	}
	
	public static Player find(String summonerName) throws Exception{
		Player ret = null;
		if((ret = getTracked(summonerName)) == null){
			ret = findPlayer(summonerName);
		}
		return ret;
	}
	
	//throws exception when player does not exist
	public static Player addPlayer(String summonerName) throws Exception{
		Player temp = null;
		for(Player p: tracked)
			if(p.equals(summonerName)){
				temp = p;
				break;
			}
		if(temp == null){
			for(Player p: players)
				if(p.equals(summonerName)){
					temp = p;
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
		return null;
	}
	
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
	
	public static void resetTracked(){
		tracked = new ArrayList<Player>();
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof Player)
			return ((Player)obj).id == id;
		if(obj instanceof String)
			return ((String)obj).equals(name);
		if(obj instanceof Integer)
			return ((Integer)obj) == id;
		return false;
	}
	
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
		int kills = 0, deaths = 0, assists = 0, streakK = 0, streakD = 0, streakA = 0,
				aKills, aDeaths, aAssists;
		boolean winStreak = recent.get(0).stats.isWin(), broken = false;
		String firstGame = Time.setDate(games.get(0).gameDate).date();
		String lastGame = Time.setDate(recent.get(0).createDate).date();
		PlayerStats stats;
		AggregatedStats aStats;
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
		//TODO each summary type should get a toString() variant - work on that. enums maybe?
		return ret;
	}
	
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
	
	public String summaryShort() {
		Debug.checkCall(getGames());
		return  summaryMini() + " - " + getGames().size() + " games";	
	}

	public String summaryMini() {
		return name + ":" + id + " (Lvl " + level + ")";
	}

	public void removeGame(GameStat g){
		games.remove(g);
	}
	
	public static void addTracked(Player p){
		if(tracked == null)
			tracked = new ArrayList<Player>();
		tracked.add(p);
	}
	
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
