package me.falconseeker.thepit.levels;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import me.clip.placeholderapi.PlaceholderAPI;
import me.falconseeker.thepit.ThePit;
import me.falconseeker.thepit.utils.XSounds;

public class Level implements Listener {

	private ThePit main = ThePit.getPlugin(ThePit.class);
	private FileConfiguration config = main.getConfig();

	@EventHandler
	public void onChange(PlayerLevelChangeEvent e) {
		Player p = e.getPlayer();
		main.getPackets().sendTitle(p, 1, 60, 1, main.getMethods().place(config.getString("Titles.Levelup-Title"), p), main.getMethods().place(config.getString("Titles.Levelup-Subtitle"), p));
		main.getMethods().changePrestige(p);
		p.playSound(p.getLocation(), XSounds.LEVEL_UP.parseSound(), 10f, 1f);
		p.sendMessage(main.getMethods().place(config.getString("Messages.Levelup"), p));
	}
	@EventHandler
	public void onEntityDamage(EntityDeathEvent e) {
		if (!(e.getEntity().getKiller() instanceof Player)) return;
		Player k = e.getEntity().getKiller();
		float xp = (16 % 100) / k.getExp();
		k.setExp(k.getExp() + xp);
	}
	@EventHandler
	public void chatFormat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (!config.getBoolean("ChatFormat.Enabled")) {
			return;
		}
		e.setFormat(main.getMethods().place(config.getString("ChatFormat.Format"), p).replace("%message%", e.getMessage()));
	}
	
}
