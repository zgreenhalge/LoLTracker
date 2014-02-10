
public class PlayerStats {

	 private int assists=0;
     private int barracksKilled=0;
     private int championsKilled=0;
     private int combatPlayerScore=0;
     private int consumablesPurchased=0;
     private int damageDealtPlayer=0;
     private int doubleKills=0;
     private int firstBlood=0;
     private int gold=0;
     private int goldEarned=0;
     private int goldSpent=0;
     private int item0=0;
     private int item1=0;
     private int item2=0;
     private int item3=0;
     private int item4=0;
     private int item5=0;
     private int item6=0;
     private int itemsPurchased=0;
     private int killingSprees=0;
     private int largestCriticalStrike=0;
     private int largestKillingSpree=0;
     private int largestMultiKill=0;
     private int legendaryItemsCreated=0; //Number of tier 3 items built
     private int level=0;
     private int magicDamageDealtPlayer=0;
     private int magicDamageDealtToChampions=0;
     private int magicDamageTaken=0;
     private int minionsDenied=0;
     private int minionsKilled=0;
     private int neutralMinionsKilled=0;
     private int neutralMinionsKilledEnemyJungle=0;
     private int neutralMinionsKilledYourJungle=0;
     private boolean nexusKilled=false; // Flag specifying if the summoner got the killing blow on the nexus
     private int nodeCapture=0;
     private int nodeCaptureAssist=0;
     private int nodeNeutralize=0;
     private int nodeNeutralizeAssist=0;
     private int numDeaths=0;
     private int numItemsBought=0;
     private int objectivePlayerScore=0;
     private int pentaKills=0;
     private int physicalDamageDealtPlayer=0;
     private int physicalDamageDealtChampions=0;
     private int physicalDamageTaken=0;
     private int quadrakills=0;
     private int sightWardsBought=0;
     private int spell1Cast=0; // Number of times first champion spell was cast
     private int spell2Cast=0; // Number of times second champion spell was cast
     private int spell3Cast=0; // see above
     private int spell4Cast=0; // see above
     private int summonSpell1Cast=0;
     private int summonSpell2Cast=0;
     private int superMonsterKilled=0;
     private int team=0;
     private int teamObjective=0;
     private int timePlayed=0;
     private int totalDamageDealt=0;
     private int totalDamageDealtToChampions=0;
     private int totalDamageTaken=0;
     private int totalHeal=0;
     private int totalPlayerScore=0;
     private int totalScoreRank=0;
     private int totalTimeCrowdControlDealt=0;
     private int totalUnitsHealed=0;
     private int tripleKills=0;
     private int trueDamageDealtPlayer=0;
     private int trueDamageDealtToChampions=0;
     private int trueDamageTaken=0;
     private int turretsKilled=0;
     private int unrealKills=0;
     private int victoryPointTotal=0;
     private int visionWardsBought=0;
     private int wardKilled=0;
     private int wardPlaced=0;
     private boolean win=false; // FLag specifying whether or not this game was won
     
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
