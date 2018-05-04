package com.massivecraft.factions.cmd;

import org.bukkit.entity.Player;

import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.engine.EngineMenuGui;
import com.massivecraft.factions.engine.EngineSobAtaque;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;

public class CmdFactionsSobAtaque extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsSobAtaque()
	{
		// Aliases
        this.addAliases("ataque");
        
		// Requisições
		this.addRequirements(RequirementIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());

		// Descrição do comando
		this.setDesc("§6 sobataque §8-§7 Mostra mais informações sobre o ataque.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		Player p = msender.getPlayer();
		Faction f = msenderFaction;
		if (!EngineSobAtaque.factionattack.containsKey(f)) {
			msender.message("§cSua facção não esta sob ataque!");
		} else {
			EngineMenuGui.abrirMenuFaccaoSobAtaque(p);
		}
	}
}
