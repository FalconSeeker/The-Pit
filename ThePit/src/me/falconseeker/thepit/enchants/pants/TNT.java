package me.falconseeker.thepit.enchants.pants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import me.falconseeker.thepit.ThePit;
import me.falconseeker.thepit.utils.XMaterial;
import net.md_5.bungee.api.ChatColor;

public class TNT implements Listener {

	
	//COMPLETED 
	
	private HashMap<Location,Entity> primedTntHash = new HashMap<Location,Entity>();
	
	public TNT(ThePit main) {
		Bukkit.getPluginManager().registerEvents(this, main);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockPlace(BlockPlaceEvent e) {
		
		if (e.getBlockPlaced().getType() == null) return;
		if (e.getBlock().getType() != XMaterial.TNT.parseMaterial()) return;
		if (!PantsEnchants.check(e.getPlayer().getInventory().getLeggings(), "TNT")) return;

		TNTPrimed tnt = (TNTPrimed) e.getPlayer().getWorld().spawnEntity(e.getBlockPlaced().getLocation().add(.5, 0, .5), EntityType.PRIMED_TNT);
		tnt.setFuseTicks(30); //1.5 seconds
		tnt.setYield(2.5F);
	    primedTntHash.put(tnt.getLocation(), tnt);
	    
		e.getBlockPlaced().setType(Material.AIR);
		e.setCancelled(true);
	}
	@EventHandler
	public void onExplode(EntityExplodeEvent e) {
      e.blockList().clear();
	}

	@EventHandler
	public void respawn(PlayerRespawnEvent e) {
		if (!PantsEnchants.check(e.getPlayer().getInventory().getLeggings(), "TNT")) return;
		
		ItemStack i = new ItemStack(Material.TNT, 2);
		List<String> l = new ArrayList<String>();
		l.add(ChatColor.RED + "TNT" + ChatColor.GRAY + " (Place Block)");
		l.add(" ");
		l.add(ChatColor.GRAY + "Does damage to players in 2 block radius");
		i.getItemMeta().setLore(l);
		e.getPlayer().getInventory().addItem(i);
			
		
	}
}

