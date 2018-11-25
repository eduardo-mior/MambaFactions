package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.MPlayerColl;
import com.massivecraft.massivecore.Lang;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.mson.Mson;

public class FactionsCommand extends MassiveCommand
{
	// -------------------------------------------- //
	// CONSTANT
	// -------------------------------------------- //
	static final Mson HELP_MESSAGE = mson(Lang.COMMAND_CHILD_NONE, Mson.NEWLINE, Lang.COMMAND_CHILD_HELP).command("/f");
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public MPlayer msender;
	public Faction msenderFaction;

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public FactionsCommand()
	{
		this.setSetupEnabled(true);
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void senderFields(boolean set)
	{
		this.msender = set ? MPlayer.get(sender) : null;
		this.msenderFaction = set ? this.msender.getFaction() : null;
	}

	// -------------------------------------------- //
	// METHODS
	// -------------------------------------------- //
	
	public Faction readFaction() throws MassiveException 
	{ 
		if (argIsSet()) 
		{
			String name = arg();
			Faction fac = FactionColl.get().getByName(name);
			if (fac == null) 
			{
				throw new MassiveException().setMsg("§cNenhuma facção encontrada com o nome '" + name + "'.");
			}
			return fac;
		}	
		return msenderFaction;
	}
	
	public Faction readFaction(String arg) throws MassiveException 
	{ 
		Faction fac = FactionColl.get().getByName(arg);
		if (fac == null) 
		{
			throw new MassiveException().setMsg("§cNenhuma facção encontrada com o nome '" + arg + "'.");
		}
		return fac;
	}
	
	public MPlayer readMPlayer() throws MassiveException
	{	
		if (argIsSet())
		{
			String name = arg();
			MPlayer mp = MPlayerColl.get().getByName(name);
			if (mp == null)
			{
				throw new MassiveException().setMsg("§cNenhum player encontrado com o nome '" + name + "'.");
			}
			return mp;
		}
		return msender;
	}
	
	public MPlayer readMPlayer(String arg) throws MassiveException
	{	
		MPlayer mp = MPlayerColl.get().getByName(arg);
		if (mp == null)
		{
			throw new MassiveException().setMsg("§cNenhum player encontrado com o nome '" + arg + "'.");
		}
		return mp;
	}	
	
	public Boolean readBoolean(boolean def) throws MassiveException 
	{
		if (argIsSet()) 
		{
			String parse = arg().toLowerCase();
			if (parse.equals("on") || parse.equals("sim") || parse.equals("true"))
			{
				return Boolean.TRUE;
			}
			else if (parse.equals("off") || parse.equals("nao") || parse.equals("false")) 
			{
				return Boolean.FALSE;
			}
			else 
			{
				return null;
			}
		}
		else 
		{
			return !def;
		}
	}
	
	public int readInt() throws MassiveException
	{
		if (argIsSet()) 
		{
			String number = arg();
			try 
			{
				return Integer.parseInt(number);
			} 
			catch (Exception e) 
			{
				throw new MassiveException().setMsg("§cO número '" + number + "' não é valido.");
			}
		}
		return 1;
	}
	
	public int readInt(String number) throws MassiveException
	{
		try 
		{
			return Integer.parseInt(number);
		} 
		catch (Exception e) 
		{
			throw new MassiveException().setMsg("§cO número '" + number + "' não é valido.");
		}
	}
	
	public Rel readRelation(String arg) throws MassiveException
	{
		arg = arg.toLowerCase().trim();
		if (arg.equals("aliança") || arg.equals("aliado") || arg.equals("ally"))   return Rel.ALLY;
		if (arg.equals("neutral") || arg.equals("neutro") || arg.equals("tregua")) return Rel.NEUTRAL;
		if (arg.equals("inimigo") || arg.equals("enemy")  || arg.equals("rival"))  return Rel.ENEMY;
		throw new MassiveException().setMsg("§cA relação '" + arg + "' não é valida.");
	}
}
