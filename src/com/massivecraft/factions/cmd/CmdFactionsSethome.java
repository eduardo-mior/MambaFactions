package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.event.EventFactionsHomeChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.ps.PS;

public class CmdFactionsSethome extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsSethome()
	{
		// Aliases
		this.addAliases("definirhome", "definirbase", "setbase");

		// Requisições
		this.addRequirements(RequirementIsPlayer.get());

		// Descrição do comando
		this.setDesc("§6 sethome §8-§7 Define a home da facção.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		Faction faction = msenderFaction;
		PS newHome = PS.valueOf(me.getLocation());
		
		// Verificando se o player possui permissão
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER)) {
			msender.message("§cVocê precisar ser capitão ou superior para poder definir a home da facção.");
			return;
		}
		
		// Por algum motivo esta verificação não funciona direito
		if (!faction.isValidHome(newHome))
		{
			msender.msg("§cVocê só pode definir a home da facção dentro dos territórios da facção.");
			return;
		}
		
        BoardColl coll = BoardColl.get();
        Faction factionC = coll.getFactionAt(PS.valueOf(msender.getPlayer().getLocation()));
        if (!(factionC.getMPlayers().contains(msender))) {
        	msender.msg("§cVocê só pode definir a home da facção dentro dos territórios da facção.");
			return;
		}
		
		// Evento
		EventFactionsHomeChange event = new EventFactionsHomeChange(sender, faction, newHome);
		event.run();
		if (event.isCancelled()) return;
		newHome = event.getNewHome();

		// Aplicando o evento
		faction.setHome(newHome);
		
		// Informando a facção
		faction.msg("§a%s§a definiu a nova home da facção!", msender.getRole().getPrefix() + msender.getName());
	}
	
}
