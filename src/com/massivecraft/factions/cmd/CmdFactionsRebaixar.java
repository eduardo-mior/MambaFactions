package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;

public class CmdFactionsRebaixar extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsRebaixar() 
	{
		// Aliases
		this.addAliases("demotar", "demote", "demover");
		
		// Descrição
		this.setDesc("§6 rebaixar §e<player> §8-§7 Rebaixa um player de cargo.");
		
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
		if(!(msender.getRole() == Rel.LEADER || msender.getRole() == Rel.OFFICER || msender.isOverriding())) {
			msg("§cVocê precisar ser capitão ou superior para poder administrar os cargos da facção.");
			return;
		}
		
		// Verficiando se os argumentos são validos
		if (!this.argIsSet()) {
			msg("§cArgumentos insuficientes, use /f rebaixar <player>");
			return;
		}
		
		// Verificando se o sender e o target são a mesma pessoa
		String name = this.arg();
		if (msender.getName().equalsIgnoreCase(name)) {
			msg("§cVocê não pode rebaixar você mesmo.");
			return;
		}
		
		// Argumentos
		MPlayer target = readMPlayer(name);
		Faction facSender = msender.getFaction();
		Faction facTarget = target.getFaction();
		
		// Verificando se o target é da mesma facão que o sender
		if (facSender != facTarget) {
			msg("§cEste jogador não esta na sua facção.");
			return;
		}
		
		Rel cargoms = msender.getRole();
		Rel cargomp = target.getRole();

		// Verificando se o target é o líder da facção
		if (cargomp == Rel.LEADER) {
			msg("§c" + cargomp.getPrefix() + target.getName() + "§c é o líder da facção portanto não pode ser rebaixado.");
			return;
		}

		// Verificando se o target já é o cargo mais baixo (recruit)
		if (cargomp == Rel.RECRUIT) {
			msg("§cEste jogador já esta no cargo mais baixo da facção, caso queira expulsa-lo use /f expulsar §c" + target.getName());
			return;
		}

		// Se o targe for member = sucesso
		if (cargomp == Rel.MEMBER) {
			msender.msg("§aPlayer rebaixado com sucesso para o cargo de Recruta.");
			target.msg("§eVocê foi rebaixado para o cargo de Recruta por " + cargoms.getPrefix() + msender.getName() + ".");
			target.setRole(Rel.RECRUIT);
			return;
		}

		// Verificando se o target é um capitão e verificando ainda se o sender é um capitão
		if (cargomp == Rel.OFFICER) {
			if (cargoms == Rel.OFFICER) {
				msg("§cApenas o líder da facção pode rebaixar um capitão.");
				return;
			}
			msender.msg("§aPlayer rebaixado com sucesso para o cargo de Membro.");
			target.msg("§eVocê foi rebaixado para o cargo de Membro por " + cargoms.getPrefix() + msender.getName() + ".");
			target.setRole(Rel.MEMBER);
			return;
		}
	}

}