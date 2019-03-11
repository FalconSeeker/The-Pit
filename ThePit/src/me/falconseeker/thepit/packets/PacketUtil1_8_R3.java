package me.falconseeker.thepit.packets;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import com.mojang.authlib.GameProfile;

import me.falconseeker.thepit.ThePit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftVillager;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;

public class PacketUtil1_8_R3 implements Listener {
	public static PacketUtil1_8_R3 get182;
	public static PacketUtil1_8_R3 getInstance() {
		return get182;
	}
	public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title,
			String subtitle) {
		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
		PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null,
				fadeIn.intValue(), stay.intValue(), fadeOut.intValue());
		connection.sendPacket(packetPlayOutTimes);
		if (subtitle != null) {
			subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
			subtitle = subtitle.replaceAll("%level%", String.valueOf(player.getLevel()));
			subtitle = subtitle.replaceAll("%oldlevel%", String.valueOf(player.getLevel() - 1));
			subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
			IChatBaseComponent titleSub = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
			PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(
					PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleSub);
			connection.sendPacket(packetPlayOutSubTitle);
		}
		if (title != null) {
			title = title.replace("%player%", player.getDisplayName());

			title = ChatColor.translateAlternateColorCodes('&', title);
			IChatBaseComponent titleMain = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
			PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE,
					titleMain);
			connection.sendPacket(packetPlayOutTitle);
		}
	}

	public static void sendActionBar(Player player, String actionbar) {
		actionbar = actionbar.replaceAll("%player%", player.getDisplayName());
		actionbar = ChatColor.translateAlternateColorCodes('&', actionbar);
		CraftPlayer p = (CraftPlayer) player;
		IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + actionbar + "\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
		p.getHandle().playerConnection.sendPacket(ppoc);
	}

	public static void createNPC(Player player, String npcName) {
		Location location = player.getLocation();
		MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer nmsWorld = ((CraftWorld) player.getWorld()).getHandle();
		GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "�a�l" + npcName);

		EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld));
		Player npcPlayer = npc.getBukkitEntity().getPlayer();
		npcPlayer.setPlayerListName("");

		npc.setLocation(location.getX(), location.getY(), location.getZ(), player.getLocation().getYaw(),
				player.getLocation().getPitch());

		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
		connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
				new EntityPlayer[] { npc }));
		connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
		connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
				new EntityPlayer[] { npc }));
	}
	 

	public static void noai(Entity v, Location loc) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException{

		new BukkitRunnable() {
			
			@Override
			public void run() {
				net.minecraft.server.v1_8_R3.Entity nmsVillager = ((CraftEntity) v).getHandle();
			    Field goal;
				try {
					
					goal = nmsVillager.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredField("goalSelector");
				    goal.setAccessible(true);
				    Field modifiersField = Field.class.getDeclaredField("modifiers");
				    modifiersField.setAccessible(true);
				    modifiersField.setInt(goal, goal.getModifiers() & ~Modifier.FINAL);//Evilness shall ensue 
				    PathfinderGoalSelector goalSelector = new PathfinderGoalSelector(nmsVillager.world != null && nmsVillager.world.methodProfiler != null ? nmsVillager.world.methodProfiler : null);
				    goal.set(nmsVillager, goalSelector);	
				} catch (NoSuchFieldException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
		}.runTaskLater(ThePit.getPlugin(ThePit.class), 30);
	}
    public static void sendTablist(Player p, String Title, String subTitle) {
        if(Title == null) Title.equals("");
        if(subTitle == null) subTitle.equals("");

       
        IChatBaseComponent tabTitle = ChatSerializer.a("{\"text\":\"" + Title+ "\"}");
        IChatBaseComponent tabSubTitle = ChatSerializer.a("{\"text\":\"" + subTitle + "\"}");

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(tabTitle);
       
        try {
           Field field = packet.getClass().getDeclaredField("b");
           field.setAccessible(true);
           field.set(packet, tabSubTitle);
        } catch (Exception e) {
           e.printStackTrace();
        } finally {
           ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
        }
    }
    public static void setAI(LivingEntity entity, boolean hasAi) {
    	  EntityLiving handle = ((CraftLivingEntity) entity).getHandle();
    	  handle.getDataWatcher().watch(15, (byte) (hasAi ? 0 : 1));
    	}
    public static String getTag(int slot, Player p, String key) {
        PlayerInventory inv = ((CraftPlayer)p).getHandle().inventory;
        NBTTagCompound com = inv.getItem(slot).getTag();
        if (com!=null && com.hasKey(key)) {
            return com.getString(key);
        } else {
            return null;
        }
    }
    private static PacketPlayOutScoreboardTeam packet; // The packet that will be sent to the player

    /**
     * This is the main method to change the players name.
     *
     * @param toSendTo   The player that you want to send the packet
     * @param player     The player wich name will be changed
     * @param prefix     The prefix that the 'player' will have
     * @param suffix     The suffix that the 'player' will have
     * @param teamAction To CREATE, UPDATE, OR DESTROY the fake team
     */
    public static void changeNameTag(Player toSendTo, Player player, String prefix, String suffix, TeamAction teamAction) {
        sendPacket(toSendTo, teamPacket(player, prefix, suffix, teamAction));
    }

    private static PacketPlayOutScoreboardTeam teamPacket(Player player, String prefix, String suffix, TeamAction teamAction) {
        packet = new PacketPlayOutScoreboardTeam();
        setField("a", player.getName());
        setField("b", player.getName());
        setField("c", Color(prefix));
        setField("d", Color(suffix));
        setField("e", ScoreboardTeamBase.EnumNameTagVisibility.ALWAYS.e);

        switch (teamAction) {
            case CREATE:
                addPlayer(player);
                break;
            case UPDATE:
                setField("h", 2);
                break;
            case DESTROY:
                setField("h", 1);
        }

        return packet;
    }

    private static void addPlayer(Player player) {
        setField("g", Collections.singleton(player.getName()));
    }

    private static String Color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    private static void sendPacket(Player player, PacketPlayOutScoreboardTeam packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    private static void setField(String field, Object value) {
        try {
            Field f = packet.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(packet, value);
            f.setAccessible(false);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

}
