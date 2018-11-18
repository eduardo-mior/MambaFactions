package com.massivecraft.factions.cmd;

import java.util.Collections;
import java.util.Set;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.ps.PS;

public class CmdFactionsSetAuto extends FactionsCommand
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsSetAuto(boolean claim)
	{
		// Fields
		this.setSetupEnabled(false);
		
		// Aliases
		this.addAliases("auto");
		
		// Descrição
		this.setDesc("§6 dominar auto §e[on/off] §8-§7 Conquista as terras automaticamente enquanto você anda.");
		
		// Parametros (não necessario)
		this.addParameter(TypeString.get(), "on/off", "erro", true);
		
		// Requisitos
		this.addRequirements(RequirementIsPlayer.get());
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{		
		// MPerm Preemptive Check
		if (!MPerm.getPermTerritory().has(msender, msenderFaction, true)) return;
		
		// Verificando se a facção esta em ataque
		if (msenderFaction.isInAttack()) {
			msg("§cVocê não pode controlar territórios enquanto sua facção estiver sobre ataque!");
			return;
		}
		
		// Argumentos
		Boolean old = msender.getAutoClaimFaction() == null ? false : true;
		Boolean target = readBoolean(old);
		
		// Verificando se o player digitou um argumento correto
		if (target == null) {
			msg("§cComando incorreto, use /f claim auto [on/off]");
			return;
		}
		
		// Descrição da ação
		String desc = target ? "§2ativado": "§cdesativado";
		
		// Verificando se o player ja esta com o modo auto-claim ativado/desativado
		if (target == old) {
			msg("§aO modo auto conquistar terras já está %s§a.", desc);
			return;
		}
		
		// Args
		final Faction newFaction = target ? msenderFaction : null;
		
		// Setando o auto-claim e informando o sender
		msender.setAutoClaimFaction(newFaction);
		msg("§aModo auto conquistar terras %s§a.", desc);
		
		// Se o auto-claim foi desligado então o processo é encerrado
		if (newFaction == null) return;
		
		// Chunks
		final PS chunk = PS.valueOf(me.getLocation()).getChunk(true);
		Set<PS> chunks = Collections.singleton(chunk);		
		
		// Apply / Inform
		msender.tryClaim(newFaction, chunks);
	}
}