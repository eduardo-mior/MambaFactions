package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.event.EventFactionsHomeTeleport;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.mixin.MixinTeleport;
import com.massivecraft.massivecore.mixin.TeleporterException;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.teleport.Destination;
import com.massivecraft.massivecore.teleport.DestinationSimple;

public class CmdFactionsHome extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsHome()
	{
		// Aliases
		this.addAliases("base", "h");
		
		// Parametros (não necessario)
		this.addParameter(TypeFaction.get(), "facção", "você");

		// Requisições
		this.addRequirements(RequirementIsPlayer.get());
		
		// Descrição do comando
		this.setDesc("§6 home §e[facção] §8-§7 Teleporta para a home da facção.");
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		
		// Argumentos
		Faction faction = this.readArg(msenderFaction);
		PS home = faction.getHome();
		String homeDesc = "§ehome da " + (msenderFaction == faction ? "§esua facção" : "§efacção §f" + faction.getName());
		
		// Verificando se o sender possui permissão
		if ( ! MPerm.getPermHome().has(msender, faction, true)) return;
		
		// Verificando se a facção possui permissão.
		if (home == null)
		{
			msender.msg("§f" + (msenderFaction == faction ? "§cSua facção" : faction.getName()) + "§c ainda não definiu a home da facção.");
			return;
		}
		
		// Verificando se o player esta em um território inimigo.
		if ( ! MConf.get().homesTeleportAllowedFromEnemyTerritory && msender.isInEnemyTerritory())
		{
			msender.msg("§eVocê não pode se teleportar para %s §epois você esta em um território de uma facção inimiga.", homeDesc);
			return;
		}

		// Evento
		EventFactionsHomeTeleport event = new EventFactionsHomeTeleport(sender);
		event.run();
		if (event.isCancelled()) return;
		
		// Aplicando o evento
		try
		{
			Destination destination = new DestinationSimple(home, homeDesc);
			MixinTeleport.get().teleport(me, destination, MConf.get().homeSeconds);
		}
		catch (TeleporterException e)
		{
			String message = e.getMessage();
			MixinMessage.get().messageOne(me, message);
		}
	}
	
}
