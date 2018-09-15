package com.massivecraft.factions.cmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;

public class CmdFactionsPermissoes extends FactionsCommand{


	public CmdFactionsPermissoes()
	{
		// Aliases
		this.addAliases("perm");
        
		// Requisições
		this.addRequirements(RequirementIsPlayer.get());
		this.addRequirements(ReqHasFaction.get());

		// Description
		this.setDesc("§6 perm §8-§7 Gerencia as permissões da facção.");
	}


	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		Player p = msender.getPlayer();		
		abrirMenuPermissoes(p, msender, msenderFaction);
	}
	
	public static void abrirMenuPermissoes(Player p, MPlayer mp, Faction f) {
		Inventory perms = Bukkit.createInventory(null, 3*9, "§8Permissões - " + f.getName());
		
		List<String> lore = new ArrayList<String>();
		
		///////////////////////////////
		
		ItemStack build = new ItemStack(Material.GRASS, 1);
		ItemMeta bm = build.getItemMeta();
		
		bm.setDisplayName("§eConstruir");
		
		lore.add("§7"); lore.add("§7Clique para gerenciar a permissão de");
		lore.add("§7construir nos territórios da sua facção.");
		lore.add("§7");
		lore.add("§eLista de permitidos:");
		for(Rel rel : f.getPermitted(MPerm.getPermBuild())) {
			lore.add(" §a- " + rel.getName());
		}
		
		bm.setLore(lore);
		
		lore.clear();
		
		build.setItemMeta(bm);
		
		////////////////////////////////
		
		ItemStack container = new ItemStack(Material.CHEST, 1);
		ItemMeta cm = container.getItemMeta();
		
		cm.setDisplayName("§eAbrir containers");
		
		lore.add("§7"); lore.add("§7Clique para gerenciar a permissão de abrir");
		lore.add("§7containers nos territórios da sua facção.");
		lore.add("§7");
		lore.add("§eLista de permitidos:");
		for(Rel rel : f.getPermitted(MPerm.getPermContainer())) {
			lore.add(" §a- " + rel.getName());
		}
		
		cm.setLore(lore);
		
		lore.clear();
		
		container.setItemMeta(cm);
		
		/////////////////////////////////
		
		ItemStack home = new ItemStack(Material.BED, 1);
		ItemMeta homem = build.getItemMeta();
		
		homem.setDisplayName("§eIr para home");
		
		lore.add("§7"); lore.add("§7Clique para gerenciar a permissão");
		lore.add("§7de acesso à home da facção.");
		lore.add("§7");
		lore.add("§eLista de permitidos:");
		for(Rel rel : f.getPermitted(MPerm.getPermHome())) {
			lore.add(" §a- " + rel.getName());
		}
		
		homem.setLore(lore);
		
		lore.clear();
		
		home.setItemMeta(homem);
		
		//////////////////////////////////
		
		ItemStack porta = new ItemStack(Material.WOOD_DOOR, 1);
		ItemMeta pm = porta.getItemMeta();
		
		pm.setDisplayName("§eAbrir portas");
		
		lore.add("§7"); lore.add("§7Clique para gerenciar a permissão de abrir");
		lore.add("§7portas nos territórios da sua facção.");
		lore.add("§7");
		lore.add("§eLista de permitidos:");
		for(Rel rel : f.getPermitted(MPerm.getPermDoor())) {
			lore.add(" §a- " + rel.getName());
		}
		
		pm.setLore(lore);
		
		lore.clear();
		
		porta.setItemMeta(pm);
		
		///////////////////////////////////
		
		ItemStack botao = new ItemStack(Material.STONE_BUTTON, 1);
		ItemMeta bom = botao.getItemMeta();
		
		bom.setDisplayName("§eUsar botões");
		
		lore.add("§7"); lore.add("§7Clique para gerenciar a permissão de usar");
		lore.add("§7botões nos territórios da sua facção.");
		lore.add("§7");
		lore.add("§eLista de permitidos:");
		for(Rel rel : f.getPermitted(MPerm.getPermButton())) {
			lore.add(" §a- " + rel.getName());
		}
		
		bom.setLore(lore);
		
		lore.clear();
		
		botao.setItemMeta(bom);
		
		////////////////////////////////////
		
		ItemStack alav = new ItemStack(Material.LEVER, 1);
		ItemMeta am = alav.getItemMeta();
		
		am.setDisplayName("§eUsar alavancas");
		
		lore.add("§7"); lore.add("§7Clique para gerenciar a permissão de usar");
		lore.add("§7alavancas nos territórios da sua facção.");
		lore.add("§7");
		lore.add("§eLista de permitidos:");
		for(Rel rel : f.getPermitted(MPerm.getPermLever())) {
			lore.add(" §a- " + rel.getName());
		}
		
		am.setLore(lore);
		
		lore.clear();
		
		alav.setItemMeta(am);
		
		////////////////////////////////////
		
		perms.setItem(10, build);
		perms.setItem(11, container);
		perms.setItem(12, home);
		perms.setItem(14, porta);
		perms.setItem(15, botao);
		perms.setItem(16, alav);
		
		p.openInventory(perms);
	}
	
}
