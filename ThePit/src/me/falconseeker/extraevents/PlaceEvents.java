package me.falconseeker.extraevents;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.falconseeker.thepit.ThePit;


public class PlaceEvents implements Listener {

	private ThePit main;

	public PlaceEvents(ThePit main) {
		this.main = main;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	@EventHandler
	public void obsidian(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		 p.getLocation().getWorld().spigot().playEffect(p.getLocation(), Effect.EXPLOSION, 1, 0, 0, 0, 0, 0, 10, 3);	

		if (!(e.getBlock().getType() == Material.OBSIDIAN || e.getBlock().getType() == Material.COBBLESTONE)) {
			return;
		}
		new BukkitRunnable() {
			
			@Override
			public void run() {
			Block b = e.getBlock();
				
			b.setType(Material.AIR);
			
			}
		}.runTaskLater(main, 120L);
	}
	@EventHandler
	public void lava(PlayerBucketEmptyEvent e) {
		if (!(e.getBucket() == Material.LAVA_BUCKET)) 
			return;
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Location b = e.getBlockClicked().getLocation().add(0D, 1D, 0D);
				b.getBlock().setType(Material.AIR);
				
			}
		}.runTaskLater(main, 120L);
	}
	
	@EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
      int id = event.getBlock().getTypeId();
      if(id == 9 || id == 8) {
        event.setCancelled(true);
      }
      if(id == 11 || id == 10) {
          event.setCancelled(true);
        }
    }
}
