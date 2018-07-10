package com.massivecraft.factions.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {
	   private ItemStack is;

	   public ItemBuilder(Material m) {
		   this(m, 1);
	   }

	   public ItemBuilder(ItemStack is) {
		   this.is=is;
	   }

	   public ItemBuilder(Material m, int quantia) {
		   is= new ItemStack(m, quantia);
	   }

	   public ItemBuilder(Material m, int quantia, byte durabilidade) {
		   is = new ItemStack(m, quantia, durabilidade);
	   }
	   
	   public ItemBuilder(Material m, int quantia, int durabilidade) {
		   is = new ItemStack(m, quantia, (short) durabilidade);
		}
	   
	   public ItemBuilder clone() {
		   return new ItemBuilder(is);
	   }
	   
	   public ItemBuilder setAmount(int amount) {
		   is.setAmount(amount);
		   ItemMeta im = is.getItemMeta();
		   im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		   is.setItemMeta(im);
		   return this;
	   }

	   public ItemBuilder setName(String nome) {
		   ItemMeta im = is.getItemMeta();
		   im.setDisplayName(nome);
		   is.setItemMeta(im);
		   return this;
	   }

	   public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {
		   is.addUnsafeEnchantment(ench, level);
		   return this;
	   }

	   public ItemBuilder removeEnchantment(Enchantment ench) {
		   is.removeEnchantment(ench);
		   return this;
	   }

	   @SuppressWarnings("deprecation")
	   public ItemBuilder setSkullOwner(String dono) {
		   try {
			   SkullMeta im = (SkullMeta)is.getItemMeta();
			   im.setOwner(dono);
			   is.setItemMeta(im);
		   } catch (ClassCastException expected) {}
		   return this;
	   }
	   
	   public ItemBuilder addItemFlag(ItemFlag flag) {
		   ItemMeta im = is.getItemMeta();
		   im.addItemFlags(flag);
		   is.setItemMeta(im);
		   return this;
	   }
	   
	   public ItemBuilder setLore(String... lore) {
		   ItemMeta im = is.getItemMeta();
		   im.setLore(Arrays.asList(lore));
		   is.setItemMeta(im);
		   return this;
	   }

	   public ItemBuilder setLore(String string, String string2, String string3, String string4, String string5, String string6, String string7, String string8, String string9, String string10, String string11, String string12, String string13, String string14, String string15, String string16, List<String> lore) {
		   ItemMeta im = is.getItemMeta();
		   List<String> l = new ArrayList<>();
		   l.add(string);
		   l.add(string2);
		   l.add(string3);
		   l.add(string4);
		   l.add(string5);
		   l.add(string6);
		   l.add(string7);
		   l.add(string8);
		   l.add(string9);
		   l.add(string10);
		   l.add(string11);
		   l.add(string12);
		   l.add(string13);
		   l.add(string14);
		   l.add(string15);
		   l.add(string16);
		   l.addAll(lore);
		   im.setLore(l);
		   is.setItemMeta(im);
		   return this;
	   }

	   public ItemBuilder setLeatherArmorColor(Color cor) {
		   try {
			   LeatherArmorMeta im = (LeatherArmorMeta)is.getItemMeta();
			   im.setColor(cor);
			   is.setItemMeta(im);
		   } catch (ClassCastException expected) {}
		   return this;
	   }

	   public ItemStack toItemStack() {
		   return is;
	   }
}
