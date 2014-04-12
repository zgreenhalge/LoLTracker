package Tracker;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

/**
 * In charge of methodically creating calls to Riot Servers.&nbsp; Always used conjunction with a RiotCaller.
 * @author Zach Greenhalge
 * @see RiotCaller
 *
 */
public class RiotAPI {
	
	private static String reqURL;
	private static Gson creator = new Gson();
	private static RiotCaller caller = new RiotCaller();
	
	/**
	 * Generates the query to Riot servers to retrieve a summary of a Player's queues by their ID.
	 * @param id of the Player who's summaries are to be retrieved
	 * @return a List of QueueSummary
	 * @throws Exception when there is an issue retrieving the data
	 */
	public static ArrayList<QueueSummary> getSummary(int summonerId) throws Exception{
		SummaryReturn temp = null;
		String string;
		try{
			reqURL = "https://prod.api.pvp.net/api/lol/na/v1.2/stats/by-summoner/"
																+ summonerId + "/summary?season=SEASON4&api_key=";
			string = caller.getJson(reqURL);
		}
		catch(Exception e){
			Logger.loudLogLine("Problem retrieving summary for summoner " + summonerId);
			throw e;
		}
		temp = creator.fromJson(string, SummaryReturn.class);
		return temp.playerStatSummaries;
	}
	
	public static PlayerReturn getSummoner(String summonerName) throws Exception{
		PlayerReturn temp = null;
		char[] string;
		try {
			reqURL = "https://prod.api.pvp.net/api/lol/na/v1.3/summoner/by-name/"
														+ summonerName + "?api_key=";
			string = caller.getJson(reqURL).toCharArray();
		} catch (Exception e) {
			Logger.loudLogLine("Problem retrieving summoner " + summonerName + ". Please check your spelling.");
			throw e;
		}
		boolean go=false;
		String finalString = "";
		for(int i=1; i<string.length; i++){
			if(string[i] == '{')
				go = true;
			if(go)
				finalString += string[i];
		}
		finalString = finalString.replace("}}", "}");
		temp = creator.fromJson(finalString, PlayerReturn.class);
		return temp;
	}
	
	public static PlayerReturn getSummonerById(int summonerId) throws Exception{
		PlayerReturn temp = null;
		char[] string;
		try {
			reqURL ="https://prod.api.pvp.net/api/lol/na/v1.3/summoner/" + summonerId + "?api_key=";
			string = caller.getJson(reqURL).toCharArray();
		} catch (Exception e) {
			Logger.loudLogLine("Problem retrieving summoner " + summonerId);
			throw e;
		}
		boolean go=false;
		String finalString = "";
		for(int i=1; i<string.length; i++){
			if(string[i] == '{')
				go = true;
			if(go)
				finalString += string[i];
		}
		finalString = finalString.replace("}}", "}");
		temp = creator.fromJson(finalString, PlayerReturn.class);
		return temp;
	}
	
	public class GameReturn{
		ArrayList<RecentGame> games;
	}
	
	public static ArrayList<RecentGame> getRecentGames(int summonerId) throws Exception{
		GameReturn temp = null;
		String string;
		try{
			reqURL = "https://prod.api.pvp.net/api/lol/na/v1.3/game/by-summoner/"
												+ summonerId + "/recent?api_key=";
			string = caller.getJson(reqURL);
		} catch(Exception e){
			Logger.loudLogLine("Problem retrieving summoner's " + summonerId + " recent games");
			throw e;
		}
		temp = creator.fromJson(string, GameReturn.class);
		return temp.games;
	}
	
	public static ArrayList<Champion> getChampions() throws Exception{
		Type champType = new TypeToken<ArrayList<Champion>>(){}.getType();
		String champJson;
		reqURL = "https://prod.api.pvp.net/api/lol/na/v1.1/champion?api_key=";
		champJson = caller.getJson(reqURL);
		champJson = champJson.replace("{\"champions\":", "").replace("}]}", "}]");
		return creator.fromJson(champJson, champType);

	}
	
	public static ArrayList<Item> getItems() throws Exception{
		String itemJson;
		reqURL = "https://prod.api.pvp.net/api/lol/static-data/na/v1/item?locale=en_US&itemListData=all&api_key=";
		itemJson = caller.getJson(reqURL);
		int dataStart = itemJson.indexOf("data") + 6;
		int dataEnd = itemJson.lastIndexOf("},\"groups\"");
		char[] chars = itemJson.toCharArray();
		chars[dataStart] = '[';
		chars[dataEnd] = ']';
		Pattern regex = Pattern.compile("(\"[0-9]{4}\":)");
		Matcher matcher = regex.matcher(itemJson);
		String itemId;
		String toReplace;
		String replaceWith;
		itemJson = new String(chars);
		int lastIndex;
		while(matcher.find()){
			lastIndex = matcher.start(); 
			itemId = itemJson.substring(lastIndex+1, lastIndex+5);
			toReplace = itemJson.substring(lastIndex, lastIndex+8);
			replaceWith = "{\"id\":" + itemId + ",";
			itemJson = itemJson.replace(toReplace, replaceWith);
			matcher.reset(itemJson);
		}
		return creator.fromJson(itemJson, ItemReturn.class).data;
	}
	
	private class ItemReturn{
		ArrayList<Item> data;
	}
	
	private class SummaryReturn{
		ArrayList<QueueSummary> playerStatSummaries;
		
	}
	
	public class PlayerReturn{
		String name;
		int id;
		int summonerLevel;
	}
	
		
}
