package com.league.stats.gui;

public class Summoner {
	String name;
	String level;
	String rank;
	String ratio;
	String prevSeason;

	public Summoner(String summonerName, String summonerLevel, String summonerRank, String previousSeason) {
		name = summonerName;
		level = summonerLevel;
		rank = summonerRank;
		prevSeason = previousSeason;
	}
	public Summoner(String summonerName, String summonerRank, String summonerRatio) {
		name = summonerName;
		rank = summonerRank;
		ratio = summonerRatio;
	}

	String getName() {
		return name;
	}
	
	String getLevel() {
		return level;
	}

	String getRank() {
		return rank;
	}

	String getRatio() {
		return ratio;
	}
}
