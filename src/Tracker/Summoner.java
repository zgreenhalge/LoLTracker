package Tracker;
import java.util.ArrayList;


public class Summoner {

    PlayerMini summoner;
    PlayerStats stats;
    boolean won;
    int[] items = new int[7];
    transient int kills, deaths, assists, gold, cs, wards;
    transient RecentGame game;
    transient TeamStruct teamStr;
    transient Champion champ;
    transient ArrayList<Item> usedItems;
    
    public Summoner(Player player, TeamStruct str, long gameId) throws Exception{
        for(RecentGame rg: player.getRecentGames()){
        	if(rg.gameId == gameId){
        		game = rg;
        		break;
        	}
        }
        if(game == null)
        	throw new Exception("Game "+ gameId + " does not exist in recent games\n  " + player.name + ":" + player.id);
        stats = game.getStats();
        won = stats.isWin();
        summoner = player.getMini();
        summoner.championId = game.getChampionId();
        summoner.teamId = game.getTeamId();
        load(str);           
    }
    
    public Summoner(Player player, RecentGame game, TeamStruct blue, TeamStruct purple) throws Exception{
    	summoner = player.getMini();
        this.game = game;
        stats = game.getStats();
        won = stats.isWin();
        summoner.championId = game.getChampionId();
        summoner.teamId = game.getTeamId();
        if(summoner.teamId == 100)
        	load(blue);
        else
        	load(purple);
    }
    
    public boolean won(){
        return won;
    }
    
    public PlayerMini getPlayer(){
        return summoner;
    }
    
    public PlayerStats getStats(){
    	return stats;
    }
    
    public ArrayList<Item> getItems(){
    	return usedItems;
    }
    
    public void load(TeamStruct str) throws Exception{
    	teamStr = str;
    	champ = Champion.findChampion(summoner.championId);
        usedItems = new ArrayList<Item>();
        kills = stats.getChampionsKilled();
        deaths = stats.getNumDeaths();
        assists = stats.getAssists();
        gold = stats.getGoldEarned();
        cs = stats.getCS();
        wards = stats.getWardPlaced();
        items[0] = stats.getItem0();
        items[1] = stats.getItem1();
        items[2] = stats.getItem2();
        items[3] = stats.getItem3();
        items[4] = stats.getItem4();
        items[5] = stats.getItem5();
        items[6] = stats.getItem6();
        for(int i: items){
        	if(i != 0)
        		try{
        			usedItems.add(Item.findItem(i));
        		}catch(Exception e){
        			Logger.log("Item " + i + "not found for " + summoner.name);
        		}
        }
    }
    
    public String summaryInDepth(boolean incomplete){
    	String repeatTimes = "";
    	switch(stats.getLargestMultiKill()){
    		case(0):case(1):default:break;
    		case(2):
    			repeatTimes = "(x"+stats.getDoubleKills()+")";
    			break;
    		case(3):
    			repeatTimes = "(x"+stats.getTripleKills()+")";
    			break;
    		case(4):
    			repeatTimes = "(x"+stats.getQuadraKills()+")";
    			break;
    		case(5):
    			repeatTimes = "(x"+stats.getPentaKills()+")";
    			break;
    			
    	}
    	return  summary(incomplete)
    			+ "   Damage dealt: " + String.format("%06d", stats.getTotalDamageDealt()) 
    			+ " Damage taken: " + String.format("%06d", stats.getTotalDamageTaken()) + "\n"
    			+ "    Damage healed: " + String.format("%06d", stats.getTotalHeal()) + " Wards placed: " + stats.getWardPlaced()
    			+ "  Largest multi-kill: " + stats.getLargestMultiKill() + repeatTimes
    			+ (stats.getFirstBlood() == 1 ? "\n  First blood" : "")
    			+ "\n    Items: " + listItems(3);
    }
    
    public String summary(boolean incomplete){
    	return summoner.name + ":" + summoner.summonerId + " ("+(won?"WIN":"LOSS")+")\n    "
    			+ KDAString() + String.format(" %02.2fKDA ", KDAratio())
    			+ cs + "CS (" + gold + " gold) as " + champ.getName()
    			+ (incomplete ? "" : "\n    Contribution: " + String.format("%+03.0f%%", getContribution()*100));
    }
    
    public String listItems(int num){
    	String ret = "";
    	//TODO print out num elements per line
        	for(int i=0; i<usedItems.size(); i++)
        		ret += usedItems.get(i).getName() + (i!=usedItems.size()-1 ? ", " : "");
    	return ret;
    }
    
    public double getKillParticipation(){
    	if(teamStr == null || teamStr.kills == 0)
    		return 0;
    	return ((double)(kills+assists))/teamStr.kills;
    }
    
    public double getKillContribution(){
    	if(teamStr == null || teamStr.kills == 0)
    		return kills;
    	return ((double)kills/teamStr.kills);
    }
    
    public int kills(){
        return kills;
    }
    
    public int deaths(){
        return deaths;
    }
    
    public int assists(){
        return assists;
    }
    
    public double getAssistContribution(){
    	if(teamStr == null || teamStr.assists == 0)
    		return assists;
    	return ((double)assists)/teamStr.assists;
    }
    
    public double getDeathContribution(){
    	if(teamStr == null || teamStr.deaths == 0)
    		return 0;
    	return ((double)(deaths))/teamStr.deaths;
    }
    
    public double getContribution(){
    	return getKillContribution() - getDeathContribution();
    }
    
    public String KDAString(){
    	return String.format("%02dK/%02dD/%02dA", stats.getChampionsKilled(), stats.getNumDeaths(), stats.getAssists());
    }
    
    public int KDAint(){
    	String ret = String.format("%02d%02d%02d", stats.getChampionsKilled(), stats.getNumDeaths(), stats.getAssists());
    	return Integer.parseInt(ret);
    }
    
    public long getGold(){
    	return gold;
    }
    
    public int getCS(){
        return cs;
    }
    
    public int wardsPlaced(){
        return wards;
    }
    
    public double KDAratio(){
    	if(stats.getNumDeaths()>0)
    		return (((double)kills+(double)assists)/(double)deaths);
    	return kills+assists;
    }
    
}
