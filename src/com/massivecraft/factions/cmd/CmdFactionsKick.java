package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsKick extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsKick()
	{
		// Aliases
		this.addAliases("expulsar", "kickar");
		
		// Parametros (necessario)
		this.addParameter(TypeMPlayer.get(), "player");
		
		// Requisições
		this.addRequirements(ReqHasFaction.get());
		
		// Descrição do comando
		this.setDesc("§6 kick §e<player> §8-§7 Expulsa um player da facção.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Verificando se o player possui permissão
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msender.message("§cVocê precisar ser capitão ou superior para poder expulsar membros da facção.");
			return;
		}
		
		// Argumentos
		MPlayer mplayer = this.readArg();
		
		// Verificando se o target é da mesma facão que o sender
		if (mplayer.getFaction() != msenderFaction) {
			msender.message("§cEste jogador não esta na sua facção.");
			return;
		}
		
		// Verificando se o sender e o target são os mesmos
		if (msender == mplayer)
		{
			msg("§cVocê não pode se expulsar da facção, caso queira sair use /f sair.");
			return;
		}
		
		// Verificando se o target é o líder e verificando se o sender não é 1 admin
		if (mplayer.getRole() == Rel.LEADER && !msender.isOverriding())
		{
			throw new MassiveException().addMsg("§cO líder da facção não pode ser expulso!");
		}
		
		// Verificando se o rank do sender é maior que o do target e verificando se o sender não é 1 admin
		if (mplayer.getRole() == Rel.OFFICER && msender.getRole() == Rel.OFFICER && ! msender.isOverriding())
		{
			throw new MassiveException().addMsg("§cApenas o líder da facção pode expulsar ou rebaixar outros capitões.");
		}

		// Evento
		EventFactionsMembershipChange event = new EventFactionsMembershipChange(sender, mplayer, FactionColl.get().getNone(), MembershipChangeReason.KICK);
		event.run();
		if (event.isCancelled()) return;

		Faction mplayerFaction = mplayer.getFaction();
		// Aplicando o evento e informando o player e facção
		if (mplayer.getRole() == Rel.LEADER)
		{
			mplayerFaction.promoteNewLeader();
		}
		mplayerFaction.uninvite(mplayer);
		mplayer.resetFactionData();
		mplayerFaction.msg("§e%s§e expulsou §e\"%s\"§e da facção! :O", msender.getRole().getPrefix() + msender.getName(), mplayer.getName());
		mplayer.msg("§eVocê foi expulso da facção §f%s §epor §e%s!§e :O", mplayerFaction.getName(), msender.getRole().getPrefix() + msender.getName());
	}
	
}
