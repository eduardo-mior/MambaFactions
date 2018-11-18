package com.massivecraft.factions.util;

import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.collections.BackstringSet;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class EnumerationUtil
{
	// -------------------------------------------- //
	// MATERIAL EDIT ON INTERACT
	// -------------------------------------------- //
	
	public static final BackstringSet<Material> MATERIALS_EDIT_ON_INTERACT = new BackstringSet<>(Material.class,
		"DIODE_BLOCK_OFF", // Minecraft 1.?
		"DIODE_BLOCK_ON", // Minecraft 1.?
		"NOTE_BLOCK", // Minecraft 1.?
		"CAULDRON", // Minecraft 1.?
		"SOIL", // Minecraft 1.?
		"DAYLIGHT_DETECTOR", // Minecraft 1.5
		"DAYLIGHT_DETECTOR_INVERTED", // Minecraft 1.5
		"REDSTONE_COMPARATOR_OFF", // Minecraft 1.?
		"REDSTONE_COMPARATOR_ON" // Minecraft 1.?
	);
	
	public static boolean isMaterialEditOnInteract(Material material)
	{
		return MATERIALS_EDIT_ON_INTERACT.contains(material) || MConf.get().materialsEditOnInteract.contains(material);
	}
	
	// -------------------------------------------- //
	// MATERIAL EDIT TOOLS
	// -------------------------------------------- //
	
	public static final BackstringSet<Material> MATERIALS_EDIT_TOOL = new BackstringSet<>(Material.class,
		"FIREBALL", // Minecraft 1.?
		"FLINT_AND_STEEL", // Minecraft 1.?
		"BUCKET", // Minecraft 1.?
		"WATER_BUCKET", // Minecraft 1.?
		"LAVA_BUCKET", // Minecraft 1.?
		"ARMOR_STAND", // Minecraft 1.8
		"END_CRYSTAL", // Minecraft 1.10
		
		// The duplication bug found in Spigot 1.8 protocol patch
		// https://github.com/MassiveCraft/Factions/issues/693
		"CHEST", // Minecraft 1.?
		"SIGN_POST", // Minecraft 1.?
		"TRAPPED_CHEST", // Minecraft 1.?
		"SIGN", // Minecraft 1.?
		"WOOD_DOOR", // Minecraft 1.?
		"IRON_DOOR" // Minecraft 1.?
	);
	
	public static boolean isMaterialEditTool(Material material)
	{
		return MATERIALS_EDIT_TOOL.contains(material) || MConf.get().materialsEditTools.contains(material);
	}
	
	// -------------------------------------------- //
	// MATERIAL DOOR
	// -------------------------------------------- //
	
	// Interacting with these materials placed in the terrain results in door toggling.
	public static final BackstringSet<Material> MATERIALS_DOOR = new BackstringSet<>(Material.class,
		"WOODEN_DOOR", // Minecraft 1.?
		"ACACIA_DOOR", // Minecraft 1.8
		"BIRCH_DOOR", // Minecraft 1.8
		"DARK_OAK_DOOR", // Minecraft 1.8
		"JUNGLE_DOOR", // Minecraft 1.8
		"SPRUCE_DOOR", // Minecraft 1.8
		"TRAP_DOOR", // Minecraft 1.?
		"FENCE_GATE", // Minecraft 1.?
		"ACACIA_FENCE_GATE", // Minecraft 1.8
		"BIRCH_FENCE_GATE", // Minecraft 1.8
		"DARK_OAK_FENCE_GATE", // Minecraft 1.8
		"JUNGLE_FENCE_GATE", // Minecraft 1.8
		"SPRUCE_FENCE_GATE" // Minecraft 1.8
	);
	
	public static boolean isMaterialDoor(Material material)
	{
		return MATERIALS_DOOR.contains(material) || MConf.get().materialsDoor.contains(material);
	}
	
	// -------------------------------------------- //
	// MATERIAL CONTAINER
	// -------------------------------------------- //
	
	public static final BackstringSet<Material> MATERIALS_CONTAINER = new BackstringSet<>(Material.class,
		"DISPENSER", // Minecraft 1.?
		"CHEST", // Minecraft 1.?
		"FURNACE", // Minecraft 1.?
		"BURNING_FURNACE", // Minecraft 1.?
		"JUKEBOX", // Minecraft 1.?
		"BREWING_STAND", // Minecraft 1.?
		"ENCHANTMENT_TABLE", // Minecraft 1.?
		"ANVIL", // Minecraft 1.?
		"BEACON", // Minecraft 1.?
		"TRAPPED_CHEST", // Minecraft 1.?
		"HOPPER", // Minecraft 1.?
		"DROPPER", // Minecraft 1.?
		
		// The various shulker boxes, they had to make each one a different material -.-
		"BLACK_SHULKER_BOX", // Minecraft 1.11
		"BLUE_SHULKER_BOX", // Minecraft 1.11
		"BROWN_SHULKER_BOX", // Minecraft 1.11
		"CYAN_SHULKER_BOX", // Minecraft 1.11
		"GRAY_SHULKER_BOX", // Minecraft 1.11
		"GREEN_SHULKER_BOX", // Minecraft 1.11
		"LIGHT_BLUE_SHULKER_BOX", // Minecraft 1.11
		"LIME_SHULKER_BOX", // Minecraft 1.11
		"MAGENTA_SHULKER_BOX", // Minecraft 1.11
		"ORANGE_SHULKER_BOX", // Minecraft 1.11
		"PINK_SHULKER_BOX", // Minecraft 1.11
		"PURPLE_SHULKER_BOX", // Minecraft 1.11
		"RED_SHULKER_BOX", // Minecraft 1.11
		"SILVER_SHULKER_BOX", // Minecraft 1.11
		"WHITE_SHULKER_BOX", // Minecraft 1.11
		"YELLOW_SHULKER_BOX" // Minecraft 1.11
	);
	
	public static boolean isMaterialContainer(Material material)
	{
		return MATERIALS_CONTAINER.contains(material) || MConf.get().materialsContainer.contains(material);
	}
	
	// -------------------------------------------- //
	// ENTITY TYPE EDIT ON INTERACT
	// -------------------------------------------- //
	
	// Interacting with these entities results in an edit.
	public static final BackstringSet<EntityType> ENTITY_TYPES_EDIT_ON_INTERACT = new BackstringSet<>(EntityType.class,
		"ITEM_FRAME", // Minecraft 1.?
		"ARMOR_STAND" // Minecraft 1.8
	);
	
	public static boolean isEntityTypeEditOnInteract(EntityType entityType)
	{
		return ENTITY_TYPES_EDIT_ON_INTERACT.contains(entityType) || MConf.get().entityTypesEditOnInteract.contains(entityType);
	}
	
	// -------------------------------------------- //
	// ENTITY TYPE EDIT ON DAMAGE
	// -------------------------------------------- //
	
	// Damaging these entities results in an edit.
	public static final BackstringSet<EntityType> ENTITY_TYPES_EDIT_ON_DAMAGE = new BackstringSet<>(EntityType.class,
		"ITEM_FRAME", // Minecraft 1.?
		"ARMOR_STAND", // Minecraft 1.8
		"ENDER_CRYSTAL" // Minecraft 1.10
	);
	
	public static boolean isEntityTypeEditOnDamage(EntityType entityType)
	{
		return ENTITY_TYPES_EDIT_ON_DAMAGE.contains(entityType) || MConf.get().entityTypesEditOnDamage.contains(entityType);
	}
	
	// -------------------------------------------- //
	// ENTITY TYPE CONTAINER
	// -------------------------------------------- //
	
	public static final BackstringSet<EntityType> ENTITY_TYPES_CONTAINER = new BackstringSet<>(EntityType.class,
		"MINECART_CHEST", // Minecraft 1.?
		"MINECART_HOPPER" // Minecraft 1.?
	);
	
	public static boolean isEntityTypeContainer(EntityType entityType)
	{
		return ENTITY_TYPES_CONTAINER.contains(entityType) || MConf.get().entityTypesContainer.contains(entityType);
	}
}
