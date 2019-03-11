package me.falconseeker.extraevents;

import org.apache.commons.lang.math.IntRange;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.falconseeker.thepit.ThePit;
import net.md_5.bungee.api.ChatColor;

public class PlayerDamage implements Listener, CommandExecutor {
	
	private ThePit main;
	
	public PlayerDamage(ThePit main) {
		this.main = main;
		Bukkit.getPluginManager().registerEvents(this, main);
		main.getCommand("protect").setExecutor(this);
	}
	
	Location temp1;
	Location temp2;
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (e.getPlayer().getItemInHand().getType() != Material.GOLD_AXE) return;
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			e.setCancelled(true);
			temp1 = e.getClickedBlock().getLocation();
			e.getPlayer().sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "POSITION " + ChatColor.GRAY + "position 1 set.");
			return;
		}
		if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
			e.setCancelled(true);
			temp2 = e.getClickedBlock().getLocation();
			e.getPlayer().sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "POSITION " + ChatColor.GRAY + "position 2 set. Type " + ChatColor.YELLOW +"/protect" + ChatColor.GRAY + " to enable it.");
		}
	}
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		
		if (main.getConfig().get("Locations.Protection.1") == null || main.getConfig().get("Locations.Protection.2") == null) return;
		Location loc1 = (Location) main.getConfig().get("Locations.Protection.1");
		Location loc2 = (Location) main.getConfig().get("Locations.Protection.2");
	    if (inRegion(e.getEntity().getLocation(), loc1, loc2)) {
	    	e.setCancelled(true);
	    }
	}
	@EventHandler
	public void ONbREAK(BlockBreakEvent e) {
		if (main.getConfig().get("Locations.Protection.1") == null || main.getConfig().get("Locations.Protection.2") == null) return;
		Location loc1 = (Location) main.getConfig().get("Locations.Protection.1");
		Location loc2 = (Location) main.getConfig().get("Locations.Protection.2");
		if (inRegion(e.getPlayer().getLocation(), loc1, loc2)) {
	    	e.setCancelled(true);
	    }
	}
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (main.getConfig().get("Locations.Protection.1") == null || main.getConfig().get("Locations.Protection.2") == null) return;
		Location loc1 = (Location) main.getConfig().get("Locations.Protection.1");
		Location loc2 = (Location) main.getConfig().get("Locations.Protection.2");
	    if (inRegion(e.getPlayer().getLocation(), loc1, loc2)) {
	    	e.setCancelled(true);
	    }
	}
    public boolean inRegion(Location origin, Location l1, Location l2){
        return new IntRange(l1.getX(), l2.getX()).containsDouble(origin.getX())
                && new IntRange(l1.getY(), l2.getY()).containsDouble(origin.getY())
                &&  new IntRange(l1.getZ(), l2.getZ()).containsDouble(origin.getZ());
    }
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String com, String[] args) {
		if (!(sender instanceof Player)) return true;
		if (cmd.getLabel().equalsIgnoreCase("protect")) {
			if (temp1 == null) {
				sender.sendMessage("NO LOCATION 1. Right click a block with a golden axe to set location 1.");
				return true;
			}
			if (temp2 == null) {
				sender.sendMessage("NO LOCATION 1. Left click a block with a golden axe to set location 2.");
				return true;
			}
			main.getConfig().set("Locations.Protection.1", temp1);
			main.getConfig().set("Locations.Protection.2", temp2);
			main.saveDefaultConfig();
			sender.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "POSITION " + ChatColor.GRAY + "spawn area set.");
			return true;
		}
		return false;
	}
}
