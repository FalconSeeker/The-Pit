package me.falconseeker.thepit.enchants.pants;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.falconseeker.thepit.ThePit;
import me.falconseeker.thepit.utils.ArmorEquipEvent;
import me.falconseeker.thepit.utils.XTags;

public class Booboo implements Listener {

	private ThePit main;

	public Booboo(ThePit main) {
		this.main = main;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	
	ArrayList<Player> cancel = new ArrayList<>();
	
	//Extra event that does not belong in this class
	@EventHandler
	public void on(PlayerItemDamageEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onArmorEquipEvent(ArmorEquipEvent e) {
		if (XTags.getItemTag(e.getNewArmorPiece(), "BOOBOO") == null) {
			return;

		}
		if (XTags.getItemTag(e.getNewArmorPiece(), "BOOBOO").equals("true")) {
			booBoo(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onUnqeup(ArmorEquipEvent e) {

		if (e.getOldArmorPiece() == null) {
	return;
}
		if (XTags.getItemTag(e.getOldArmorPiece()) == null) {
			return;

		}
		if (XTags.getItemTag(e.getOldArmorPiece(), "BOOBOO") == null) return;
		if (XTags.getItemTag(e.getOldArmorPiece(), "BOOBOO").equals("true")) {
			cancel.add(e.getPlayer());
		}
	}
	public void booBoo(Player p) {
		new BukkitRunnable() {
			
			boolean full = false;
			@Override
			public void run() {
				if (cancel.contains(p)) {
					cancel.remove(p);
					cancel();
				}
				p.setHealth(p.getHealth() + 4);
			}
		}.runTaskTimer(main, 20L, 100L);
	}
}
