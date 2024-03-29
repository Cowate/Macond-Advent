package com.cowate.solitudeback;

import com.cowate.solitudeback.commands.CommonCommands;
import com.cowate.solitudeback.listeners.EntityListener;
import com.cowate.solitudeback.listeners.InventoryListener;
import com.cowate.solitudeback.listeners.ServerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MacondoAdvent extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Hello to MacondoAdvent");
        Items.register();
        CommonRecipes.register();
        EntityListener entityListener = new EntityListener(this);
        ServerListener serverListener = new ServerListener(this);
        InventoryListener inventoryListener = new InventoryListener(this);
        CommonCommands commonCommands = new CommonCommands();
        inventoryListener.setEntityListener(entityListener);
        inventoryListener.setServerListener(serverListener);
        entityListener.setServerListener(serverListener);
        commonCommands.setEntityListener(entityListener);
        Bukkit.getPluginManager().registerEvents(entityListener, this);
        Bukkit.getPluginManager().registerEvents(serverListener, this);
        Bukkit.getPluginManager().registerEvents(inventoryListener, this);
        this.getCommand("macd").setExecutor(commonCommands);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
