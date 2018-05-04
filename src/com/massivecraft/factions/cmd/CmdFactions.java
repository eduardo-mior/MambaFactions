package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Perm;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.massivecore.command.MassiveCommandDeprecated;
import com.massivecraft.massivecore.command.MassiveCommandVersion;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

import java.util.List;

public class CmdFactions extends FactionsCommand
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static CmdFactions i = new CmdFactions();
	public static CmdFactions get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdFactionsChatFaccao cmdFactionsChatFaccao = new CmdFactionsChatFaccao();
	public CmdFactionsChatAliados cmdFactionsChatAliados = new CmdFactionsChatAliados();
	public CmdFactionsCriar cmdFactionsCriar = new CmdFactionsCriar();
	public CmdFactionsEntrar cmdFactionsEntrar = new CmdFactionsEntrar();
	public CmdFactionsInfo cmdFactionsInfo = new CmdFactionsInfo();
	public CmdFactionsMembros cmdFactionsMembros = new CmdFactionsMembros();
	public CmdFactionsPerfil cmdFactionsPerfil = new CmdFactionsPerfil();
	public CmdFactionsListar cmdFactionsListar = new CmdFactionsListar();
	public CmdFactionsHome cmdFactionsHome = new CmdFactionsHome();
	public CmdFactionsMapa cmdFactionsMapa = new CmdFactionsMapa();
	public CmdFactionsPromover cmdFactionsPromover = new CmdFactionsPromover();
	public CmdFactionsTransferir cmdFactionsTransferir = new CmdFactionsTransferir();
	public CmdFactionsRebaixar cmdFactionsRebaixar = new CmdFactionsRebaixar();
	public CmdFactionsKick cmdFactionsKick = new CmdFactionsKick();
	public CmdFactionsNome cmdFactionsNome = new CmdFactionsNome();
	public CmdFactionsDesc cmdFactionsDesc = new CmdFactionsDesc();
	public CmdFactionsMotd cmdFactionsMotd = new CmdFactionsMotd();
	public CmdFactionsSethome cmdFactionsSethome = new CmdFactionsSethome();
	public CmdFactionsDelhome cmdFactionsDelhome = new CmdFactionsDelhome();
	public CmdFactionsPermissoes cmdFactionsPermissoes = new CmdFactionsPermissoes();
	public CmdFactionsRelacao cmdFactionsRelacao = new CmdFactionsRelacao();
	public CmdFactionsRelacaoOld cmdFactionsRelacaoOldAlly = new CmdFactionsRelacaoOld("aliado").setAliases("ally", "aliado");
	public CmdFactionsRelacaoOld cmdFactionsRelacaoOldNeutral = new CmdFactionsRelacaoOld("neutro").setAliases("neutral", "tregua", "neutro");
	public CmdFactionsRelacaoOld cmdFactionsRelacaoOldEnemy = new CmdFactionsRelacaoOld("inimigo").setAliases("enemy", "rival", "inimigo");
	public CmdFactionsConvite cmdFactionsConvite = new CmdFactionsConvite();
	public CmdFactionsClaim cmdFactionsClaim = new CmdFactionsClaim();
	public CmdFactionsUnclaim cmdFactionsUnclaim = new CmdFactionsUnclaim();
	public CmdFactionsDesfazer cmdFactionsDesfazer = new CmdFactionsDesfazer();
	public CmdFactionsSair cmdFactionsSair = new CmdFactionsSair();
	public CmdFactionsEscapar cmdFactionsEscapar = new CmdFactionsEscapar();
	public CmdFactionsSobAtaque cmdFactionsSobAtaque = new CmdFactionsSobAtaque();
	public CmdFactionsTop cmdFactionsTop = new CmdFactionsTop();
	public CmdFactionsVerTerras cmdFactionsVerTerras = new CmdFactionsVerTerras();
	public CmdFactionsTitulos cmdFactionsTitulos = new CmdFactionsTitulos();
	public MassiveCommandVersion cmdFactionsVersion = new MassiveCommandVersion(Factions.get()).setAliases("v", "version").addRequirements(RequirementHasPerm.get(Perm.VERSION));
	public CmdFactionsAdmin cmdFactionsAdmin = new CmdFactionsAdmin();
	public CmdFactionsPoder cmdFactionsPoder = new CmdFactionsPoder();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactions()
	{
		// Deprecated Commands
		this.addChild(new MassiveCommandDeprecated(this.cmdFactionsUnclaim.cmdFactionsUnclaimAll, "unclaimall"));
		this.addChild(new MassiveCommandDeprecated(this.cmdFactionsInfo, "ver", "quem"));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<String> getAliases()
	{
		return MConf.get().aliasesF;
	}

}
