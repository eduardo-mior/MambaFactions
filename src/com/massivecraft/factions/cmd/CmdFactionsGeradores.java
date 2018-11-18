package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;

public class CmdFactionsGeradores extends FactionsCommand
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsGeradores() 
	{    
		// Verificando se o comando esta habilitado
		if (MConf.get().colocarIconeDoFGeradoresNoMenuGUI)
		{	
			// Aliases
		    this.addAliases("spawners");
		    
			// Descrição
			this.setDesc("§6 geradores §8-§7 Administra os geradores da facção.");
			
			// Requisitos
			this.addRequirements(RequirementIsPlayer.get());
			this.addRequirements(ReqHasFaction.get());
		}
		
		else 
		{
			// Visibilidade do comando
		    this.setVisibility(Visibility.INVISIBLE);	
		}
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
	    // Verificando se o comando esta habiltiado
		if (!MConf.get().colocarIconeDoFGeradoresNoMenuGUI)
		{
			message(HELP_MESSAGE);
		}
	}
}