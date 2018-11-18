package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.engine.EngineMenuGui;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;

public class CmdFactionsConviteListar extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsConviteListar()
	{
		// Aliases
	    this.addAliases("ver", "list");
		
		// Descrição
		this.setDesc("§6 convite listar §8-§7 Mostra a lista de convites pendentes.");
		
		// Requisitos
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(RequirementIsPlayer.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //	
	
	@Override
	public void perform() throws MassiveException
	{		
		// Verificando se a facção possui convites pendentes
		if (msenderFaction.getInvitations().isEmpty()) {
			msg("§cSua facção não possui convites pendentes!");
			return;
		}
		
		// Abrindo o menu dos convites
		EngineMenuGui.get().abrirMenuConvitesEnviados(msenderFaction, msender);
	}
}
