package Tracker;
import java.util.ArrayList;
import java.util.Iterator;
import Utils.*;


public class TeamStruct implements Iterable<Summoner>{

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
    	String ret = StringFunctions.centerPad(String.format("Turrets: %02d Kills: %03d Deaths: %03d Assists: %03d", turrets, kills, deaths, assists), 60, ' ');
    	ret += "\n" + StringFunctions.centerPad(String.format("Total Gold: %06d Avg Gold: %06.2f Avg CS: %03.2f ", gold, avgGold, avgCS), 60, ' ');
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
	

