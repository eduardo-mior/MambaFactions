package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.mson.Mson;

public class CmdFactionsPromover extends FactionsCommand 
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsPromover() 
	{	
		// Aliases
		this.addAliases("promote", "up");
		
		// Descrição
		this.setDesc("§6 promover §e<player> §8-§7 Promove um player de cargo.");
		
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
			msg("§cVocê precisar ser capitão ou superior para poder administrar os cargos da facção.");
			return;
		}
		
		// Verficiando se os argumentos são validos
		if (!this.argIsSet()) {
			msg("§cArgumentos insuficientes, use /f promover <player>");
			return;
		}
		
		// Verificando se o sender e o target são a mesma pessoa
		String name = this.arg();
		if (msender.getName().equalsIgnoreCase(name)) {
			msg("§cVocê não pode promover você mesmo.");
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

		// Verificando se o target ja é líder
		if (cargomp == Rel.LEADER) {
			msg("§c"+ cargomp.getPrefix() + target.getName() + "§c já é o líder da facção.");
			return;
		}

		// Se o target for recruit = sucesso
		if (cargomp == Rel.RECRUIT) {
			msender.msg("§aPlayer promovido com sucesso para o cargo de Membro.");
			target.msg("§aVocê foi promovido para o cargo de Membro por " + cargoms.getPrefix() + msender.getName() + ".");
			target.setRole(Rel.MEMBER);
			return;
		}

		// Verificando se o target é um membro e verificando ainda se o sender é capitão
		if (cargomp == Rel.MEMBER) {
			if (cargoms == Rel.OFFICER) {
				msg("§cApenas o líder da facção pode promover um membro para capitão.");
				return;
			}
			msender.msg("§aPlayer promovido com sucesso para o cargo de Capitão.");
			target.msg("§aVocê foi promovido para o cargo de Capitão por " + cargoms.getPrefix() + msender.getName() + ".");
			target.setRole(Rel.OFFICER);
			return;
		}

		// Verificando se o target é um capitão e verificando ainda se o sender é líder
		if (cargomp == Rel.OFFICER) {
			if (cargoms == Rel.LEADER) {
				message(Mson.parse("§cEste jogador já é capitão da facção, caso queira transferir a liderança da facção use /f transferir §c" +  target.getName()).suggest("/f transferir " + target.getName()));
				return;
			}
			msg("§cApenas o líder da facção pode promover um capitão para líder.");
			return;
		}
	}

}
