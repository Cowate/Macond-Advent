package com.cowate.solitudeback.listeners;

import com.cowate.solitudeback.inventoryholders.SpearAttachment;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTEntity;
import de.tr7zw.nbtapi.NBTItem;
import dev.lone.itemsadder.api.CustomStack;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class EntityListener implements Listener {
    private Plugin pluginInstance;
    private final Set<UUID> fishPowerReturn;
    private final Set<String> sunLightAgain;
    private final Map<String, Integer> cdSunLightAgain;
    private final Map<String, Integer> channelTough;
    private final Map<String, Integer> cdTough;
    private final Map<String, Integer> pulling;
    private final Map<UUID, UUID> fishPowerMarker;
    private ServerListener serverListener = null;


    class cdPlayerRunnable implements Runnable {
        private final String player;
        private final int setId;
        public cdPlayerRunnable(String name, int id) {
            player = name;
            setId = id;
        }
        @Override
        public void run() {
            switch (setId) {
                case 0 -> {
                }
                case 1 -> {
                    if (Bukkit.getServer().getPlayer(player) != null) {
                        if (!cdTough.containsKey(player)) {
                            Player playerEntity = Bukkit.getServer().getPlayer(player);
                            double health = playerEntity.getHealth();
                            playerEntity.damage(0.1);
                            Bukkit.getServer().getPlayer(player).setHealth(Math.max(health-20.0f, 0.0f));
                            BukkitTask task = Bukkit.getScheduler().runTaskLater(pluginInstance, new cdPlayerRunnable(player, 2), 160);
                            BukkitTask taskCD = Bukkit.getScheduler().runTaskLater(pluginInstance, new cdPlayerRunnable(player, 3), 18000);
                            channelTough.put(player, task.getTaskId());
                            cdTough.put(player, taskCD.getTaskId());
                        }
                    }
                }
                case 2 -> {
                    if (Bukkit.getServer().getPlayer(player) != null) {
                        Bukkit.getServer().getPlayer(player).addPotionEffect((new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 5)));
                        Bukkit.getServer().getPlayer(player).setHealth(Bukkit.getServer().getPlayer(player).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                    }
                    channelTough.remove(player);
                }
                case 3 -> {
                    cdTough.remove(player);
                }
                default -> {
                }
            }
        }
    }

    public EntityListener(Plugin plugin) {
        fishPowerReturn = new HashSet<>();
        pluginInstance = plugin;
        fishPowerMarker = new HashMap<>();
        sunLightAgain = new HashSet<>();
        cdTough = new HashMap<>();
        cdSunLightAgain = new HashMap<>();
        pulling = new HashMap<>();
        channelTough = new HashMap<>();
    }
    public void setServerListener(ServerListener listener) {
        serverListener = listener;
    }
    public Listener getInstance() {
        return this;
    }
    public void clearCd() {
        cdTough.clear();
        cdSunLightAgain.clear();
        Bukkit.getLogger().info("[SolitudeBack] All player cd cleared");

    }
    public void clearCd(String player) {
        cdTough.remove(player);
        cdSunLightAgain.remove(player);
        Bukkit.getLogger().info("[SolitudeBack] " + player + " cd cleared");
    }
    @EventHandler
    public void onEntityHitBySpear(ProjectileHitEvent event){
        if (event.getHitEntity() != null) {
            fishPowerMarker.remove(event.getHitEntity().getUniqueId());
        }
        if (event.getHitEntity() != null && event.getHitEntity() instanceof LivingEntity entity && event.getEntity() instanceof Trident spear) {
            ItemStack spearSource = spear.getItem();
            NBTItem nbtItem = new NBTItem(spearSource);
            if (nbtItem.hasKey("attachment1") || nbtItem.hasKey("attachment2")) {
                if (event.getEntity().getOwnerUniqueId() != null && pluginInstance.getServer().getPlayer(event.getEntity().getOwnerUniqueId()) != null) {
                    Player player = pluginInstance.getServer().getPlayer(event.getEntity().getOwnerUniqueId());
                    // Effect on Shooter
                    // e.g. player.setHealth(Math.min(player.getHealth() + 4.0f, 20.0f));0f
                }
                if (Objects.equals(nbtItem.getString("attachment1"), "real_golden_fish") || Objects.equals(nbtItem.getString("attachment2"), "real_golden_fish")) {
                    fishPowerMarker.put(event.getHitEntity().getUniqueId(), spear.getOwnerUniqueId());
                }
            }

        }
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (fishPowerMarker.containsKey(event.getEntity().getUniqueId())) {
            if (fishPowerMarker.get(event.getEntity().getUniqueId()) != null) {
                fishPowerReturn.add(fishPowerMarker.get(event.getEntity().getUniqueId()));
            }
            fishPowerMarker.remove(event.getEntity().getUniqueId());
            float rate = (new Random()).nextFloat();
            if (rate < 0.8f) {
                event.getEntity().getWorld().dropItemNaturally((event.getEntity()).getLocation(), (new ItemStack(Material.DIAMOND, 2)));
            }
        }
        if (event.getEntity() instanceof Player player) {
            if (sunLightAgain.contains(player.getName())) {
                if (player.getBedSpawnLocation() != null && !cdSunLightAgain.containsKey(player.getName())) {
                    player.setHealth(1.0f);
                    BukkitTask task = Bukkit.getScheduler().runTaskLater(pluginInstance, new cdPlayerRunnable(player.getName(), 0), 18000);
                    cdSunLightAgain.put(player.getName(), task.getTaskId());
                    player.teleport(player.getBedSpawnLocation());
                    event.setCancelled(true);
                    return;
                }
            }
            serverListener.removeSleepless(player.getName());
        }
    }
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {

    }
    @EventHandler
    public void onPlayerShotSpear(ProjectileLaunchEvent event) {
        fishPowerMarker.remove(event.getEntity().getUniqueId());
        if (event.getEntity() instanceof Trident spear) {
            NBTEntity nbtEntity = new NBTEntity(spear);
            if (nbtEntity.hasKey("Trident") != null && nbtEntity.getCompound("Trident").hasKey("tag") && nbtEntity.getCompound("Trident").getCompound("tag").hasKey("attachment1")) {
                NBTCompound compound = nbtEntity.getCompound("Trident").getCompound("tag");
                ItemStack spearItem = CustomStack.getInstance("solitude:spear").getItemStack();
                NBTItem nbt_spear = new NBTItem(spearItem, true);
                nbt_spear.mergeCompound(compound);
                if (Objects.equals(nbt_spear.getString("attachment1"), "parchment") || Objects.equals(nbt_spear.getString("attachment2"), "parchment")) {
                    //Bukkit.getPlayer(event.getEntity().getOwnerUniqueId()).getInventory().addItem(spearItem);
                    event.setCancelled(true);
                }
                if (Objects.equals(nbt_spear.getString("attachment1"), "pianola_broken_strings") || Objects.equals(nbt_spear.getString("attachment2"), "pianola_broken_strings")) {
                    event.setCancelled(true);
                }
            }

        }
    }
    public boolean setSunLightAgainPlayer(String name) {
        return sunLightAgain.add(name);
    }
    public boolean removeSunLightAgainPlayer(String name) {
        return sunLightAgain.remove(name);
    }
    @EventHandler
    public void onEntityDamageByPlayer(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            ItemStack spearItem = player.getInventory().getItemInMainHand();
            if (spearItem.getType() != Material.AIR && spearItem.getAmount() != 0) {
                NBTItem nbt_spear = new NBTItem(spearItem, true);
                if (nbt_spear.hasKey("attachment1")) {
                    if (Objects.equals(nbt_spear.getString("attachment1"), "real_golden_fish")) {
                        fishPowerMarker.put(event.getEntity().getUniqueId(), null);
                        if (nbt_spear.getByte("count1") < 1 ) {
                            nbt_spear.setByte("count1", (byte) (nbt_spear.getByte("count1")+1));
                        }
                        else {
                            nbt_spear.setString("attachment1", "air");
                            nbt_spear.setByte("count1", (byte)0);
                        }
                    }
                    else if (Objects.equals(nbt_spear.getString("attachment2"), "real_golden_fish")) {
                        fishPowerMarker.put(event.getEntity().getUniqueId(), null);
                        if (nbt_spear.getByte("count2") < 1 ) {
                            nbt_spear.setByte("count2", (byte) (nbt_spear.getByte("count2")+1));
                        }
                        else {
                            nbt_spear.setString("attachment2", "air");
                            nbt_spear.setByte("count2", (byte)0);
                        }
                    }
                }
            }
        }
    }
    @EventHandler
    public void onPlayerResurrect(EntityResurrectEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (sunLightAgain.contains(player.getName())) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onPlayerPickUpSpear(PlayerPickupArrowEvent event) {
        if (event.getArrow() instanceof Trident spear) {
            NBTEntity nbtEntity = new NBTEntity(spear);
            if (nbtEntity.hasKey("Trident") != null && nbtEntity.getCompound("Trident").hasKey("tag") && nbtEntity.getCompound("Trident").getCompound("tag").hasKey("attachment1")) {
                NBTCompound compound = nbtEntity.getCompound("Trident").getCompound("tag");
                ItemStack spearItem = CustomStack.getInstance("solitude:spear").getItemStack();
                NBTItem nbt_spear = new NBTItem(spearItem, true);
                nbt_spear.mergeCompound(compound);
                if (fishPowerReturn.contains(event.getPlayer().getUniqueId())) {
                    if (Objects.equals(nbt_spear.getString("attachment1"), "real_golden_fish") || Objects.equals(nbt_spear.getString("attachment2"), "real_golden_fish")) {
                        if (Objects.equals(nbt_spear.getString("attachment1"), "real_golden_fish")) {
                            if (nbt_spear.getByte("count1") < 1 ) {
                                nbt_spear.setByte("count1", (byte) (nbt_spear.getByte("count1")+1));
                            }
                            else {
                                nbt_spear.setString("attachment1", "air");
                                nbt_spear.setByte("count1", (byte)0);
                            }
                            fishPowerReturn.remove(event.getPlayer().getUniqueId());
                        }
                        else if (Objects.equals(nbt_spear.getString("attachment2"), "real_golden_fish")) {
                            if (nbt_spear.getByte("count2") < 1 ) {
                                nbt_spear.setByte("count2", (byte) (nbt_spear.getByte("count2")+1));
                            }
                            else {
                                nbt_spear.setString("attachment2", "air");
                                nbt_spear.setByte("count2", (byte)0);
                            }
                            fishPowerReturn.remove(event.getPlayer().getUniqueId());
                        }
                    }
                }
                if (Objects.equals(nbt_spear.getString("attachment1"), "sleepless_pony") || Objects.equals(nbt_spear.getString("attachment2"), "sleepless_pony")) {
                    serverListener.addSleepless(event.getPlayer().getName());
                }
                event.getPlayer().getInventory().addItem(spearItem);
                event.getArrow().remove();
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onPlayerChangeSlot(PlayerItemHeldEvent event) {
        ItemStack spear = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
        if (spear != null) {
            NBTItem nbt_spear = new NBTItem(spear);
            if (nbt_spear.hasKey("attachment1")) {
                if (Objects.equals(nbt_spear.getString("attachment1"), "parchment") || Objects.equals(nbt_spear.getString("attachment2"), "parchment")) {
                    if (pulling.containsKey(event.getPlayer().getName())) {
                        Bukkit.getScheduler().cancelTask(pulling.get(event.getPlayer().getName()));
                        pulling.remove(event.getPlayer().getName());
                    }
                }
            }
        }
    }
    @EventHandler
    public void onPlayerStopPulling(PlayerStopUsingItemEvent event) {
        if ((new NBTItem(event.getItem())).hasKey("attachment1")) {
            if (Objects.equals((new NBTItem(event.getItem())).getString("attachment1"), "parchment") || Objects.equals((new NBTItem(event.getItem())).getString("attachment2"), "parchment")) {
                if (pulling.containsKey(event.getPlayer().getName())) {
                    Bukkit.getScheduler().cancelTask(pulling.get(event.getPlayer().getName()));
                    pulling.remove(event.getPlayer().getName());
                }
            }
        }
    }
    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        if (event.getItem() != null && (new NBTItem(event.getItem())).hasKey("spear_attachment")) {
            if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.JUKEBOX) {
                event.setCancelled(true);
                return;
            }
        }
        if (event.getItem() != null && (new NBTItem(event.getItem())).hasKey("attachment1")) {
            //open inventory
            if (event.getPlayer().isSneaking() && event.getAction() == Action.RIGHT_CLICK_AIR) {
                SpearAttachment spearAttachment = new SpearAttachment(event.getItem());
                event.setCancelled(true);
                event.getPlayer().openInventory(spearAttachment.getInventory());
                return;
            }
            //bone ashes effect
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && Objects.equals((new NBTItem(event.getItem())).getString("attachment1"), "bone_ashes") || Objects.equals((new NBTItem(event.getItem())).getString("attachment2"), "bone_ashes")) {
                if (event.getClickedBlock() != null && isDirtFood(event.getClickedBlock().getType())) {
                    if (event.getPlayer().getFoodLevel() < 20) {
                        event.getPlayer().setFoodLevel(event.getPlayer().getFoodLevel() + 2);
                        event.getPlayer().setSaturation(2.8f);
                        event.getItem().damage(50, event.getPlayer());
                    }
                }
            }
            // parchment pulling
            if (event.getAction().isRightClick() && Objects.equals((new NBTItem(event.getItem())).getString("attachment1"), "parchment") || Objects.equals((new NBTItem(event.getItem())).getString("attachment2"), "parchment")) {
                if (!cdTough.containsKey(event.getPlayer().getName()) && event.getClickedBlock() != null && event.getClickedBlock().getType().isInteractable()) {

                }
                else {
                    BukkitTask task = Bukkit.getScheduler().runTaskLater(pluginInstance, new cdPlayerRunnable(event.getPlayer().getName(), 1), 20);
                    pulling.put(event.getPlayer().getName(), task.getTaskId());
                }
            }
        }
    }

    private boolean isDirtFood(@NotNull Material block) {
        return block == Material.DIRT || block == Material.GRASS_BLOCK || block == Material.PODZOL || block == Material.COARSE_DIRT || block == Material.DIRT_PATH || block == Material.FARMLAND || block == Material.MYCELIUM;
    }

}
