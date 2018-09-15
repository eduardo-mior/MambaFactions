package com.massivecraft.factions.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.engine.EngineSobAtaque;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeBooleanYes;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsVoar extends FactionsCommand
{	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsVoar() 
	{
		// Aliases
	    this.addAliases("fly");
		
		// Parametros (não necessario)
		this.addParameter(TypeBooleanYes.get(), "on/off", "uma vez");
	    
		// Requisições
		this.addRequirements(RequirementIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());
		
		// Descrição do comando
		this.setDesc("§6 voar §e[on/off] §8-§7 Habilita o fly nos territórios da facção.");
	
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Pegando o player
		Player p = msender.getPlayer();
		
		// Verificando se o player pode ligar o fly
		PS ps = PS.valueOf(p.getLocation());
		if (!msender.isOverriding() && !BoardColl.get().getFactionAt(ps).equals(msenderFaction)) {
			msg("§cVocê não pode habilitar o modo voar fora dos territórios da sua facção.");
			return;
		}
		 
		// Verificando se a facção não esta sob ataque
		if (EngineSobAtaque.factionattack.containsKey(msenderFaction.getName())) {
			msg("§cVocê não pode habilitar o modo voar enquanto sua facção estiver sob ataque.");
			return;
		}
		
		// Argumentos
		boolean old = p.getAllowFlight();
		boolean target = this.readArg(!old);
		String targetDesc = Txt.parse(target ? "§2ativado": "§cdesativado");
		
		// Verificando se o player ja esta com o modo fly ativado
		if (target == old)
		{
			msg("§aO modo voar já está %s§a.", targetDesc);
			return;
		}
		
		// Setando o modo fly como ativado/desativado
		p.setAllowFlight(target);
		
		// Informando o msender
		msg("§aModo voar %s§a.", targetDesc);
	}
}
