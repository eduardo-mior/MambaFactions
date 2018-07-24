package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.event.EventFactionsHomeChange;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsDelhome extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsDelhome()
	{
		// Aliases
		this.addAliases("unsethome", "removerbase", "delbase");
		
		// Descrição do comando
		this.setDesc("§6 delhome §8-§7 Deleta a home da facção.");
		
		// Requisições
		this.addRequirements(ReqHasFaction.get());
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{		
		// Verificando se o player possui permissão
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msender.message("§cVocê precisar ser capitão ou superior para poder deletar a home da facção.");
			return;
		}
				
		// Verificando se a facção possui home
		if ( ! msenderFaction.hasHome())
		{
			msender.msg("§cA sua facção ainda não definiu a home da facção.");
			return;
		}
		
		// Evento
		EventFactionsHomeChange event = new EventFactionsHomeChange(sender, msenderFaction, null);
		event.run();
		if (event.isCancelled()) return;

		// Aplicando o evento
		msenderFaction.setHome(null);
		
		// Informando os players da facção
		msenderFaction.msg("§e%s§e deletou a home da facção!", msender.getRole().getPrefix() + msender.getName());
	}
	
}
