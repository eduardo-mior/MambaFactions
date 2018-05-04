package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsRebaixar extends FactionsCommand {

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsRebaixar() {
		
		// Aliases
		this.addAliases("demotar", "demote", "demover");
		
		// Parametros (necessario)
		this.addParameter(TypeMPlayer.get(), "jogador");
		
		// Requisições
		this.addRequirements(ReqHasFaction.get());
		
		// Descrição do comando
		this.setDesc("§6 rebaixar §e<player> §8-§7 Rebaixa um player de cargo.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException {
		MPlayer mp = this.readArg(msender);
		Faction msf = msender.getFaction();
		Faction mpf = mp.getFaction();
		
		// Verificando se o player possui permissão
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msender.message("§cVocê precisar ser capitão ou superior para poder administrar os cargos da facção.");
			return;
		}
		
		// Verificando se o target é da mesma facão que o sender
		if (msf != mpf) {
			msender.message("§cEste jogador não esta na sua facção.");
			return;
		}

		// Verificando se o sender e o target são a mesma pessoa
		if (msender == mp) {
			msender.message("§cVocê não pode rebaixar você mesmo.");
			return;
		}
		
		Rel cargoms = msender.getRole();
		Rel cargomp = mp.getRole();

		// Verificando se o target é o líder da facção
		if (cargomp == Rel.LEADER) {
			msender.message("§c" + cargomp.getName() + "§c é o líder da facção portanto não pode ser rebaixado.");
			return;
		}

		// Verificando se o target já é o cargo mais baixo (recruit)
		if (cargomp == Rel.RECRUIT) {
			msender.message("§cEste jogador já esta no cargo mais baixo da facção caso queira expulsa-lo use /f expulsar §c" + mp.getName() + "§c" );
			return;
		}

		// Se o targe for member = sucesso
		if (cargomp == Rel.MEMBER) {
			msf.msg("§e" + msender.getRole().getPrefix() + msender.getName() + "§e rebaixou \"§e" + mp.getName() + "§e\" para o cargo de recruta da facção.");
			mp.setRole(Rel.RECRUIT);
			return;
		}

		// Verificando se o target é um capitão e verificando ainda se o sender é um capitão
		if (cargomp == Rel.OFFICER) {
			if (cargoms == Rel.OFFICER) {
				msender.message("§cApenas o líder da facção pode rebaixar um capitão.");
				return;
			}
			msf.msg("§e" + msender.getRole().getPrefix() + msender.getName() + "§e rebaixou \"§e" + mp.getName() + "§e\" para o cargo de membro da facção.");
			mp.setRole(Rel.MEMBER);
			return;
		}
	}

}
