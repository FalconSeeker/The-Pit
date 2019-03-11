package me.falconseeker.thepit.enchants.pants;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.falconseeker.thepit.ThePit;
import me.falconseeker.thepit.utils.ArmorEquipEvent;
import net.md_5.bungee.api.ChatColor;

public class DoubleJump implements Listener {
	   
	private ArrayList<UUID> canFly = new ArrayList<UUID>();
	private ThePit main;
	
	public DoubleJump(ThePit main) {
		Bukkit.getPluginManager().registerEvents(this, main);
		this.main = main;
	}
  
    @EventHandler
    public void onEquip(ArmorEquipEvent e) {
        Player player = e.getPlayer();
        if (!PantsEnchants.check(e.getNewArmorPiece(), "DOUBLEJUMP")) return;   
        canFly.add(player.getUniqueId());
        player.setAllowFlight(true);

    }
    
    @EventHandler
    public void onUnequip(ArmorEquipEvent e) {
        Player player = e.getPlayer();
        
        if (!PantsEnchants.check(e.getOldArmorPiece(), "DOUBLEJUMP")) return;       
        canFly.remove(player.getUniqueId());
    }
    
    @EventHandler
    public void setFlyOnJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        
        if(event.isFlying() && event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        if (!PantsEnchants.check(player.getInventory().getLeggings(), "DOUBLEJUMP")) return;  
        if (!canFly.contains(player.getUniqueId())) return;
            player.setFlying(false);
            event.setCancelled(true);
            Vector jump = player.getLocation().getDirection().multiply(0.2).setY(1.1);
            player.setVelocity(player.getVelocity().add(jump));
	        player.setAllowFlight(false);
            canFly.remove(player.getUniqueId());
            new BukkitRunnable() {
				
				@Override
				public void run() {
			        if (!PantsEnchants.check(player.getInventory().getLeggings(), "DOUBLEJUMP")) {
			        	cancel();
			        	return;       
			        }

			        player.setAllowFlight(true);
					canFly.add(player.getUniqueId());
				}
			}.runTaskLater(main, 20*20);
        }
}