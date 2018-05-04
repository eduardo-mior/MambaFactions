package com.massivecraft.factions.engine;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.CmdFactionsPermissoes;
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

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getInventory().getName().contains("§8Permissões")) {
			e.setCancelled(true);
			
			Player p = (Player)e.getWhoClicked();
			MPlayer mp = MPlayer.get(p);
			Faction f = mp.getFaction();
			
			if(e.getInventory().getName().contains("Construir")) {
				MPerm perme = MPerm.getPermBuild();
				if(e.getSlot() == 21) {
					CmdFactionsPermissoes.update(f, perme, Rel.RECRUIT);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 20) {
					CmdFactionsPermissoes.update(f, perme, Rel.MEMBER);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 19) {
					CmdFactionsPermissoes.update(f, perme, Rel.OFFICER);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 23) {
					CmdFactionsPermissoes.update(f, perme, Rel.ALLY);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 24) {
					CmdFactionsPermissoes.update(f, perme, Rel.NEUTRAL);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}

				if(e.getSlot() == 25) {
					CmdFactionsPermissoes.update(f, perme, Rel.ENEMY);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 40) {
					CmdFactionsPermissoes.abrirMenuPermissoes(p, mp, f);
				} return;
			} 
			
			if(e.getInventory().getName().contains("Usar botões")) {
				MPerm perme = MPerm.getPermButton();
				if(e.getSlot() == 21) {
					CmdFactionsPermissoes.update(f, perme, Rel.RECRUIT);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 20) {
					CmdFactionsPermissoes.update(f, perme, Rel.MEMBER);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 19) {
					CmdFactionsPermissoes.update(f, perme, Rel.OFFICER);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 23) {
					CmdFactionsPermissoes.update(f, perme, Rel.ALLY);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 24) {
					CmdFactionsPermissoes.update(f, perme, Rel.NEUTRAL);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}

				if(e.getSlot() == 25) {
					CmdFactionsPermissoes.update(f, perme, Rel.ENEMY);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 40) {
					CmdFactionsPermissoes.abrirMenuPermissoes(p, mp, f);
				} return;
			}
			
			if(e.getInventory().getName().contains("Usar alavancas")) {
				MPerm perme = MPerm.getPermLever();
				if(e.getSlot() == 21) {
					CmdFactionsPermissoes.update(f, perme, Rel.RECRUIT);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 20) {
					CmdFactionsPermissoes.update(f, perme, Rel.MEMBER);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 19) {
					CmdFactionsPermissoes.update(f, perme, Rel.OFFICER);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 23) {
					CmdFactionsPermissoes.update(f, perme, Rel.ALLY);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 24) {
					CmdFactionsPermissoes.update(f, perme, Rel.NEUTRAL);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}

				if(e.getSlot() == 25) {
					CmdFactionsPermissoes.update(f, perme, Rel.ENEMY);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 40) {
					CmdFactionsPermissoes.abrirMenuPermissoes(p, mp, f);
				} return;
			}
			
			if(e.getInventory().getName().contains("Ir para home")) {
				MPerm perme = MPerm.getPermHome();
				if(e.getSlot() == 21) {
					CmdFactionsPermissoes.update(f, perme, Rel.RECRUIT);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 20) {
					CmdFactionsPermissoes.update(f, perme, Rel.MEMBER);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 19) {
					CmdFactionsPermissoes.update(f, perme, Rel.OFFICER);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 23) {
					CmdFactionsPermissoes.update(f, perme, Rel.ALLY);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 24) {
					CmdFactionsPermissoes.update(f, perme, Rel.NEUTRAL);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}

				if(e.getSlot() == 25) {
					CmdFactionsPermissoes.update(f, perme, Rel.ENEMY);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 40) {
					CmdFactionsPermissoes.abrirMenuPermissoes(p, mp, f);
				} return;
			}
			
			if(e.getInventory().getName().contains("Usar portas")) {
				MPerm perme = MPerm.getPermDoor();
				if(e.getSlot() == 21) {
					CmdFactionsPermissoes.update(f, perme, Rel.RECRUIT);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 20) {
					CmdFactionsPermissoes.update(f, perme, Rel.MEMBER);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 19) {
					CmdFactionsPermissoes.update(f, perme, Rel.OFFICER);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 23) {
					CmdFactionsPermissoes.update(f, perme, Rel.ALLY);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 24) {
					CmdFactionsPermissoes.update(f, perme, Rel.NEUTRAL);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}

				if(e.getSlot() == 25) {
					CmdFactionsPermissoes.update(f, perme, Rel.ENEMY);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 40) {
					CmdFactionsPermissoes.abrirMenuPermissoes(p, mp, f);
				} return;
			}
			
			if(e.getInventory().getName().contains("Usar containers")) {
				MPerm perme = MPerm.getPermContainer();
				if(e.getSlot() == 21) {
					CmdFactionsPermissoes.update(f, perme, Rel.RECRUIT);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 20) {
					CmdFactionsPermissoes.update(f, perme, Rel.MEMBER);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 19) {
					CmdFactionsPermissoes.update(f, perme, Rel.OFFICER);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 23) {
					CmdFactionsPermissoes.update(f, perme, Rel.ALLY);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 24) {
					CmdFactionsPermissoes.update(f, perme, Rel.NEUTRAL);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}

				if(e.getSlot() == 25) {
					CmdFactionsPermissoes.update(f, perme, Rel.ENEMY);
					CmdFactionsPermissoes.abrirMenuConfig(p, perme);
				}
				
				if(e.getSlot() == 40) {
					CmdFactionsPermissoes.abrirMenuPermissoes(p, mp, f);
				} return;
			}
			
			if(e.getSlot() == 10) {
				if(!(mp.getRole() == Rel.LEADER || mp.isOverriding())) {
					p.sendMessage("§cApenas o líder da facção pode administrar as permissões.");
					p.closeInventory();
				} else {
				CmdFactionsPermissoes.abrirMenuConfig(p, MPerm.getPermBuild()); }
			}
			
			if(e.getSlot() == 11) {
				if(!(mp.getRole() == Rel.LEADER || mp.isOverriding())) {
					p.sendMessage("§cApenas o líder da facção pode administrar as permissões.");
					p.closeInventory();
				} else {
				CmdFactionsPermissoes.abrirMenuConfig(p, MPerm.getPermContainer()); }
			}
			
			if(e.getSlot() == 12) {
				if(!(mp.getRole() == Rel.LEADER || mp.isOverriding())) {
					p.sendMessage("§cApenas o líder da facção pode administrar as permissões.");
					p.closeInventory();
				} else {
				CmdFactionsPermissoes.abrirMenuConfig(p, MPerm.getPermHome()); }
			}
			
			if(e.getSlot() == 14) {
				if(!(mp.getRole() == Rel.LEADER || mp.isOverriding())) {
					p.sendMessage("§cApenas o líder da facção pode administrar as permissões.");
					p.closeInventory();
				} else {
				CmdFactionsPermissoes.abrirMenuConfig(p, MPerm.getPermDoor()); }
			}
			
			if(e.getSlot() == 15) {
				if(!(mp.getRole() == Rel.LEADER || mp.isOverriding())) {
					p.sendMessage("§cApenas o líder da facção pode administrar as permissões.");
					p.closeInventory();
				} else {
				CmdFactionsPermissoes.abrirMenuConfig(p, MPerm.getPermButton()); }
			}
			
			if(e.getSlot() == 16) {
				if(!(mp.getRole() == Rel.LEADER || mp.isOverriding())) {
					p.sendMessage("§cApenas o líder da facção pode administrar as permissões.");
					p.closeInventory();
				} else {
				CmdFactionsPermissoes.abrirMenuConfig(p, MPerm.getPermLever()); }
			}
		}
	}
}