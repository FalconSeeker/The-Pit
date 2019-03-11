package me.falconseeker.thepit.gui.perks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import me.falconseeker.thepit.ThePit;

public class GoldenHeads extends PerkTypes {

	private Perks perks;
	
	public GoldenHeads(ThePit main, Perks perks) {
		this.perks = perks;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	  @EventHandler
	  public void onDeath(EntityDeathEvent e) {
		  Player k = e.getEntity().getKiller();
		  if (!(e.getEntity().getKiller() instanceof Player)) {
			  return;
		  }
		  if (perks.goldenhead.contains(k)) {
			  if (k.getInventory().contains(Material.SKULL_ITEM)) {
				  return;
			  }
		      k.sendMessage(ChatColor.GREEN + "You have received a golden head! Right Click to use it!");
		      ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short)SkullType.PLAYER.ordinal());
		      
		      SkullMeta meta1 = (SkullMeta)skull.getItemMeta();
		      meta1.setOwner("FireTamer97");
		      meta1.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Golden Head" + ChatColor.GRAY + " (Right Click)");
		      skull.setItemMeta(meta1);
		      
		      k.getInventory().addItem(new ItemStack(skull));
		      k.updateInventory();
		      }
			  
	  }

}
