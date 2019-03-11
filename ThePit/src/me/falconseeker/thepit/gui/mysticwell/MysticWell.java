package me.falconseeker.thepit.gui.mysticwell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.falconseeker.extraevents.GoldenHeads;
import me.falconseeker.thepit.ThePit;
import me.falconseeker.thepit.enchants.enchants.PantEnchantTypes;
import me.falconseeker.thepit.gui.GUI;
import me.falconseeker.thepit.utils.Methods;
import me.falconseeker.thepit.utils.XMaterial;
import me.falconseeker.thepit.utils.XSounds;
import me.falconseeker.thepit.utils.XTags;

public class MysticWell
extends Methods
implements CommandExecutor, Listener
{


public MysticWell(ThePit main) {
		super(main);
		// TODO Auto-generated constructor stub
	}


public ArrayList<Player> ingui = new ArrayList<Player>();
private static Inventory inv;

private ThePit main = ThePit.getPlugin(ThePit.class);
private FileConfiguration config = main.guis;
private AnimationStill as;
public Map<UUID, ItemStack> enchanteditem = new HashMap<>();

public void initializeItems(Player p)
{
	  inv = Bukkit.createInventory(p, 45, "Mystic Well");

     ArrayList<String> lore = new ArrayList<String>();
     for (String s : config.getStringList("GUI.MysticWell.Enchanter.Lore")) {
    	 lore.add(ChatColor.translateAlternateColorCodes('&', s));
     }
    defaultItems(XMaterial.BLACK_STAINED_GLASS_PANE);
    inv.setItem(24, main.getMethods().createGuiItem(
    		ChatColor.translateAlternateColorCodes('&', config.getString("GUI.MysticWell.Enchanter.Title")), 
    		lore, 
    		Material.valueOf(config.getString("GUI.MysticWell.Enchanter.Material"))));
  p.openInventory(inv);
  
}


boolean running;
boolean inplayer = true;

public void openInventory(Player p)
{ 
	  initializeItems(p);
	  as = new AnimationStill(inv, XMaterial.GREEN_STAINED_GLASS_PANE);
	  as.runTaskTimer(main, 0L, 5L);
	  running = true;
}

@EventHandler
public void exchange(InventoryOpenEvent e) {
	if (e.getInventory().getName() == "Mystic Well") {
		ingui.add((Player) e.getPlayer());
	}
}
@EventHandler
public void exchanged(InventoryCloseEvent e) {
	
	if (e.getInventory().getName() == "Mystic Well") {
	if (!ingui.contains((Player) e.getPlayer())) return;
		ingui.remove((Player) e.getPlayer());
		if (inv.getItem(20) != null) {
			e.getPlayer().getInventory().addItem(inv.getItem(20));
		}
		as.cancel();
		running = false;
	}
}
@EventHandler
public void onClck(InventoryClickEvent e) {
	if (!ingui.contains((Player) e.getWhoClicked())) return;
	if (e.getCurrentItem() == null) return;
	if (e.getCurrentItem().getType() == null) return; 
	Material mat = e.getCurrentItem().getType();

	if (mat == null || mat != XMaterial.LEATHER_LEGGINGS.parseMaterial() && 
			mat != XMaterial.BOW.parseMaterial() && 
			 mat != XMaterial.GOLDEN_SWORD.parseMaterial()){
		e.setCancelled(true);
		return;
	}
		e.setCancelled(true);
		if (inplayer) {
	     ArrayList<String> lore = new ArrayList<String>();
	     for (String s : config.getStringList("GUI.MysticWell.Enchanter-Enabled.Lore")) {
	    	 if (XTags.getItemTag(e.getCurrentItem(), "Tier") == null) return;
	    	 String t = s.replace("%tier%", (String) XTags.getItemTag(e.getCurrentItem(), "Tier"));
	    	 String a = t.replace("%cost%", String.valueOf(XTags.getItemTag(e.getCurrentItem(), "Cost")));
	    	 lore.add(ChatColor.translateAlternateColorCodes('&', a));
	    	 }
	     ItemStack i = main.getMethods().createGuiItem(
 	    		ChatColor.translateAlternateColorCodes('&', config.getString("GUI.MysticWell.Enchanter-Enabled.Title")), 
 	    		lore, 
 	    		Material.valueOf(config.getString("GUI.MysticWell.Enchanter-Enabled.Material")));
	    
	    ItemStack myi = XTags.setItemTag(i, "true", "Enabled");
	     
	    inv.setItem(24, myi);
		inv.setItem(20, e.getCurrentItem());
		e.getWhoClicked().getInventory().remove(e.getCurrentItem());

		Player p = (Player) e.getWhoClicked();
		p.updateInventory();
		inplayer = false;
		return;
		}
		if (!inplayer) {

		     ArrayList<String> lore = new ArrayList<String>();

		     for (String s : config.getStringList("GUI.MysticWell.Enchanter.Lore")) {
		    	 lore.add(ChatColor.translateAlternateColorCodes('&', s));
		     }
		    inv.setItem(24, main.getMethods().createGuiItem(
		    		ChatColor.translateAlternateColorCodes('&', config.getString("GUI.MysticWell.Enchanter.Title")), 
		    		lore, 
		    		Material.valueOf(config.getString("GUI.MysticWell.Enchanter.Material"))));
				Player p = (Player) e.getWhoClicked();
				e.getWhoClicked().sendMessage(String.valueOf(running));
				p.getInventory().addItem(e.getCurrentItem());
				inv.remove(e.getCurrentItem());
				p.updateInventory();
			    inplayer = true;
		}
	}
@EventHandler
public void onChat(AsyncPlayerChatEvent e) {
	if (e.getMessage().contains("remove holo") || e.getMessage().contains("remove a holo") || e.getMessage().contains("remove the holo")) {
		if (!e.getPlayer().hasPermission("thepit.help")) return;
		e.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "TIP! " + ChatColor.GRAY + "Right click a hologram with bedrock to remove it.");
			}
}
@EventHandler
public void onClick(InventoryClickEvent e) {
	if (!(e.getWhoClicked() instanceof Player)) {
		return;
	}
	String invName = e.getInventory().getName();
	if (!invName.equals("Mystic Well")) {
		return;
	}
	Player p = (Player) e.getWhoClicked();
	ItemStack clickedItem = e.getCurrentItem();
	if (clickedItem == null) {
		return;
	}
	if (!clickedItem.hasItemMeta()) {
		return;
	}
	ItemMeta meta = clickedItem.getItemMeta();
	if (!meta.hasDisplayName()) {
		return;
	}
	e.setCancelled(true);
	if (e.getSlot() == 24) {
		if (e.getClickedInventory().getItem(20) == null) return;
		Material mat = e.getClickedInventory().getItem(20).getType();
		
		if (mat != XMaterial.LEATHER_LEGGINGS.parseMaterial() && 
			mat != XMaterial.BOW.parseMaterial() && 
		    mat != XMaterial.GOLDEN_SWORD.parseMaterial()){
			p.sendMessage("You must insert an item.");
			return;
		}
	if (XTags.getItemTag(e.getCurrentItem(), "Enabled") == null) return;
	if (XTags.getItemTag(e.getCurrentItem(), "Enabled").equals("true")) {
		int cost = (int) XTags.getItemTag(inv.getItem(20), "Cost");
		if (XTags.getItemTag(inv.getItem(20), "Enchantable") == null) return;

		if (XTags.getItemTag(inv.getItem(20), "Enchantable").equals("false")) {
			List<String> l = new ArrayList<>();
			l.add(ChatColor.translateAlternateColorCodes('&', " "));
			l.add(ChatColor.translateAlternateColorCodes('&', "&cThis item is already max level."));

			ItemMeta a = inv.getItem(24).getItemMeta();
			a.setLore(l);
			inv.getItem(24).setItemMeta(a);
			e.getWhoClicked().sendMessage(ChatColor.RED  + "This item has the maximum amount of enchants applied to it."); 
			return; 
		}
		if (main.getMethods().removeGold((Player) e.getWhoClicked(), cost)) {
			as.cancel();
			enchanteditem.put(e.getWhoClicked().getUniqueId(), inv.getItem(20));
			ClickAnimation s = new ClickAnimation(inv, this, e.getWhoClicked().getUniqueId());
			s.runTaskTimer(main, 0L, 3L);
		    inv.setItem(24, main.getMethods().createGuiItem(
		    		ChatColor.translateAlternateColorCodes('&', "&eRolling..."), 
		    		null, 
		    		XMaterial.ENCHANTING_TABLE.parseItem()));
		}
	}
	return;
	}
	
}

