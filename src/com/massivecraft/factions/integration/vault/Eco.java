package com.massivecraft.factions.integration.vault;

import org.bukkit.entity.Player;

import com.massivecraft.massivecore.money.MoneyMixinVault;

import net.milkbowl.vault.economy.Economy;

@SuppressWarnings("deprecation")
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
		try 
		{
			return getEconomy().has(p, amount);
		} 
		catch (Throwable e) 
		{
			return getEconomy().has(p.getName(), amount);
		}
	}

	public static void Withdraw(Player p, Double amount)
	{
		try 
		{
			getEconomy().withdrawPlayer(p, amount);
		} 
		catch (Throwable e)
		{
			getEconomy().withdrawPlayer(p.getName(), amount);
		}
	}
}
