package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.engine.EngineMenuGui;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.ps.PS;

public class CmdFactionsProteger extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsProteger()
	{	
		// Descrição
		this.setDesc("§6 proteger §8-§7 Protege territórios temporariamente.");
		
		// Requisitos
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(RequirementIsPlayer.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Verificando se tem permissão
		if (!MPerm.getPermTerritory().has(msender, msenderFaction, true)) return;
		
		// Verificando se a facção não esta sob ataque
		if (msenderFaction.isInAttack()) {
			msg("§cVocê não pode proteger terrenos enquanto sua facção estiver sobre ataque!");
			return;
		}
		
		// Verificando se é possível proteger claims nesse mundo
		if (!MConf.get().worldsClaimingEnabled.contains(PS.valueOf(me.getLocation()).getWorld())) {
			msg("§cA compra de territórios esta desabilitada neste mundo.");
			return;
		}
		
		// Verificando se a facção atingiu o limite de claims temporários
		int limit = MConf.get().limiteDeProtecoesTemporaria;
		if (limit > 0 && msenderFaction.getTempClaims().size() >= limit) {
			msg("§cLimite máximo de terrenos temporários atingido (" + limit + ")! Abandone terrenos temporário antigos para poder proteger novos terrenos.");
			return;
		}
		
		// Verificando se a facção já não é dona do território
		Faction factionAt = BoardColl.get().getFactionAt(PS.valueOf(me.getLocation()));
		if (factionAt.equals(msenderFaction)) {
			msg("§eSua facção já é dona deste território.");
			return;
		}
		
		// Verificando se o território esta livre
		if (!factionAt.isNone()) {
			msg("§cVocê só pode proteger terrenos que estejam livres.");
			return;
		} 
		
		EngineMenuGui.get().abrirMenuProtegerTerreno(msender);
	}
}
