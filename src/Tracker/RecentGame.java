package Tracker;
import java.util.ArrayList;


public class RecentGame {

     public int summonerId;
	 public int championId;
     public long createDate;
     public String createDateStr;
     public ArrayList<PlayerMini> fellowPlayers;
     public long gameId;
     public String gameMode;
     public String gameType;
     public boolean invalid;
     public int level;
     public int mapId;
     public int spell1;
     public int spell2;
     public PlayerStats stats;
     public String subType;
     public int teamId;
     
     public int getChampionId() {
             return championId;
     }
     public long getCreateDate() {
             return createDate;
     }
     public String getCreateDateStr() {
             return createDateStr;
     }
     public ArrayList<PlayerMini> getFellowPlayers() {
             return fellowPlayers;
     }
     public long getGameId() {
             return gameId;
     }
     public String getGameMode() {
             return gameMode;
     }
     public String getGameType() {
             return gameType;
     }
     public boolean isInvalid() {
             return invalid;
     }
     public int getLevel() {
             return level;
     }
     public int getMapId() {
             return mapId;
     }
     public int getSpell1() {
             return spell1;
     }
     public int getSpell2() {
             return spell2;
     }
     public PlayerStats getStats() {
             return stats;
     }
     public String getSubType() {
             return subType;
     }
     public int getTeamId() {
             return teamId;
     }
}
