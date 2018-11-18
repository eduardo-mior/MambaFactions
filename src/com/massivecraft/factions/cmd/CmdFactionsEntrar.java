package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasntFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsEntrar extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsEntrar()
	{
		// Aliases
        this.addAliases("join", "aceitar");

		// Descrição
		this.setDesc("§6 entrar §e<facção> §8-§7 Entra em uma facção.");
		
		// Requisitos
		this.addRequirements(ReqHasntFaction.get());
		
		// Parametros (necessario)
		this.addParameter(TypeString.get(), "facção", "erro", true);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Verficiando se os argumentos são validos
		if (!this.argIsSet()) {
			msg("§cArgumentos insuficientes, use /f entrar <facção>");
			return;
		}
				
		// Argumentos
		String name = this.arg();
		Faction faction = readFaction(name);

		// Verificando se a facção atingiu o limite de membros definido na config
		if (MConf.get().factionMemberLimit > 0 && faction.getMPlayers().size() >= MConf.get().factionMemberLimit) {
			msg("§cA facção §f[%s§f] já atingiu o limite maximo de membros por facção (%d), portanto você não podera entrar.", faction.getName(), MConf.get().factionMemberLimit);
			return;
		}

		// Verificando se o player possui um convite para entrar na facção
		// Nós tambem verificando se ele é admin ><
		if (!(faction.isInvited(msender) || msender.isOverriding())) {
			msg("§cVocê precisa de um convite para poder entrar na facção.");
			return;
		}

		// Evento
		EventFactionsMembershipChange membershipChangeEvent = new EventFactionsMembershipChange(sender, msender, faction, MembershipChangeReason.JOIN);
		membershipChangeEvent.run();
		if (membershipChangeEvent.isCancelled()) return;
		
		// Informando o player e a facção
		faction.msg("§f%s§e entrou na facção§e.", msender.getName());
		msender.msg("§aVocê entrou na facção §f[%s§f]§a.", faction.getName());
		
		// Aplicando o evento
		msender.resetFactionData();
		msender.setFaction(faction);
		
		faction.uninvite(msender);
	}
	
}