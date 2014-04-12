package Tracker;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.imageio.ImageIO;

/**
 * Data and statistics pertaining to a specific champion.&nbsp; 
 * Contains getters and setters, as well as several static methods to find/upload Champion objects.
 * 
 * @author Zach Greenhalge
 *
 */
public class Champion {
	
    public static ArrayList<Champion> champions;

    
    //TODO add images
    private boolean active;
    private int attackRank;
    private boolean botEnabled;
    private boolean botMmEnabled;
    private int defenseRank;
    private int difficultyRank;
    private boolean freeToPlay;
    private long id;
    private int magicRank;
    private String name;
    private boolean rankedPlayEnabled;
    private static final String iconPath = Main.getRoot() + "resources" + Main.getSeperator() + "icons" + Main.getSeperator() + "champions";
    private static final File FileNotFound = new File(iconPath + Main.getSeperator() + "null");
    private BufferedImage icon;
    
    /**
     * Generates a statistical summary of this Champion for the given set of GameStat.&nbsp;
     * If multiple Players play this Champion, both players will be included in the summary.
     * 
     * @param games - the games over which this Champion should be summarized
     * @return A NonPlayerStats summary of this Champion in the given GameStats
     */
    public NonPlayerStats generateStats(List<GameStat> games){
    	NonPlayerStats ret = new NonPlayerStats();
    	for(GameStat g: games){
    		if(g.incomplete)
    			ret.add(g.playedAs());
    		for(Summoner s: g.blueTeam.activePlayers)
    			if(s.champ.name.equals(name))
    				ret.add(s);
    		for(Summoner s: g.purpleTeam.activePlayers)
    			if(s.champ.name.equals(name))
    				ret.add(s);
    	}
    	return ret;
    }
    
    /**
     * Loads all Champion data from Riot servers. 
     * 
     * @throws Exception when there is a communication issue with Riot servers
     */
    public static void init() throws Exception{
    	Logger.loudLogLine("Loading champion data..");
    	champions = RiotAPI.getChampions();
    }
    
    public BufferedImage getImage(){
        if(icon == null){
            File iconFile = new File(iconPath + Main.getSeperator() + name);
            try {
                icon = ImageIO.read(iconFile);
            } catch (IOException ex) {
                try {
                    icon = ImageIO.read(FileNotFound);
                } catch (IOException ex1) {
                    Logger.log(ex1);
                }
            }
        }
        return icon;
    }
    
    /**
     * Finds a champion by ID.&nbsp;
     * This method may throw errors after a patch if champion IDs have changed.&nbsp;
     * The only workaround for this issue at the time is to remove the game files with the old champion ids.
     * 
     * @param id of the champion to be retrieved
     * @return Champion with matching ID
     * @throws Exception when a champion with the given ID is not found
     */
    public static Champion findChampion(int id) throws Exception{
		for(Champion c: champions)
			if(c.getId() == id)
				return c;
		throw new Exception("Champion id [" + id + "] does not exist. " + champions.size() + " champions checked.");
	}
    
    public boolean isActive() {
            return active;
    }
    public int getAttackRank() {
            return attackRank;
    }
    public boolean isBotEnabled() {
            return botEnabled;
    }
    public boolean isBotMmEnabled() {
            return botMmEnabled;
    }
    public int getDefenseRank() {
            return defenseRank;
    }
    public int getDifficultyRank() {
            return difficultyRank;
    }
    public boolean isFreeToPlay() {
            return freeToPlay;
    }
    public long getId() {
            return id;
    }
    public int getMagicRank() {
            return magicRank;
    }
    public String getName() {
            return name;
    }
    public boolean isRankedPlayEnabled() {
            return rankedPlayEnabled;
    }
    
    @Override
    public String toString(){
    	return name + ":" + id + (freeToPlay ? " (FREE)" : "");
    }
}
