package me.falconseeker.extraevents;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionType;

import me.falconseeker.thepit.ThePit;
import me.falconseeker.thepit.utils.XMaterial;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagString;

public class PlayerVault implements Listener {
	 
	private ThePit main;

	public PlayerVault(ThePit main) {
		this.main = main;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	@EventHandler
	public void onClci(InventoryClickEvent e) {
		if (e.getInventory().getName().equals("Vault")) {
			if (e.getCurrentItem().getType() == XMaterial.OAK_PLANKS.parseMaterial()){
				e.setCancelled(true);
				e.getWhoClicked().sendMessage(ChatColor.RED + "Perk items cannot be stored.");
			}
		}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		if (e.getClickedBlock().getType() != Material.ENDER_CHEST) {
			return;
		}
		e.setCancelled(true);
	      
		Inventory i = Bukkit.createInventory(e.getPlayer(), 36, "Vault");
		restoreInventory(e.getPlayer(), i);
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if (e.getInventory().getName().equals("Vault")) {
		    List<ItemStack> items = new ArrayList<ItemStack>();
			for (ItemStack a : e.getInventory().getContents()) {
				if (a == null) {
					continue;
				}
				items.add(a);
			}
			main.vaults.set(e.getPlayer().getUniqueId() + ".Contents", items);
			main.vaults.save();
		}
	}
	  public void restoreInventory(Player p, Inventory i)
			    {
		  if (main.vaults.get(p.getUniqueId() + ".Contents") == null) {
		      p.openInventory(i);
		       List<ItemStack> items = new ArrayList<ItemStack>();
		       items.add(new ItemStack(Material.AIR, 1));
		       main.vaults.set(p.getUniqueId() + ".Contents", items);	
		       main.vaults.save();

		    	 return;
			     }
          List<ItemStack> l = (List<ItemStack>) main.vaults.getList(p.getUniqueId() + ".Contents");
		  	for (ItemStack s : l) {
		  		 i.addItem(s);
		  	}
			      p.openInventory(i);
			    			  
			}
	}