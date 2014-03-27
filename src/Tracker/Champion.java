package Tracker;
import java.util.ArrayList;


public class Champion {
	
	public static ArrayList<Champion> champions;
	
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
    
    public static void init() throws Exception{
    	System.out.println("Loading champion data..");
    	champions = RiotAPI.getChampions();
    }
    
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
    
    public String toString(){
    	return name + ":" + id + (freeToPlay ? " (FREE)" : "");
    }
}
