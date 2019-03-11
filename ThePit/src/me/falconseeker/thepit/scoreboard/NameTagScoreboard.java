package me.falconseeker.thepit.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.clip.placeholderapi.PlaceholderAPI;
import me.falconseeker.thepit.ThePit;
import me.falconseeker.thepit.packets.PacketUtil1_8_R3;
import me.falconseeker.thepit.packets.TeamAction;

public class NameTagScoreboard implements Listener{
	
	private ThePit main = ThePit.getPlugin(ThePit.class);
	
	@EventHandler
	public void join(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		ScoreboardManager.applyScoreboard(p); 
		main.getPackets().sendTablist(p, main.getMethods().place(main.getConfig().getString("Tablist.Header"), e.getPlayer()), main.getMethods().place(main.getConfig().getString("Tablist.Footer"), p));
		for (Player s : Bukkit.getOnlinePlayers()) {
			main.getPackets().changePlayerName(s, p, main.getMethods().place(main.getConfig().getString("ChatFormat.Nametag"), e.getPlayer()), "&e", TeamAction.CREATE);				
		}
		new BukkitRunnable() {
			
			@Override 
			public void run() {
				ScoreboardManager.applyScoreboard(p); 
				for (Player p : Bukkit.getOnlinePlayers()) {
					
					main.getPackets().changePlayerName(p, e.getPlayer(), checkName(e.getPlayer()), "&e", TeamAction.CREATE);				
				}

			}
		}.runTaskTimer(main, 0L, main.getConfig().getLong("Scoreboard.RefreshRate")*20L);
}
	
	public String checkName(Player p) {
		for (String s : main.getConfig().getConfigurationSection("Tags").getKeys(false)) {
			if (p.hasPermission(s)) {
				return ChatColor.translateAlternateColorCodes('&', s);
			}
		}
		return ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("ChatFormat.Nametag"));
	}
	@EventHandler
	public void leave(PlayerQuitEvent e) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			main.getPackets().changePlayerName(p, e.getPlayer(), main.getMethods().place(main.getConfig().getString("ChatFormat.Nametag"), e.getPlayer()), "&e", TeamAction.DESTROY);				
		}
	}
}
