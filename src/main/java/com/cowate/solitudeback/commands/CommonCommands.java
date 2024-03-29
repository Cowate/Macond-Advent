package com.cowate.solitudeback.commands;

import com.cowate.solitudeback.Items;
import com.cowate.solitudeback.listeners.EntityListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CommonCommands implements CommandExecutor {
    private EntityListener entityListener;

    public void setEntityListener(EntityListener entityListener) {
        this.entityListener = entityListener;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            /*if (command.getName().equalsIgnoreCase("giveMA")) {
                String item = args[0];
                int count = 1;
                if (args.length > 1) {
                    count = Integer.parseInt(args[1]);
                }
                if (count > 0) {
                    ItemStack items = new ItemStack(Material.AIR);
                    switch (item.toLowerCase()) {
                        case "spear" -> items = Items.SPEAR;
                        case "ice_cube" -> items = Items.ICE_CUBE;
                        case "sextant" -> items = Items.SEXTANT;
                        case "magnifier" -> items = Items.MAGNIFIER;
                        case "mirror" -> items = Items.MIRROR;
                        case "bone_ashes" -> items = Items.BONE_ASHES;
                        case "sleepless_pony" -> items = Items.SLEEPLESS_PONY;
                        case "ruby" -> items = Items.RUBY;
                        case "imitation_golden_fish" -> items = Items.IMITATION_GOLDEN_FISH;
                        case "spear_grid" -> items = Items.SPEAR_GRID;
                    }
                    items.setAmount(count);
                    player.getInventory().addItem(items);
                }
            }*/
        }
        else {
            switch (command.getName()) {
                case "macd" ->
                {
                    if (args.length > 0) {
                        switch (args[1]) {
                            case "clear" -> {
                                if (args[0].equals("all")) {
                                    entityListener.clearCd();
                                }
                                else {
                                    if (Bukkit.getPlayer(args[0]) != null) {
                                        entityListener.clearCd(args[0]);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
