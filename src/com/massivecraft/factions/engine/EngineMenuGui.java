package com.massivecraft.factions.engine;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.engine.GuiHolder.Menu;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.Invitation;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.TemporaryBoard;
import com.massivecraft.factions.integration.vault.Eco;
import com.massivecraft.factions.util.Heads;
import com.massivecraft.factions.util.ItemBuilder;
import com.massivecraft.factions.util.OthersUtil;
import com.massivecraft.factions.util.WorldName;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.EntityInternalMap;
import com.massivecraft.massivecore.util.TimeDiffUtil;
import com.massivecraft.massivecore.util.TimeUnit;

public class EngineMenuGui extends Engine 
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineMenuGui i = new EngineMenuGui();
	public static EngineMenuGui get() { return i; }
	
	/**
	 * Menu principal dos players com facção
	 * @Menu: COM_FACÇÃO
	 */
	public void abrirMenuPlayerComFaccao(Player p) {

		// Variaveis
		final MPlayer mplayer = MPlayer.get(p);
		final Rel cargo = mplayer.getRole();
		final Faction factionclaim = BoardColl.get().getFactionAt(PS.valueOf(p.getLocation()));
		final Faction faction = mplayer.getFaction();
		final String factionNome = faction.getName();
		final String lider = faction.getLeader() == null ? "Indefinido" : faction.getLeader().getName() ;
		final String factiondesc = faction.getDescriptionDesc();
		int fackills = faction.getKills();
		int facmortes = faction.getDeaths();
		int membrosonline = faction.getOnlinePlayers().size();
		int membrosnafac = faction.getMembersCount();
		int membroslimite = faction.getMembersLimit();
		int terrastotal = faction.getLandCount();
		int mortes = mplayer.getDeaths();
		int kills = mplayer.getKills();
		int factionpodermaximo = faction.getPowerMaxRounded();
		int playerpodermaximo = mplayer.getPowerMaxRounded();
		int factionpoder = faction.getPowerRounded();
		int playerpoder = mplayer.getPowerRounded();
		final String kdr = mplayer.getKdrRounded();
		final String fackdr = faction.getKdrRounded();
		final GuiHolder holder = new GuiHolder(Menu.COM_FACCAO);
	
		// Criando o inventario
		Inventory inv = Bukkit.createInventory(holder, 54, "Facção [" + factionNome + "§r]");
				
		// Setando os itens normais
		inv.setItem(10, new ItemBuilder(Material.SKULL_ITEM,1,3).setSkullOwner(p.getName()).setName("§7"+p.getName()).setLore("§fPoder: §7" +  playerpoder + "/" + playerpodermaximo,"§fCargo: §7" + cargo.getPrefix() + cargo.getName(),"§fAbates: §7" + kills,"§fMortes: §7" + mortes, "§fKdr: §7" + kdr).toItemStack());
		inv.setItem(14, new ItemBuilder(Heads.ROXO.clone()).setName("§eRanking das Facções").setLore("§7Clique para ver os rankings com as", "§7facções mais poderosas do servidor.").toItemStack());
		inv.setItem(15, new ItemBuilder(Heads.LARANJA.clone()).setName("§eFacções Online").setLore("§7Clique para ver a lista de facções","§7online no servidor.").toItemStack());
		inv.setItem(16, new ItemBuilder(Heads.AMARELO.clone()).setName("§eAjuda").setLore("§7Todas as ações disponíveis neste menu", "§7também podem ser realizadas por","§7comando. Utilize o comando '§f/f ajuda§7'", "§7para ver todos os comandos disponíveis.").toItemStack());
		inv.setItem(30, new ItemBuilder(Material.PAPER).setName("§eGerenciar Convites").setLore("§7Clique para gerenciar os","§7convites da sua facção.").toItemStack());
		inv.setItem(31, new ItemBuilder(Material.BOOK_AND_QUILL).setName("§9Gerenciar permissões").setLore("§7Clique para gerenciar as","§7permissões da sua facção.").toItemStack());
		inv.setItem(39, new ItemBuilder(Material.SKULL_ITEM,membrosnafac,3).setName("§aMembros").setLore("§7A sua facção possui §a" + membrosnafac + (membrosnafac == 1 ? "§7 membro." : "§7 membros."),"§7Clique para obter mais informações.").toItemStack());
		inv.setItem(40, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§aRelações").setLore("§7Clique para gerenciar todas","§7as relações da sua facção.").setLeatherArmorColor(Color.LIME).toItemStack());
		
		// Setando os itens que usam verificações
		if (faction.isInAttack()) {
		inv.setItem(34, new ItemBuilder(Heads.VERMELHO.clone()).setName("§e[" + factionNome + "§e]").setLore("§cFacção sob ataque! Clique para mais detalhes.","§fTerras: §7" + terrastotal,"§fPoder: §7" + factionpoder, "§fPoder máximo: §7" + factionpodermaximo, "§fAbates: §7" + fackills, "§fMortes: §7" + facmortes, "§fKdr: §7" + fackdr, "§fLíder: §7" + lider, "§fMembros: §7(" + membrosnafac + "/" + membroslimite + ") " + membrosonline + " online", OthersUtil.fplayers(faction), "§7","§fDescrição:", "§7'" + factiondesc + "§7'", "§f", "§fMotd: §7", OthersUtil.fmotd(faction)).toItemStack()); }
		else {
		inv.setItem(34, new ItemBuilder(Heads.BRANCO.clone()).setName("§e[" + factionNome + "§e]").setLore("§aA facção não esta sob ataque.","§fTerras: §7" + terrastotal,"§fPoder: §7" + factionpoder, "§fPoder máximo: §7" + factionpodermaximo, "§fAbates: §7" + fackills, "§fMortes: §7" + facmortes, "§fKdr: §7" + fackdr, "§fLíder: §7" + lider, "§fMembros: §7(" + membrosnafac + "/" + membroslimite + ") " + membrosonline + " online", OthersUtil.fplayers(faction), "§7", "§fDescrição:","§7'" + factiondesc + "§7'","§f", "§fMotd: §7", OthersUtil.fmotd(faction)).toItemStack()); }
			
		if (mplayer.getRole() == Rel.LEADER) {
		inv.setItem(43, new ItemBuilder(Material.DARK_OAK_DOOR_ITEM).setName("§cDesfazer facção").setLore("§7Clique para desfazer a sua facção.").toItemStack()); }
		else {
		inv.setItem(43, new ItemBuilder(Material.DARK_OAK_DOOR_ITEM).setName("§cSair da facção").setLore("§7Clique para abandonar a sua facção.").toItemStack()); }	
			
		if (mplayer.isTerritoryInfoTitles()) {
		inv.setItem(37, new ItemBuilder(Material.PAINTING).setName("§eTitulos dos Territórios").setLore("§7Clique para alternar.","","§fStatus: §aAtivado").toItemStack()); }
		else {
		inv.setItem(37, new ItemBuilder(Material.PAINTING).setName("§eTitulos dos Territórios").setLore("§7Clique para alternar.","","§fStatus: §cDesativado").toItemStack()); }
			
		if (mplayer.isMapAutoUpdating()) {
		inv.setItem(38, new ItemBuilder(Material.MAP).setName("§aMapa dos Territórios").setLore("§7Você esta pisando em um território","§7protegido pela facção §e" + factionclaim.getName() + "§7.","","§fBotão esquerdo: §7Desliga o mapa automático.","§fBotão direito: §7Mostra o mapa completo.", "", "§fMapa automático: §aLigado").addItemFlag(ItemFlag.HIDE_POTION_EFFECTS).toItemStack()); }
		else {
		inv.setItem(38, new ItemBuilder(Material.EMPTY_MAP).setName("§aMapa dos Territórios").setLore("§7Você esta pisando em um território","§7protegido pela facção §e" + factionclaim.getName() + "§7.","","§fBotão esquerdo: §7Liga o mapa automático.","§fBotão direito: §7Mostra o mapa completo.", "", "§fMapa automático: §cDesligado").toItemStack()); }
			
		if (MConf.get().colocarIconeDoFGeradoresNoMenuGUI) {
		inv.setItem(41, new ItemBuilder(Material.MOB_SPAWNER).setName("§aGeradores").setLore("§7Clique para gerenciar os", "§7geradores da sua facção.").toItemStack());}
			
		if (MConf.get().colocarIconeDoFBauNoMenuGUI) {
		inv.setItem(41, new ItemBuilder(Material.CHEST).setName("§aBaú da facção").setLore("§7Clique para abir o baú", "§7virtual da sua facção.").toItemStack());}
			
		if (mplayer.isSeeingChunk()) {
		inv.setItem(28, new ItemBuilder(Material.GRASS).setName("§aDelimitações das Terras").setLore("§7Clique para alternar.","","§fStatus: §aAtivado").toItemStack()); }
		else {
		inv.setItem(28, new ItemBuilder(Material.GRASS).setName("§aDelimitações das Terras").setLore("§7Clique para alternar.","","§fStatus: §cDesativado").toItemStack());}
			
		if (p.getAllowFlight()) {
		inv.setItem(32, new ItemBuilder(Material.FEATHER).setName("§aModo voar").setLore("§7Clique para alternar.","","§fStatus: §aAtivado").toItemStack()); }
		else {
		inv.setItem(32, new ItemBuilder(Material.FEATHER).setName("§aModo voar").setLore("§7Clique para alternar.","","§fStatus: §cDesativado").toItemStack());}
		
		if (faction.hasHome() && (cargo == Rel.LEADER || cargo == Rel.OFFICER)) {
		inv.setItem(29, new ItemBuilder(Material.BEDROCK).setName("§aBase da Facção").setLore("§7Sua facção possui uma base!","","§fBotão esquerdo: §7Ir para base.","§fBotão direito: §7Definir base.","§fShift + Botão direito: §7Remover base.").toItemStack()); }
		else if (faction.hasHome()  && (!(cargo == Rel.LEADER || cargo == Rel.OFFICER))) {
		inv.setItem(29, new ItemBuilder(Material.BEDROCK).setName("§aBase da Facção").setLore("§7Sua facção possui uma base!","","§fBotão esquerdo: §7Ir para base.").toItemStack()); }
		else if (!faction.hasHome() && (cargo == Rel.LEADER || cargo == Rel.OFFICER)) {
		inv.setItem(29, new ItemBuilder(Material.BEDROCK).setName("§aBase da Facção").setLore("§7Sua facção ainda não definiu uma base.","","§fBotão direito: §7Definir base.").toItemStack()); }
		else if (!faction.hasHome() && (!(cargo == Rel.LEADER || cargo == Rel.OFFICER))) { 	
		inv.setItem(29, new ItemBuilder(Material.BEDROCK).setName("§aBase da Facção").setLore("§7Sua facção ainda não definiu uma base.").toItemStack()); }

		// Abrindo o inventario
		p.openInventory(inv);
	}
	
	
	/**
	 * Menu principal dos players sem facção
	 * @Menu: SEM_FACÇÃO
	 */
	public void abrirMenuPlayerSemFaccao(Player p) {
		
		// Variaveis
		final MPlayer mplayer = MPlayer.get(p);
		final Faction factionclaim = BoardColl.get().getFactionAt(PS.valueOf(p.getLocation()));
		int mortes = mplayer.getDeaths();
		int kills = mplayer.getKills();
		int playerpodermaximo = mplayer.getPowerMaxRounded();
		int playerpoder = mplayer.getPowerMaxRounded();
		int invitations = mplayer.getInvitations().size();
		final String kdr2f = mplayer.getKdrRounded();
		final GuiHolder holder = new GuiHolder(Menu.SEM_FACCAO);
		
		// Criando o inventario
		Inventory inv = Bukkit.createInventory(holder, 45, "Sem facção");
		
		// Setando os itens normais
		inv.setItem(10, new ItemBuilder(Material.SKULL_ITEM,1,3).setSkullOwner(p.getName()).setName("§7"+p.getName()).setLore("§fPoder: §7" +  playerpoder + "/" + playerpodermaximo,"§fCargo: §7§oNenhum","§fAbates: §7" + kills,"§fMortes: §7" + mortes, "§fKdr: §7" + kdr2f).toItemStack());
		inv.setItem(14, new ItemBuilder(Heads.ROXO.clone()).setName("§eRanking das Facções").setLore("§7Clique para ver os rankings com as", "§7facções mais poderosas do servidor.").toItemStack());
		inv.setItem(15, new ItemBuilder(Heads.LARANJA.clone()).setName("§eFacções Online").setLore("§7Clique para ver a lista de facções","§7online no servidor.").toItemStack());
		inv.setItem(16, new ItemBuilder(Heads.AMARELO.clone()).setName("§eAjuda").setLore("§7Todas as ações disponíveis neste menu", "§7também podem ser realizadas por","§7comando. Utilize o comando '§f/f ajuda§7'", "§7para ver todos os comandos disponíveis.").toItemStack());
		inv.setItem(29, new ItemBuilder(Material.BANNER,1,15).setName("§aCriar facção").setLore("§7Clique para criar a sua", "§7facção ou se preferir use", "§7o comando '§f/f criar <nome>§7'").toItemStack());
		
		// Setando os itens que usam verificações
		if (invitations > 0) {
		inv.setItem(30, new ItemBuilder(Material.PAPER).setName("§eConvites de Facções").setLore("§7Você possui §e" + invitations + (invitations == 1 ? "§7 convite pendente." : "§7 convites pendentes."), "§7Clique para gerenciar " + (invitations == 1 ? "o convite pendente." : "os convites pendentes.")).setAmount(invitations).toItemStack()); }
		else {
		inv.setItem(30, new ItemBuilder(Material.PAPER).setName("§eConvites de Facções").setLore("§cVocê não possui convites pendentes.").setAmount(0).toItemStack()); }

		if (mplayer.isMapAutoUpdating()) {
		inv.setItem(31, new ItemBuilder(Material.MAP).setName("§aMapa dos Territórios").setLore("§7Você esta pisando em um território","§7protegido pela facção §e" + factionclaim.getName() + "§7.","","§fBotão esquerdo: §7Desliga o mapa automático.", "§fBotão direito: §7Mostra o mapa completo.", "", "§fMapa automático: §aLigado").addItemFlag(ItemFlag.HIDE_POTION_EFFECTS).toItemStack()); }
		else {
		inv.setItem(31, new ItemBuilder(Material.EMPTY_MAP).setName("§aMapa dos Territórios").setLore("§7Você esta pisando em um território","§7protegido pela facção §e" + factionclaim.getName() + "§7.","","§fBotão esquerdo: §7Liga o mapa automático." ,"§fBotão direito: §7Mostra o mapa completo.", "", "§fMapa automático: §cDesligado").toItemStack()); }
		
		if (mplayer.isTerritoryInfoTitles()) {
		inv.setItem(32, new ItemBuilder(Material.PAINTING).setName("§eTitulos dos Territórios").setLore("§7Clique para alternar.","","§fStatus: §aAtivado").toItemStack()); }
		else {
		inv.setItem(32, new ItemBuilder(Material.PAINTING).setName("§eTitulos dos Territórios").setLore("§7Clique para alternar.","","§fStatus: §cDesativado").toItemStack()); }
			
		if (mplayer.isSeeingChunk()) {
		inv.setItem(33, new ItemBuilder(Material.GRASS).setName("§aDelimitações das Terras").setLore("§7Clique para alternar.","","§fStatus: §aAtivado").toItemStack()); }
		else {
		inv.setItem(33, new ItemBuilder(Material.GRASS).setName("§aDelimitações das Terras").setLore("§7Clique para alternar.","","§fStatus: §cDesativado").toItemStack());	}
	
		// Abrindo o inventario
		p.openInventory(inv);
	}
	
	
	/**
	 * Menu da lista de membros da facção
	 * @Menu: MEMBROS
	 */
	public void abrirMenuMembrosDaFaccao(MPlayer mplayer, Faction faction) {
		
		// Variaveis
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'às' hh:mm");
		int limitemembros = faction.getMembersLimit();
		List<MPlayer> mps = faction.getMPlayers();
		Player p = mplayer.getPlayer();
		GuiHolder holder = new GuiHolder(Menu.MEMBROS);
		
		// Definindo o tamanho do menu com base no limite de membros por facção
		int tamanhodomenu = 54;
		int slot = 11;
		if (limitemembros <= 5) {
			tamanhodomenu = 27;
		} else if (limitemembros <= 10) {
			tamanhodomenu = 36;
		} else if (limitemembros < 20) {
			tamanhodomenu = 45;
		}
		
		// Criando o inventario
		Inventory inv = Bukkit.createInventory(holder, tamanhodomenu, "Membros da [" + faction.getName() + "§r]");
		
		// Setando as cabeças "vagas"
		if (limitemembros > 25) slot = 2;
		ItemStack vago = new ItemBuilder(Material.SKULL_ITEM,1,3).setName("§8Vago").toItemStack();
		for (int i = 0; i < limitemembros && i < 30; i++) {
			inv.setItem(slot, vago);
			slot += slot == 6 || slot == 15 || slot == 24 || slot == 33 || slot == 42 ? + 5 : + 1;
		}
		
		// Setando as cabeças dos players da facção
		if (limitemembros > 25) slot = 2;
		else slot = 11;
		for (int i = 0; i < mps.size() && slot < 30; i++) {
			MPlayer mp = mps.get(i);
			Rel cargo = mp.getRole();
			String nome = mp.getName();
			boolean isOnline = mp.isOnline();
			int poderMax = mp.getPowerMaxRounded();
			int poderAtual = mp.getPowerMaxRounded();
			int kills = mp.getKills();
			int deaths = mp.getDeaths();
			String kdr2f = mp.getKdrRounded();
			String ultimoLogin = sdf.format(new Date(mp.getLastActivityMillis()));
			inv.setItem(slot, new ItemBuilder(Material.SKULL_ITEM,1,3).setSkullOwner(nome).setName("§7"+nome).setLore("§fPoder: §7" +  poderAtual + "/" + poderMax,"§fCargo: §7" + cargo.getPrefix() + cargo.getName(),"§fAbates: §7" + kills,"§fMortes: §7" + deaths, "§fKdr: §7" + kdr2f, "§fStats: " + (isOnline ? "§aOnline" : "§cOffline"), "§fÚltimo login: §7" + ultimoLogin).toItemStack());
			slot += slot == 6 || slot == 15 || slot == 24 || slot == 33 || slot == 42 ? + 5 : + 1;
		}
		
		// Abrindo o inventário
		p.openInventory(inv);
	}
	
	
	/**
	 * Menu dos territórios sobataque da facção
	 * @Menu: SOB_ATAQUE
	 */
	public void abrirMenuFaccaoSobAtaque(MPlayer mplayer) {
		
		// Variaveis
		Player p = mplayer.getPlayer();
        Faction faction = mplayer.getFaction();		
		GuiHolder holder = new GuiHolder(Menu.SOB_ATAQUE);
		
		// Criando o inventário
		Inventory inv = Bukkit.createInventory(holder, 54, "Terrenos Sob ataque");

		// Setando os itens no inventario
		int slot = 0;
		for (Entry<Chunk, Location> entry : EngineSobAtaque.infoattack.entrySet()) {
			Chunk c = entry.getKey();
			Faction fac = BoardColl.get().getFactionAt(PS.valueOf(c));
			if (fac.equals(faction)) {
				Location l = entry.getValue();
				String worldName = WorldName.valueOf(c.getWorld().getEnvironment().name()).getName();
				inv.setItem(slot, new ItemBuilder(Material.GRASS).setName("§e#" + (slot+1) + "§e Território sob ataque!").setLore("§fChunk: §7X:"+ c.getX() + "§8, §7Z:" + c.getZ(),"§fCoordenadas: §7X:" + l.getBlockX() + "§8, §7Y:" + l.getBlockY() + "§8, §7Z:" + l.getBlockZ(),"§fMundo: §7" + worldName).toItemStack());
			}
			slot++;
		}
		
		// Abrindo o inventário
		p.openInventory(inv);
	}
	
	
	/**
	 * Menu de confirmação para desfazer facção
	 * @Menu: DESFAZER_FACCAO
	 */
	public void abrirMenuDesfazerFaccao(MPlayer mp) {
		
		// Variaveis
        Player p = mp.getPlayer();
        Faction faction = mp.getFaction();
        String factionNome = faction.getName();
        GuiHolder holder = new GuiHolder(Menu.DESFAZER_FACCAO);
		
        // Criando o inventario
		Inventory inv = Bukkit.createInventory(holder, 36, "Desfazer facção");
		
		// Setando os itens
		inv.setItem(13, new ItemBuilder(Material.PAPER).setName("§fInformações").setLore("§7Você esta prestes a desfazer", "§7totalmente a facção §e" + factionNome + "§7.").toItemStack());
		inv.setItem(20, new ItemBuilder(Material.WOOL, 1, (byte) 5).setName("§aConfirmar ação").setLore("§7Clique para confirmar.").toItemStack());
		inv.setItem(24, new ItemBuilder(Material.WOOL, 1, (byte) 14).setName("§cCancelar ação").setLore("§7Clique para cancelar.").toItemStack());

		// Abrindo o inventario
		p.openInventory(inv);
	}
	
	
	/**
	 * Menu de confirmação para desfazer facção
	 * @Menu: ABANDONAR_FACCAO
	 */
	public void abrirMenuAbandonarFaccao(MPlayer mp) {
		
		// Variaveis
        Player p = mp.getPlayer();
        Faction faction = mp.getFaction();
        String factionNome = faction.getName();
        GuiHolder holder = new GuiHolder(Menu.ABANDONAR_FACCAO);
		
        // Criando o inventario
		Inventory inv = Bukkit.createInventory(holder, 36, "Abandonar facção");
		
		// Setando os itens
		inv.setItem(13, new ItemBuilder(Material.PAPER).setName("§fInformações").setLore("§7Você esta prestes a abandonar", "§7a facção §e" + factionNome + "§7.").toItemStack());
		inv.setItem(20, new ItemBuilder(Material.WOOL, 1, (byte) 5).setName("§aConfirmar ação").setLore("§7Clique para confirmar.").toItemStack());
		inv.setItem(24, new ItemBuilder(Material.WOOL, 1, (byte) 14).setName("§cCancelar ação").setLore("§7Clique para cancelar.").toItemStack());

		// Abrindo o inventario
		p.openInventory(inv);
	}
	
	
	/**
	 * Menu de confirmação para abandonar todas as terras
	 * @Menu: PROTEGER_TERRENO
	 */
	public void abrirMenuProtegerTerreno(Player p) {
		
		// Variaveis
        GuiHolder holder = new GuiHolder(Menu.PROTEGER_TERRENO);
		
        // Criando o inventario
		Inventory inv = Bukkit.createInventory(holder, 36, "Proteger terreno");
		
		// Setando os itens
		inv.setItem(13, new ItemBuilder(Material.PAPER).setName("§fInformações").setLore("§7Você esta prestes a gastar §2" + MConf.get().dinheiroCobradoParaProteger + " §7coins.", "§7para proteger este terreno por 30 minutos.").toItemStack());
		inv.setItem(20, new ItemBuilder(Material.WOOL, 1, (byte) 5).setName("§aConfirmar ação").setLore("§7Clique para confirmar.").toItemStack());
		inv.setItem(24, new ItemBuilder(Material.WOOL, 1, (byte) 14).setName("§cCancelar ação").setLore("§7Clique para cancelar.").toItemStack());

		// Abrindo o inventario
		p.openInventory(inv);
	}
	
	
	/**
	 * Menu de confirmação para abandonar todas as terras
	 * @Menu: ABANDONAR_TERRAS
	 */
	public void abrirMenuAbandonarTerras(Player p) {
		
		// Variaveis
        MPlayer mplayer = MPlayer.get(p);
        Faction faction = mplayer.getFaction();
        int terras = faction.getLandCount();
        GuiHolder holder = new GuiHolder(Menu.ABANDONAR_TERRAS);
		
        // Criando o inventario
		Inventory inv = Bukkit.createInventory(holder, 36, "Abandonar todas as terras");
		
		// Setando os itens
		inv.setItem(13, new ItemBuilder(Material.PAPER).setName("§fInformações").setLore("§7Você esta prestes a abandonar §2" + terras + " §7terras.").toItemStack());
		inv.setItem(20, new ItemBuilder(Material.WOOL, 1, (byte) 5).setName("§aConfirmar ação").setLore("§7Clique para confirmar.").toItemStack());
		inv.setItem(24, new ItemBuilder(Material.WOOL, 1, (byte) 14).setName("§cCancelar ação").setLore("§7Clique para cancelar.").toItemStack());

		// Abrindo o inventario
		p.openInventory(inv);
	}
	
	
	/**
	 * Menu principal do sistema de convites
	 * @Menu: CONVITES
	 */
	public void abrirMenuConvites(Player p) {
		
		// Variaveis
        MPlayer mplayer = MPlayer.get(p);
        Faction faction = mplayer.getFaction();
		int nconvites = faction.getInvitations().size();
        GuiHolder holder = new GuiHolder(Menu.CONVITES);
        
        // Criando o inventario
		Inventory inv = Bukkit.createInventory(holder, 27, "Gerenciar convites");
		
		//Setando os itens
		inv.setItem(11, new ItemBuilder(Heads.AZURE.clone()).setName("§aEnviar convite").setLore("§7Clique para enviar um convite de facção", "§7para um player ou se preferir use", "§7o comando '§f/f convite enviar <nome>§7'").toItemStack());
		if (nconvites == 0) {
		inv.setItem(15, new ItemBuilder(Material.PAPER).setName("§eGerenciar convites pendentes").setLore("§cSua facção não possui convites pendentes.").setAmount(nconvites).toItemStack()); }
		else {
		inv.setItem(15, new ItemBuilder(Material.PAPER).setName("§eGerenciar convites pendentes").setLore("§7Sua facção possui §e" + nconvites + (nconvites == 1 ? " §7convite pendente." : " §7convites pendentes."),"§7Clique para gerenciar" + (nconvites == 1 ? " §7o convite pendente." : " §7os convites pendentes.")).setAmount(nconvites).toItemStack()); }
		
		// Abrindo o inventario
		p.openInventory(inv);
	}
	
	
	/**
	 * Menu dos convites enviados facção
	 * @Menu: CONVITES_ENVIADOS
	 */
	public void abrirMenuConvitesEnviados(Faction faction, MPlayer mp) {
		
		// Variaveis
		Player p = mp.getPlayer();
		long now = System.currentTimeMillis();
		EntityInternalMap<Invitation> invitations = faction.getInvitations();
		int convites = invitations.size();
		int tamanho = 54;
		int flecha = 49;
		int slot = 10;
        GuiHolder holder = new GuiHolder(Menu.CONVITES_ENVIADOS);
		
        // Defindindo o tamanho do inv
		if (convites <= 7) {
			tamanho = 36;
			flecha = 31;
		} else if (convites <= 14) {
			tamanho = 45;
			flecha = 40;
		}
		
		//Criando o inventario
		Inventory inv = Bukkit.createInventory(holder, tamanho, "Convites pendentes (" + invitations.size() + ")");
		
		//Fazendo um loop pelos convites e setando os itens no inventário
		int n = 1;
		for (Entry<String, Invitation> invitation : invitations.entrySet()) {
			
		    // Pegando jogador convidado;
		    MPlayer invited = MPlayer.get(invitation.getKey());
		    
		    // Pegando jogador que convidou;
		    MPlayer inviter = MPlayer.get(invitation.getValue().getInviterId());
		    
		    // Pegando o nome do jogador convidado
		    String invitedName = invited != null ? invited.getName() : "§8§oDesconhecido";
		    
		    // Pegando o nome do jogador que convidou
		    String inviterName = inviter != null ? inviter.getRole().getPrefix() + inviter.getName() : "§8§oDesconhecido";
		    
		    // Pegando o tempo desde o envio do convite
			String inviteTime = "§7§o0 minutos";
			if (invitation.getValue().getCreationMillis() != null) {
				long millis = now - invitation.getValue().getCreationMillis();
				LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(millis, TimeUnit.getAllButMillis()), 2);
				inviteTime = TimeDiffUtil.formatedMinimal(ageUnitcounts);
			}

		    // Criando o item no menu gui
			inv.setItem(slot, new ItemBuilder(Material.PAPER).setName("§eConvite #"+n).setLore("§7Player convidado: §f" + invitedName, "§7Quem convidou: §f" + inviterName, "§7Convite enviado há " + inviteTime + "§7 atrás.", "","§fBotão direito: §7Deletar convite", "§fShift + Botão direito: §7Informações do player").toItemStack());
			slot += slot == 16 || slot == 25 || slot == 34 ? + 3 : + 1;
			n++;
		}
		inv.setItem(flecha, new ItemBuilder(Material.ARROW).setName("§cVoltar").toItemStack());
		
		// Abrindo o inventario
		p.openInventory(inv);
	}
	
	
	/**
	 * Menu dos convites recebidos do player
	 * @Menu: CONVITES_RECEBIDOS
	 */
	public void abrirMenuConvitesRecebidos(MPlayer mp) {
		
		// Variaveis
		Player p = mp.getPlayer();
		long now = System.currentTimeMillis();
		Set<String> invitations = mp.getInvitations();
		int convites = invitations.size();
		int tamanho = 54;
		int flecha = 49;
		int slot = 10;
        GuiHolder holder = new GuiHolder(Menu.CONVITES_RECEBIDOS);
		
        // Defindindo o tamanho do inv
		if (convites <= 7) {
			tamanho = 36;
			flecha = 31;
		} else if (convites <= 14) {
			tamanho = 45;
			flecha = 40;
		}
        
		//Criando o inventario
		Inventory inv = Bukkit.createInventory(holder, tamanho, "Convites pendentes (" + invitations.size() + ")");
		
		//Fazendo um loop pelos convites e setando os itens no inventário
		int n = 1;
		for (String factionId : invitations) {
			
		    // Pegando a facção que enviou o convite
		    Faction faction = Faction.get(factionId);
		    
		    // Pegando o nome da facção que convidou
		    String factionName = faction != null ? faction.getName() : "§cFacção Deletada";
		    Invitation invitation = faction != null ? faction.getInvitations().get(mp.getId()) : null;
		    
		    // Pegando o tempo desde o envio do convite e pegando o player que convite
			String inviteTime = "§7§o0 minutos";
			String inviterName = "§8§oDesconhecido";
			if (invitation != null && invitation.getCreationMillis() != null) {
				long millis = now - invitation.getCreationMillis();
				LinkedHashMap<TimeUnit, Long> ageUnitcounts = TimeDiffUtil.limit(TimeDiffUtil.unitcounts(millis, TimeUnit.getAllButMillis()), 2);
				inviteTime = TimeDiffUtil.formatedMinimal(ageUnitcounts);
				MPlayer inviter = MPlayer.get(invitation.getInviterId());
			    inviterName = inviter != null ? inviter.getRole().getPrefix() + inviter.getName() : "§8§oDesconhecido";
			}
		    
		    // Criando o item no menu gui
			inv.setItem(slot, new ItemBuilder(Material.PAPER).setName("§eConvite #"+n).setLore("§7Convite da facção: §f[" + factionName + "§f]", "§7Convite enviado por: §f" + inviterName, "§7Convite recebido há " + inviteTime + "§7 atrás.", "", "§fBotão Esquerdo: §7Aceitar convite", "§fBotão direito: §7Recusar convite", "§fShift + Botão direito: §7Informações da facção").toItemStack());			
			slot += slot == 16 || slot == 25 || slot == 34 ? + 3 : + 1;
			n++;
		}
		inv.setItem(flecha, new ItemBuilder(Material.ARROW).setName("§cVoltar").toItemStack());
		
		// Abrindo o inventario
		p.openInventory(inv);
	}

	
	
	/**
	 * Menu principal do sistema de relações
	 * @Menu: GERENCIAR_RELACOES
	 */
	public void abrirMenuGerenciarRelacoes(Player p) {
		
		// Variaveis
        MPlayer mplayer = MPlayer.get(p);
        Faction faction = mplayer.getFaction();
        int enviados = OthersUtil.getAliadosPendentesEnviados(faction).size();
        int nrelations = faction.getRelationWishes().size() - enviados;
        int pendentes = enviados + faction.getPendingRelations().size();
        GuiHolder holder = new GuiHolder(Menu.GERENCIAR_RELACOES);

        // Criando o inventário
		Inventory inv = Bukkit.createInventory(holder, 27, "Gerenciar relações");
		
		// Setando os itens
		inv.setItem(11, new ItemBuilder(Heads.AZURE.clone()).setName("§aDefinir relação").setLore("§7Clique para definir uma relação com", "§7alguma facção ou se preferir use", "§7o comando '§f/f relação <facção>§7'").toItemStack());
		
		if (pendentes < 1) {
		inv.setItem(13, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§aAlianças pendentes").setLore("§cSua facção não possui relações pendentes.").setLeatherArmorColor(Color.LIME).setAmount(0).toItemStack());
		} else {
		inv.setItem(13, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§aAlianças pendentes").setLore("§7Clique para ver todos os convites","§7de aliança pendentes recebidos ou", "§7enviados pela sua facção.").setLeatherArmorColor(Color.LIME).setAmount(pendentes).toItemStack()); }
		
		if (nrelations < 1) {
		inv.setItem(15, new ItemBuilder(Material.BOOK).setName("§eVer relações").setLore("§cSua facção não possui relações definidas.").setAmount(0).toItemStack());
		} else {
		inv.setItem(15, new ItemBuilder(Material.BOOK).setName("§eVer relações").setLore("§7Clique para ver a lista", "§7de relações da sua facção.").setAmount(nrelations > 64 ? 1 : nrelations).toItemStack()); }
		
		// Abrindo o inventário
		p.openInventory(inv);
	}
	
	
	/**
	 * Menu principal do sistema de relações
	 * @Menu: VER_RELACOES
	 */
	public void abrirMenuVerRelacoes(Player p) {
		
		// Variaveis
        MPlayer mplayer = MPlayer.get(p);
        Faction faction = mplayer.getFaction();
        int nrelations = faction.getRelationWishes().size() - OthersUtil.getAliadosPendentesEnviados(faction).size();
        GuiHolder holder = new GuiHolder(Menu.VER_RELACOES);

        // Criando o inventário
		Inventory inv = Bukkit.createInventory(holder, 27, "Gerenciar relações");
		
		// Setando os itens
		inv.setItem(11, new ItemBuilder(Material.BARRIER).setName("§aDefinir relação").setLore("§cVocê não tem permissão para isso.").toItemStack());
		inv.setItem(13, new ItemBuilder(Material.BARRIER).setName("§aAlianças pendentes").setLore("§cVocê não tem permissão para isso.").setLeatherArmorColor(Color.LIME).toItemStack());
		
		if (nrelations < 1) {
		inv.setItem(15, new ItemBuilder(Material.BOOK).setName("§eVer relações").setLore("§cSua facção não possui relações definidas.").setAmount(nrelations).toItemStack());
		} else {
		inv.setItem(15, new ItemBuilder(Material.BOOK).setName("§eVer relações").setLore("§7Clique para ver a lista", "§7de relações da sua facção.").setAmount(nrelations > 64 ? 1 : nrelations).toItemStack()); }
		
		// Abrindo o inventário
		p.openInventory(inv);
	}
	
	
	/**
	 * Menu para definir relação com uma facção
	 * @Menu: DEFINIR_RELACAO
	 */
	public void abrirMenuDefinirRelacao(MPlayer mplayer, Faction otherFaction) {
		
		// Variaveis
		String factionNome = otherFaction.getName();
		Player p = mplayer.getPlayer();
        Faction faction = mplayer.getFaction();
        GuiHolder holder = new GuiHolder(Menu.DEFINIR_RELACAO);

        // Criando o inventário
		Inventory inv = Bukkit.createInventory(holder, 27, "Relação com [" + factionNome + "§r]");
		
		// Setando os itens
		if (faction.getRelationTo(otherFaction) == Rel.ALLY) {
		inv.setItem(11, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§bSua a facção já é aliada da [" + factionNome + "§b]").setLeatherArmorColor(Color.AQUA).toItemStack()); }
		else if (faction.getRelationWish(otherFaction) == Rel.ALLY || otherFaction.getRelationWish(faction) == Rel.ALLY) {
		inv.setItem(11, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§bDefinir aliança com a [" + factionNome + "§b]").setLore("§eSua facção já possui um convite de", "§ealiança pendente com a §7"+ factionNome + "§e!", "", "§fClique para ver a lista de", "§fconvites de aliança pendentes.").setLeatherArmorColor(Color.AQUA).toItemStack()); }
		else {
		inv.setItem(11, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§bDefinir aliança com a [" + factionNome + "§b]").setLore("§fClique para definir relação de aliança.").setLeatherArmorColor(Color.AQUA).toItemStack()); }
		
		if (faction.getRelationWish(otherFaction) == Rel.NEUTRAL) {
		inv.setItem(13, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§fSua a facção já é neutra da [" + factionNome + "§f]").setLeatherArmorColor(Color.WHITE).toItemStack()); }
		else {
		inv.setItem(13, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§fDefinir neutralidade com [" + factionNome + "§f]").setLore("§fClique para definir relação de neutralidade.").setLeatherArmorColor(Color.WHITE).toItemStack()); }
		
		if (faction.getRelationWish(otherFaction) == Rel.ENEMY) {
		inv.setItem(15, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§cSua a facção já é rival da [" + factionNome + "§c]").setLeatherArmorColor(Color.RED).toItemStack()); }
		else {
		inv.setItem(15, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§cDefinir rivalidade com [" + factionNome + "§c]").setLore("§fClique para definir relação de aliança.").setLeatherArmorColor(Color.RED).toItemStack()); }

		// Abrindo o inventario
		p.openInventory(inv);
	}
	
	
	/**
	 * Menu do sistema de relações pendentes
	 * @Menu: RELACOES_PENDENTES
	 */
	public void abrirMenuRelacoesPendentes(Player p) {
		
		// Variaveis
        MPlayer mplayer = MPlayer.get(p);
        Faction faction = mplayer.getFaction();
        int enviados = OthersUtil.getAliadosPendentesEnviados(faction).size();
        int recebidos = faction.getPendingRelations().size();
        int npendentes = enviados + recebidos;
        GuiHolder holder = new GuiHolder(Menu.RELACOES_PENDENTES);
        
        // Criando o inventário
		Inventory inv = Bukkit.createInventory(holder, 36, "Alianças pendentes (" + npendentes + ")");
		
		// Setando os itens
		inv.setItem(31, new ItemBuilder(Material.ARROW).setName("§cVoltar").toItemStack());
		
		if (recebidos > 0) {
		inv.setItem(11, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§bConvites de aliança recebidos pendentes").setLore("§7Sua facção possui §b" + recebidos + (recebidos == 1 ? " §7convite recebido pendente." : " §7convites recebidos pendentes.")).setLeatherArmorColor(Color.AQUA).setAmount(recebidos).toItemStack()); }
		else {
		inv.setItem(11, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§bConvites de aliança recebidos pendentes").setLore("§cSua facção não possui nenhum", "§cconvite recebido pendente.").setLeatherArmorColor(Color.AQUA).setAmount(0).toItemStack()); }
		
		if (enviados > 0) {
		inv.setItem(15, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§aConvites de aliança enviados pendentes").setLore("§7Sua facção possui §a" + enviados + (enviados == 1 ? " §7convite enviado pendente." : " §7convites enviados pendentes.")).setLeatherArmorColor(Color.LIME).setAmount(enviados).toItemStack()); }	
		else {
		inv.setItem(15, new ItemBuilder(Material.LEATHER_CHESTPLATE).setName("§aConvites de aliança enviados pendentes").setLore("§cSua facção não possui nenhum", "§cconvite enviado pendente.").setLeatherArmorColor(Color.LIME).setAmount(0).toItemStack()); }
				
		// Abrindo o inventario
		p.openInventory(inv);
	}
	
	
	/**
	 * Menu das relações pendentes enviadas do sistema de relações pendentes
	 * @Menu: RELACOES_PENDENTES_ENVIADAS
	 */
	public void abrirMenuRelacoesPendentesEnviados(Player p) {
		
		// Variaveis
        MPlayer mplayer = MPlayer.get(p);
        Faction faction = mplayer.getFaction();
		Set<Faction> facs = OthersUtil.getAliadosPendentesEnviados(faction);
        int enviados = facs.size();
		int tamanho = 54;
		int flecha = 49;
		int slot = 10;
        GuiHolder holder = new GuiHolder(Menu.RELACOES_PENDENTES_ENVIADAS);
		
		// Tamanho do menu GUI
		if (enviados <= 7) {
			tamanho = 36;
			flecha = 31;
		} else if (enviados <= 14) {
			tamanho = 45;
			flecha = 40;
		}
		
		// Criando o inventário
		Inventory inv = Bukkit.createInventory(holder, tamanho, "Convites enviados pendentes");
		
		// Setando os itens
		for (Faction f : facs) {
			String factionNome = f.getName();
			inv.setItem(slot, new ItemBuilder(Material.PAPER).setName("§eConvite para facção §f[" + factionNome + "§f]").setLore("§fBotão Direito: §7Deletar convite","§fShift + Botão direito: §7Informações da facção").toItemStack());
			slot += slot == 16 || slot == 25 || slot == 34 ? + 3 : + 1;
		}
		inv.setItem(flecha, new ItemBuilder(Material.ARROW).setName("§cVoltar").toItemStack());

		// Abrindo o inventário
		p.openInventory(inv);
	}
	
	
	/**
	 * Menu das relações pendentes recebidas do sistema de relações pendentes
	 * @Menu: RELACOES_PENDENTES_RECEBIDAS
	 */
	public void abrirMenuRelacoesPendentesRecebidos(Player p) {
		
		// Variaveis
        MPlayer mplayer = MPlayer.get(p);
        Faction faction = mplayer.getFaction();
		Set<String> facs = faction.getPendingRelations();
        int recebidos = facs.size();
		int tamanho = 54;
		int flecha = 49;
		int slot = 10;
        GuiHolder holder = new GuiHolder(Menu.RELACOES_PENDENTES_RECEBIDAS);
		
		//Tamano do menu GUI
		if (recebidos <= 7) {
			tamanho = 36;
			flecha = 31;
		} else if (recebidos <= 14) {
			tamanho = 45;
			flecha = 40;
		}
		
		// Criando o inventário
		Inventory inv = Bukkit.createInventory(holder, tamanho, "Convites recebidos pendentes");
		
		 // Setando os itens
		for (String s : facs) {
			Faction f = Faction.get(s);
			String factionNome = f.getName();
			inv.setItem(slot, new ItemBuilder(Material.PAPER).setName("§eConvite da facção §f[" + factionNome + "§f]").setLore("§fBotão Esquerdo: §7Aceitar convite","§fBotão Direito: §7Deletar convite","§fShift + Botão direito: §7Informações da facção").toItemStack());	
			slot += slot == 16 || slot == 25 || slot == 34 ? + 3 : + 1;
		}
		inv.setItem(flecha, new ItemBuilder(Material.ARROW).setName("§cVoltar").toItemStack());

		// Inventário
		p.openInventory(inv);
	}
	
	
	// -------------------------------------------- //
	// CLICK LISTENER
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void aoClickar(InventoryClickEvent e) {
		Inventory inv = e.getInventory();
		if (e.getCurrentItem() == null || inv.getType() != InventoryType.CHEST)
			return;

		InventoryHolder holder = inv.getHolder();
		if (holder == null || !(holder instanceof GuiHolder))
			return;
		
		e.setCancelled(true);
		e.setResult(Result.DENY);
		
		Menu menu = ((GuiHolder) holder).getType();
		Player p = (Player) e.getWhoClicked();
		MPlayer mp = MPlayer.get(p);
    	int slot = e.getRawSlot();
    	
    	if (inv.getSize() < slot) return;

    	/**
    	 * @Menu: SEM_FACCAO
    	 */
		if (menu == Menu.SEM_FACCAO) {
			
			if (slot == 14) {
				p.chat("/f top");
				return;
			}
		
			else if (slot == 15) {
				p.chat("/f listar");
				p.closeInventory();
				return;
			}
		
			else if (slot == 16) {
				p.chat("/f ajuda");
				p.closeInventory();
				return;
			}
			
			else if (slot == 29) {
				EngineChatCommands.USING.put(p, Command.CREATE);
	    		p.sendMessage("\n§eQual sera o nome de sua Facção?\n§7Caso queira cancelar, responda '§ccancelar§7'.\n §a");
				p.closeInventory();
				return;
			}
			
			else if (slot == 30) {
				ItemStack item = e.getCurrentItem();
				if (item.getAmount() > 0) {
					abrirMenuConvitesRecebidos(mp);
					return;
				}
				return;
			}
			
			else if (slot == 31) {
				if (e.getClick().isRightClick()) {
					p.chat("/f mapa");
					return;
				}
				else if (e.getClick().isLeftClick()) {
					if (mp.isMapAutoUpdating()) {
						mp.setMapAutoUpdating(false);
					} else { 
						mp.setMapAutoUpdating(true);
					}
					abrirMenuPlayerSemFaccao(p);
					return;
				}
			}
			
			else if (slot == 32) {
				p.chat("/f tt");
				abrirMenuPlayerSemFaccao(p);
				return;
			}
			
			else if (slot == 33) {
				p.chat("/f sc");
				abrirMenuPlayerSemFaccao(p);
				return;
			}
		}
		
    	
    	/**
    	 * @Menu: COM_FACCAO
    	 */
		else if (menu == Menu.COM_FACCAO) {
			Faction f = mp.getFaction();
			
			if (slot == 34) {
				if (f.isInAttack()) {
					abrirMenuFaccaoSobAtaque(mp);
					return;
				}
			}
			
			else if (slot == 14) {
				p.chat("/f top");
				return;
			}
		
			else if (slot == 15) {
				p.chat("/f listar");
				p.closeInventory();
				return;
			}
		
			else if (slot == 16) {
				p.chat("/f ajuda");
				p.closeInventory();
				return;
			}
			
			else if (slot == 28) {
				p.chat("/f sc");
				abrirMenuPlayerComFaccao(p);
				return;
			}
			
			else if (slot == 29) {
				if (f.hasHome() == false) {
					if (mp.getRole() == Rel.LEADER || mp.getRole() == Rel.OFFICER || mp.isOverriding()) {
						if (e.getClick().isRightClick()) {
							p.chat("/f sethome");
							abrirMenuPlayerComFaccao(p);
							return;
						}
					}
				} else {
					if (e.getClick().isShiftClick()) {
						if (mp.getRole() == Rel.LEADER || mp.getRole() == Rel.OFFICER || mp.isOverriding()) {
							p.chat("/f delhome");
							abrirMenuPlayerComFaccao(p);
							return;
						}
					}
					if (e.getClick().isRightClick()) {
						if (mp.getRole() == Rel.LEADER || mp.getRole() == Rel.OFFICER || mp.isOverriding()) {
							p.chat("/f sethome");
							return;
						}
					}
					if (e.getClick().isLeftClick()) {
						p.chat("/f home");
						p.closeInventory();
						return;
					}
				}
			}
			
			else if (slot == 30) {
				if (mp.getRole() == Rel.LEADER || mp.getRole() == Rel.OFFICER || mp.isOverriding()) {
					abrirMenuConvites(p);
				} else {
					p.sendMessage("§cVocê precisar ser capitão ou superior para poder gerenciar os convites da facção.");
				}
				return;
			}
			
			else if (slot == 31) {
				EngineMenuPermissoes.get().abrirMenuPermissoes(mp, f);
				return;
			}
			
			else if (slot == 32) {
				p.chat("/f voar");
				abrirMenuPlayerComFaccao(p);
				return;
			}
			
			else if (slot == 37) {
				p.chat("/f tt");
				abrirMenuPlayerComFaccao(p);
				return;
			}
			
			else if (slot == 38) {
				if (e.getClick().isRightClick()) {
					p.chat("/f mapa");
					return;
				}
				else if (e.getClick().isLeftClick()) {
					if (mp.isMapAutoUpdating()) {
						mp.setMapAutoUpdating(false);
					} else { 
						mp.setMapAutoUpdating(true);
					}
					abrirMenuPlayerComFaccao(p);
					return;
				}
			}
			
			else if (slot == 39) {
				abrirMenuMembrosDaFaccao(mp, f);
				return;
			}
			
			else if (slot == 40) {
				if (mp.getRole() == Rel.LEADER || mp.getRole() == Rel.OFFICER || mp.isOverriding()) {
					abrirMenuGerenciarRelacoes(p);
				} else {
					abrirMenuVerRelacoes(p);
				}
				return;
			}
			
			else if (slot == 41) {
				if (MConf.get().colocarIconeDoFBauNoMenuGUI) {
					p.chat("/f bau");
				} else if (MConf.get().colocarIconeDoFGeradoresNoMenuGUI) {
					p.chat("/f geradores");
				}
				return;
			}
			
			else if (slot == 43) {
				if (mp.getRole() == Rel.LEADER || mp.isOverriding()) {
					abrirMenuDesfazerFaccao(mp);
				} else { 
					abrirMenuAbandonarFaccao(mp);
				}
				return;
			}
		}
    	
		
    	/**
    	 * @Menu: DESFAZER_FACCAO
    	 */
		else if (menu == Menu.DESFAZER_FACCAO) {
			
			if (slot == 20) {
				p.chat("/f desfazer confirmar");
				p.closeInventory();
				return;
			}
			
			else if (slot == 24) {
				p.sendMessage("§cAção cancelada com sucesso.");
				p.closeInventory();
				return;
			}
		}
    	
		
    	/**
    	 * @Menu: ABANDONAR_FACCAO
    	 */
		else if (menu == Menu.ABANDONAR_FACCAO) {
			
			if (slot == 20) {
				p.chat("/f sair confirmar");
				p.closeInventory();
				return;
			}
			
			else if (slot == 24) {
				p.sendMessage("§cAção cancelada com sucesso.");
				p.closeInventory();
				return;
			}
		}
		
		
    	/**
    	 * @Menu: ABANDONAR_TERRAS
    	 */
		else if (menu == Menu.ABANDONAR_TERRAS) {
			
			if (slot == 20) {
				p.chat("/f unclaim all confirmar");
				p.closeInventory();
				return;
			}
			
			else if (slot == 24) {
				p.sendMessage("§cAção cancelada com sucesso.");
				p.closeInventory();
				return;
			}
		}		
		
		
    	/**
    	 * @Menu: PROTEGER_TERRENO
    	 */
		else if (menu == Menu.PROTEGER_TERRENO) {
			if (slot == 20) {
				Faction f = mp.getFaction();
				
				if (!Eco.isEnabled()) {
					p.sendMessage("§cNenhum sistema de economia pode ser encontrado.");
					p.closeInventory();
					return;
				}
				
				double price = MConf.get().dinheiroCobradoParaProteger;
				if (!Eco.has(p, price)) {
					p.sendMessage("§cVocê não possui dinheiro suficiente para proteger este terreno.");
					p.closeInventory();
					return;
				}
				
				PS ps = PS.valueOf(p.getLocation());
				Faction at = BoardColl.get().getFactionAt(ps);
				if (!at.isNone()) {
					p.sendMessage("§cVocê só pode proteger terrenos que estejam livres.");
					p.closeInventory();
					return;
				}
				
				int limit = MConf.get().limiteDeProtecoesTemporaria;
				if (limit > 0 && f.getTempClaims().size() >= limit) {
					p.sendMessage("§cLimite máximo de terrenos temporários atingido (" + limit + ")! Abandone terrenos temporário antigos para poder proteger novos terrenos.");
					p.closeInventory();
					return;
				}
				
				p.closeInventory();
				Eco.Withdraw(p, price);
				TemporaryBoard.get().create(ps, f);
				p.sendMessage("§aTerreno protegido com sucesso.");
				return;
			}
			
			else if (slot == 24) {
				p.sendMessage("§cAção cancelada com sucesso.");
				p.closeInventory();
				return;
			}
		}
    	
		
    	/**
    	 * @Menu: CONVITES
    	 */
		else if (menu == Menu.CONVITES) {
			Faction f = mp.getFaction();
		
			if (slot == 11) {
				EngineChatCommands.USING.put(p, Command.INVITE);
	    		p.sendMessage("\n§eQual o nome do player que você deseja convidar?\n§7Caso queira cancelar, responda '§ccancelar§7'.\n §a");
				p.closeInventory();
				return;
			}
			
			else if (slot == 15) {
				if (f.getInvitations().size() > 0) {
					abrirMenuConvitesEnviados(mp.getFaction(), mp);
				}
				return;
			}
		}
    	
		
    	/**
    	 * @Menu: CONVITES_ENVIADOS
    	 */
		else if (menu == Menu.CONVITES_ENVIADOS) {	
			ItemStack item = e.getCurrentItem();
			
			if (item.getType() == Material.ARROW) {
				abrirMenuConvites(p);
				return;
			}
			
			else if (item.getType() == Material.PAPER) {
				String nome = item.getItemMeta().getLore().get(0).replace("§7Player convidado: §f", "").replace(" ", "");
				if (e.getClick().isShiftClick()) {
					p.chat("/f perfil " + nome);
					p.closeInventory();
				} else if (e.getClick().isRightClick()) {
					p.chat("/f convite del " + nome);
					abrirMenuConvitesEnviados(mp.getFaction(), mp);
				}
				return;
			}
		}
    	
		
    	/**
    	 * @Menu: CONVITES_RECEBDOS
    	 */
		else if (menu == Menu.CONVITES_RECEBIDOS) {	
			ItemStack item = e.getCurrentItem();
			
			if (item.getType() == Material.ARROW) {
				abrirMenuPlayerSemFaccao(p);
				return;
			}
			
			else if (item.getType() == Material.PAPER) {
				String nome = item.getItemMeta().getLore().get(0).replace("§7Convite da facção: §f[", "").replace("§f]", "").replace(" ", "");
				if (e.getClick().isShiftClick()) {
					p.chat("/f info " + nome);
					p.closeInventory();
				} else if (e.getClick().isLeftClick()) {
					p.chat("/f entrar " + nome);
					p.closeInventory();
				} else {
					Faction target = FactionColl.get().getByName(nome);
					if (target == null) {
						p.sendMessage("§cUm erro interno foi detectado, por favor contato um administrou ou aguarde a reinicialização do servidor.");
						return;
					}
					p.sendMessage("§aConvite da facção §f[" + nome + "§f]§a deletado com sucesso.");
					target.msg("§f" + mp.getName() + "§e recuso o convite para entrar na facção.");
					target.uninvite(mp);
					abrirMenuConvitesRecebidos(mp);
				}
				return;
			}
		}	
		
		
    	/**
    	 * @Menu: GERENCIAR_RELACOES
    	 */
		else if (menu == Menu.GERENCIAR_RELACOES) {
	    	
			if (slot == 11) {
	    		EngineChatCommands.USING.put(p, Command.RELATION);
	    		p.sendMessage("\n§eQual o nome da facção que desejá alterar a relação?\n§7Caso queira cancelar, responda '§ccancelar§7'.\n §a");
				p.closeInventory();
				return;
			}
			
			else if (slot == 13) {
				if (e.getCurrentItem().getAmount() > 0) {
					abrirMenuRelacoesPendentes(p);
				}
				return;
			}
			
			else if (slot == 15) {
				if (e.getCurrentItem().getAmount() > 0) {
					p.chat("/f relacao listar");
					p.closeInventory();
				}
				return;
			}
		}
    	
		
    	/**
    	 * @Menu: VER_RELACOES
    	 */
		else if (menu == Menu.VER_RELACOES) {	
			
			if (slot == 15) {
				ItemStack item = e.getCurrentItem();
				if (item.getAmount() > 0) {
					p.chat("/f relacao listar");
					p.closeInventory();
				}
				return;
			}
		}
		
		
    	/**
    	 * @Menu: DEFINIR_RELACAO
    	 */
		else if (menu == Menu.DEFINIR_RELACAO) {
			
			ItemStack item = e.getCurrentItem();
			if (!item.getItemMeta().hasLore()) return;
			
			String faction = e.getInventory().getName().replace("Relação com [", "").replace("§r]", "").replace(" ", "");
			
			if (slot == 11) {
				if (item.getItemMeta().getLore().size() < 2) {
					p.chat("/f relacao " + faction + " ally");
					p.closeInventory();
				} else {
					abrirMenuRelacoesPendentes(p);
				}
				return;
			}
			
			else if (slot == 13) {
				p.chat("/f relacao " + faction + " neutral");
				p.closeInventory();
				return;
			}
			
			else if (slot == 15) {
				p.chat("/f relacao " + faction + " enemy");
				p.closeInventory();
				return;
			}	
		}
    	
		
    	/**
    	 * @Menu: RELACOES_PENDENTES
    	 */
		else if (menu == Menu.RELACOES_PENDENTES) {
	    	ItemStack item = e.getCurrentItem();
			
			if (slot == 11) {
				if (item.getAmount() > 0) {
					abrirMenuRelacoesPendentesRecebidos(p);
				}
				return;
			}
			
			else if (slot == 15) {
				if (item.getAmount() > 0) {
					abrirMenuRelacoesPendentesEnviados(p);
				}
				return;
			}
			
			else if (slot == 31) {
				abrirMenuGerenciarRelacoes(p);
			}
			return;
		}
    	
		
    	/**
    	 * @Menu: RELACOES_PENDENTES_ENVIADAS
    	 */
		else if (menu == Menu.RELACOES_PENDENTES_ENVIADAS) {
			Faction f = mp.getFaction();
			ItemStack item = e.getCurrentItem();
			
			if (item.getType() == Material.ARROW) {
				abrirMenuRelacoesPendentes(p);
				return;
			}
			
			else if (item.getType() == Material.PAPER) {
				String nome = item.getItemMeta().getDisplayName().replace("§eConvite para facção §f[", "").replace("§f]", "").replace(" ", "");
				if (e.getClick().isShiftClick()) {
					p.chat("/f info " + nome.replace(" ", ""));
					p.closeInventory();
				} else if (e.getClick().isRightClick()) {
					Faction target = FactionColl.get().getByName(nome);
					f.setRelationWish(target, Rel.NEUTRAL);
					p.sendMessage("§eConvite de aliança para a facção §f[" + nome + "§f]§e deletado com sucesso.");
					abrirMenuRelacoesPendentesEnviados(p);
				}
				return;
			}
		}
    	
		
    	/**
    	 * @Menu: RELACOES_PENDENTES_RECEBIDAS
    	 */
		else if (menu == Menu.RELACOES_PENDENTES_RECEBIDAS) {
			Faction f = mp.getFaction();
	    	String factionNome = f.getName();
			ItemStack item = e.getCurrentItem();
			
			if (item.getType() == Material.ARROW) {
				abrirMenuRelacoesPendentes(p);
				return;
			}
			
			else if (item.getType() == Material.PAPER) {
				String nome = item.getItemMeta().getDisplayName().replace("§eConvite da facção §f[", "").replace("§f]", "").replace(" ", "");
				if (e.getClick().isShiftClick()) {
					p.chat("/f info " + nome.replace(" ", ""));
					p.closeInventory();
				} else if (e.getClick().isLeftClick()) {
					p.chat("/f relacao " + nome.replace(" ", "") + " ally" );
					abrirMenuRelacoesPendentesRecebidos(p);
				} else if (e.getClick().isRightClick()) {
					Faction target = FactionColl.get().getByName(nome);
					target.setRelationWish(f, Rel.NEUTRAL);
					target.msg("§eA facção §f[" + factionNome + "§f]§e recusou seu pedido de aliança.");
					f.removePendingRelation(target);
					f.msg("§ePedido de aliança da facção §f[" + target.getName() + "§f]§e recusado.");
					abrirMenuRelacoesPendentesRecebidos(p);
				}
				return;
			}
		}
    	
		
    	/**
    	 * @Menu: MEMBROS
    	 */
		else if (menu == Menu.MEMBROS) {
			return;
		}
    	
		
    	/**
    	 * @Menu: SOB_ATAQUE
    	 */
		else if (menu == Menu.SOB_ATAQUE) {
			return;
		}
	}
}

class GuiHolder implements InventoryHolder {
	
	private Menu type;

	public GuiHolder(Menu type) {
		this.type = type;
	}

	public Menu getType() {
		return this.type;
	}

	public Inventory getInventory() {
		return null;
	}

	enum Menu {
		COM_FACCAO, SEM_FACCAO,	MEMBROS, DESFAZER_FACCAO, PROTEGER_TERRENO,
		ABANDONAR_FACCAO, CONVITES, CONVITES_ENVIADOS, CONVITES_RECEBIDOS,
		SOB_ATAQUE, GERENCIAR_RELACOES, VER_RELACOES, RELACOES_PENDENTES, 
		RELACOES_PENDENTES_ENVIADAS, RELACOES_PENDENTES_RECEBIDAS, DEFINIR_RELACAO, 
		ABANDONAR_TERRAS, PERMISSOES;
	}
}