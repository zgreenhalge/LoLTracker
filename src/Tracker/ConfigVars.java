package Tracker;
import java.util.ArrayList;

/**
 * An object to hold configuration variables that are stored on disk between sessions.&nbsp;
 * ConfigVars should only be accessed statically, as there is no way to instantiate it.&nbsp;
 * To set ConfigVars, an instance created by GSON is passed to add(ConfigVars).&nbsp;
 * 
 * @author Zach Greenhalge
 * @see IOHandler
 *
 */
public class ConfigVars {

	private static ConfigVars instance;
	private boolean completeOnly;
	private boolean devMode;
	private ArrayList<Long> incompleteGames;
	
	public static final String COMPLETE_ONLY = "completeOnly";
	public static final String DEV_MODE = "devMode";
	public static final String INCOMPLETE_GAMES = "incompleteGames";
	
/*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *!!!When you add a new variable, make sure it goes in every method except set(ConfigVars)!!!
 *!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */
	
	private ConfigVars(){
		completeOnly = false;
		devMode = false;
		incompleteGames = new ArrayList<Long>();
	}
	
	/**
	 * Initializes ConfigVars with the passed instance.&nbsp;
	 * If the passed object is null, all variables will be initialzed in their default values.
	 * 
	 * @param cv - instance of ConfigVars to initialize with.
	 */
	public static void set(ConfigVars cv){
		if(cv == null)
			instance = new ConfigVars();
		else
			instance = cv;
		
	}
	
	/**
	 * Returns an instance of ConfigVars with identical fields to the running copy.
	 * 
	 * @return a ConfigVars instance with the same fields as the static parent.
	 */
	public static ConfigVars getVars(){
		return instance;
	}
	
	/**
	 * Returns an Object representation of the variable String passed in.&nbsp;
	 * Will return null if the String passed is null or if the variable is not found. 
	 * 
	 * @param variable - a String representation of the variable to be retrieved
	 * @return an Object representation of the passed variable String
	 */
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
	
	/**
	 * Set a variable by passing the name of the variable and an Object representation of it.&nbsp;
	 * 
	 * @param variable - a String representation of the variable to be set
	 * @param value - an Object representation of the variable to be set
	 */
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
	
	/**
	 * Creates a String with every variable name as well as the value it currently holds.
	 * 
	 * @return a String with the name of every variable and it's value.
	 */
	public static String listVars(){
		return instance.toString();
	}
	
	/**
	 * Override of Object.&nbsp;toString().&nbsp;
	 * Used in ConfigVars.&nbsp;listVars().
	 * 
	 * @return a String representation of the ConfigVars instance
	 */
	@Override
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
