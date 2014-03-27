package Tracker;

public class QueueSummary {

	String playerStatSummaryType;
	AggregatedStats aggregatedStats;
	long modifyDate;
	int wins;
	int losses;
	
	public String summary(){
		if(wins == 0 && losses == 0 && aggregatedStats == null)
			return "";
		int totalKills = aggregatedStats.totalChampionKills;
		int totalAssists = aggregatedStats.totalAssists;
		int totalCS = aggregatedStats.totalMinionKills + aggregatedStats.totalNeutralMinionsKilled;
		int totalTurrets = aggregatedStats.totalTurretsKilled;
		if(playerStatSummaryType.equals("AramUnranked5x5"))
			return String.format("     %s - (%d WINS) %02d Kills %02d Assists %02d Turrets", playerStatSummaryType, wins,
											totalKills, totalAssists, totalTurrets);
		if(playerStatSummaryType.equals("CoopVsAI"))
			return String.format("     %s - (%d WINS) %02d Kills %02d Assists %02d CS %02d Turrets", playerStatSummaryType,
											wins, totalKills, totalAssists,	totalCS, totalTurrets);
		if(playerStatSummaryType.equals("Unranked"))
			return String.format("     %s - (%d WINS) %02d Kills %02d Assists %02d CS %02d Turrets", "Normal5x5",
											wins, totalKills, totalAssists, totalCS, totalTurrets);
		if(playerStatSummaryType.equals("RankedSolo5x5"))
			return String.format("     %s - (%d WINS) %02d Kills %02d Assists %02d CS %02d Turrets", playerStatSummaryType,
					wins, totalKills, totalAssists, totalCS, totalTurrets);
		if(playerStatSummaryType.equals("Unranked3x3"))
			return String.format("     %s - (%d WINS) %02d Kills %02d Assists %02d CS %02d Turrets", "Normal3x3",
					wins, totalKills, totalAssists, totalCS, totalTurrets);
		if(playerStatSummaryType.equals("RankedTeam3x3"))
			return String.format("     %s - (%d WINS) %02d Kills %02d Assists %02d CS %02d Turrets", playerStatSummaryType,
					wins, totalKills, totalAssists, totalCS, totalTurrets);
		if(playerStatSummaryType.equals("RankedTeam5x5"))
			return String.format("     %s - (%d WINS) %02d Kills %02d Assists %02d CS %02d Turrets", playerStatSummaryType,
					wins, totalKills, totalAssists, totalCS, totalTurrets);
		return "";
	}
}
