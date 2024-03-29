package com.cowate.solitudeback.listeners;

import com.cowate.solitudeback.inventoryholders.SpearAttachment;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.ChestBoat;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class ServerListener implements Listener {
    private Plugin pluginInstance;
    private boolean launched = false;
    private Set<String> sleepless;

    public ServerListener(Plugin plugin) {
        pluginInstance = plugin;
        sleepless = new HashSet<>();
    }
    Runnable sleeplessPlan = new Runnable() {
        @Override
        public void run() {
            if (!sleepless.isEmpty()) {
                sleepless.forEach(name -> {
                    Player player = Bukkit.getPlayer(name);
                    if (player != null && checkInventory(player)) {
                        long t = player.getWorld().getTime();
                        if (t / 1000 >= 13) {
                            player.getWorld().getNearbyEntities(player.getLocation(), 32,32,32).forEach(entity -> {
                                if (entity instanceof Monster) {
                                    ((Monster) entity).addPotionEffect((new PotionEffect(PotionEffectType.GLOWING, 110, 1)));
                                }
                            });
                            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 110, 1));
                        }
                    }
                });
            }

        }
    };

    private boolean checkInventory(Player player) {
        for (int i = 0 ; i < player.getInventory().getContents().length ; i++) {
            if (player.getInventory().getContents()[i] != null) {
                NBTItem nbtItem = new NBTItem(player.getInventory().getContents()[i]);
                if (nbtItem.hasKey("attachment1") && (Objects.equals(nbtItem.getString("attachment1"), "sleepless_pony") || Objects.equals(nbtItem.getString("attachment2"), "sleepless_pony"))) {
                    sleepless.add(player.getName());
                    //Bukkit.getLogger().info("add player " + player.getName());
                    return true;
                }
            }
        }
        sleepless.remove(player.getName());
        //Bukkit.getLogger().info("remove player " + player.getName());
        return false;
    }
    @EventHandler
    public void onPlayerSleep(PlayerBedEnterEvent event) {
        //sleepless pony effect
        if (sleepless.contains(event.getPlayer().getName())) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!launched) {

            Bukkit.getScheduler().runTaskTimer(pluginInstance, sleeplessPlan, 0, 100);
            launched = true;
        }
    }
    public void addSleepless(String name) {
        if (name != null) {
            sleepless.add(name);
        }
    }
    public void removeSleepless(String name) {
        if (name != null) {
            sleepless.remove(name);
        }
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (!(inventory.getHolder() instanceof HumanEntity) && !(inventory.getHolder(false) instanceof SpearAttachment)) {
            checkInventory((Player) event.getPlayer());
        }
    }
    @EventHandler
    public void onPlayerPickUp(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player && event.getItem().getItemStack().getType() == Material.TRIDENT) {
            NBTItem nbtItem = new NBTItem(event.getItem().getItemStack());
            if (nbtItem.hasKey("attachment1") && (Objects.equals(nbtItem.getString("attachment1"), "sleepless_pony") || Objects.equals(nbtItem.getString("attachment2"), "sleepless_pony"))) {
                sleepless.add(player.getName());
                //Bukkit.getLogger().info("add player " + player.getName());
            }
        }
    }
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() == Material.TRIDENT) {
            checkInventory(event.getPlayer());
        }
    }
}
