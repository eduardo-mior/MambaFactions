package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Rel;
import com.massivecraft.factions.cmd.type.TypeFaction;
import com.massivecraft.factions.cmd.type.TypeRelation;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.command.Parameter;
import com.massivecraft.massivecore.command.type.container.TypeSet;
import com.massivecraft.massivecore.pager.Pager;
import com.massivecraft.massivecore.pager.Stringifier;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class CmdFactionsRelacaoListar extends FactionsCommand
{
	{
		
	// Aliases
    this.addAliases("list", "lista");
    
	// Descrição do comando
	this.setDesc("§6 relacao listar §e<facção> §8-§7 Mostra a lista de relações.");
	
	}
	
	// -------------------------------------------- //
	// COSTANTS
	// -------------------------------------------- //

	public static final Set<Rel> RELEVANT_RELATIONS = new MassiveSet<>(Rel.ALLY, Rel.ENEMY);
	public static final String SEPERATOR = Txt.parse("§f: ");

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public CmdFactionsRelacaoListar()
	{
		// Parameter
		this.addParameter(Parameter.getPage());
		this.addParameter(TypeFaction.get(), "facção", "você");
		this.addParameter(TypeSet.get(TypeRelation.get()), "relação", "all");
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Argumentos (não necessario)
		int page = this.readArg();
		final Faction faction = this.readArg(msenderFaction);
		final Set<Rel> relations = this.readArg(RELEVANT_RELATIONS);

		// Pager Create
		final Pager<String> pager = new Pager<>(this, "", page, new Stringifier<String>()
		{
			@Override
			public String toString(String item, int index)
			{
				return item;
			}
		});

		Bukkit.getScheduler().runTaskAsynchronously(Factions.get(), new Runnable()
		{
			@Override
			public void run()
			{
				// Prepare Items
				List<String> relNames = new MassiveList<>();
				for (Entry<Rel, List<String>> entry : FactionColl.get().getRelationNames(faction, relations).entrySet())
				{
					Rel relation = entry.getKey();
					String coloredName = relation.getColor().toString() + relation.getName();

					for (String name : entry.getValue())
					{
						relNames.add(coloredName + SEPERATOR + name);
					}
				}

				// Pager Title
				pager.setTitle(Txt.parse("§eRelações da %s §2(%d)", faction.getName(), relNames.size()));

				// Pager Items
				pager.setItems(relNames);

				// Pager Message
				pager.message();
			}
		});
	}
	
}
