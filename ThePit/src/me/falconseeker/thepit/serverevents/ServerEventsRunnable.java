package me.falconseeker.thepit.serverevents;

import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import me.falconseeker.thepit.ThePit;

public class ServerEventsRunnable extends BukkitRunnable {
	
	private ThePit main;
	
    public ServerEventsRunnable(ThePit main) {
    	this.main = main;
	}
	
	@Override
	public void run() {
		
		Bukkit.broadcastMessage(randomEvent());
		
	}

	private String randomEvent() {
		List<String> events = main.getConfig().getStringList("Events");
		
	    int pick = new Random().nextInt(events.size());
	    return events.get(pick);
	}
}
