package me.falconseeker.thepit.enchants.pants;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import me.falconseeker.thepit.ThePit;

public class Cricket implements Listener {

	public Cricket(ThePit main) {
		Bukkit.getPluginManager().registerEvents(this, main);	
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player)) return;
		Player p = (Player) e.getEntity();
		Entity k = e.getDamager();
		if (!PantsEnchants.check(p.getInventory().getLeggings(), "CRICKET")) return;
		
		if (p.getLocation().subtract(0, -2, 0).getBlock().getType() != Material.GRASS 
				&& p.getLocation().subtract(0, -3, 0).getBlock().getType() != Material.GRASS 
				&& k.getLocation().subtract(0, -2, 0).getBlock().getType() != Material.GRASS
				&& k.getLocation().subtract(0, -3, 0).getBlock().getType() != Material.GRASS) return;
		
		e.setDamage(e.getDamage() - (0.07*e.getDamage()));
	}

}
