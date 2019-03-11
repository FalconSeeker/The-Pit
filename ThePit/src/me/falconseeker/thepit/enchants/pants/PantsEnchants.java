package me.falconseeker.thepit.enchants.pants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.falconseeker.thepit.ThePit;
import me.falconseeker.thepit.utils.XTags;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class PantsEnchants implements Listener, CommandExecutor {

	
	//COMPLETED
	
	
	private ThePit main;
	public HashMap<UUID, Boolean> Booboo = new HashMap<>();
	public HashMap<UUID, Boolean> Creative = new HashMap<>();
	private HashMap<UUID, ItemStack> pants = new HashMap<>();
	private HashMap<UUID, Integer> slot = new HashMap<>();
	private ArrayList<UUID> cantpickup = new ArrayList<>();
	private HashMap<UUID, ItemStack> respawnleggings = new HashMap<>();

	public PantsEnchants(ThePit main) {
		this.main = main;
		Bukkit.getPluginManager().registerEvents(this, main);
		main.getCommand("dropitem").setExecutor(this);
		
		new Creative(main);
		new Booboo(main);
		new Cricket(main);
		new DangerClose(main);
		new Eggs(main);
		new Electrolytes(main);
		new Hearts(main);
		new TNT(main);
		new StrikeGold(main);
		
		//Rare Enchants
		new DoubleJump(main);
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Pants Enchantments Registered.");
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (e.getItemDrop().getItemStack().getType().equals(Material.LEATHER_LEGGINGS)) {
			
			pants.put(e.getPlayer().getUniqueId(), e.getItemDrop().getItemStack());
			slot.put(e.getPlayer().getUniqueId(), e.getPlayer().getInventory().getHeldItemSlot());
			TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("Messages.DropItem")).replace("%item%", "item"));
			message.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/dropitem"));
			message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "&e&lCLICK TO CONFIRM DROP:\n&7\n&eFresh Gear\n&7Kept on death\n&7\n&eUsed in the mythic well\n&eAlso, a fashion statement")).create()));
			e.getPlayer().spigot().sendMessage(message);
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void pci(PlayerPickupItemEvent e) {
		if (cantpickup.contains(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
		}
	}
	@Override
	public boolean onCommand(CommandSender s, Command c, String arg, String[] args) {
		if (!(s instanceof Player)) {
			s.sendMessage("not a player");
			return true;
		}
		
		Player player = (Player) s;
		
		if (c.getName().equalsIgnoreCase("dropitem")) {
			
			if (pants.get(player.getUniqueId()) == null) {
				player.sendMessage(main.getMessages().noperms);
				return true;
			}
			ItemStack item = pants.get(player.getUniqueId());
			
            player.getWorld().dropItemNaturally(player.getLocation().add(player.getLocation().getDirection()), item);
            pants.remove(player.getUniqueId());
            player.getInventory().setItem(slot.get(player.getUniqueId()), null);

            }
            cantpickup.add(player.getUniqueId());
            new BukkitRunnable() {
				
				@Override
				public void run() {
					cantpickup.remove(player.getUniqueId());
				}
			}.runTaskLater(main, 100L);
		return true;
	}
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if (!checkNbt(e.getEntity().getInventory().getLeggings())) {
			return;
		}
		ItemStack i = e.getEntity().getInventory().getLeggings();
		ItemMeta s = i.getItemMeta();
        List<String> lore = i.getItemMeta().getLore();
		lore.set(0, ChatColor.GREEN + String.valueOf((Integer) XTags.getItemTag(i, "Lives") - 1) + ChatColor.GRAY + "/" + String.valueOf(XTags.getItemTag(i, "MaxLives")));
		s.setLore(lore);
		i.setItemMeta(s);

		if ((Integer) XTags.getItemTag(e.getEntity().getInventory().getLeggings(), "Lives") <= 0) {
			e.getEntity().sendMessage(ChatColor.RED + "Leggings lost.");
			e.getEntity().playSound(e.getEntity().getLocation(), Sound.BLOCK_ANVIL_BREAK, 1F, 1F);
			return;
		}
		respawnleggings.put(e.getEntity().getUniqueId(),  XTags.setItemTag(
	    		   i,
	    		  (Integer) XTags.getItemTag(
	    				   e.getEntity().getInventory().getLeggings(), 
	    				   "Lives") - 1, 
	    		  
	    		   "Lives"));
	}
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		if (respawnleggings.get(e.getPlayer().getUniqueId()) == null) return;
		new BukkitRunnable() {
			
			@Override
			public void run() {
				e.getPlayer().getInventory().setLeggings(respawnleggings.get(e.getPlayer().getUniqueId()));
				respawnleggings.remove(e.getPlayer().getUniqueId());
			}
		}.runTaskLater(main, 20L);
	}
	public static boolean checkNbt(ItemStack i) {
		if (i == null) {
			return false;
		}
		if (XTags.getItemTag(i) == null) {
			return false;
		}
		if (!i.hasItemMeta()) {
			return false;
		}
		return true;
	}
	public static boolean check(ItemStack i, String enchant) {
		if (!checkNbt(i)) {
			return false;
		}
		
		if (XTags.getItemTag(i, enchant) == null) {
			return false;
		}
		if (XTags.getItemTag(i, enchant).equals("true")) {
			return true;
		}
		return false;
	}
	
}
