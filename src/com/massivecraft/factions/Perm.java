package com.massivecraft.factions;

import com.massivecraft.massivecore.Identified;
import com.massivecraft.massivecore.util.PermissionUtil;
import org.bukkit.permissions.Permissible;

public enum Perm implements Identified
{
	// -------------------------------------------- //
	// ENUM
	// -------------------------------------------- //
	
	ADMIN,
	BASECOMMAND,
	CHATALIADOS,
	CHATFACCAO,
	CLAIM,
	CLAIM_ALL,
	CLAIM_AUTO,
	CLAIM_ONE,
	CONVITE,
	CONVITE_ADD,
	CONVITE_DEL,
	CONVITE_LISTAR,
	CRIAR,
	DELHOME,
	DESC,
	DESFAZER,
	ENTRAR,
	ESCAPAR,
	HOME,
	INFO,
	KICK,
	LISTAR,
	MAPA,
	MEMBROS,
	MOTD,
	NOME,
	OPEN,
	PERFIL,
	PERM,
	PERMISSOES,
	PODER,
	PODER_FACTION,
	PODER_PLAYER,
	PODER_SET,
	PROMOVER,
	REBAIXAR,
	RELACAO,
	RELACAO_DEFINIR,
	RELACAO_LISTAR,
	SAIR,
	SETHOME,
	SOBATAQUE,
	TITULOS,
	TOP,
	TRANSFERIR,
	UNCLAIM,
	UNCLAIM_ALL,
	UNCLAIM_ONE,
	VERSION,
	VERTERRAS,
	
	// END OF LIST
	;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String id;
	@Override public String getId() { return this.id; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	Perm()
	{
		this.id = PermissionUtil.createPermissionId(Factions.get(), this);
	}
	
	// -------------------------------------------- //
	// HAS
	// -------------------------------------------- //
	
	public boolean has(Permissible permissible, boolean verboose)
	{
		return PermissionUtil.hasPermission(permissible, this, verboose);
	}
	
	public boolean has(Permissible permissible)
	{
		return PermissionUtil.hasPermission(permissible, this);
	}
	
}
