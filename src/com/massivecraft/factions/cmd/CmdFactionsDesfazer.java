package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.engine.EngineMenuGui;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MFlag;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsDisband;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsDesfazer extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsDesfazer()
	{
		// Aliases
		this.addAliases("disband", "deletar", "excluir");
		
		// Descrição
		this.setDesc("§6 desfazer §8-§7 Desfaz a sua facção.");
		
		// Requisitos
		this.addRequirements(ReqHasFaction.get());
		
		// Parametros (não necessario)
		this.addParameter(TypeString.get(), "confirmação", "null", true);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{			
		// Verificando se o player possui permissão
		if (!(msender.getRole() == Rel.LEADER || msender.isOverriding())) {
			msg("§cApenas o líder da facção pode desfazer a facção.");
			return;
		}
		
		// Verificando se a facção esta em ataque
		if (msenderFaction.isInAttack()) {
			msg("§cVocê não pode desfazer sua facção enquanto ela estiver sobre ataque!");
			return;
		}			
		
		// Verificando se a facção é uma facção permanente (zonalivre, zonadeguerra ou zonaprotegida)
		if (msenderFaction.getFlag(MFlag.getFlagPermanent())) {
			msg("§cEsta facção é uma facção permanente portanto não pode ser desfeita.");
			return;
		}
		
		// Caso não haja o argumento "confirmar" então é aberto um menu de confirmação
		if ((!this.argIsSet() || !this.arg().equalsIgnoreCase("confirmar")) && msender.isPlayer()) {
			EngineMenuGui.get().abrirMenuDesfazerFaccao(msender);
			return;
		}

		// Evento
		EventFactionsDisband event = new EventFactionsDisband(me, msenderFaction);
		event.run();
		if (event.isCancelled()) return;
		
		// Eviando todos os jogadores para a zona livre e informando os mesmo que a facção foi desfeita
		for (MPlayer mplayer : msenderFaction.getMPlayers()) {
			EventFactionsMembershipChange membershipChangeEvent = new EventFactionsMembershipChange(sender, mplayer, FactionColl.get().getNone(), MembershipChangeReason.DISBAND);
			membershipChangeEvent.run();
		}

		// Informando os players da facção
		for (MPlayer mplayer : msenderFaction.getMPlayersWhereOnline(true)) 	{
			mplayer.msg("§f%s§e desfez a facção!", msender.describeTo(mplayer).replace("Você", "§eVocê"));
		}
		
		// Removendo os convites da facção
		for (String playerId : msenderFaction.getInvitations().keySet()) {
			MPlayer mplayer = MPlayer.get(playerId);
			if (mplayer != null) mplayer.removeInvitation(msenderFaction.getId());
		}
		
		// Aplicando o evento.
		msenderFaction.detach();
	}
	
}