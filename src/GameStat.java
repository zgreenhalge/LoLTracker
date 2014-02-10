import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class GameStat{
    
	boolean incomplete;
    long gameId;
    long gameDate;
    long gameLength;
    String subType;
    PlayerMini playedAs;
    TeamStruct blueTeam = new TeamStruct();
    TeamStruct purpleTeam = new TeamStruct();
    transient Summoner playedAsSummoner;
    public static transient ArrayList<GameStat> allGames = new ArrayList<GameStat>();
    
    public GameStat(RecentGame game, Player p){
        gameId = game.gameId;
        gameDate = game.createDate;
        gameLength = game.stats.getTimePlayed();
        Logger.logLine("    Building game "+ gameId + " (" + Time.setDate(gameDate).dateTime() + ")");
        System.out.print("    Building game "+ gameId + " (" + Time.setDate(gameDate).dateTime() + ")");
        try{
        	playedAs = p.getMini();
        	playedAs.championId = game.getChampionId();
        	playedAs.teamId = game.getTeamId();
        	subType = game.subType;
        	playedAsSummoner = new Summoner(p, game);
        	if(game.teamId==100)
                blueTeam.activePlayers.add(playedAsSummoner);
            else
                purpleTeam.activePlayers.add(playedAsSummoner);
        }
        catch(Exception e){
        	e.printStackTrace();
        	//Logger.log(e);
       	 	//Logger.loudLog("\n  " + e.getMessage() + "\n   gameId: " + gameId);
        }
        try{
        	for(PlayerMini pm: game.fellowPlayers){
	    		//TODO this can be optimized - add an arraylist of active players, add functionality in RiotAPI to get multiple Summoners
		        if(pm.teamId==100)
		            blueTeam.activePlayers.add(new Summoner(Player.findPlayerById(pm.summonerId), blueTeam));
		        else
		            purpleTeam.activePlayers.add(new Summoner(Player.findPlayerById(pm.summonerId), purpleTeam));
		        Logger.loudLog(".");
	    	}
        }
    	catch(Exception e){
    		Logger.log(e);
       	 	incomplete = true;
    	}
	    Logger.loudLog(incomplete ? " created incompletely" : " created!");
	    System.out.println();
        blueTeam.init();
        purpleTeam.init();
    }
    
    public String summaryShort(){
    	return "ID: " + gameId + " " + subType + 
    			(incomplete ? " (INCOMPLETE) " : " (COMPLETE) on ") + Time.setDate(gameDate).dateTime() +
    			"\n Played by: " + playedAsSummoner.summary(incomplete);
    }
    
    public String summary(){
    	String ret = "ID: " + gameId + " " + subType + 
    			(incomplete ? " (INCOMPLETE) " : "") + Time.setDate(gameDate).dateTime() + "\n";
    	ret += blueTeam.won ? centerPad("$!$!$Blue Team Wins$!$!$", 60, ' ') + "\n" : centerPad("$!$!$Purple Team Wins$!$!$", 60, ' ') + "\n"; 
    	if(incomplete) ret += playedAsSummoner.summaryInDepth(incomplete);
		else{
			ret += centerPad("BLUE TEAM", 60, '#');
			ret += "\n" + blueTeam.summary() + "\n";
			int pos = 0;
			for(Summoner s: blueTeam)
				ret += (++pos) + ")" + s.summaryInDepth(incomplete) + "\n";
			ret += centerPad("PURPLE TEAM", 60, '#');
			ret += "\n" + purpleTeam.summary() + "\n";
			pos = 0;
			for(Summoner s: purpleTeam)
				ret += (++pos) + ")" + s.summaryInDepth(incomplete) + "\n";
			ret += "\n  " + mvp() + "\n";
		}
		
		return ret;
    }
  
    private static String centerPad(String s, int length, char c){
        int padding = length - s.length();
        int padL = padding - padding/2;
        int padR = padding - padL;
        String ret = padLeft(s, padL);
        ret = padRight(ret, padR);
        return ret.replaceAll("\\s\\B", ""+c);
    }
    
    private static String padLeft(String s, int length){
    	if(length < 0)
    		return s;
    	char[] fill = new char[length];
    	Arrays.fill(fill, ' ');
    	String ret = String.copyValueOf(fill);
        return ret.concat(s);
    }
    
    private static String padRight(String s, int length){
    	if(length < 0)
    		return s;
    	char[] fill = new char[length];
    	Arrays.fill(fill, ' ');
    	String ret = String.copyValueOf(fill);
        return s.concat(ret);
    }
    
    private String mvp(){
    	if(incomplete) return "Incomplete game - no MVP can be determined";
    	Summoner mvp = null;
    	Summoner mnt = null;
    	for(Summoner s: blueTeam){
    		if(mvp == null || s.getEffectiveness() > mvp.getEffectiveness() && s.stats.isWin())
    			mvp = s;
    		if(mnt == null || s.getEffectiveness() > mnt.getEffectiveness() && s != mvp)
    			mnt = s;
    	}
    	for(Summoner s: purpleTeam){
    		if(mvp == null || s.getEffectiveness() > mvp.getEffectiveness() && s.stats.isWin())
    			mvp = s;
    		if(mnt == null || s.getEffectiveness() > mnt.getEffectiveness() && s != mvp)
    			mnt = s;
    	}
    	return "MVP: " + mvp.summary(incomplete).replace(":"+mvp.summoner.summonerId, "") + "\n" +
    			"  Honorable mention: " + mnt.summary(incomplete).replace(":"+mvp.summoner.summonerId, "");
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
    
    public void load() throws Exception{
    	Player temp = Player.getTracked(playedAs.name);
    	TeamStruct team = findTeam(playedAs.summonerId);
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
    
    public static ArrayList<GameStat> getGames(){
    	if(allGames == null)
    		allGames = new ArrayList<GameStat>();
    	return allGames;
    }
    
    private TeamStruct findTeam(int id){
    	if(blueTeam == null)
    		blueTeam = new TeamStruct();
    	if(purpleTeam == null)
    		purpleTeam = new TeamStruct();
    	if(blueTeam.contains(id))
    		return blueTeam;
    	else 
    		return purpleTeam;
    }
    
    private class TeamStruct implements Iterable<Summoner>{
        ArrayList<Summoner> activePlayers;
        boolean firstBlood, won;
        int kills=0, deaths=0, assists=0, turrets=0, wardsPlaced=0, gold=0, cs=0, objectives=0;
        double avgKills=0, avgDeaths=0, avgAssists=0, avgGold=0, avgCS=0;
        
        public TeamStruct(){
        	activePlayers = new ArrayList<Summoner>();
        }

        public boolean contains(int id){
        	for(Summoner s: activePlayers)
        		if(s.summoner.summonerId == id)
        			return true;
        	return false;
        }
        
        public String summary(){
        	String ret = centerPad(String.format("Turrets: %02d Kills: %03d Deaths: %03d Assists: %03d", turrets, kills, deaths, assists), 60, ' ');
        	ret += "\n" +centerPad(String.format("Total Gold: %06d Avg Gold: %06.2f Avg CS: %03.2f ", gold, avgGold, avgCS), 60, ' ');
        	return ret;
        }
        
        public void init(){
        	if(activePlayers == null)
        		activePlayers = new ArrayList<Summoner>();
        	if(activePlayers.size() < 1) return;
            for(Summoner s: activePlayers){
            	s.teamStr = this;
                kills += s.stats.getChampionsKilled();
                deaths += s.stats.getNumDeaths();
                assists += s.stats.getAssists();
                turrets += s.stats.getTurretsKilled();
                wardsPlaced += s.stats.getWardPlaced();
                gold += s.stats.getGoldEarned();
                cs += s.stats.getCS();
                if(s.stats.isWin())
                	won = true;
                if(s.stats.getFirstBlood() == 1)
                	firstBlood = true;
            }
            avgKills = (kills/activePlayers.size());
            avgDeaths = (deaths/activePlayers.size());
            avgAssists = (assists/activePlayers.size());
            avgGold = (gold/activePlayers.size());
            avgCS = (cs/activePlayers.size());
        }

		@Override
		public Iterator<Summoner> iterator() {
			return activePlayers.iterator();
		}
    } 
    private class Summoner{
        PlayerMini summoner;
        PlayerStats stats;
        transient int item0, item1, item2, item3, item4, item5, item6;
        int[] items = {item0, item1, item2, item3, item4, item5, item6};
        transient RecentGame game;
        transient TeamStruct teamStr;
        transient Champion champ;
        transient ArrayList<Item> usedItems;
        
        public Summoner(Player player, TeamStruct str) throws Exception{
        	if(player.id == playedAs.summonerId)
        		summoner = playedAs;
        	else
        		summoner = player.getMini();
        	if(summoner.summonerId != playedAs.summonerId){
	            for(RecentGame rg: player.getRecentGames()){
	            	if(rg.gameId == gameId){
	            		game = rg;
	            		break;
	            	}
	            }
	            if(game == null)
	            	throw new Exception("Game does not exist in recent games\n  " + player.name + ":" + player.id);
	            stats = game.getStats();
	            summoner.championId = game.getChampionId();
	            summoner.teamId = game.getTeamId();
        	}
            load(str);           
        }
        
        public Summoner(Player player, RecentGame game) throws Exception{
        	summoner = player.getMini();
            this.game = game;
            stats = game.getStats();
            summoner.championId = game.getChampionId();
            summoner.teamId = game.getTeamId();
            item0 = stats.getItem0();
            item0 = stats.getItem1();
            item0 = stats.getItem2();
            item0 = stats.getItem3();
            item0 = stats.getItem4();
            item0 = stats.getItem5();
            item0 = stats.getItem6();
            load(findTeam(summoner.summonerId));    
        }
        
        public void load(TeamStruct str) throws Exception{
        	champ = Champion.findChampion(summoner.championId);
        	teamStr = str;
            usedItems = new ArrayList<Item>();
            for(int i: items)
            	if(i != 0)
            		usedItems.add(Item.findItem(i)); 
            if(stats == null){
            	throw new Exception("No stats for " + summoner.name + " game " + gameId + " on " + Time.setDate(gameDate).dateTime());
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
        			+ "   Damage healed: " + String.format("%06d", stats.getTotalHeal()) + " Wards placed: " + stats.getWardPlaced()
        			+ "  Largest multi-kill: " + stats.getLargestMultiKill() + repeatTimes
        			+ (stats.getFirstBlood() == 1 ? "\n  First blood" : "")
        			+ "\n   Items: " + listItems();
        }
        
        public String summary(boolean incomplete){
        	return summoner.name + ":" + summoner.summonerId + "("+(teamStr.won?"WON":"LOSS")+")\n  "
        			+ KDAString() + String.format(" %02.2fKDA ", KDAratio())
        			+ stats.getCS() + "CS (" + stats.getGoldEarned() + " gold) as " + champ.getName()
        			+ (incomplete ? "" : "\n   Effectiveness: " + String.format("%+3.2f%%",getEffectiveness()));
        }
        
        public String listItems(){
        	String ret = "";
        	for(int i=0; i<usedItems.size(); i++)
        		ret += usedItems.get(i).getName() + (i!=usedItems.size()-1 ? ", " : "");
        	return ret;
        }
        
        public double getKillParticipation(){
        	if(teamStr == null)
        		return 0;
        	if(teamStr.kills == 0)
        		return 0;
        	return (stats.getAssists()+stats.getChampionsKilled())/teamStr.kills;
        }
        
        public double getDeathContribution(){
        	if(teamStr == null)
        		return 0;
        	if(teamStr.deaths == 0)
        		return 0;
        	return stats.getNumDeaths()/teamStr.deaths;
        }
        
        public double getEffectiveness(){
        	return getKillParticipation() - getDeathContribution();
        }
        
        public String KDAString(){
        	return String.format("%02dK/%02dD/%02dA", stats.getChampionsKilled(), stats.getNumDeaths(), stats.getAssists());
        }
        
        public int KDAint(){
        	String ret = String.format("%02d%02d%02d", stats.getChampionsKilled(), stats.getNumDeaths(), stats.getAssists());
        	return Integer.parseInt(ret);
        }
        
        //TODO figure out why this returns only integer values
        public double KDAratio(){
        	if(stats.getNumDeaths()>0)
        		return (((double)stats.getChampionsKilled()+(double)stats.getAssists())/(double)stats.getNumDeaths());
        	return stats.getChampionsKilled()+stats.getAssists();
        }
    }
	
}
