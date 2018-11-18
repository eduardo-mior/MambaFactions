package com.massivecraft.factions.cmd;

import com.massivecraft.massivecore.command.Visibility;

public class CmdFactionsPoder extends FactionsCommand
{	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdFactionsPoderPlayer cmdFactionsPoderPlayer = new CmdFactionsPoderPlayer();
	public CmdFactionsPoderFaction cmdFactionsPoderFaction = new CmdFactionsPoderFaction();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsPoder()
	{
		// Child (filhos?)
		this.addChild(this.cmdFactionsPoderPlayer);
		this.addChild(this.cmdFactionsPoderFaction);
		
		// Aliases
	    this.addAliases("powerboost", "power", "pb");
	    
		// Descrição
		this.setDesc("§6 poder §8-§7 Adiciona ou remove poder de um player ou facção.");
		
	    // Visibilidade do comando
	    this.setVisibility(Visibility.SECRET);
	}
	
}