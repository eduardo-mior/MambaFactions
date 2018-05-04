package com.massivecraft.factions.cmd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

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
		
		Faction f = msender.getFaction();
		
		abrirMenuPermissoes(p, msender, f);
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
	
	public static void abrirMenuConfig(Player p, MPerm perm) {
		String permnome = "";
		
		if(perm == MPerm.getPermBuild()) {
			permnome = "Construir";
		}
		
		if(perm == MPerm.getPermButton()) {
			permnome = "Usar botões";
		}
		
		if(perm == MPerm.getPermLever()) {
			permnome = "Usar alavancas";
		}
		
		if(perm == MPerm.getPermHome()) {
			permnome = "Ir para home";
		}
		
		if(perm == MPerm.getPermDoor()) {
			permnome = "Usar portas";
		}
		
		if(perm == MPerm.getPermContainer()) {
			permnome = "Usar containers";
		}
		
		Inventory permconfig = Bukkit.createInventory(null, 5*9, "§8Permissões - " + permnome);
		
		ItemStack recruta = new ItemStack(Material.STONE_SWORD, 1);
		ItemMeta rm = recruta.getItemMeta();
		rm.setDisplayName("§eRecruta");
		recruta.setItemMeta(rm);
		
		ItemStack membro = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta mm = membro.getItemMeta();
		mm.setDisplayName("§eMembro");
		membro.setItemMeta(mm);
		
		ItemStack capitao = new ItemStack(Material.DIAMOND_SWORD, 1);
		ItemMeta cm = capitao.getItemMeta();
		cm.setDisplayName("§eCapitão");
		capitao.setItemMeta(cm);
		
		ItemStack aliado = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta am = (LeatherArmorMeta)aliado.getItemMeta();
		am.setColor(Color.LIME);
		am.setDisplayName("§eAliado");
		aliado.setItemMeta(am);
		
		ItemStack neutro = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta nm = (LeatherArmorMeta)neutro.getItemMeta();
		nm.setColor(Color.WHITE);
		nm.setDisplayName("§eNeutro");
		neutro.setItemMeta(nm);
		
		ItemStack inimigo = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta im = (LeatherArmorMeta)inimigo.getItemMeta();
		im.setColor(Color.RED);
		im.setDisplayName("§eInimigo");
		inimigo.setItemMeta(im);
		
		/////
		
		boolean recruit;
		boolean member;
		boolean officer;
		
		boolean ally;
		boolean neutral;
		boolean enemy;
		
		Faction f = MPlayer.get(p).getFaction();
		
		if(f.getPermitted(perm).contains(Rel.RECRUIT)) {
			recruit = true;
		} else {
			recruit = false;
		}
		
		if(f.getPermitted(perm).contains(Rel.MEMBER)) {
			member = true;
		} else {
			member = false;
		}
		
		if(f.getPermitted(perm).contains(Rel.OFFICER)) {
			officer = true;
		} else {
			officer = false;
		}
		
		if(f.getPermitted(perm).contains(Rel.ALLY)) {
			ally = true;
		} else {
			ally = false;
		}
		
		if(f.getPermitted(perm).contains(Rel.NEUTRAL)) {
			neutral = true;
		} else {
			neutral = false;
		}
		
		if(f.getPermitted(perm).contains(Rel.ENEMY)) {
			enemy = true;
		} else {
			enemy = false;
		}
		
		ItemStack voltar = new ItemStack(Material.ARROW, 1);
		ItemMeta vm = voltar.getItemMeta();
		vm.setDisplayName("§cVoltar");
		voltar.setItemMeta(vm);
		
		permconfig.setItem(12, recruta);
		permconfig.setItem(11, membro);
		permconfig.setItem(10, capitao);
		permconfig.setItem(14, aliado);
		permconfig.setItem(15, neutro);
		permconfig.setItem(16, inimigo);
		
		permconfig.setItem(21, vidro(recruit));
		permconfig.setItem(20, vidro(member));
		permconfig.setItem(19, vidro(officer));
		permconfig.setItem(23, vidro(ally));
		permconfig.setItem(24, vidro(neutral));
		permconfig.setItem(25, vidro(enemy));
		
		permconfig.setItem(40, voltar);
		
		p.openInventory(permconfig);
	}
	
	public static ItemStack vidro(boolean b) {
		ItemStack vidro;
		if(b) {
			vidro = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5);
			ItemMeta vab = vidro.getItemMeta();
			vab.setDisplayName("§aPermitido");
			vidro.setItemMeta(vab);
		} else {
			vidro = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
			ItemMeta vab = vidro.getItemMeta();
			vab.setDisplayName("§cNão permitido");
			vidro.setItemMeta(vab);
		}
		return vidro;
	}
	
	public static void update(Faction fac, MPerm perm, Rel rel) {
		if(fac.getPermitted(perm).contains(rel)) {
			fac.setRelationPermitted(perm, rel, false);
		} else {
			fac.setRelationPermitted(perm, rel, true);
		}
	}
}
