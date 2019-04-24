package com.massivecraft.factions.cmd;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.event.EventFactionsHomeTeleport;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
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
		
		// Descrição
		this.setDesc("§6 home §e[facção] §8-§7 Teleporta para a home da facção.");

		// Requisitos
		this.addRequirements(RequirementIsPlayer.get());
		
		// Parametros (não necessario)
		this.addParameter(TypeString.get(), "outra facção", "sua facção", true);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		Faction faction = readFaction();
		
		// Verificando se a facção é a zona livre
		if (faction.isNone()) {
			msg("§cArgumentos insuficientes, use /f home <facção>");
			return;
		}
		
		// Verificando se o sender possui permissão
		if (!MPerm.getPermHome().has(msender, faction, true)) return;
		
		// Pegando a home da facção e a descrição da ação
		PS home = faction.getHome();
		String homeDesc = "§ehome da " + (msenderFaction == faction ? "§efacção" : "§f[" + faction.getName() + "§f]");
		
		// Verificando se a facção possui permissão.
		if (home == null) {
			msg("§f" + (msenderFaction == faction ? "§cSua facção não possui a home definida." : "§f[" + faction.getName() + "§f]§c não possui a home da facção definida."));
			return;
		}
		
		// Verificando se o player esta em um território inimigo.
		if (!MConf.get().homesTeleportAllowedFromEnemyTerritory && msender.isInEnemyTerritory()) {
			msg("§eVocê não pode se teleportar para %s §epois você esta em um território de uma facção inimiga.", homeDesc);
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
			MixinMessage.get().messageOne(me, e.getMessage());
		}
	}
	
}