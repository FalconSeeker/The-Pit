package me.falconseeker.thepit.gui.perks;

import java.awt.List;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.falconseeker.thepit.ThePit;
import me.falconseeker.thepit.utils.XMaterial;
import me.falconseeker.thepit.utils.XTags;

public class PerksMainMenu implements Listener{
	  
	  private Inventory inv;
	  
	  private ThePit main;
	  private FileConfiguration config;
	  
	  public PerksMainMenu(ThePit main) {
		  this.main = main;
		  this.config = main.guis;
		  Bukkit.getPluginManager().registerEvents(this, main);
	  }
	  public void initializeItems(Player p)
	  {
		  inv = Bukkit.createInventory(p, 45, "Perks - Select");

	       ArrayList<String> s = new ArrayList<String>();
	       s.add(ChatColor.GRAY + "Select a perk to fill this");
	       s.add(ChatColor.GRAY + "slot");
	       s.add(ChatColor.GRAY + " ");
	       s.add(ChatColor.YELLOW + "Click to choose perk!");

	       Integer slot = 1;
	       
	       for (int i=12; i <= 14; i++, slot++) {
	    	   
	    	   inv.setItem(i, main.getMethods().createGuiItem(ChatColor.GREEN + "Perk Slot #" + String.valueOf(slot), s, new ItemStack(XMaterial.DIAMOND_BLOCK.parseMaterial(), slot)));
	    	   if (main.getMethods().perk1.get(p) != null) {
	    		   if (main.getMethods().perk1.get(p).get(i) != null) {
		    		   p.sendMessage("2");

		    	   inv.setItem(i, main.getMethods().createGuiItem(ChatColor.GREEN + "Perk Slot #"+ String.valueOf(slot), s, new ItemStack(XMaterial.valueOf(main.getMethods().perk1.get(p).get(i)).parseMaterial(), slot)));
	    		   }
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
	    if (!invName.equals("Perks - Select")) {
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
	    if (e.getCurrentItem() != null) {
	    Perks perk = new Perks(main, e.getSlot());
	    if (main.getMethods().dataSlot.get(e.getWhoClicked().getUniqueId()) != null) {
		    main.getMethods().dataSlot.remove(e.getWhoClicked().getUniqueId());
	    }
	    main.getMethods().dataSlot.put(e.getWhoClicked().getUniqueId(), e.getSlot());
	    perk.openInventory(p);
	    }
	 }
}
