package Tracker;
import java.util.ArrayList;
import Utils.*;

public class GameStat implements Comparable{
    
	boolean incomplete;
	boolean ranked;
    long gameId;
    long gameDate;
    long gameLength;
    String subType;
    PlayerMini playedAs;
    TeamStruct blueTeam = new TeamStruct();
    TeamStruct purpleTeam = new TeamStruct();
    transient Summoner playedAsSummoner;
    private static transient ArrayList<GameStat> allGames = new ArrayList<GameStat>();
    
    public GameStat(RecentGame game, Player p){
        gameId = game.gameId;
        gameDate = game.createDate;
        gameLength = game.stats.getTimePlayed();
        ranked = game.getSubType().contains("RANKED_");
        Logger.loudLog("    Building game "+ gameId + " (" + Time.setDate(gameDate).dateTime() + ")");
        try{
        	playedAs = p.getMini();
        	playedAs.championId = game.getChampionId();
        	playedAs.teamId = game.getTeamId();
        	subType = game.subType;
        	playedAsSummoner = new Summoner(p, game, blueTeam, purpleTeam);
        	if(game.teamId==100)
                blueTeam.activePlayers.add(playedAsSummoner);
            else
                purpleTeam.activePlayers.add(playedAsSummoner);
        	
        	try{
        		for(PlayerMini pm: game.fellowPlayers){
        			//TODO this can be optimized - add an arraylist of active players, add functionality in RiotAPI to get multiple Summoners
        			if(pm.teamId==100)
        				blueTeam.activePlayers.add(new Summoner(Player.findPlayerById(pm.summonerId), blueTeam, gameId));
        			else
        				purpleTeam.activePlayers.add(new Summoner(Player.findPlayerById(pm.summonerId), purpleTeam, gameId));
        			Logger.loudLog(".");
        		}
        	}
        	catch(Exception e){
        		Logger.log(e);
        		incomplete = true;
        	}
        }
        catch(Exception e){
        	Logger.log(e);
       	 	Logger.loudLog("\n  Error:" + e.getMessage() + "\n  in gameId: " + gameId);
        }
        
	    Logger.loudLogLine(incomplete ? " created incompletely" : " created!");
        blueTeam.init();
        purpleTeam.init();
    }
    
    public String summaryShort(){
    	return "ID: " + gameId + " " + subType + 
    			(incomplete ? " (INCOMPLETE) " : " (COMPLETE) on ") + Time.setDate(gameDate).dateTime() +
    			"\n    Played by: " + playedAsSummoner.summary(incomplete);
    }
    
    public String summary(){
    	String ret = "ID: " + gameId + " " + subType + 
    			(incomplete ? " (INCOMPLETE) " : "") + Time.setDate(gameDate).dateTime() + "\n";
    	ret += blueTeam.won ? StringFunctions.centerPad("$!$!$Blue Team Wins$!$!$", 60, ' ') + "\n" : StringFunctions.centerPad("$!$!$Purple Team Wins$!$!$", 60, ' ') + "\n"; 
    	if(incomplete) ret += playedAsSummoner.summaryInDepth(incomplete);
		else{
			ret += "\n" + StringFunctions.centerPad("BLUE TEAM", 60, '#');
			ret += "\n" + blueTeam.summary() + "\n\n";
			int pos = 0;
			for(Summoner s: blueTeam)
				ret += (++pos) + ")" + s.summaryInDepth(incomplete) + "\n";
			ret += "\n" + StringFunctions.centerPad("PURPLE TEAM", 60, '#');
			ret += "\n" + purpleTeam.summary() + "\n\n";
			pos = 0;
			for(Summoner s: purpleTeam)
				ret += (++pos) + ")" + s.summaryInDepth(incomplete) + "\n";
			ret += "\n  " + mvp() + "\n";
		}
		
		return ret;
    }
    
    private String mvp(){
    	if(incomplete) return "Incomplete game - no MVP can be determined";
    	Summoner mvp = null;
    	Summoner mnt = null;
    	for(Summoner s: blueTeam){
    		if(mvp == null || s.getGold() > mvp.getGold() && s.stats.isWin())
    			mvp = s;
    		else if(mnt == null || s.getGold() > mnt.getGold() && s != mvp)
    			mnt = s;
    	}
    	for(Summoner s: purpleTeam){
    		if(mvp == null || s.getGold() > mvp.getGold() && s.stats.isWin())
    			mvp = s;
    		else if(mnt == null || s.getGold() > mnt.getGold() && s != mvp)
    			mnt = s;
    	}
    	return "MVP: " + mvp.summary(incomplete).replace(":"+mvp.summoner.summonerId, "") + "\n" +
    			"  Honorable mention: " + mnt.summary(incomplete).replace(":"+mnt.summoner.summonerId, "");
    }
     
    @Override
    public boolean equals(Object obj){
    	if(obj == null)
    		return false;
    	long objId = 0;
    	if(obj instanceof GameStat)
    		objId = ((GameStat) obj).gameId;
    	else if(obj instanceof Integer)
    		objId = ((Integer)obj);
    	else if(obj instanceof Long)
    		objId = ((Long)obj);
    	else if(obj instanceof String)
    		objId = Integer.parseInt(((String)obj));
    	else return false;
    	return gameId == objId;
    }
    
    public static boolean contains(Object obj){
    	if(obj == null)
    		return false;
    	if(allGames == null || allGames.size() == 0)
    		return false;
    	long objId = 0;
    	if(obj instanceof GameStat)
    		objId = ((GameStat) obj).gameId;
    	else if(obj instanceof Long)
    		objId = ((Long)obj);
    	else if(obj instanceof String)
    		objId = Integer.parseInt(((String)obj));
    	else return false;
    	for(GameStat g: allGames){
    		if(g.gameId == objId)
    			return true;
    	}
    	return false;
    }
    
    public static int numGames(){
    	return allGames.size();
    }
    
    public void load() throws Exception{
    	Player temp = Player.getTracked(playedAs.name);
    	for(Summoner s: blueTeam){
    		s.load(blueTeam);
    		Debug.checkCall(temp.id);
    		Debug.checkCall(s.summoner.summonerId);
    		if(s.summoner.summonerId == temp.id)
    			playedAsSummoner = s;
    	}
    	for(Summoner s: purpleTeam){
    		s.load(purpleTeam);
	    	if(s.summoner.summonerId == temp.id)
				playedAsSummoner = s;
    	}
    	blueTeam.init();
    	purpleTeam.init();
    }
    
    public static ArrayList<GameStat> getGames(){
    	if(allGames == null)
    		allGames = new ArrayList<GameStat>();
    	return allGames;
    }
    
    public static void setGames(ArrayList<GameStat> games){
    	allGames = games;
    }
    
    public static void delete(long id){
    	for(GameStat g: allGames){
    		if(g.equals(new Long(id))){
    			allGames.remove(g);
    			Player.getTracked(g.playedAs.name).removeGame(g);
    			return;
    		}
    	}
    }

	@Override
	public int compareTo(Object obj){
		long ret = gameDate - ((GameStat)obj).gameDate;
		return (int)ret;
	}
     
}
