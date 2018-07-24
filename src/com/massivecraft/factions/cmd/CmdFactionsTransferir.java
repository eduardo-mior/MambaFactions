package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.cmd.type.TypeMPlayer;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;

public class CmdFactionsTransferir extends FactionsCommand {

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsTransferir() {
		
		// Aliases
		this.addAliases("lider", "liderança", "lideranca");
		
		// Parametros (necessario)
		this.addParameter(TypeMPlayer.get(), "jogador");
		
		// Requisições
		this.addRequirements(ReqHasFaction.get());
		
		// Descrição do comando
		this.setDesc("§6 transferir §e<player> §8-§7 Transfere a liderança da facção.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public void perform() throws MassiveException {
		MPlayer target = this.readArg(msender);
		Faction facSender = msender.getFaction();
		Faction facTarget = target.getFaction();
		Rel cargoSender = msender.getRole();
		
		// Verificando se o sender é lider da facção
		if (cargoSender != Rel.LEADER) {
			msender.message("§cApenas o líder da facção pode promover um capitão para líder.");
			return;
		}
		
		// Verificando se o sender e o target são a mesma pessoa
		if (msender == target) {
			msender.message("§cVocê não pode transferir a liderança para você mesmo");
			return;
		}

		// Verificando se o target é da mesma facão que o sender
		if (facSender != facTarget) {
			msender.message("§cEste jogador não esta na sua facção.");
			return;
		}
		
		// Aplicando o evento
		msender.setRole(Rel.OFFICER);
		target.setRole(Rel.LEADER);
		
		// Informando o sender e o target
		facSender.msg("§e" + msender.getName() + "§e transferiu a lideração da facção para \"§e" + target.getName() + "\"§e.");
	}
}
