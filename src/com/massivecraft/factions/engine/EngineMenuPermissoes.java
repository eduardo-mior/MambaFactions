package com.massivecraft.factions.engine;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.engine.GuiHolder.Menu;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.Engine;

public class EngineMenuPermissoes extends Engine 
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static EngineMenuPermissoes i = new EngineMenuPermissoes();
	public static EngineMenuPermissoes get() { return i; }

	// -------------------------------------------- //
	// CLICK LISTENER
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onClick(InventoryClickEvent e) 
	{
		InventoryHolder holder = e.getInventory().getHolder();
		if (!(holder instanceof GuiHolder))
			return;
		
		Menu menu = ((GuiHolder) holder).getType();
		
		if (menu == Menu.PERMISSOES) {
			
			e.setCancelled(true);
			e.setResult(Result.DENY);

			Player p = (Player) e.getWhoClicked();
			MPlayer mp = MPlayer.get(p);
			int slot = e.getSlot();

			if (slot == 40) {
				Faction f = mp.getFaction();
				abrirMenuPermissoes(mp, f);
				return;
			}
			
			if (e.getInventory().getName().contains("[")) {
				
				if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
				
				if (!(mp.getRole() == Rel.LEADER || mp.isOverriding())) {
					p.sendMessage("§cApenas o líder da facção pode administrar as permissões.");
					p.closeInventory();
					return;
				}
				
				if (slot == 10) {
					abrirMenuConfig(p, MPerm.getPermBuild());
					return;
				}

				else if (slot == 11) {
					abrirMenuConfig(p, MPerm.getPermContainer());
					return;
				}

				else if (slot == 12) {
					abrirMenuConfig(p, MPerm.getPermHome());
				}

				else if (slot == 14) {
					abrirMenuConfig(p, MPerm.getPermDoor());
					return;
				}

				else if (slot == 15) {
					abrirMenuConfig(p, MPerm.getPermButton());
					return;
				}

				if (slot == 16) {
					abrirMenuConfig(p, MPerm.getPermLever());
					return;
				}
				
				return;
			} 
			
			else {
				MPerm perme = getPerm(e.getInventory());
				Faction f = mp.getFaction();
				
				if (slot == 21) {
					updatePermission(f, perme, Rel.RECRUIT);
					abrirMenuConfig(p, perme);
					return;
				}

				else if (slot == 20) {
					updatePermission(f, perme, Rel.MEMBER);
					abrirMenuConfig(p, perme);
					return;
				}

				else if (slot == 19) {
					updatePermission(f, perme, Rel.OFFICER);
					abrirMenuConfig(p, perme);
					return;
				}

				else if (slot == 23) {
					updatePermission(f, perme, Rel.ALLY);
					abrirMenuConfig(p, perme);
					return;
				}

				else if (slot == 24) {
					updatePermission(f, perme, Rel.NEUTRAL);
					abrirMenuConfig(p, perme);
					return;
				}

				else if (slot == 25) {
					updatePermission(f, perme, Rel.ENEMY);
					abrirMenuConfig(p, perme);
					return;
				}
			}
		}
	}
	
	private MPerm getPerm(Inventory inv) {
		String name = inv.getName();
		if (name.contains("Construir")) return MPerm.getPermBuild();
		if (name.contains("Usar botões")) return MPerm.getPermButton();
		if (name.contains("Usar alavancas")) return MPerm.getPermLever();
		if (name.contains("Ir para home")) return MPerm.getPermHome();
		if (name.contains("Usar portas")) return MPerm.getPermDoor();
		if (name.contains("Usar containers")) return MPerm.getPermContainer();
		return null;
	}
	
	public void abrirMenuPermissoes(MPlayer mp, Faction f) {
		Player p = mp.getPlayer();
        GuiHolder holder = new GuiHolder(Menu.PERMISSOES);
		Inventory perms = Bukkit.createInventory(holder, 3*9, "Permissões [" + f.getName() + "§r]");
		
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
		for (Rel rel : f.getPermitted(MPerm.getPermContainer())) {
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

	private void abrirMenuConfig(Player p, MPerm perm) {
		String permnome = "";

		if (perm == MPerm.getPermBuild()) {
			permnome = "Construir";
		}

		else if (perm == MPerm.getPermButton()) {
			permnome = "Usar botões";
		}

		else if (perm == MPerm.getPermLever()) {
			permnome = "Usar alavancas";
		}

		else if (perm == MPerm.getPermHome()) {
			permnome = "Ir para home";
		}

		else if (perm == MPerm.getPermDoor()) {
			permnome = "Usar portas";
		}

		else if (perm == MPerm.getPermContainer()) {
			permnome = "Usar containers";
		}

        GuiHolder holder = new GuiHolder(Menu.PERMISSOES);
		Inventory permconfig = Bukkit.createInventory(holder, 5 * 9, "Permissões - " + permnome);

		ItemStack recruta = new ItemStack(Material.STONE_SWORD, 1);
		ItemMeta rm = recruta.getItemMeta();
		rm.setDisplayName("§eRecruta");
		rm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		recruta.setItemMeta(rm);

		ItemStack membro = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta mm = membro.getItemMeta();
		mm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		mm.setDisplayName("§eMembro");
		membro.setItemMeta(mm);

		ItemStack capitao = new ItemStack(Material.DIAMOND_SWORD, 1);
		ItemMeta cm = capitao.getItemMeta();
		cm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		cm.setDisplayName("§eCapitão");
		capitao.setItemMeta(cm);

		ItemStack aliado = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta am = (LeatherArmorMeta) aliado.getItemMeta();
		am.setColor(Color.LIME);
		am.setDisplayName("§eAliado");
		aliado.setItemMeta(am);

		ItemStack neutro = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta nm = (LeatherArmorMeta) neutro.getItemMeta();
		nm.setColor(Color.WHITE);
		nm.setDisplayName("§eNeutro");
		neutro.setItemMeta(nm);

		ItemStack inimigo = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta im = (LeatherArmorMeta) inimigo.getItemMeta();
		im.setColor(Color.RED);
		im.setDisplayName("§eInimigo");
		inimigo.setItemMeta(im);

		/////
		
		Faction f = MPlayer.get(p).getFaction();

		boolean recruit = f.getPermitted(perm).contains(Rel.RECRUIT);
		boolean member = f.getPermitted(perm).contains(Rel.MEMBER);
		boolean officer = f.getPermitted(perm).contains(Rel.OFFICER);

		boolean ally = f.getPermitted(perm).contains(Rel.ALLY);
		boolean neutral = f.getPermitted(perm).contains(Rel.NEUTRAL);
		boolean enemy = f.getPermitted(perm).contains(Rel.ENEMY);

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

	private ItemStack vidro(boolean b) {
		ItemStack vidro;
		if (b) {
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

	private void updatePermission(Faction fac, MPerm perm, Rel rel) {
		if (fac.getPermitted(perm).contains(rel)) {
			fac.setRelationPermitted(perm, rel, false);
		} else {
			fac.setRelationPermitted(perm, rel, true);
		}
	}

}