package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.ps.PS;

public class CmdFactionsVoar extends FactionsCommand
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsVoar() 
	{
		// Aliases
	    this.addAliases("fly");
		
		// Descrição
		this.setDesc("§6 voar §e[on/off] §8-§7 Habilita o fly nos territórios da facção.");
	    
		// Requisitos
		this.addRequirements(RequirementIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		
		// Parametros (não necessario)
		this.addParameter(TypeString.get(), "on/off", "erro", true);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{		
		// Verificando se o player pode ligar o fly
		PS ps = PS.valueOf(me.getLocation());
		if (!msender.isOverriding() && !BoardColl.get().getFactionAt(ps).equals(msenderFaction) && !me.hasPermission("factions.voar.bypass")) {
			msg("§cVocê não pode habilitar o modo voar fora dos territórios da sua facção.");
			return;
		}
		 
		// Verificando se a facção não esta sob ataque
		if (msenderFaction.isInAttack()) {
			msg("§cVocê não pode habilitar o modo voar enquanto sua facção estiver sob ataque.");
			return;
		}
		
		// Argumentos
		boolean old = me.getAllowFlight();
		Boolean target = readBoolean(old);
		
		// Verificando se o player digitou um argumento correto
		if (target == null) {
			msg("§cComando incorreto, use /f voar [on/off]");
			return;
		}
		
		// Descrição da ação
		String desc = target ? "§2ativado": "§cdesativado";
		
		// Verificando se o player ja esta com o modo fly ativado/desativado
		if (target == old) {
			msg("§aO seu modo voar já está %s§a.", desc);
			return;
		}
		
		// Setando o modo fly como ativado/desativado
		me.setAllowFlight(target);
		
		// Informando o msender
		msg("§aModo voar %s§a.", desc);
	}
}
