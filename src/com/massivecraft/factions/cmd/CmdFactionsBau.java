package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.Visibility;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsBau extends FactionsCommand
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsBau() 
	{    
		// Verificando se o comando esta habilitado
		if (MConf.get().colocarIconeDoFBauNoMenuGUI) 
		{	
			// Aliases
		    this.addAliases("chest");
		    
			// Descrição
			this.setDesc("§6 bau §8-§7 Abre o baú virtual da facção.");
			
			// Requisitos
			this.addRequirements(RequirementIsPlayer.get());
			this.addRequirements(ReqHasFaction.get());
		}
		
		else 
		{
			// Visibilidade do comando
		    this.setVisibility(Visibility.INVISIBLE);	
		}
		
		// Parametros (não necessario)
		this.addParameter(TypeString.get(), "erro", "erro", true);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
	    // Verificando se o comando esta habiltiado
		if (!MConf.get().colocarIconeDoFBauNoMenuGUI)
		{
			message(HELP_MESSAGE);
		}
	}
}
