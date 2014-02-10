import java.util.ArrayList;


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
		return games;
	}
	
	public void addGame(GameStat game){
		if(games == null)
			games = new ArrayList<GameStat>();
		games.add(game);
		if(!GameStat.getGames().contains(game))
			GameStat.allGames.add(game);
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
	}	
	
	//throws exception when player does not exist
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
			if(normEq(p.name, summonerName))
				return true;
		return false;
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
	
	private static boolean normEq(String s1, String s2){
		s1 = s1.replace(" ", "").replace("'", "").toLowerCase();
		s2 = s2.replace(" ", "").replace("'", "").toLowerCase();
		return s1.equals(s2);		
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
	
	public String summaryShort() {
		return  header() + " - " + games.size() + " games.";	
	}

	public String header() {
		return name + ":" + id + " (Lvl " + level + ")";
	}

	public static Player getTracked(String name) throws Exception{
		for(Player p: tracked){
			if(normEq(p.name, name))
				return p;
		}
		Player temp = new Player(RiotAPI.getSummoner(name));
		temp.updateRecent();
		tracked.add(temp);
		return temp;
	}
	

}
