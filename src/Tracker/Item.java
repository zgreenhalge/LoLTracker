package Tracker;
import java.util.ArrayList;
import java.util.List;

/**
 * Data and statistics pertaining to a specific item.&nbsp; 
 * Contains getters and setters, as well as several static methods to find/upload Item objects.
 * 
 * @author Zach Greenhalge
 *
 */
public class Item {

	public static ArrayList<Item> items;
	
	private ArrayList<String> tags;
	private int id;
	private String plaintext;
	private ItemStat stats;
	private String name;
	private ItemImage image;
	private GoldValue gold;
	private int[] into;
	
	 /**
     * Generates a statistical summary of this Item for the given set of GameStat.&nbsp;
     * If multiple Players buy this Item, all players will be included in the summary.
     * 
     * @param games - the games over which this Item should be summarized
     * @return A NonPlayerStats summary of this Item in the given GameStats
     */
	public NonPlayerStats generateStats(List<GameStat> games){
    	NonPlayerStats ret = new NonPlayerStats();
    	for(GameStat g: games){
    		if(g.incomplete)
    			ret.add(g.playedAs());
    		for(Summoner s: g.blueTeam.activePlayers)
    			for(Item i: s.getItems())
    				if(i.getId() == id)
    					ret.add(s);
    		for(Summoner s: g.purpleTeam.activePlayers)
    			for(Item i: s.getItems())
    				if(i.getId() == id)
    					ret.add(s);
    	}
    	return ret;
    }
	
	/**
     * Loads all Item data from Riot servers. 
     * 
     * @throws Exception when there is a communication issue with Riot servers
     */
	public static void init() throws Exception{
		Logger.loudLogLine("Loading item data..");
		items = RiotAPI.getItems();
	}
	
	 /**
     * Finds an item by ID.&nbsp;
     * This method may throw errors after a patch if item IDs have changed or if items have been removed from the game.&nbsp;
     * The only workaround for this issue at the time is to remove the game files with the old item ids.
     * 
     * @param id of the Item to be retrieved
     * @return Item with matching ID
     * @throws Exception when an Item with the given ID is not found
     */
	public static Item findItem(int id) throws Exception{
		for(Item i: items)
			if(i.id==id)
				return i;
		throw new Exception("Item " + id + " does not exist. " + items.size() + " checked.");
	}
	
	public ArrayList<String> getTags(){
		return tags;
	}
	
	public int getId(){
		return id;
	}
	
	public String getPlainText(){
		return plaintext;
	}
	
	public String getName(){
		return name;
	}
	
	public ItemStat getStats(){
		return stats;
	}
	
	public GoldValue getGold(){
		return gold;
	}
	
	public int[] getInto(){
		return into;
	}
}
