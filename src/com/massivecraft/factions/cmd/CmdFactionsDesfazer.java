package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MFlag;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsDisband;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsDesfazer extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsDesfazer()
	{
		// Aliases
		this.addAliases("disband", "deletar", "excluir");

		// Requisições
		this.addRequirements(ReqHasFaction.get());
		
		// Descrição do comando
		this.setDesc("§6 desfazer §8-§7 Desfaz a sua facção.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{		
		
		// Verificando se o player possui permissão
		if(!(msender.getRole() == Rel.LEADER || msender.isOverriding())) {
			msender.message("§cApenas o líder da facção pode desfazer a facção.");
			return;
		}
				
		// Verificando se a facção é uma facção permanente (zonalivre, zonadeguerra ou zonaprotegida)
		if (msenderFaction.getFlag(MFlag.getFlagPermanent()))
		{
			msg("§cEsta facção é uma facção permanente portanto não pode ser desfeita.");
			return;
		}

		// Evento
		EventFactionsDisband event = new EventFactionsDisband(me, msenderFaction);
		event.run();
		if (event.isCancelled()) return;

		
		// Eviando todos os jogadores para a zona livre e informandos os mesmo que a facção foi desfeita
		for (MPlayer mplayer : msenderFaction.getMPlayers())
		{
			EventFactionsMembershipChange membershipChangeEvent = new EventFactionsMembershipChange(sender, mplayer, FactionColl.get().getNone(), MembershipChangeReason.DISBAND);
			membershipChangeEvent.run();
		}

		// Informando os players da facção
		for (MPlayer mplayer : msenderFaction.getMPlayersWhereOnline(true))
		{
			mplayer.msg("§e%s§e desfez a sua facção!", msender.describeTo(mplayer).replace("você", "§eVocê"));
		}
		
		// Aplicando o evento.
		msenderFaction.detach();
	}
	
}
