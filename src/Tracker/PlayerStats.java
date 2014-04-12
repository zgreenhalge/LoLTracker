package Tracker;

/**
 * A struct for storing a Player's in-game data.&nbsp; Passed from Riot servers.
 *
 */
public class PlayerStats {

	 protected int assists=0;
     protected int barracksKilled=0;
     protected int championsKilled=0;
     protected int combatPlayerScore=0;
     protected int consumablesPurchased=0;
     protected int damageDealtPlayer=0;
     protected int doubleKills=0;
     protected int firstBlood=0;
     protected int gold=0;
     protected int goldEarned=0;
     protected int goldSpent=0;
     protected int item0=0;
     protected int item1=0;
     protected int item2=0;
     protected int item3=0;
     protected int item4=0;
     protected int item5=0;
     protected int item6=0;
     protected int itemsPurchased=0;
     protected int killingSprees=0;
     protected int largestCriticalStrike=0;
     protected int largestKillingSpree=0;
     protected int largestMultiKill=0;
     protected int legendaryItemsCreated=0; //Number of tier 3 items built
     protected int level=0;
     protected int magicDamageDealtPlayer=0;
     protected int magicDamageDealtToChampions=0;
     protected int magicDamageTaken=0;
     protected int minionsDenied=0;
     protected int minionsKilled=0;
     protected int neutralMinionsKilled=0;
     protected int neutralMinionsKilledEnemyJungle=0;
     protected int neutralMinionsKilledYourJungle=0;
     protected boolean nexusKilled=false; // Flag specifying if the summoner got the killing blow on the nexus
     protected int nodeCapture=0;
     protected int nodeCaptureAssist=0;
     protected int nodeNeutralize=0;
     protected int nodeNeutralizeAssist=0;
     protected int numDeaths=0;
     protected int numItemsBought=0;
     protected int objectivePlayerScore=0;
     protected int pentaKills=0;
     protected int physicalDamageDealtPlayer=0;
     protected int physicalDamageDealtChampions=0;
     protected int physicalDamageTaken=0;
     protected int quadrakills=0;
     protected int sightWardsBought=0;
     protected int spell1Cast=0; // Number of times first champion spell was cast
     protected int spell2Cast=0; // Number of times second champion spell was cast
     protected int spell3Cast=0; // see above
     protected int spell4Cast=0; // see above
     protected int summonSpell1Cast=0;
     protected int summonSpell2Cast=0;
     protected int superMonsterKilled=0;
     protected int team=0;
     protected int teamObjective=0;
     protected int timePlayed=0;
     protected int totalDamageDealt=0;
     protected int totalDamageDealtToChampions=0;
     protected int totalDamageTaken=0;
     protected int totalHeal=0;
     protected int totalPlayerScore=0;
     protected int totalScoreRank=0;
     protected int totalTimeCrowdControlDealt=0;
     protected int totalUnitsHealed=0;
     protected int tripleKills=0;
     protected int trueDamageDealtPlayer=0;
     protected int trueDamageDealtToChampions=0;
     protected int trueDamageTaken=0;
     protected int turretsKilled=0;
     protected int unrealKills=0;
     protected int victoryPointTotal=0;
     protected int visionWardsBought=0;
     protected int wardKilled=0;
     protected int wardPlaced=0;
     protected boolean win=false; // FLag specifying whether or not this game was won
     
