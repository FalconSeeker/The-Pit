package me.falconseeker.thepit.packets;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.falconseeker.thepit.ThePit;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;

public class PacketUtil
{
  public enum V
  {
    R111,  R19, R192, R182, R183, R110, R112;
  }

  	private ThePit main = ThePit.getPlugin(ThePit.class);
  	
  	public PacketUtil(ThePit main) {
	  this.main = main;
    }
  	
  	
  public V getVersion()
  {
    if (Bukkit.getBukkitVersion().startsWith("1.12")) {
      return V.R112;
    }
    if (Bukkit.getBukkitVersion().startsWith("1.11")) {
      return V.R111;
    }
    if (Bukkit.getBukkitVersion().startsWith("1.10")) {
      return V.R110;
    }
    if (Bukkit.getBukkitVersion().startsWith("1.9")) {
        return V.R192;
      }
    if (Bukkit.getBukkitVersion().startsWith("1.8")) {
        return V.R183;
      }
    return V.R182;
  }
  public void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle)
  {
	switch(getVersion()) {
	case R111:
		PacketUtil1_11_R1.sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
		break;
	case R112:
		PacketUtil1_12_R1.sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
		break;
	case R19:
		PacketUtil1_9_R1.sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
		break;
	case R110:
	    PacketUtil1_10_R1.sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
		break;
	case R182:
    	PacketUtil1_8_R2.sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
		break;
	case R183:
    	PacketUtil1_8_R3.sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
		break;
	case R192:
    	PacketUtil1_9_R2.sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
		break;
	default:
		player.sendMessage(ChatColor.RED + "UNSUPPORTED VERSION!");
		break;
	}
  }
  public void sendTablist(Player player, String header, String footer)
  {
	switch(getVersion()) {
	case R111:
    	PacketUtil1_11_R1.sendTablist(player, header, footer	);
		break;
	case R112:
    	PacketUtil1_12_R1.sendTablist(player, header, footer	);
		break;
	case R19:
    	PacketUtil1_9_R1.sendTablist(player, header, footer	);
		break;
	case R110:
    	PacketUtil1_10_R1.sendTablist(player, header, footer	);
		break;
	case R182:
    	PacketUtil1_8_R2.sendTablist(player, header, footer	);
		break;
	case R183:
    	PacketUtil1_8_R3.sendTablist(player, header, footer	);
		break;
	case R192:
    	PacketUtil1_9_R2.sendTablist(player, header, footer	);
		break;
	default:
		player.sendMessage(ChatColor.RED + "UNSUPPORTED VERSION!");
		break;
	}
  }

	 public void noAI(Entity v, Location loc) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	  {
		switch(getVersion()) {
		case R19:
		    PacketUtil1_9_R2.noai(v, loc);
		    break;
		case R110:
		    PacketUtil1_10_R1.noai(v, loc);
		    break;
		case R111:
		    PacketUtil1_11_R1.noai(v, loc);
			break;
		case R112:
			PacketUtil1_12_R1.noai(v, loc);
			break;
		case R182:
			PacketUtil1_8_R2.noai(v, loc);
			break;
		case R183:
			PacketUtil1_8_R3.noai(v, loc);
			break;
		case R192:
			PacketUtil1_9_R2.noai(v, loc);
			break;
		default:
			break;
		}
  }

	    private Team team;
	    private Scoreboard scoreboard;

		/**
		 * Gets an NBT tag in a given block with the specified keys
		 * 
		 * @param collection
		 * The packet to send other players
		 * @param player
		 * The player whos nametag is changing
		 */
	    public void changePlayerName(Player collection, Player player, String prefix, String suffix, TeamAction action) {
			switch(getVersion()) {
			case R19:
				PacketUtil1_9_R2.changeNameTag(collection, player, prefix, suffix, action);
			    break;
			case R110:
				PacketUtil1_10_R1.changeNameTag(collection, player, prefix, suffix, action);
			    break;
			case R111:
				PacketUtil1_11_R1.changeNameTag(collection, player, prefix, suffix, action);
				break;
			case R112:
				PacketUtil1_12_R1.changeNameTag(collection, player, prefix, suffix, action);
				break;
			case R182:
				PacketUtil1_8_R2.changeNameTag(collection, player, prefix, suffix, action);
				break;
			case R183:
				PacketUtil1_8_R3.changeNameTag(collection, player, prefix, suffix, action);
				break;
			case R192:
				PacketUtil1_9_R2.changeNameTag(collection, player, prefix, suffix, action);
				break;
			default:
				break;
			}
	    }

	    private static String Color(String input) {
	        return ChatColor.translateAlternateColorCodes('&', input);
	    }
	    public void sendActionBar(Player player, String message) {
	        message = ChatColor.translateAlternateColorCodes('&', message);

	        try {
	            if (main.v.equals("v1_12_R1") || main.v.startsWith("v1_13")) {
	                new PreAction(player, message);
	            } else if (!(main.v.equalsIgnoreCase("v1_8_R1") || (main.v.contains("v1_7_")))) {
	                Class<?> c1 = Class.forName("org.bukkit.craftbukkit." + main.v + ".entity.CraftPlayer");
	                Object p = c1.cast(player);
	                Object ppoc;
	                Class<?> c4 = Class.forName("net.minecraft.server." + main.v + ".PacketPlayOutChat");
	                Class<?> c5 = Class.forName("net.minecraft.server." + main.v + ".Packet");

	                Class<?> c2 = Class.forName("net.minecraft.server." + main.v + ".ChatComponentText");
	                Class<?> c3 = Class.forName("net.minecraft.server." + main.v + ".IChatBaseComponent");
	                Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(message);
	                ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(o, (byte) 2);

	                Method getHandle = c1.getDeclaredMethod("getHandle");
	                Object handle = getHandle.invoke(p);

	                Field fieldConnection = handle.getClass().getDeclaredField("playerConnection");
	                Object playerConnection = fieldConnection.get(handle);

	                Method sendPacket = playerConnection.getClass().getDeclaredMethod("sendPacket", c5);
	                sendPacket.invoke(playerConnection, ppoc);
	            } else {
	                Class<?> c1 = Class.forName("org.bukkit.craftbukkit." + main.v + ".entity.CraftPlayer");
	                Object p = c1.cast(player);
	                Object ppoc;
	                Class<?> c4 = Class.forName("net.minecraft.server." + main.v + ".PacketPlayOutChat");
	                Class<?> c5 = Class.forName("net.minecraft.server." + main.v+ ".Packet");

	                Class<?> c2 = Class.forName("net.minecraft.server." + main.v + ".ChatSerializer");
	                Class<?> c3 = Class.forName("net.minecraft.server." + main.v + ".IChatBaseComponent");
	                Method m3 = c2.getDeclaredMethod("a", String.class);
	                Object cbc = c3.cast(m3.invoke(c2, "{\"text\": \"" + message + "\"}"));
	                ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(cbc, (byte) 2);

	                Method getHandle = c1.getDeclaredMethod("getHandle");
	                Object handle = getHandle.invoke(p);

	                Field fieldConnection = handle.getClass().getDeclaredField("playerConnection");
	                Object playerConnection = fieldConnection.get(handle);

	                Method sendPacket = playerConnection.getClass().getDeclaredMethod("sendPacket", c5);
	                sendPacket.invoke(playerConnection, ppoc);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    private Class<?> getNMSClass(String name) throws ClassNotFoundException {
	        return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + name);
	    }
	}
