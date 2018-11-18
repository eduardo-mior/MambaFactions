package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.massivecore.command.Visibility;

public class CmdFactionsPoderFaction extends CmdFactionsPoderAbstract
{
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsPoderFaction()
	{
		// Super
		super(TypeFaction.get(), "faction");
        
    	// Descrição
    	this.setDesc("§6 poder f §e<facção> <quantia> §8-§7 Adiciona poder a um facção.");
    	
        // Visibilidade do comando
        this.setVisibility(Visibility.SECRET);
	}
	
}