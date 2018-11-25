package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.mson.Mson;

public class CmdFactionsKick extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsKick()
	{
		// Aliases
		this.addAliases("expulsar", "kickar");
		
		// Descrição
		this.setDesc("§6 kick §e<player> §8-§7 Expulsa um player da facção.");
		
		// Requisitos
		this.addRequirements(ReqHasFaction.get());
		
		// Parametros (necessario)
		this.addParameter(TypeString.get(), "player", "erro", true);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Verificando se o player possui permissão
		if (!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msg("§cVocê precisar ser capitão ou superior para poder expulsar membros da facção.");
			return;
		}
		
		 // Verificando se a facção não esta sob ataque
		if (msenderFaction.isInAttack()) {
			msg("§cVocê não pode expulsar membros da facção enquanto ela estiver sobre ataque.");
			return;
		}
		
		// Verficiando se os argumentos são validos
		if (!this.argIsSet()) {
			msg("§cArgumentos insuficientes, use /f kick <player>");
			return;
		}
		
		// Verificando se o sender e o target são os mesmos
		String name = this.arg();
		if (msender.getName().equalsIgnoreCase(name)) {
			message(Mson.parse("§cVocê não pode se expulsar da facção, caso queira sair use /f sair").command("/f sair"));
			return;
		}
		
		// Argumentos
		MPlayer mplayer = readMPlayer(name);
		
		// Verificando se o target é da mesma facão que o sender
		if (!msenderFaction.getMPlayers().contains(mplayer)) {
			msg("§cEste jogador não esta na sua facção.");
			return;
		}
		
		// Verificando se o target é o líder e verificando se o sender não é 1 admin
		if (mplayer.getRole() == Rel.LEADER && !msender.isOverriding()) {
			msg("§cO líder da facção não pode ser expulso!");
			return;
		}
		
		// Verificando se o rank do sender é maior que o do target e verificando se o sender não é 1 admin
		if (mplayer.getRole() == Rel.OFFICER && msender.getRole() == Rel.OFFICER && !msender.isOverriding()) {
			msg("§cApenas o líder da facção pode expulsar ou rebaixar outros capitões.");
			return;
		}

		// Evento
		EventFactionsMembershipChange event = new EventFactionsMembershipChange(sender, mplayer, FactionColl.get().getNone(), MembershipChangeReason.KICK);
		event.run();
		if (event.isCancelled()) return;

		Faction faction = mplayer.getFaction();
		// Aplicando o evento e informando o player e facção
		if (mplayer.getRole() == Rel.LEADER) {
			faction.promoteNewLeader();
		}
		faction.uninvite(mplayer);
		mplayer.resetFactionData();
		faction.msg("§e%s§e expulsou §f%s§e da facção! :O", msender.getRole().getPrefix() + msender.getName(), mplayer.getName());
		mplayer.msg("§eVocê foi expulso da facção §f[%s§f]§e por §e%s!", faction.getName(), msender.getRole().getPrefix() + msender.getName());
	}
	
}