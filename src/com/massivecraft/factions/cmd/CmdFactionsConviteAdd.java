package com.massivecraft.factions.cmd;

import java.util.Collection;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.Invitation;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsInvitedChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.container.TypeSet;
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
		
		// Descrição do comando
		this.setDesc("§6 convite add §e<player> §8-§7 Envia um convite para um player.");
		
		// Parametros (necessario)
		this.addParameter(TypeSet.get(TypeMPlayer.get()), "players", true);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //	
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		Collection<MPlayer> mplayers = this.readArg();
		
		// Variaveis
		String senderId = IdUtil.getId(sender);
		long creationMillis = System.currentTimeMillis();
		
		// Verificando se o player possui permissão
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msender.message("§cVocê precisar ser capitão ou superior para poder gerenciar os convites da facção.");
			return;
		}
		
		// Verificando se a facção não exedeu o limite de convites
		if(msenderFaction.getInvitations().size() >= 10) {
			msender.message("§cLimite máximo de convites pendentes atingido (10)! Apague alguns convites inúteis para poder enviar novos convites.");
			return;
		}
		
		for (MPlayer mplayer : mplayers)
		{	
			
			// Verificando se o sender e o target são a mesma pessoa
			if (msender == mplayer) {
				msender.message("§cVocê já faz parte de uma facção e você não pode adicionar um convite para você mesmo.");
				continue;
			}
			
			// Verificando se o player já é um membro
			if (mplayer.getFaction() == msenderFaction)
			{
				msg("§c\"%s\"§c já é membro da sua facção.", mplayer.getName());
				continue;
			}
			
			// Verificando se o player já esta possui um convite
			boolean isInvited = msenderFaction.isInvited(mplayer);
			
			if ( ! isInvited)
			{
				// Evento
				EventFactionsInvitedChange event = new EventFactionsInvitedChange(sender, mplayer, msenderFaction, isInvited);
				event.run();
				if (event.isCancelled()) continue;
				isInvited = event.isNewInvited();
				
				// Informando o sender e o target
				mplayer.msg("§a%s§a convidou você para entrar na facção §f%s§a.", msender.getName(), msenderFaction.getName());
				msenderFaction.msg("§e%s§e convidou §e\"%s\"§e para entrar na sua facção.", msender.getRole().getPrefix() + msender.getName(), mplayer.getName());
				
				// Aplicando o evento
				Invitation invitation = new Invitation(senderId, creationMillis);
				msenderFaction.invite(mplayer.getId(), invitation);
				msenderFaction.changed();
			}
			else
			{
				// Informando que o player ja possui 1 convite
				msg("§c\"%s\"§c já possui um convite para entrar na sua facção.", mplayer.getName());
			}
		}
	}
	
}
