package Tracker;
import java.util.ArrayList;


public class ConfigVars {

	private static ConfigVars instance;
	private boolean completeOnly;
	private boolean devMode;
	private ArrayList<Long> incompleteGames;
	
	public static final String COMPLETE_ONLY = "completeOnly";
	public static final String DEV_MODE = "devMode";
	public static final String INCOMPLETE_GAMES = "incompleteGames";
	
	/*
	 * When you add a new variable, make sure it goes in every method except set(ConfigVars)
	 */
	
	private ConfigVars(){
		completeOnly = false;
		devMode = false;
		incompleteGames = new ArrayList<Long>();
	}
	
	public static void set(ConfigVars cv){
		if(cv == null)
			instance = new ConfigVars();
		else
			instance = cv;
		
	}
	
	public static ConfigVars getVars(){
		return instance;
	}
	
	public static Object get(String variable){
		if(variable == null)
			return null;
		else if(variable.equals(COMPLETE_ONLY))
			return instance.completeOnly;
		else if(variable.equals(DEV_MODE))
			return instance.devMode;
		else if(variable.equals(INCOMPLETE_GAMES))
			return instance.incompleteGames;
		else
			return null;
			
	}
	
	@SuppressWarnings("unchecked")
	public static void set(String variable, Object value){
		if(variable == null)
			return;
		else if(variable.equals(COMPLETE_ONLY))
			instance.completeOnly = (Boolean)value;	
		else if(variable.equals(DEV_MODE))
			instance.devMode = (Boolean)value;
		else if(variable.equals(INCOMPLETE_GAMES))
			instance.incompleteGames = (ArrayList<Long>)value;
	}
	
	public static String listVars(){
		return instance.toString();
	}
	
	public String toString(){
		String ret = "";
		ret += "\n Complete Only___________" + instance.completeOnly;
		ret += "\n Dev mode enabled________" + instance.devMode;
		ret += "\n Known incomplete games (" + incompleteGames.size()+"/"+GameStat.numGames()+"): ";
		Long l;
		for(int i=0; i< incompleteGames.size(); i++){
			l = incompleteGames.get(i);
			ret += (i != incompleteGames.size()-1 ? l + ",": l);
		}
		if(incompleteGames.size()==0)
			ret += "NONE";
		return ret;
	}
	
}
