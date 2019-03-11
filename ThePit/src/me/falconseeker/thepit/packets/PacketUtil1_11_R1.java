package me.falconseeker.thepit.packets;

import com.mojang.authlib.GameProfile;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.UUID;

import net.minecraft.server.v1_11_R1.EntityVillager;
import net.minecraft.server.v1_11_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_11_R1.EntityPlayer;
import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_11_R1.MinecraftServer;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;
import net.minecraft.server.v1_11_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_11_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_11_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_11_R1.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_11_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_11_R1.PlayerConnection;
import net.minecraft.server.v1_11_R1.PlayerInteractManager;
import net.minecraft.server.v1_11_R1.ScoreboardTeamBase;
import net.minecraft.server.v1_11_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftVillager;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_11_R1.CraftServer;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.Listener;

public class PacketUtil1_11_R1 implements Listener {
	public static PacketUtil1_11_R1 get182;
	public static PacketUtil1_11_R1 getInstance() {
		return get182;
	}
	public static void noai(Entity v, Location loc) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException{
		v.teleport(loc);
		v.getLocation().setYaw(loc.getYaw());
		v.getLocation().setPitch(loc.getPitch());
		net.minecraft.server.v1_11_R1.Entity nmsVillager = ((CraftEntity) v).getHandle();
	    Field goal = nmsVillager.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredField("goalSelector");
	    goal.setAccessible(true);//remove the protected modifier
	    Field modifiersField = Field.class.getDeclaredField("modifiers");
	    modifiersField.setAccessible(true);
	    modifiersField.setInt(goal, goal.getModifiers() & ~Modifier.FINAL);//Evilness shall ensue 
	    PathfinderGoalSelector goalSelector = new PathfinderGoalSelector(nmsVillager.world != null && nmsVillager.world.methodProfiler != null ? nmsVillager.world.methodProfiler : null);
	    goal.set(nmsVillager, goalSelector);
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
                setField("g", 2);
                break;
            case DESTROY:
                setField("g", 1);
        }

        return packet;
    }

    private static void addPlayer(Player player) {
        setField("h", Collections.singleton(player.getName()));
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
