package com.massivecraft.factions.util;

public enum WorldName {

	NETHER("Mundo do Nether"),
	NORMAL("Mundo Normal"),
	THE_END("Mundo do Fim");
	
	private String name;
	public String getName() { return this.name; }

	WorldName(String name) {
		this.name = name;
	}
		
}
