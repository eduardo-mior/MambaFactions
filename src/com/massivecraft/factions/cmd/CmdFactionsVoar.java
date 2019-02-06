package com.massivecraft.factions.cmd;

import com.massivecraft.factions.engine.EngineFly;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.MConf;
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
		
		// Parametros (não necessario)
		this.addParameter(TypeString.get(), "on/off", "erro", true);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{	
		// Verificando se o player permissão de bypass
		boolean bypass = me.hasPermission("factions.voar.bypass") || msender.isOverriding();
		if (!bypass) {
		
			// Verificando se o player possui facção
			if (!msender.hasFaction()) {
				msg("§cVocê precisa estar em alguma facção para poder utilizar este comando.");
				return;
			}
			
			// Verificando se o player pode ligar o fly
			PS ps = PS.valueOf(me.getLocation());
			if (!BoardColl.get().getFactionAt(ps).equals(msenderFaction) && !MConf.get().mundosComFlyAtivado.contains(me.getWorld().getName())) {
				msg("§cVocê não pode habilitar o modo voar fora dos territórios da sua facção.");
				return;
			}
			 
			// Verificando se a facção não esta sob ataque
			if (msenderFaction.isInAttack()) {
				msg("§cVocê não pode habilitar o modo voar enquanto sua facção estiver sob ataque.");
				return;
			}
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
		
		// Caso o player não possua bypass, ativando
		if (!bypass) {
			if (target) EngineFly.flys.add(me);
			else        EngineFly.flys.remove(me);
		}
		
		// Setando o modo fly como ativado/desativado
		me.setAllowFlight(target);
		
		// Informando o msender
		msg("§aModo voar %s§a.", desc);
	}
}
