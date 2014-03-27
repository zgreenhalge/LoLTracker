package Tracker;
import java.util.ArrayList;

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
	
	public static void init() throws Exception{
		System.out.println("Loading item data..");
		items = RiotAPI.getItems();
	}
	
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
