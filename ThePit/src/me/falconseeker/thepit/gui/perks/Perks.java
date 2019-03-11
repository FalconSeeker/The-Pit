package me.falconseeker.thepit.gui.perks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.falconseeker.thepit.ThePit;
import me.falconseeker.thepit.utils.Methods;
import me.falconseeker.thepit.utils.XMaterial;
import me.falconseeker.thepit.utils.XSounds;
import me.falconseeker.thepit.utils.XTags;

public class Perks
  implements Listener, CommandExecutor
{
  ArrayList<Player> vampire = new ArrayList<Player>();
  ArrayList<Player> goldenhead = new ArrayList<Player>();
  ArrayList<Player> extrahp = new ArrayList<Player>();
  private static Inventory inv;
  ArrayList<Player> strength = new ArrayList<Player>();
  ArrayList<Player> goldpickup = new ArrayList<Player>();
  ArrayList<Player> regen = new ArrayList<Player>();
  ArrayList<Player> lava = new ArrayList<Player>();
 
  private ThePit main;
  private FileConfiguration config;
  private Integer slot;
  
  public Perks(ThePit main) {
	  this.main = main;
	  this.config = main.guis;
  }
  public Perks(ThePit main, int i) {
	  this.slot = i;
	  this.main = main;
	  this.config = main.guis;
  }
  
  public void initializeItems(Player p)
  {
	  inv = Bukkit.createInventory(p, 27, "Perks");

	  if (config.getStringList("GUI.Perks.") == null) return; 
	  
	  for (String key : config.getConfigurationSection("GUI.Perks").getKeys(false)) {
		     ArrayList<String> lore = new ArrayList<String>();
		     for (String s : config.getStringList("GUI.Perks." + key + ".Lore")) {
		    	 lore.add(ChatColor.translateAlternateColorCodes('&', s));
		     }
		  ItemStack i = main.getMethods().createGuiItem(main.getMethods().place(config.getString("GUI.Perks." + key + ".Name"), p), 
				  lore, Material.valueOf(config.getString("GUI.Perks." + key + ".Material")));
		  
		  inv.setItem(config.getInt("GUI.Perks." + key + ".Slot"), XTags.setItemTag(i, config.getString("GUI.Perks." + key + ".Perk-Type"), "Type"));
		  if (!p.hasPermission("GUI.Perks." + key + ".Permission")) {
			  inv.setItem(config.getInt("GUI.Perks." + key + ".Slot"), main.getMethods().createGuiItem("a", new ArrayList<String>(), Material.DIRT));
		  }
	  }
    p.openInventory(inv);
  }
  
  
  public void openInventory(Player p)
  {
	initializeItems(p);
  }
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent e)
  {
    if (!(e.getWhoClicked() instanceof Player)) {
      return;
    }
    String invName = e.getInventory().getName();
    if (!invName.equals("Perks")) {
      return;
    }
    e.setCancelled(true);
    
    Player p = (Player)e.getWhoClicked();
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
    
    switch((String) XTags.getItemTag(clickedItem, "Type")) {
    case "Go Back":
    	p.closeInventory();
    	main.getPerks().openInventory(p);
    	return;
    case "Fishing Rod":
        remove(p);
        vampire.add(p);
        p.sendMessage( ChatColor.GREEN + "You equipped the vampire perk");
        return;
    case "Golden Head":
        HashMap<Integer, String> data = new HashMap<Integer, String>();
        data.put(slot, "GOLDEN_APPLE");
        remove(p);
        goldenhead.add(p);
        p.sendMessage( ChatColor.GREEN + "You equipped GoldenHeads perkttt");

        main.getMethods().perk1.put(p, data);
        return;
    case "Lava Bucket":
        remove(p);
        lava.add(p);
        HashMap<Integer, String> data2 = new HashMap<Integer, String>();
        data2.put(main.getMethods().dataSlot.get(p.getUniqueId()), "LAVA_BUCKET");
        main.getMethods().perk1.put(p, data2);
        p.sendMessage( ChatColor.GREEN + "You equipped lavabucket perk");

        return;
    case "Extra HP + 4":
        p.sendMessage( ChatColor.GREEN + "You equipped firestorm animation");
        remove(p);
        extrahp.add(p);
        p.setHealth(28);
        return; 
    case "LOCKED!":
		p.sendMessage( ChatColor.RED + "This perk is locked! Please upgrade your level to gain access.");
		p.playSound(p.getLocation(), XSounds.ENDERMAN_HIT.parseSound(), 1, 1);
		p.closeInventory();
		return;
    default:
		p.sendMessage( ChatColor.RED + "PERK NOT FOUND! ERROR");
    	break;
    }
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
  {
    Player player = (Player)sender;
    if (commandLabel.equalsIgnoreCase("perks")) {
      openInventory(player);
    }
    return false;
  }
  //Vampire
  
  @EventHandler
  public void onHit(EntityDamageByEntityEvent e) {
	  Entity d = e.getDamager();
	  if (!(d instanceof Player)) {
		  return;
	  }
	  Player p = (Player) e.getDamager();
	  if (vampire.contains(p)) {
		  p.setHealth(1 + p.getHealth());
	  }
  }
  public void remove(Player p) {
	  vampire.remove(p);
	  goldenhead.remove(p);
	  extrahp.remove(p);
	  strength.remove(p);
	  goldpickup.remove(p);
	  regen.remove(p);
  }
  public void giveItems(Player p) {
	  if (goldenhead.contains(p)) {
		  return;
	  }
	  if (vampire.contains(p)) {
		  p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1, 100000));
	  }
	  if (lava.contains(p)) {
		  p.getInventory().addItem(new ItemStack(Material.LAVA_BUCKET));
		  p.getInventory().addItem(new ItemStack(Material.OBSIDIAN, 4));
		  p.updateInventory();

		  return;
	  }
	  else return;
  }
  private void giveStandardItems(Player p) {
	  
	  p.getInventory().setLeggings(XMaterial.CHAINMAIL_LEGGINGS.parseItem());
	  p.getInventory().setChestplate(XMaterial.CHAINMAIL_CHESTPLATE.parseItem());
	  p.getInventory().setBoots(XMaterial.CHAINMAIL_BOOTS.parseItem());
	  p.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD));
	  p.getInventory().setItem(1, new ItemStack(Material.BOW));
	  p.getInventory().setItem(2, new ItemStack(Material.ARROW, 32));
	  
	  Random ran = new Random();
	  
	  Integer o = ran.nextInt(3);
	  
	  switch(o) {
	  case 1:
		  p.getInventory().setLeggings(XMaterial.IRON_LEGGINGS.parseItem());
		  break;
	  case 2:
		  p.getInventory().setChestplate(XMaterial.IRON_CHESTPLATE.parseItem());
		  break;
	  case 3:
		  p.getInventory().setBoots(XMaterial.IRON_BOOTS.parseItem());
		  break;
	  }
	  giveItems(p);
	  p.updateInventory();

  }
  @EventHandler
  public void on(PlayerDeathEvent e) {
	  new BukkitRunnable() {
		
		@Override
		public void run() {
			if (e.getEntity().getInventory().contains(Material.IRON_SWORD)){
				return;
			}
			  giveStandardItems(e.getEntity().getPlayer());
			
		}
	}.runTaskLater(main, 20L);
  }
  @EventHandler
  public void one(PlayerJoinEvent e) {
	  
	  e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 180000000, 2, false, false));
	  new BukkitRunnable() {
		
		@Override
		public void run() {
			  giveStandardItems(e.getPlayer());
			
		}
	}.runTaskLater(main, 20L);
  }
}
