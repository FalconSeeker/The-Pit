package me.falconseeker.thepit.enchants.pants;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.falconseeker.thepit.ThePit;
import me.falconseeker.thepit.utils.ArmorEquipEvent;

public class Hearts implements Listener {

	
	//COMPLETED 
	
	public Hearts(ThePit main) {
		Bukkit.getPluginManager().registerEvents(this, main);
	}

	@EventHandler
	public void onEquip(ArmorEquipEvent e) {
		if (e.getNewArmorPiece() == null) return;
		if (!PantsEnchants.check(e.getNewArmorPiece(), "HEARTS")) return;
		e.getPlayer().setMaxHealth(e.getPlayer().getMaxHealth() + 2);
	}
	@EventHandler
	public void onUnequip(ArmorEquipEvent e) {
		if (e.getOldArmorPiece() == null) return;
		if (!PantsEnchants.check(e.getOldArmorPiece(), "HEARTS")) return;
		e.getPlayer().setMaxHealth(20);
	}
}
