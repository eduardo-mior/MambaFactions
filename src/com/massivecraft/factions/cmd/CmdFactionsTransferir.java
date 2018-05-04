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
		MPlayer mp = this.readArg(msender);
		Faction msf = msender.getFaction();
		Faction mpf = mp.getFaction();
		Rel cargoms = msender.getRole();
		
		// Verificando se o sender é lider da facção
		if (cargoms != Rel.LEADER) {
			msender.message("§cApenas o líder da facção pode promover um capitão para líder.");
			return;
		}
		
		// Verificando se o sender e o target são a mesma pessoa
		if (msender == mp) {
			msender.message("§cVocê não pode transferir a liderança para você mesmo");
			return;
		}

		// Verificando se o target é da mesma facão que o sender
		if (msf != mpf) {
			msender.message("§cEste jogador não esta na sua facção.");
			return;
		}
		
		// Aplicando o evento
		msender.setRole(Rel.OFFICER);
		mp.setRole(Rel.LEADER);
		
		// Informando o sender e o target
		msf.msg("§e" + msender.getName() + "§e transferiu a lideração da facção para \"§e" + mp.getName() + "\"§e.");
	}
}
