package me.falconseeker.thepit.enchants.pants;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.falconseeker.thepit.ThePit;
import me.falconseeker.thepit.utils.ArmorEquipEvent;
import me.falconseeker.thepit.utils.XTags;

public class Creative implements Listener {

	private ThePit main;

	public Creative(ThePit main) {
		this.main = main;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	@EventHandler
	public void onArmorEquipEvent(PlayerRespawnEvent e) {
		if (!PantsEnchants.check(e.getPlayer().getInventory().getLeggings(), "CREATIVE"))
			new BukkitRunnable() {
				
				@Override
				public void run() {
					e.getPlayer().getInventory().addItem(new ItemStack(Material.WOOD, 16));
					e.getPlayer().updateInventory();
				}
			}.runTaskLater(main, 10L);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if (!(e.getEntity().getKiller() instanceof Player)) return;
			if (!(e.getEntity() instanceof Player)) return;
			Player p = (Player) e.getEntity();
			Player k = p.getKiller();
				if (!PantsEnchants.check(p.getInventory().getLeggings(), "CREATIVE")) return;
					k.getInventory().addItem(new ItemStack(Material.WOOD, 6));
					k.updateInventory();
			}	
}
