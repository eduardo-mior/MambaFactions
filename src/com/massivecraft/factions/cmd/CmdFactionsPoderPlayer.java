package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.massivecore.command.Visibility;

public class CmdFactionsPoderPlayer extends CmdFactionsPoderAbstract
{
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsPoderPlayer()
	{
		// Super
		super(TypeMPlayer.get(), "player");
        
    	// Descrção do comando
    	this.setDesc("§6 poder p §e<player> <quantia> §8-§7 Adiciona poder a um player.");
    	
        // Visibilidade do comando
        this.setVisibility(Visibility.SECRET);
	}
	
}