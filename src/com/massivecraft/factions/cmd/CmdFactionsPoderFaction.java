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
		super(TypeFaction.get(), "faction");
		
        // Visibilidade do comando
        this.setVisibility(Visibility.SECRET);
        
    	// Descrição do comando
    	this.setDesc("§6 poder f §e<facção> <quantia> §8-§7 Adiciona poder a um facção.");
	}
	
}
