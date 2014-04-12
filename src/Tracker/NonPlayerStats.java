package Tracker;

/**
 * A wrapper around PlayerStats allowing for aggregation and summarizing.&nbsp;
 * Includes a method to automatically add a Summoner to the summary.
 * 
 * @author Zach Greenhalge
 * @see PlayerStats
 */
public class NonPlayerStats extends PlayerStats {

	private int wins = 0;
	private int losses = 0;
	private int games = 0;
	
	/**
	 * A convenience method that adds a Summoner to the Stat structure.&nbsp
	 * This is equivalent to calling every inc method with the equivalent get from the Summoner as an argument.&nbsp;
	 * Please use this method and do not waste your time doing it manually.
	 * 
	 * @param s - Summoner to add to this Stat structure
	 */
	public void add(Summoner s){
		if(s.won())
			increaseWins();
		else
			increaseLosses();
		PlayerStats temp = s.getStats();
		incAssists(temp.getAssists());
		incChampionsKilled(temp.getChampionsKilled());
		incDamageDealtPlayer(temp.getDamageDealtPlayer());
		incDoubleKills(temp.getDoubleKills());
		incFirstBlood(temp.getFirstBlood());
		incGold(temp.getGold());
		incGoldEarned(temp.getGoldEarned());
		incGoldSpent(temp.getGoldSpent());
		incItemsPurchased(temp.getItemsPurchased());
		incKillingSprees(temp.getKillingSprees());
		tasLargestCriticalStrike(temp.getLargestCriticalStrike());
		tasLargestKillingSpree(temp.getLargestKillingSpree());
		tasLargestMultiKill(temp.getLargestMultiKill());
		incLevel(temp.getLevel());
		incMagicDamageDealtPlayer(temp.getMagicDamageDealtPlayer());
		incMagicDamageTaken(temp.getMagicDamageTaken());
		incMinionsKilled(temp.getMinionsKilled());
		incNeutralMinionsKilled(temp.getNeutralMinionsKilled());
		incNumDeaths(temp.getNumDeaths());
		incPentaKills(temp.getPentaKills());
		incPhysicalDamageDealtPlayer(temp.getPhysicalDamageDealtPlayer());
		incPhysicalDamageTaken(temp.getPhysicalDamageTaken());
		incQuadraKills(temp.getQuadraKills());
		incSightWardsBought(temp.getSightWardsBought());
		incTimePlayed(temp.getTimePlayed());
		incTotalDamageDealt(temp.getTotalDamageDealt());
		incTotalDamageTaken(temp.getTotalDamageTaken());
		incTotalHeal(temp.getTotalHeal());
		incTripleKills(temp.getTripleKills());
		incTrueDamageDealtPlayer(temp.getTrueDamageDealtPlayer());
		incTrueDamageTaken(temp.getTrueDamageTaken());
		incTurretsKilled(temp.getTurretsKilled());
		incVisionWardsBought(temp.getVisionWardsBought());
		incWardPlaced(temp.getWardPlaced());
		incWardKilled(temp.getWardKilled());
		
	}
	
	public void increaseWins(){
		wins++;
		games++;
	}
	
	public int getWins(){
		return wins;
	}
	
	public void increaseLosses(){
		losses++;
		games++;
	}
	
	public int getLosses(){
		return losses;
	}
	
	public int getGames(){
		return games;
	}
		
	public void incAssists(int i){
		assists += i;
	}
	
	public void incChampionsKilled(int i){
		championsKilled += i;
	}
	
	public void incDamageDealtPlayer(int i){
		damageDealtPlayer += i;
	}
	
	public void incDoubleKills(int i){
		doubleKills += i;
	}
	
	public void incFirstBlood(int i){
		firstBlood += i;
	}
	
	public void incGold(int i){
		gold += i;
	}
	
	public void incGoldEarned(int i){
		goldEarned += i;
	}
	
	public void incGoldSpent(int i){
		goldSpent += i;
	}
	
	public void incItemsPurchased(int i){
		itemsPurchased += i;
	}
	
	public void incKillingSprees(int i){
		killingSprees += i;
	}
	
	public void tasLargestCriticalStrike(int i){
		if(i > largestCriticalStrike)
			largestCriticalStrike = i;
	}
	
	public void tasLargestKillingSpree(int i){
		if(i > largestKillingSpree)
			largestKillingSpree = i;
	}
	
	public void tasLargestMultiKill(int i){
		if(i > largestMultiKill)
			largestMultiKill = i;
	}
	
	public void incLevel(int i){
		level += i;
	}
	
	public void incMagicDamageDealtPlayer(int i){
		magicDamageDealtPlayer += i;
	}
	
	public void incMagicDamageTaken(int i){
		magicDamageTaken += i;
	}
	
	public void incMinionsKilled(int i){
		minionsKilled += i;
	}
	
	public void incNeutralMinionsKilled(int i){
		neutralMinionsKilled += i;
	}
	
	public void incNumDeaths(int i){
		numDeaths += i;
	}
	
	public void incPentaKills(int i){
		pentaKills += i;
	}
	
	public void incPhysicalDamageDealtPlayer(int i){
		physicalDamageDealtPlayer += i;
	}
	
	public void incPhysicalDamageTaken(int i){
		physicalDamageTaken += i;
	}
	
	public void incQuadraKills(int i){
		quadrakills += i;
	}
	
	public void incSightWardsBought(int i){
		sightWardsBought += i;
	}
	
	public void incTimePlayed(int i){
		timePlayed += i;
	}
	
	public void incTotalDamageDealt(int i){
		totalDamageDealt += i;
	}
	
	public void incTotalDamageTaken(int i){
		totalDamageTaken += i;
	}
	
	public void incTotalHeal(int i){
		totalHeal += i;
	}
	
	public void incTripleKills(int i){
		tripleKills += i;
	}
	
	public void incTrueDamageDealtPlayer(int i){
		trueDamageDealtPlayer += i;
	}
	
	public void incTrueDamageTaken(int i){
		trueDamageTaken += i;
	}
	
	public void incTurretsKilled(int i){
		turretsKilled += i;
	}
	
	public void incVisionWardsBought(int i){
		visionWardsBought += i;
	}
	
	public void incWardKilled(int i){
		wardKilled += i;
	}
	
	public void incWardPlaced(int i){
		wardPlaced += i;
	}
	
}
