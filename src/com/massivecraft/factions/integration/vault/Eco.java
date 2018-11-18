package com.massivecraft.factions.integration.vault;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.money.MoneyMixinVault;

import net.milkbowl.vault.economy.Economy;

public class Eco {
	
	public static boolean isEnabled()
	{
		return MoneyMixinVault.get().enabled();
	}
	
	public static Economy getEconomy()
	{
		return MoneyMixinVault.get().getEconomy();
	}
	
	public static boolean has(Player p, Double amount)
	{
		return getEconomy().has(p, amount);
	}

	public static void Withdraw(Player p, Double amount)
	{
		getEconomy().withdrawPlayer(p, amount);
	}
}
