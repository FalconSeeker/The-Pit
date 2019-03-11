package me.falconseeker.thepit.gui;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class GUI  
{ 
    Player p; 
    Inventory inv;
    
    abstract double area(); 

    public GUI(Inventory inv, Player p) { 
        this.inv = inv;
        this.p = p;
    } 
      
    public Player getPlayer() { 
        return p; 
    } 
    
    public Inventory getInventory() {
    	return inv;
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
} 