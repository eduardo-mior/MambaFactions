package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;

public class CmdFactionsBau extends FactionsCommand
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsBau() 
	{
		// Aliases
	    this.addAliases("chest");
	    
		// Requisições
		this.addRequirements(RequirementIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		
		// Descrição do comando
		this.setDesc("§6 bau §8-§7 Abre o baú virtual da facção.");
		
	    // Visibilidade do comando
		if (!MConf.get().colocarIconeDoFBauNoMenuGUI)
		{
		    this.setVisibility(Visibility.INVISIBLE);
		}
		
		/*
		 * A unica razão deste comando estar aqui
		 * é porque nós queriamos mostrar o comando /f bau
		 * na lista de comandos do /f ajuda
		 * 
		 * Você pode ignorar isso ou apagar se quiser :D
		 */
	}
}
