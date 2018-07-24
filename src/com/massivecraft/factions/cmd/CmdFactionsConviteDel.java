package com.massivecraft.factions.cmd;

import java.util.HashSet;
import java.util.Set;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsInvitedChange;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.container.TypeSet;

public class CmdFactionsConviteDel extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	public CmdFactionsConviteDel()
	{
		// Aliases
	    this.addAliases("delete", "remove", "remover", "deletar");
		
		// Descrição do comando
		this.setDesc("§6 convite del §e<player> §8-§7 Deleta um convite enviado.");
		
		// Parametros (necessario)
		this.addParameter(TypeSet.get(TypeMPlayer.get()), "players", true);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //	
	
	@Override
	public void perform() throws MassiveException
	{	
		// Verificando se o player possui permissão
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msender.message("§cVocê precisar ser capitão ou superior para poder deletar convites enviados.");
			return;
		}
		
		Set<MPlayer> mplayers = new HashSet<>();
		mplayers = this.readArgAt(0);
		
		for (MPlayer mplayer : mplayers)
		{
			
			// Verificando se o sender e o target são a mesma pessoa
			if (msender == mplayer) {
				msender.message("§cVocê já faz parte de uma facção e você não pode remover um convite enviado a você.");
				return;
			}
			
			// Verificando se o player ja é membro da facção
			if (mplayer.getFaction() == msenderFaction)
			{				
				// Informando o sender
				msg("§c%s§c já é membro da sua facção.", mplayer.getName());
				continue;
			}
			
			// Verificando se o player esta convidado
			boolean isInvited = msenderFaction.isInvited(mplayer);
			
			if (isInvited)
			{
				// Evento
				EventFactionsInvitedChange event = new EventFactionsInvitedChange(sender, mplayer, msenderFaction, isInvited);
				event.run();
				if (event.isCancelled()) continue;
				isInvited = event.isNewInvited();
				
				// Informando o Player
				mplayer.msg("§e%s§e removeu seu convite para entrar na facção §f%s§e.", msender.getName(), msenderFaction.getName());
				
				// Informormando a facção
				msenderFaction.msg("§e%s§e removeu o convite de facção de §e\"%s\"§e.", msender.getRole().getPrefix() + msender.getName(), mplayer.getName());
				
				// Aplicando o evento
				msenderFaction.uninvite(mplayer);
				
				// If all, we do this at last. So we only do it once.
				msenderFaction.changed();
			}
			else
			{			
				// Informando o sender que o target não possui convite
				msg("§c\"%s\"§c não possui um convite para entrar na sua facção.", mplayer.getName());
			}
		}
	}
	
}
