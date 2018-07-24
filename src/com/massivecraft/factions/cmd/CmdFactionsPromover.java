package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsPromover extends FactionsCommand {

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsPromover() {
		
		// Aliases
		this.addAliases("promote", "up");
		
		// Parametros (necessario)
		this.addParameter(TypeMPlayer.get(), "jogador");
		
		// Requisições
		this.addRequirements(ReqHasFaction.get());
		
		// Descrição do comando
		this.setDesc("§6 promover §e<player> §8-§7 Promove um player de cargo.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException {
		
		// Verificando se o player possui permissão
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msender.message("§cVocê precisar ser capitão ou superior para poder administrar os cargos da facção.");
			return;
		}
		
		// Argumentos
		MPlayer target = this.readArg(msender);
		Faction facSender = msender.getFaction();
		Faction facTarget = target.getFaction();
		
		// Verificando se o sender e o target são a mesma pessoa
		if (msender == target) {
			msender.message("§cVocê não pode promover você mesmo.");
			return;
		}

		// Verificando se o target é da mesma facão que o sender
		if (facSender != facTarget) {
			msender.message("§cEste jogador não esta na sua facção.");
			return;
		}

		Rel cargoms = msender.getRole();
		Rel cargomp = target.getRole();

		// Verificando se o target ja é líder
		if (cargomp == Rel.LEADER) {
			msender.message("§c"+ target.getName() + "§c já é o líder da facção.");
			return;
		}

		// Se o targe for recruit = sucesso
		if (cargomp == Rel.RECRUIT) {
			facSender.msg("§e" + msender.getRole().getPrefix() + msender.getName() + "§e promoveu \"§e" + target.getName() + "§e\" para o cargo de membro da facção.");
			target.setRole(Rel.MEMBER);
			return;
		}

		// Verificando se o target é um membro e verificando ainda se o sender é capitão
		if (cargomp == Rel.MEMBER) {
			if (cargoms == Rel.OFFICER) {
				msender.message("§cApenas o líder da facção pode promover um membro para capitão.");
				return;
			}
			facSender.msg("§e" + msender.getRole().getPrefix() + msender.getName() + "§e promoveu \"§e" + target.getName() + "§e\"§e para o cargo de capitão da facção.");
			target.setRole(Rel.OFFICER);
			return;
		}

		// Verificando se o target é um capitão e verificando ainda se o sender é líder
		if (cargomp == Rel.OFFICER) {
			if (cargoms == Rel.LEADER) {
				msender.message(
						"§cEste jogador já é capitão da facção, caso queira transferir a liderança da facção use /f transferir §c" +  target.getName() + "§c");
				return;
			}
			msender.message("§cApenas o líder da facção pode promover um capitão para líder.");
			return;
		}
	}

}
