package me.falconseeker.thepit.utils;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.util.Vector;

import me.falconseeker.thepit.ThePit;

public class VillagerSpawn {

	private ThePit main;
	private Methods methods;
	private Villager v;
	
	public VillagerSpawn(ThePit main) {
		this.main = main;
	}
	
	public Villager getVillager() {
		return this.v;
	}
	
	public void spawnVillager(String topholo, String bottomholo, Location loc, Player p, String tag)  {
        Villager v = (Villager) loc.getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
        
        v.setCustomName(tag);
        v.setCustomNameVisible(false);
        v.setCanPickupItems(false);
        v.setRemoveWhenFarAway(false);
        v.setAdult();
        v.teleport(loc);
      	
        try {
			main.getPackets().noAI(v, loc);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        methods.createHolo(topholo, v.getLocation().add(new Vector(0.0, 1, 0.0)), true);
        methods.createHolo(bottomholo, v.getLocation().add(new Vector(0.0, 0.8, 0.0)), true);
	}
}