     public int getAssists() {
             return assists;
     }
     public int getBarracksKilled() {
             return barracksKilled;
     }
     public int getChampionsKilled() {
             return championsKilled;
     }
     public int getCombatPlayerScore() {
             return combatPlayerScore;
     }
     public int getConsumablesPurchased() {
             return consumablesPurchased;
     }
     public int getCS(){
         return minionsKilled+neutralMinionsKilled;
     }
     public int getDamageDealtPlayer() {
             return damageDealtPlayer;
     }
     public int getDoubleKills() {
             return doubleKills;
     }
     public int getFirstBlood() {
             return firstBlood;
     }
     public int getGold() {
             return gold;
     }
     public int getGoldEarned() {
             return goldEarned;
     }
     public int getGoldSpent() {
             return goldSpent;
     }
     public int getItem0() {
             return item0;
     }
     public int getItem1() {
             return item1;
     }
     public int getItem2() {
             return item2;
     }
     public int getItem3() {
             return item3;
     }
     public int getItem4() {
             return item4;
     }
     public int getItem5() {
             return item5;
     }
     public int getItem6() {
             return item6;
     }
     public int getItemsPurchased() {
             return itemsPurchased;
     }
     public int getKillingSprees() {
             return killingSprees;
     }
     public int getLargestCriticalStrike() {
             return largestCriticalStrike;
     }
     public int getLargestKillingSpree() {
             return largestKillingSpree;
     }
     public int getLargestMultiKill() {
             return largestMultiKill;
     }
     public int getLegendaryItemsCreated() {
             return legendaryItemsCreated;
     }
     public int getLevel() {
             return level;
     }
     public int getMagicDamageDealtPlayer() {
             return magicDamageDealtPlayer;
     }
     public int getMagicDamageDealtToChampions() {
             return magicDamageDealtToChampions;
     }
     public int getMagicDamageTaken() {
             return magicDamageTaken;
     }
     public int getMinionsDenied() {
             return minionsDenied;
     }
     public int getMinionsKilled() {
             return minionsKilled;
     }
     public int getNeutralMinionsKilled() {
             return neutralMinionsKilled;
     }
     public int getNeutralMinionsKilledEnemyJungle() {
             return neutralMinionsKilledEnemyJungle;
     }
     public int getNeutralMinionsKilledYourJungle() {
             return neutralMinionsKilledYourJungle;
     }
     public boolean isNexusKilled() {
             return nexusKilled;
     }
     public int getNodeCapture() {
             return nodeCapture;
     }
     public int getNodeCaptureAssist() {
             return nodeCaptureAssist;
     }
     public int getNodeNeutralize() {
             return nodeNeutralize;
     }
     public int getNodeNeutralizeAssist() {
             return nodeNeutralizeAssist;
     }
     public int getNumDeaths() {
             return numDeaths;
     }
     public int getNumItemsBought() {
             return numItemsBought;
     }
     public int getObjectivePlayerScore() {
             return objectivePlayerScore;
     }
     public int getPentaKills() {
             return pentaKills;
     }
     public int getPhysicalDamageDealtPlayer() {
             return physicalDamageDealtPlayer;
     }
     public int getPhysicalDamageDealtChampions() {
             return physicalDamageDealtChampions;
     }
     public int getPhysicalDamageTaken() {
             return physicalDamageTaken;
     }
     public int getQuadraKills() {
             return quadrakills;
     }
     public int getSightWardsBought() {
             return sightWardsBought;
     }
     public int getSpell1Cast() {
             return spell1Cast;
     }
     public int getSpell2Cast() {
             return spell2Cast;
     }
     public int getSpell3Cast() {
             return spell3Cast;
     }
     public int getSpell4Cast() {
             return spell4Cast;
     }
     public int getSummonSpell1Cast() {
             return summonSpell1Cast;
     }
     public int getSummonSpell2Cast() {
             return summonSpell2Cast;
     }
     public int getSuperMonsterKilled() {
             return superMonsterKilled;
     }
     public int getTeam() {
             return team;
     }
     public int getTeamObjective() {
             return teamObjective;
     }
     public int getTimePlayed() {
             return timePlayed;
     }
     public int getTotalDamageDealt() {
             return totalDamageDealt;
     }
     public int getTotalDamageDealtToChampions() {
             return totalDamageDealtToChampions;
     }
     public int getTotalDamageTaken() {
             return totalDamageTaken;
     }
     public int getTotalHeal() {
             return totalHeal;
     }
     public int getTotalPlayerScore() {
             return totalPlayerScore;
     }
     public int getTotalScoreRank() {
             return totalScoreRank;
     }
     public int getTotalTimeCrowdControlDealt() {
             return totalTimeCrowdControlDealt;
     }
     public int getTotalUnitsHealed() {
             return totalUnitsHealed;
     }
     public int getTripleKills() {
             return tripleKills;
     }
     public int getTrueDamageDealtPlayer() {
             return trueDamageDealtPlayer;
     }
     public int getTrueDamageDealtToChampions() {
             return trueDamageDealtToChampions;
     }
     public int getTrueDamageTaken() {
             return trueDamageTaken;
     }
     public int getTurretsKilled() {
             return turretsKilled;
     }
     public int getUnrealKills() {
             return unrealKills;
     }
     public int getVictoryPointTotal() {
             return victoryPointTotal;
     }
     public int getVisionWardsBought() {
             return visionWardsBought;
     }
     public int getWardKilled() {
             return wardKilled;
     }
     public int getWardPlaced() {
             return wardPlaced;
     }
     public boolean isWin() {
             return win;
     }
}
