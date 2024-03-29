package com.cowate.solitudeback.listeners;

import com.cowate.solitudeback.inventoryholders.SpearAttachment;
import de.tr7zw.nbtapi.NBTItem;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.sql.SQLSyntaxErrorException;
import java.util.Objects;

public class InventoryListener implements Listener {
    private Plugin pluginInstance;
    private EntityListener entityListener = null;
    private ServerListener serverListener = null;
    public InventoryListener(Plugin plugin) {
        pluginInstance = plugin;
    }

    public void setEntityListener(EntityListener entityListener) {
        this.entityListener = entityListener;
    }
    public void setServerListener(ServerListener serverListener) {
        this.serverListener = serverListener;
    }
    public Listener getInstance() {
        return this;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getHolder(false) instanceof SpearAttachment) {
            ItemStack itemStack1 = event.getInventory().getItem(0);
            ItemStack itemStack2 = event.getInventory().getItem(1);
            ItemStack spear = event.getPlayer().getInventory().getItemInMainHand();
            if (spear.getType() == Material.AIR || !(new NBTItem(spear)).hasKey("attachment1")) {
                spear = event.getPlayer().getInventory().getItemInOffHand();
            }
            if (spear.getType() == Material.AIR || !(new NBTItem(spear)).hasKey("attachment1")) {
                if (itemStack1 != null){
                    event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), itemStack1);
                }
                if (itemStack2 != null){
                    event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), itemStack2);
                }
                event.getPlayer().sendMessage("Please put your spear on your hand.");
                return;
            }
            boolean hasPianola = false;
            boolean hasBullet = false;
            boolean hasSleepless = false;
            NBTItem nbt_spear = new NBTItem(spear, true);
            if (itemStack1 != null && itemStack1.getType() != Material.AIR && itemStack1.getAmount() !=0 && (new NBTItem(itemStack1)).hasKey("spear_attachment")) {
                NBTItem nbtItem1 = new NBTItem(itemStack1);
                String attachment = nbtItem1.getCompound("itemsadder").getString("id");
                if (Objects.equals(attachment, "pianola_broken_strings")) {
                    hasPianola = true;
                }
                if (Objects.equals(attachment, "bullet")) {
                    hasBullet = true;
                }
                if (Objects.equals(attachment, "sleepless_pony")) {
                    hasSleepless = true;
                }
                nbt_spear.setString("attachment1", attachment);
                if (Objects.equals(attachment, "ice_cube")) {
                    nbt_spear.setByte("count1", (byte) itemStack1.getAmount());
                }
                else if (Objects.equals(attachment, "real_golden_fish")) {
                    nbt_spear.setByte("count1", nbtItem1.getByte("count") );
                }
                else {
                    nbt_spear.setByte("count1", (byte) 0);
                }
            }
            else {
                nbt_spear.setString("attachment1", "air");
                nbt_spear.setByte("count1", (byte) 0);
                if (itemStack1 != null) {
                    event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), itemStack1);
                }
            }
            if (itemStack2 != null && itemStack2.getType() != Material.AIR && itemStack2.getAmount() !=0 && (new NBTItem(itemStack2)).hasKey("spear_attachment")) {
                NBTItem nbtItem2 = new NBTItem(itemStack2);
                String attachment = nbtItem2.getCompound("itemsadder").getString("id");
                if (Objects.equals(attachment, "pianola_broken_strings")) {
                    hasPianola = true;
                }
                if (Objects.equals(attachment, "bullet")) {
                    hasBullet = true;
                }
                if (Objects.equals(attachment, "sleepless_pony")) {
                    hasSleepless = true;
                }
                nbt_spear.setString("attachment2", attachment);
                if (Objects.equals(attachment, "ice_cube")) {
                    nbt_spear.setByte("count2", (byte) itemStack2.getAmount());
                }
                else if (Objects.equals(attachment, "real_golden_fish")) {
                    nbt_spear.setByte("count2", nbtItem2.getByte("count") );
                }
                else {
                    nbt_spear.setByte("count2", (byte) 0);
                }
            }
            else {
                nbt_spear.setString("attachment2", "air");
                nbt_spear.setByte("count2", (byte) 0);
                if (itemStack2 != null) {
                    event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), itemStack2);
                }
            }
            if (entityListener != null) {
                if (hasBullet) {
                    entityListener.setSunLightAgainPlayer(event.getPlayer().getName());
                }
                else {
                    entityListener.removeSunLightAgainPlayer(event.getPlayer().getName());
                }
            }
            if (serverListener != null) {
                if (hasSleepless) {
                    serverListener.addSleepless(event.getPlayer().getName());
                }
                else {
                    serverListener.removeSleepless(event.getPlayer().getName());
                }
            }
            if (hasPianola) {
                nbt_spear.getCompoundList("AttributeModifiers").forEach(nbt -> {
                    if (nbt.hasKey("Slot") && Objects.equals(nbt.getString("Slot"), "mainhand")) {
                        if (nbt.hasKey("AttributeName") && Objects.equals(nbt.getString("AttributeName"), "minecraft:generic.attack_speed")) {
                            nbt.setFloat("Amount", -2.4f);
                        }
                        if (nbt.hasKey("AttributeName") && Objects.equals(nbt.getString("AttributeName"), "minecraft:generic.attack_damage")) {
                            nbt.setFloat("Amount", 12.0f);
                        }
                    }
                });
            }
            else {
                nbt_spear.getCompoundList("AttributeModifiers").forEach(nbt -> {
                    if (nbt.hasKey("Slot") && Objects.equals(nbt.getString("Slot"), "mainhand")) {
                        if (nbt.hasKey("AttributeName") && Objects.equals(nbt.getString("AttributeName"), "minecraft:generic.attack_speed")) {
                            nbt.setFloat("Amount", -2.9f);
                        }
                        if (nbt.hasKey("AttributeName") && Objects.equals(nbt.getString("AttributeName"), "minecraft:generic.attack_damage")) {
                            nbt.setFloat("Amount", 8.0f);
                        }
                    }
                });
            }
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getHolder(false) instanceof SpearAttachment && event.getSlot() > 1) {
            event.setCancelled(true);
            return;
        }
        /*InventoryAction action = event.getAction();
        ItemStack itemStack = event.getCurrentItem();
        if (action == InventoryAction.SWAP_WITH_CURSOR || action == InventoryAction.HOTBAR_SWAP || action == InventoryAction.UNKNOWN || action == InventoryAction.HOTBAR_MOVE_AND_READD) {
            event.setCancelled(true);
            return;
        }

        if (itemStack == null || (itemStack.getItemMeta().getPersistentDataContainer().has(NameSpacedKeys.SPEAR_ATTACHMENT))) {

        }
        else
            event.setCancelled(true);*/
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onGridStone(PrepareGrindstoneEvent event) {
        if (event.getInventory().getUpperItem() != null && event.getInventory().getLowerItem() == null) {
            NBTItem nbtItem = new NBTItem(event.getInventory().getUpperItem());
            if (nbtItem.hasKey("itemsadder") && Objects.equals(nbtItem.getCompound("itemsadder").getString("id"), "golden_fish")) {
                ItemStack itemStack = CustomStack.getInstance("solitude:real_golden_fish").getItemStack();
                itemStack.setAmount(1);
                event.getInventory().setUpperItem(itemStack);
            }
        }

        if (event.getInventory().getUpperItem() == null && event.getInventory().getLowerItem() != null) {
            NBTItem nbtItem = new NBTItem(event.getInventory().getLowerItem());
            if (nbtItem.hasKey("itemsadder") && Objects.equals(nbtItem.getCompound("itemsadder").getString("id"), "golden_fish")) {
                ItemStack itemStack = CustomStack.getInstance("solitude:real_golden_fish").getItemStack();
                itemStack.setAmount(1);
                event.getInventory().setLowerItem(itemStack);
            }
        }
    }
}
