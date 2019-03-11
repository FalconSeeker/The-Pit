package me.falconseeker.thepit.enchants.pants;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.falconseeker.thepit.ThePit;
import me.falconseeker.thepit.utils.ArmorEquipEvent;

public class DangerClose implements Listener {

	
	//COMPLETED
	
	public DangerClose(ThePit main) {
		Bukkit.getPluginManager().registerEvents(this, main);
	}

	@EventHandler
	public void healthBar(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) return;
		
		Player p = (Player) e.getEntity();
		if (!PantsEnchants.check(p.getInventory().getLeggings(), "DANGERCLOSE")) {
			return;
		}
		if (p.getHealth() <= 8) {
		    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*10, 2));
		}
    }
	
}
