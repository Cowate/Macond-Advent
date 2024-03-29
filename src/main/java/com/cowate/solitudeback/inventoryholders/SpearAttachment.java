package com.cowate.solitudeback.inventoryholders;

import de.tr7zw.nbtapi.NBTItem;
import dev.lone.itemsadder.api.CustomStack;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SpearAttachment implements InventoryHolder {
    private final Inventory inventory;
    private final Component title = Component.text("Attachment");

    public SpearAttachment(ItemStack spear) {
        inventory = Bukkit.createInventory(this, 9, title);
        String attachment1 = (new NBTItem(spear)).getString("attachment1");
        String attachment2 = (new NBTItem(spear)).getString("attachment2");
        ItemStack itemStack1 = getItemStack(spear, attachment1, 1);
        ItemStack itemStack2 = getItemStack(spear, attachment2, 2);
        inventory.setItem(0, itemStack1);
        inventory.setItem(1, itemStack2);
        for (int i = 2 ; i < 9 ; i++) {
            inventory.setItem(i, new ItemStack(Material.GLASS_PANE));
        }
    }

    private ItemStack getItemStack(ItemStack spear, String name, int order) {
        String count_tag;
        if (order == 1) {
            count_tag = "count1";
        }
        else {
            count_tag = "count2";
        }

        ItemStack itemStack;
        switch (name) {
            case "ice_cube" -> {
                itemStack = CustomStack.getInstance("solitude:ice_cube").getItemStack();
                itemStack.setAmount((new NBTItem(spear)).getInteger(count_tag));
            }
            case "sextant" -> {
                itemStack = CustomStack.getInstance("solitude:sextant").getItemStack();
                itemStack.setAmount(1);
            }
            case "magnifier" -> {
                itemStack = CustomStack.getInstance("solitude:magnifier").getItemStack();
                itemStack.setAmount(1);
            }
            case "mirror" -> {
                itemStack = CustomStack.getInstance("solitude:mirror").getItemStack();
                itemStack.setAmount(1);
            }
            case "bone_ashes" -> {
                itemStack = CustomStack.getInstance("solitude:bone_ashes").getItemStack();
                itemStack.setAmount(1);
            }
            case "sleepless_pony" -> {
                itemStack = CustomStack.getInstance("solitude:sleepless_pony").getItemStack();
                itemStack.setAmount(1);
            }
            case "pianola_broken_strings" -> {
                itemStack = CustomStack.getInstance("solitude:pianola_broken_strings").getItemStack();
                itemStack.setAmount(1);
            }
            case "real_golden_fish" -> {
                itemStack = CustomStack.getInstance("solitude:real_golden_fish").getItemStack();
                NBTItem nbtItem = new NBTItem(itemStack, true);
                nbtItem.setByte("count", (byte) (int)(new NBTItem(spear)).getInteger(count_tag));
                itemStack.setAmount(1);
            }
            case "black_armband" -> {
                itemStack = CustomStack.getInstance("solitude:black_armband").getItemStack();
                itemStack.setAmount(1);
            }
            case "silver_candlestick" -> {
                itemStack = CustomStack.getInstance("solitude:silver_candlestick").getItemStack();
                itemStack.setAmount(1);
            }
            case "bullet" -> {
                itemStack = CustomStack.getInstance("solitude:bullet").getItemStack();
                itemStack.setAmount(1);
            }
            case "parchment" -> {
                itemStack = CustomStack.getInstance("solitude:parchment").getItemStack();
                itemStack.setAmount(1);
            }
            default -> itemStack = new ItemStack(Material.AIR);
        }
        return itemStack;
    }
    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }
}
