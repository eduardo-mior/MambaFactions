package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Invitation;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsInvitedChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.util.IdUtil;

public class CmdFactionsConviteAdd extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsConviteAdd()
	{
		// Aliases
	    this.addAliases("a", "add", "enviar", "adicionar");
		
		// Descrição
		this.setDesc("§6 convite add §e<player> §8-§7 Envia um convite para um player.");
		
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
			msg("§cVocê precisar ser capitão ou superior para poder gerenciar os convites da facção.");
			return;
		}
		
		// Verficiando se os argumentos são validos
		if (!this.argIsSet()) {
			msg("§cArgumentos insuficientes, use /f convite add <player>");
			return;
		}
		
		// Verificando se a facção não exedeu o limite de convites
		if (msenderFaction.getInvitations().size() >= 21) {
			msg("§cLimite máximo de convites pendentes atingido (21)! Apague alguns convites inúteis para poder enviar novos convites.");
			return;
		}
		
		// Verificando se o sender e o target são a mesma pessoa
		String name = this.arg();
		if (msender.getName().equalsIgnoreCase(name)) {
			msg("§cVocê não pode enviar um convite para você mesmo.");
			return;
		}
		
		// Argumentos
		MPlayer mplayer = readMPlayer(name);
			
		// Verificando se o player já é um membro
		if (mplayer.getFaction() == msenderFaction) {
			msg("§c'%s'§c já é membro da sua facção.", mplayer.getName());
			return;
		}
			
		// Verificando se o player já esta possui um convite
		boolean isInvited = msenderFaction.isInvited(mplayer);
			
		if (!isInvited) {
			
			// Verificando se o player já não possui muitos conites
			if (mplayer.getInvitations().size() >= 21) {
				msg("§cO player '" +  mplayer.getName() + "' já antingiu o limite máximo de convites de facções pendentes (21), peça para que ele apague alguns convites inúteis para que você possa enviar o seu.");
				return;
			}
			
			// Evento
			EventFactionsInvitedChange event = new EventFactionsInvitedChange(sender, mplayer, msenderFaction, isInvited);
			event.run();
			if (event.isCancelled()) return;
			isInvited = event.isNewInvited();
				
			// Informando o sender e o target
			mplayer.msg("§a%s§a convidou você para entrar na facção §f[%s§f]§a.", msender.getRole().getPrefix() + msender.getName(), msenderFaction.getName());
			msg("§aConvite enviado com sucesso para '%s'.", mplayer.getName());
				
			// Aplicando o evento
			String senderId = IdUtil.getId(sender);
			long creationMillis = System.currentTimeMillis();
			Invitation invitation = new Invitation(senderId, creationMillis);
			msenderFaction.invite(mplayer, invitation);
			msenderFaction.changed();
		}
		else {
			msg("§c'%s'§c já possui um convite para entrar na sua facção.", mplayer.getName());
		}
	}
	
}