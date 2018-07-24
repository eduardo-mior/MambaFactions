package com.massivecraft.factions.cmd;

import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.engine.EngineKdr;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;

public class CmdFactionsMembros extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsMembros()
	{
		// Aliases
		this.addAliases("status", "s");
        
		// Requisições
		this.addRequirements(RequirementIsPlayer.get());
		
		// Parametros (não necessario)
		this.addParameter(TypeFaction.get(), "facção", "você");
		
		// Descrição do comando
		this.setDesc("§6 membros §e<facção> §8-§7 Mostra a lista de membros da facção.");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos
		Faction faction = this.readArg(msenderFaction);
		
		// Lista de membros da facção
		List<MPlayer> mps = faction.getMPlayers();
		
		// Verificando se a facção tem muitos membros
		if (faction.isNone() || mps.size() > 43) {
			msender.msg("§cA facção §f"+ faction.getName() +"§c possui muitos membros portanto o Menu GUI não sera aberto.");
			return;
		}
		
		// Verificando se a facção tem membros (zonadeguerra e zonaprotegida não tem...)
		if (mps.size() == 0) {
			msender.msg("§cA facção §f" + faction.getName() + "§c não possui membros!");
			return;
		}
		
		// Limite de membros por facção
		int limitemembros = MConf.get().factionMemberLimit;
		
		// Definindo o tamanho do menu com base no limite de membros por facção
		int tamanhodomenu = 54;
		if (limitemembros <= 10) {
			tamanhodomenu = 36;
		} else if (limitemembros > 10 && limitemembros < 20) {
			tamanhodomenu = 45;
		}
		
		Inventory inv = Bukkit.createInventory(null, tamanhodomenu, "§8Membros da " + faction.getName());
		
		if (tamanhodomenu == 36) {
			inv.setItem(11, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(12, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(13, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(14, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(15, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(20, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(21, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(22, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(23, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(24, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());

		} else if (tamanhodomenu == 45) {
			inv.setItem(11, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(12, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(13, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(14, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(15, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(20, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(21, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(22, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(23, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(24, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(29, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(30, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(31, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(32, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(33, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			
		} else {
			inv.setItem(11, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(12, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(13, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(14, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(15, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(20, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(21, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(22, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(23, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(24, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(29, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(30, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(31, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(32, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(33, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(38, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(39, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(40, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(41, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
			inv.setItem(42, new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack());
		}
		
		MPlayer mplayer = msender;
		Player p = mplayer.getPlayer();
		int slot = 11;
		for (int i = 0; i < faction.getMPlayers().size(); i++) {
			MPlayer mp = mps.get(i);
			Rel cargo = mp.getRole();
			String nome = mp.getName();
			boolean isOnline = mp.isOnline();
			double poderMax = mp.getPowerMax();
			double poderAtual = mp.getPower();
			int kills = EngineKdr.getPlayerKills(mp);
			int deaths = EngineKdr.getPlayerDeaths(mp);
			String kdr2f = EngineKdr.getPlayerKdr(mp);
			String poderAtual1f = String.format("%.1f", poderAtual);
			long ultimoLoginMillis = mp.getLastActivityMillis() - System.currentTimeMillis();
			LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(ultimoLoginMillis, TimeUnit.getAllButMillis()), 3);
			String ultimoLogin = TimeDiffUtil.formatedMinimal(ageUnitcounts, "§e");
			inv.setItem(slot, new ItemBuilder(Material.SKULL_ITEM,1,3).setSkullOwner(nome).setName("§7"+nome).setLore("§fPoder: §7" +  poderAtual1f + "/" + poderMax,"§fCargo: §7" + cargo.getPrefix() + cargo.getName(),"§fAbates: §7" + kills,"§fMortes: §7" + deaths, "§fKdr: §7" + kdr2f, "§fStats: " + (isOnline ? "§aOnline" : "§cOffline"), "§fÚltimo login: §7" + ultimoLogin + "§e atrás").toItemStack());
			slot+= slot == 15 || slot == 24 || slot == 33 ? + 5 : + 1;
		}
		p.openInventory(inv);
	}
}
