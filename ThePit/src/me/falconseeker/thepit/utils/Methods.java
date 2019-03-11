package me.falconseeker.thepit.utils;

import java.awt.Event;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.clip.placeholderapi.PlaceholderAPI;
import me.falconseeker.thepit.ThePit;
import me.falconseeker.thepit.enchants.enchants.PantEnchantTypes;

public class Methods {

	private ThePit main = ThePit.getPlugin(ThePit.class);
	private FileConfiguration config;
	public ArrayList<Player> combatlog = new ArrayList<Player>();
	private Messages messages;
	public ExtraConfigs data = new ExtraConfigs(main, "dataa.yml");
	private String b = ("playerData");
    public boolean event;
    public HashMap<Player, HashMap<Integer, String>> perk1 = new HashMap<Player, HashMap<Integer, String>>();
    public HashMap<UUID, Integer> dataSlot = new HashMap<UUID, Integer>();

	public Methods(ThePit main){
	  this.main = main;
	  this.messages = main.getMessages();
	  this.config = main.getConfig();
	}
	 /*
	 * @param  p  the player to check their combat tag 
	 */
	public String getCombat(Player p) {
		if (event == true) {
			return config.getString("Messages.EventLog");
		}
		if (combatlog.contains(p)) {
			return config.getString("Messages.Fighting");
		}
		return config.getString("Messages.Idle");		
	}
	 /**
	 * Changes the prestige of a player (usually when leveling up 
	 * @param  url  an absolute URL giving the base location of the image
	 * @param  name the location of the image, relative to the url argument
	 * @return      the image at the specified URL
	 * @see         Image
	 */
	public void changePrestige(Player p) {
		if (p.getLevel() >= 100) {
			config.set("Data." + p.getName(), config.getInt("Data." + p.getName()) + 1);
			p.sendMessage(config.getString("Messages.PrestigeReset"));
			p.setLevel(0);
			main.saveDefaultConfig();
		}
		else return;
	}
	public String levelColor(Player p) { //TODO: EDIT ALL THE IF STATEMENTS - LOOKS CLUTTERED
		String str = null;
		String level = String.valueOf(p.getLevel());
		int levels = p.getLevel();
		if (levels >= 0 && levels < 10) {
			return str = (ChatColor.GRAY + level);
		}
		if (levels >= 10 && levels < 20) {
			return str = (ChatColor.RED + level);
		}
		if (levels >= 20 && levels < 30) {
			return str = (ChatColor.AQUA + level);
		}
		if (levels >= 30 && levels < 40) {
			return str = (ChatColor.DARK_AQUA + level);
		}
		if (levels >= 40 && levels < 50) {
			return str = (ChatColor.LIGHT_PURPLE + level);
		}
		if (levels >= 50 && levels < 60) {
			return str = (ChatColor.GREEN + level);
		}
		if (levels >= 60) {
			return str = (ChatColor.WHITE + level); 
		}
		return str;
	}
	public int getPrestige(Player p) {
		if (config.get("Data." + p.getName()) == null) {
			return 0;
		}
		int check = config.getInt("Data." + p.getName());
		switch(check) {
		case 1:
			return 1;
		case 2:
			return 2;
		case 3:
			return 3;
		default:
			return 4;
		}
	}
	public String getChatLevel(Player p) {
		if (getPrestige(p) >= 4) {
			return (ChatColor.AQUA + "[" + ChatColor.GRAY + levelColor(p) + ChatColor.AQUA + "]");
		}
		switch(getPrestige(p)) {
		case 1:
			return (ChatColor.BLUE + "[" + ChatColor.GRAY + levelColor(p) + ChatColor.BLUE + "]");
		case 2:
			return (ChatColor.WHITE + "[" + ChatColor.GRAY + levelColor(p) + ChatColor.WHITE + "]");
		case 3:
			return (ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + levelColor(p) + ChatColor.DARK_GRAY + "]");
		default:
			return (ChatColor.GRAY + "[" + ChatColor.GRAY + levelColor(p) + ChatColor.GRAY + "]");
		}
	}
	public ItemStack createGuiItem(String name, ArrayList<String> desc, Material material) {
		ItemStack i = new ItemStack(material, 1);
		ItemMeta iMeta = i.getItemMeta();
		iMeta.setDisplayName(name);
		iMeta.setLore(desc);
		i.setItemMeta(iMeta);	
		return i;
	}
	public ItemStack createGuiItem(String name, ArrayList<String> s, ItemStack itemStack) {
		ItemStack i = itemStack;
		ItemMeta iMeta = i.getItemMeta();
		iMeta.setDisplayName(name);
		iMeta.setLore(s);
		i.setItemMeta(iMeta);	
		return i;		
	}
	@SuppressWarnings("deprecation")
	public String place(String string, Player p) {
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceHolderAPI")) {
			return PlaceholderAPI.setPlaceholders(p, string);
		}
	    Date now = new Date();
	    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	    String date = format.format(now);
		return ChatColor.translateAlternateColorCodes('&', string
				.replace("%thepit_name%", p.getName())
				.replace("%thepit_level%", getChatLevel(p))
				.replace("%thepit_stat%", getCombat(p))
				.replace("%thepit_date%", date)
				.replace("%thepit_xp%", String.valueOf(p.getExpToLevel()))
				.replace("%thepit_gold%", String.valueOf(getGold(p))));
	}

	public Collection<? extends String> place(List<String> list, Player p) {
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceHolderAPI")) {
			return PlaceholderAPI.setPlaceholders(p, list);
		}
		List<String> strings = new ArrayList<>();
	    Date now = new Date();
	    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	    String date = format.format(now);
	    for (String string : list) {
		strings.add(placeholderWithout(string, p));
	    }
	    return strings;
	}
	private String placeholderWithout(String string, Player p) {
	    Date now = new Date();
	    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	    String date = format.format(now);
		return ChatColor.translateAlternateColorCodes('&', string
				.replace("%thepit_name%", p.getName())
				.replace("%thepit_level%", getChatLevel(p))
				.replace("%thepit_stat%", getCombat(p))
				.replace("%thepit_date%", date)
				.replace("%thepit_xp%", String.valueOf(p.getExpToLevel()))
				.replace("%thepit_gold%", String.valueOf(getGold(p))));	
		}
	public void createHolo(String nextTop, Integer nextTopKills, Location location) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				ArmorStand as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND); //Spawn the ArmorStand

				as.setGravity(false); //Make sure it doesn't fall
				as.setCanPickupItems(false); //I'm not sure what happens if you leave this as it is, but you might as well disable it
				as.setCustomName(nextTop + " : " + String.valueOf(nextTopKills)); //Set this to the text you want
				as.setCustomNameVisible(true); //This makes the text appear no matter if your looking at the entity or not
				as.setVisible(false); //Makes the ArmorStand invisible					
			}
		}.runTaskLater(main, 20L);
		}
	public void createHolo(String text, Location location, Boolean boo) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				ArmorStand as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND); //Spawn the ArmorStand

				as.setGravity(false); //Make sure it doesn't fall
				as.setCanPickupItems(false); //I'm not sure what happens if you leave this as it is, but you might as well disable it
				as.setCustomName(ChatColor.translateAlternateColorCodes('&', text)); //Set this to the text you want
				as.setCustomNameVisible(true); //This makes the text appear no matter if your looking at the entity or not
				as.setVisible(false); //Makes the ArmorStand invisible	
				if (boo) {
					as.setSmall(true);
				}
			}
		}.runTaskLater(main, 1L);
		}

		private String c(String s) {
		// TODO Auto-generated method stub
		return ChatColor.translateAlternateColorCodes('&', s);
	}

		
		private List<String> c(List<String> stringList) {
			List<String> l = new ArrayList<>();
			for (String s  : stringList) {
				l.add(ChatColor.translateAlternateColorCodes('&', s));
			}
			return l;
	}

		public ItemStack setItemName(ItemStack i, List<String> list, PantEnchantTypes en, Player p, boolean t) {
			//XTag will NEVER be null, custom items always have this data. Null check removed.
	       			
			switch(String.valueOf(XTags.getItemTag(i, "Tier"))) {
	        case "Tier I":
		        i = XTags.setItemTag(i, "Tier II", "Tier");
		        i = XTags.setItemTag(i, 8, "Lives");
		        i = XTags.setItemTag(i, 8, "MaxLives");
		        i = XTags.setItemTag(i, 4000, "Cost"); 

		        break;
	        case "Tier II":
		        i = XTags.setItemTag(i, "Tier III", "Tier");
		        i = XTags.setItemTag(i, 10, "Lives");
		        i = XTags.setItemTag(i, 10, "MaxLives");
		        i = XTags.setItemTag(i, 8000, "Cost"); 
		        break;
	        case "Tier III":
		        i = XTags.setItemTag(i, "MAXED OUT", "Tier");
		        i = XTags.setItemTag(i, 15, "MaxLives");
		        i = XTags.setItemTag(i, "false", "Enchantable"); 
		        i = XTags.setItemTag(i, 15, "Lives"); 
		        break;
		    default:
		    	p.sendMessage(ChatColor.RED + "Maximum enchanting limit reached on this item.");
		    	return i;
	        }
			ItemMeta itemMeta = i.getItemMeta();
			
			list.set(0, ChatColor.GREEN + String.valueOf(XTags.getItemTag(i, "Lives")) + ChatColor.GRAY + "/" + String.valueOf(XTags.getItemTag(i, "MaxLives")));
	        for (String s : main.getConfig().getStringList("EnchantmentTypes." + String.valueOf(en) + ".Lore")) {
	        	if (s == null) {
	        		break;
	        	}
	        	list.add(ChatColor.translateAlternateColorCodes('&', s));
	        }
	        itemMeta.setLore(list);
	        i.setItemMeta(itemMeta);
	        i = XTags.setItemTag(i, "true", String.valueOf(en));
	        return i;
	        
		}

		public void setGold(Player p, int amount) {
			if (amount > 999999999) {
				p.sendMessage(ChatColor.RED + "MAXIMUM AMOUNT IS 999,999,999");
				return;
			}
			UUID uuid = p.getUniqueId();
			data.set(b + "." + uuid + ".gold", Integer.valueOf(getGold(p) + amount));
			data.set(b + "." + uuid + ".name", p.getName());
			data.save();
		}

		public boolean removeGold(Player p, int amount) {
			UUID uuid = p.getUniqueId();
			if (getGold(p) - amount < 0) {
				p.sendMessage(ChatColor.RED + "Not enough gold.");
				return false;
			}
			data.set(b + "." +  uuid + ".gold", Integer.valueOf(getGold(p) - amount));
			data.save();
			return true;
		}

		public int getGold(Player p) {
			UUID uuid = p.getUniqueId();
			return data.getInt(b + "." +  uuid + ".gold");
		}
}