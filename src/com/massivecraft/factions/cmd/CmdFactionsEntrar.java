package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasntFaction;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsEntrar extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsEntrar()
	{
		// Aliases
        this.addAliases("join");
        
		// Parametros (necessario)
		this.addParameter(TypeFaction.get(), "facção");
		
		// Requisições
		this.addRequirements(ReqHasntFaction.get());

		// Descrição do comando
		this.setDesc("§6 entrar §e<facção> §8-§7 Entra em uma facção.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		Faction faction = this.readArg();		

		// Verificando se a facção atingiu o limite de membros definido na config
		if (MConf.get().factionMemberLimit > 0 && faction.getMPlayers().size() >= MConf.get().factionMemberLimit)
		{
			msg("§cA facção %s atingiu o limite maximo de membros (%d), portanto você não podera entrar na facção.", faction.getName(), MConf.get().factionMemberLimit);
			return;
		}

		// Verificando se o player possui um convite para entrar na facção
		// Nós tambem verificando se ele é admin ><
		if( ! (faction.isInvited(msender) || msender.isOverriding()))
		{
			msg("§cVocê precisa de um convite para poder entrar na facção.");
			return;
		}

		// Evento
		EventFactionsMembershipChange membershipChangeEvent = new EventFactionsMembershipChange(sender, msender, faction, MembershipChangeReason.JOIN);
		membershipChangeEvent.run();
		if (membershipChangeEvent.isCancelled()) return;
		
		// Informando o player e a facção
		faction.msg("§e\"%s\" §eentrou na sua facção§e.", msender.getName());
		msender.msg("§aVocê entrou na facção §f%s§a.", faction.getName());
		
		// Aplicando o evento
		msender.resetFactionData();
		msender.setFaction(faction);
		
		faction.uninvite(msender);
	}
	
}
