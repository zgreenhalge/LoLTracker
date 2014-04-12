package Tracker;

import java.util.ArrayList;
import GUI.GamePanel;
import Utils.*;

/**
 * A representation of a specific game played.&nbsp;
 * During initialization if a RecentGame with the same ID as this cannot be found in a fellow player's history,
 *  this GameStat will become marked as incomplete.&nbsp;
 *  From that point on, only information relating to the playedAs Player should be accessed, and any guarantee made involving another Player should be ignored.
 * 
 * @author Zach Greenhalge
 * @see RecentGame
 * @see Summoner
 * @see TeamStruct
 */
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
        ranked = (subType = game.getSubType()).contains("RANKED_");
        Logger.loudLog("    Building game "+ gameId + " (" + Time.setDate(gameDate).dateTime() + ")");
        try{
        	playedAs = p.getMini();
        	playedAs.championId = game.getChampionId();
        	playedAs.teamId = game.getTeamId();
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
    
    /**
     * Returns the Summoner Object of the player who played this game.
     * 
     * @return Summoner of the main Player
     */
    public Summoner playedAs(){
        return playedAsSummoner;
    }
    
    /**
     * Generates the GamePanel for this game.
     * 
     * @return GamePanel with the relevant fields completed.
     */
    public GamePanel generatePanel(){
        PlayerStats temp = playedAsSummoner.stats;
        Summoner pTemp = playedAsSummoner;
        GamePanel ret = new GamePanel();
        String label;
        if(pTemp.won()){
            ret.color(GamePanel.GREEN);
        }
        else
            ret.color(GamePanel.RED);
        label = subType + " - " + (pTemp.won() ? "WIN" : "LOSS");
        ret.setQueueLabel(label);
        try{
            ret.setPlayedAsName_Level(Player.getTracked(playedAs.name).summaryMini());
        }
        catch(Exception e){
            Logger.loudLogLine("Error creating panel for game " + gameId);
        }
        ret.setKDARatioValue(String.valueOf(playedAsSummoner.KDAratio()));
        ret.setCSValue(String.valueOf(playedAsSummoner.getCS()));
        ret.setGoldValue(String.valueOf(playedAsSummoner.getGold()));
        ret.setWardsPlacedValue(String.valueOf(playedAsSummoner.wardsPlaced()));
        ret.setKillValue(String.valueOf(playedAsSummoner.kills()));
        ret.setDeathValue(String.valueOf(playedAsSummoner.deaths()));
        ret.setAssistValue(String.valueOf(playedAsSummoner.assists()));
        ret.setDamageDealtValue(String.valueOf(""));
        return null;
    }
    
    /**
     * Returns a short summary of the GameStat with the following information:
     * 
     * -ID
     * -queue type
     * -incomplete status
     * -date played
     * -summoner game was played by
     * 
     * @return a String summary of the GameStat
     */
    public String summaryShort(){
    	return "ID: " + gameId + " " + subType + 
    			(incomplete ? " (INCOMPLETE) " : " (COMPLETE) on ") + Time.setDate(gameDate).dateTime() +
    			"\n    Played by: " + playedAsSummoner.summary(incomplete);
    }
    
    /**
     * Returns a full summary of the GameStat by calling summary() for each team and then calling summaryInDepth() for each Summoner on the team.&nbsp;
     * Also creates and returns an MVP and runner up choice.&nbsp;
     * 
     * If this GameStat is incomplete, will return instead summaryShort() followed by a summaryInDepth() call to the played as Summoner.
     * 
     * @return a String representation of this GameStat
     */
    public String summary(){
    	String ret = summaryShort();
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
    
    /**
     * Finds the MVP based on game contribution.&nbsp;
     * Game contribution is calculated by subtracting the death contribution of a player from their kill contribution.&nbsp;
     * 
     * @return a String representation of the MVP results
     * @see Summoner
     */
    private String mvp(){
    	if(incomplete) return "Incomplete game - no MVP can be determined";
    	Summoner mvp = null;
    	Summoner mnt = null;
    	for(Summoner s: blueTeam){
    		if(mvp == null || s.getContribution() > mvp.getContribution() && s.stats.isWin())
    			mvp = s;
    		else if(mnt == null || s.getContribution() > mnt.getContribution() && s != mvp)
    			mnt = s;
    	}
    	for(Summoner s: purpleTeam){
    		if(mvp == null || s.getContribution() > mvp.getContribution() && s.stats.isWin())
    			mvp = s;
    		else if(mnt == null || s.getContribution() > mnt.getContribution() && s != mvp)
    			mnt = s;
    	}
    	return "MVP: " + mvp.summary(incomplete).replace(":"+mvp.summoner.summonerId, "") + "\n" +
    			"  Honorable mention: " + mnt.summary(incomplete).replace(":"+mnt.summoner.summonerId, "");
    }
     
    @Override
    public boolean equals(Object obj){
    	if(obj == null)
    		return false;
    	long objId;
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
    
    /**
     * A static method to determine if a game exists.&nbsp;
     * More precisely, will return true if the Object passed is a GameStat object and it exists, 
     * or if the Object passed is either a Long or a String and a GameStat object exists with an equivalent ID.
     * Returns false if the given Object is null, or if there are no games created yet. 
     * 
     * @param obj - a GameStat object or a String or Long of a game's ID
     * @return true if the game exists already, false if otherwise (or invalid input)
     */
    public static boolean contains(Object obj){
    	if(obj == null)
    		return false;
    	if(allGames == null || allGames.isEmpty())
    		return false;
    	return allGames.contains(obj);
    }
    
    /**
     * Returns the total number of existing games
     * 
     * @return total number of GameStat object
     */
    public static int numGames(){
    	return allGames.size();
    }
    
    /**
     * Loads the GameStat object.&nbsp;
     * Used when a GameStat is read from file in order to initialize fields and run calculations.
     * 
     * @throws Exception when a Summoner loads incorrectly
     */
    public void load() throws Exception{
    	Player temp = Player.getTracked(playedAs.name);
    	for(Summoner s: blueTeam){
    		s.load(blueTeam);
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
    
    /**
     * Returns an ArrayList of all existing GameStat objects.
     * 
     * @return ArrayList of all GameStat objects in existence
     */
    public static ArrayList<GameStat> getGames(){
    	if(allGames == null)
    		allGames = new ArrayList<GameStat>();
    	return allGames;
    }
    
    /**
     * Used to initialize GameStat's static portions.&nbsp;
     * Any GameStat not on the passed List will be lost. 
     * 
     * @param games - a List of GameStat
     */
    public static void setGames(ArrayList<GameStat> games){
    	allGames = games;
    }
    
    /**
     * Removes a GameStat from existence.
     * 
     * @param id of the GameStat to be deleted
     */
    public static void delete(long id){
    	for(GameStat g: allGames){
    		if(g.equals(new Long(id))){
    			allGames.remove(g);
    			Player.getTracked(g.playedAs.name).removeGame(g);
    			return;
    		}
    	}
    }

    /**
     * Implements Comparable.&nbsp; Returns the difference of the dates of the games.&nbsp;
     * More specifically, returns this.&nbsp;gameDate-((GameStat)obj).&nbsp;gameDate as an int.
     * 
     * @return int difference of the dates of the two games
     */
	@Override
	public int compareTo(Object obj){
		long ret = gameDate - ((GameStat)obj).gameDate;
		return (int)ret;
	}
     
}
