package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.engine.EngineMenuGui;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;

public class CmdFactionsSobAtaque extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsSobAtaque()
	{
		// Aliases
        this.addAliases("ataque");

		// Descrição
		this.setDesc("§6 sobataque §8-§7 Mostra mais informações sobre o ataque.");
		
		// Requisitos
		this.addRequirements(RequirementIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		if (!msenderFaction.isInAttack()) 
		{
			msg("§cSua facção não esta sob ataque!");
		}
		else 
		{
			EngineMenuGui.get().abrirMenuFaccaoSobAtaque(msender);
		}
	}
}
