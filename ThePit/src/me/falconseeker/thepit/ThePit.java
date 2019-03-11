package me.falconseeker.thepit;

import me.falconseeker.commands.CommandModule;
import me.falconseeker.commands.TabComplete;
import me.falconseeker.deaths.InstantDeath;
import me.falconseeker.econ.Econ;
import me.falconseeker.extraevents.CombatLog;
import me.falconseeker.extraevents.GoldenHeads;
import me.falconseeker.extraevents.HealthBar;
import me.falconseeker.extraevents.Launchpads;
import me.falconseeker.extraevents.PlaceEvents;
import me.falconseeker.extraevents.PlayerDamage;
import me.falconseeker.extraevents.PlayerVault;
import me.falconseeker.extraevents.Streaks;
import me.falconseeker.extraevents.Villagers;
import me.falconseeker.thepit.commands.subcommands.RegisterCommands;
import me.falconseeker.thepit.enchants.pants.PantsEnchants;
import me.falconseeker.thepit.gui.perks.Perks;
import me.falconseeker.thepit.gui.perks.PerksMainMenu;
import me.falconseeker.thepit.gui.shop.Shop;
import me.falconseeker.thepit.packets.PacketUtil;
import me.falconseeker.thepit.scoreboard.ScoreboardManager;
import me.falconseeker.thepit.serverevents.ServerEventsRunnable;
import me.falconseeker.thepit.utils.ArmorListener;
import me.falconseeker.thepit.utils.ExtraConfigs;
import me.falconseeker.thepit.utils.Messages;
import me.falconseeker.thepit.utils.Methods;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ThePit extends JavaPlugin {

	//TODO: Add getCommand("thepit");
	
	 public static String v = null;
	 private Econ econ;
	 private Methods methods;
	 private Messages messages;
	 private PacketUtil packets;
	 private Streaks streaks;
	 private PerksMainMenu perks;
	 private Shop shop;
	 //GUI customization config
	 public ExtraConfigs guis = new ExtraConfigs(this, "guis.yml", "guis.yml");
	 //Data gui for vaults
	 public ExtraConfigs vaults = new ExtraConfigs(this, "vaults.yml", "vaults.yml");
     public static HashMap<String, CommandModule> commands;
	 
	 private PluginManager pm = Bukkit.getPluginManager();
	 private ConsoleCommandSender sender = Bukkit.getConsoleSender();
	 
	@Override
	public void onEnable() {	
	    v = Bukkit.getServer().getClass().getPackage().getName();
	    v = v.substring(v.lastIndexOf(".") + 1);
	    
        if (pm.isPluginEnabled("PlaceholderAPI")) {
			new Placeholder(this).register();
			sender.sendMessage(ChatColor.GREEN + "PlaceholderAPI found, working with it.");
		}
		else {
			sender.sendMessage(ChatColor.RED + "PlaceholderAPI not found, working witwhout it.");
		}
        
        registerEvents();
        disableEntities();
        		
        new RegisterCommands(this);
        
		getCommand("thepit").setTabCompleter(new TabComplete());

		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		
		startEvents();
	}
	
	@Override
	public void onDisable() {
	}
	
	//INSTANCES ---
	public Methods getMethods() {
		return this.methods;
	}
	public Econ getEcon(){
		  return this.econ;
	}
	public Messages getMessages() {
		return this.messages;
	}
	public PacketUtil getPackets() {
		return this.packets;
	}
	public Streaks getStreaks() {
		return this.streaks;
	}
	public PerksMainMenu getPerks() {
		return this.perks;
	}
	public Shop getShop() {
		return this.shop;
	}
	//MAIN METHODS
	private void disableEntities() {
		//Disabling villager movement
		for (World world : Bukkit.getWorlds()) {
			for (Entity en : world.getEntities()) {
				if (en instanceof Villager) {
					try {
						packets.noAI(en, en.getLocation());
					} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
							| SecurityException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private void registerEvents() {
		ScoreboardManager man = new ScoreboardManager();

        pm.registerEvents(new ArmorListener(getConfig().getStringList("blocked")), this);
		pm.registerEvents(new Perks(this), this);
		this.econ = new Econ(this);
		this.methods = new Methods(this);
		this.messages = new Messages(this);
		this.packets = new PacketUtil(this);
		this.streaks = new Streaks(this);
		this.perks = new PerksMainMenu(this);
		this.shop = new Shop(this);
		
		new CombatLog(this);
		new GoldenHeads(this);
		new PlaceEvents(this);
		new InstantDeath(this);
		new PlayerVault(this);
		new Launchpads(this);
		new HealthBar(this);
		new PantsEnchants(this);
		new PlayerDamage(this);
		new Villagers(this);
		sender.sendMessage(ChatColor.GREEN + "Events Registered");
	}
	private void startEvents() {
		ServerEventsRunnable r = new ServerEventsRunnable(this);
		r.runTaskTimer(this, 100L, 100L);
	}
}