public PantEnchantTypes ranEnchant() {
    Random random = new Random();
    return PantEnchantTypes.values()[random.nextInt(PantEnchantTypes.values().length)];
}
@EventHandler
public void onRightCLick(PlayerInteractEvent e) {
	if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
	if (e.getClickedBlock().getType() != Material.ENCHANTMENT_TABLE) return;
	e.setCancelled(true);
	openInventory(e.getPlayer());
}
public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
{
  Player player = (Player)sender;
  if (commandLabel.equalsIgnoreCase("perks")) {
    openInventory(player);
  }
  return false;
}
public void defaultItems(XMaterial mat) {
    inv.setItem(12, main.getMethods().createGuiItem(
     		" ",
     		null, 
     		mat.parseItem()));
     inv.setItem(11, main.getMethods().createGuiItem(
     		" ",
     		null, 
     		mat.parseItem()));
     inv.setItem(10, main.getMethods().createGuiItem(
     		" ",
     		null, 
     		mat.parseItem()));
    inv.setItem(19, main.getMethods().createGuiItem(
    		" ",
    		null, 
     		mat.parseItem()));
    inv.setItem(21, main.getMethods().createGuiItem(
    		" ",
    		null, 
     		mat.parseItem()));
    inv.setItem(28, main.getMethods().createGuiItem(
    		" ",
    		null, 
     		mat.parseItem()));
    inv.setItem(29, main.getMethods().createGuiItem(
    		" ",
    		null, 
     		mat.parseItem()));
    inv.setItem(30, main.getMethods().createGuiItem(
    		" ",
    		null, 
     		mat.parseItem()));
	}
}
