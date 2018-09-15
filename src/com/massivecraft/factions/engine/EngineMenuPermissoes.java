package com.massivecraft.factions.engine;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.CmdFactionsPermissoes;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.Engine;

public class EngineMenuPermissoes extends Engine {
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static EngineMenuPermissoes i = new EngineMenuPermissoes();

	public static EngineMenuPermissoes get() {
		return i;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onClick(InventoryClickEvent e) {
		if (e.getInventory().getName().contains("§8Permissões")) {
			e.setCancelled(true);
			e.setResult(Result.DENY);

			Player p = (Player) e.getWhoClicked();
			MPlayer mp = MPlayer.get(p);
			Faction f = mp.getFaction();
			int slot = e.getSlot();

			if (slot == 40) {
				CmdFactionsPermissoes.abrirMenuPermissoes(p, mp, f);
				return;
			}
			
			if (e.getInventory().getName().contains("Construir")) {
				MPerm perme = MPerm.getPermBuild();
				if (slot == 21) {
					update(f, perme, Rel.RECRUIT);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 20) {
					update(f, perme, Rel.MEMBER);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 19) {
					update(f, perme, Rel.OFFICER);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 23) {
					update(f, perme, Rel.ALLY);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 24) {
					update(f, perme, Rel.NEUTRAL);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 25) {
					update(f, perme, Rel.ENEMY);
					abrirMenuConfig(p, perme);
					return;
				}

				return;
			}

			if (e.getInventory().getName().contains("Usar botões")) {
				MPerm perme = MPerm.getPermButton();
				if (slot == 21) {
					update(f, perme, Rel.RECRUIT);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 20) {
					update(f, perme, Rel.MEMBER);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 19) {
					update(f, perme, Rel.OFFICER);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 23) {
					update(f, perme, Rel.ALLY);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 24) {
					update(f, perme, Rel.NEUTRAL);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 25) {
					update(f, perme, Rel.ENEMY);
					abrirMenuConfig(p, perme);
					return;
				}

				return;
			}

			if (e.getInventory().getName().contains("Usar alavancas")) {
				MPerm perme = MPerm.getPermLever();
				if (slot == 21) {
					update(f, perme, Rel.RECRUIT);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 20) {
					update(f, perme, Rel.MEMBER);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 19) {
					update(f, perme, Rel.OFFICER);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 23) {
					update(f, perme, Rel.ALLY);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 24) {
					update(f, perme, Rel.NEUTRAL);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 25) {
					update(f, perme, Rel.ENEMY);
					abrirMenuConfig(p, perme);
					return;
				}

				return;
			}

			if (e.getInventory().getName().contains("Ir para home")) {
				MPerm perme = MPerm.getPermHome();
				if (slot == 21) {
					update(f, perme, Rel.RECRUIT);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 20) {
					update(f, perme, Rel.MEMBER);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 19) {
					update(f, perme, Rel.OFFICER);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 23) {
					update(f, perme, Rel.ALLY);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 24) {
					update(f, perme, Rel.NEUTRAL);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 25) {
					update(f, perme, Rel.ENEMY);
					abrirMenuConfig(p, perme);
					return;
				}

				return;
			}

			if (e.getInventory().getName().contains("Usar portas")) {
				MPerm perme = MPerm.getPermDoor();
				if (slot == 21) {
					update(f, perme, Rel.RECRUIT);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 20) {
					update(f, perme, Rel.MEMBER);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 19) {
					update(f, perme, Rel.OFFICER);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 23) {
					update(f, perme, Rel.ALLY);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 24) {
					update(f, perme, Rel.NEUTRAL);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 25) {
					update(f, perme, Rel.ENEMY);
					abrirMenuConfig(p, perme);
					return;
				}

				return;
			}

			if (e.getInventory().getName().contains("Usar containers")) {
				MPerm perme = MPerm.getPermContainer();
				if (slot == 21) {
					update(f, perme, Rel.RECRUIT);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 20) {
					update(f, perme, Rel.MEMBER);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 19) {
					update(f, perme, Rel.OFFICER);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 23) {
					update(f, perme, Rel.ALLY);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 24) {
					update(f, perme, Rel.NEUTRAL);
					abrirMenuConfig(p, perme);
					return;
				}

				if (slot == 25) {
					update(f, perme, Rel.ENEMY);
					abrirMenuConfig(p, perme);
					return;
				}
				
				return;
			}

			if (slot == 10) {
				if (!(mp.getRole() == Rel.LEADER || mp.isOverriding())) {
					p.sendMessage("§cApenas o líder da facção pode administrar as permissões.");
					p.closeInventory();
				} else {
					abrirMenuConfig(p, MPerm.getPermBuild());
				}
			}

			if (slot == 11) {
				if (!(mp.getRole() == Rel.LEADER || mp.isOverriding())) {
					p.sendMessage("§cApenas o líder da facção pode administrar as permissões.");
					p.closeInventory();
				} else {
					abrirMenuConfig(p, MPerm.getPermContainer());
				}
			}

			if (slot == 12) {
				if (!(mp.getRole() == Rel.LEADER || mp.isOverriding())) {
					p.sendMessage("§cApenas o líder da facção pode administrar as permissões.");
					p.closeInventory();
				} else {
					abrirMenuConfig(p, MPerm.getPermHome());
				}
			}

			if (slot == 14) {
				if (!(mp.getRole() == Rel.LEADER || mp.isOverriding())) {
					p.sendMessage("§cApenas o líder da facção pode administrar as permissões.");
					p.closeInventory();
				} else {
					abrirMenuConfig(p, MPerm.getPermDoor());
				}
			}

			if (slot == 15) {
				if (!(mp.getRole() == Rel.LEADER || mp.isOverriding())) {
					p.sendMessage("§cApenas o líder da facção pode administrar as permissões.");
					p.closeInventory();
				} else {
					abrirMenuConfig(p, MPerm.getPermButton());
				}
			}

			if (slot == 16) {
				if (!(mp.getRole() == Rel.LEADER || mp.isOverriding())) {
					p.sendMessage("§cApenas o líder da facção pode administrar as permissões.");
					p.closeInventory();
				} else {
					abrirMenuConfig(p, MPerm.getPermLever());
				}
			}
		}
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

		Inventory permconfig = Bukkit.createInventory(null, 5 * 9, "§8Permissões - " + permnome);

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

		boolean recruit;
		boolean member;
		boolean officer;

		boolean ally;
		boolean neutral;
		boolean enemy;

		Faction f = MPlayer.get(p).getFaction();

		if (f.getPermitted(perm).contains(Rel.RECRUIT)) {
			recruit = true;
		} else {
			recruit = false;
		}

		if (f.getPermitted(perm).contains(Rel.MEMBER)) {
			member = true;
		} else {
			member = false;
		}

		if (f.getPermitted(perm).contains(Rel.OFFICER)) {
			officer = true;
		} else {
			officer = false;
		}

		if (f.getPermitted(perm).contains(Rel.ALLY)) {
			ally = true;
		} else {
			ally = false;
		}

		if (f.getPermitted(perm).contains(Rel.NEUTRAL)) {
			neutral = true;
		} else {
			neutral = false;
		}

		if (f.getPermitted(perm).contains(Rel.ENEMY)) {
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

	private void update(Faction fac, MPerm perm, Rel rel) {
		if (fac.getPermitted(perm).contains(rel)) {
			fac.setRelationPermitted(perm, rel, false);
		} else {
			fac.setRelationPermitted(perm, rel, true);
		}
	}

}