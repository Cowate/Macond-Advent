package com.cowate.solitudeback;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.units.qual.N;

public class NameSpacedKeys {
    public static NamespacedKey ATTACHMENT_ID;

    public static NamespacedKey SPEAR;
    public static NamespacedKey SPEAR_ATTACHMENT;
    public static NamespacedKey ATTACHMENT_1;
    public static NamespacedKey COUNT_1;
    public static NamespacedKey ATTACHMENT_2;
    public static NamespacedKey COUNT_2;
    public static NamespacedKey HEALING_POINT;
    public static NamespacedKey DROPPING_MULTIPLE;
    public static NamespacedKey FLAMMABLE;
    public static NamespacedKey RESISTANCE;
    public static NamespacedKey DUST_CONSUMER;
    public static NamespacedKey INSOMNIA;

    public static NamespacedKey ICE_CUBE;
    public static NamespacedKey SEXTANT;
    public static NamespacedKey MAGNIFIER;
    public static NamespacedKey MIRROR;
    public static NamespacedKey BONE_ASHES;
    public static NamespacedKey SLEEPLESS_PONY;
    public static NamespacedKey IMITATION_GOLDEN_FISH;
    public static NamespacedKey SPEAR_GRID;

    public static void register(Plugin pluginInstance) {
        ATTACHMENT_ID = new NamespacedKey(pluginInstance, "attachment_id");
        SPEAR = new NamespacedKey(pluginInstance, "spear");
        SPEAR_ATTACHMENT = new NamespacedKey(pluginInstance, "spear_attachment");
        ATTACHMENT_1 = new NamespacedKey(pluginInstance, "attachment1");
        COUNT_1 = new NamespacedKey(pluginInstance, "count1");
        ATTACHMENT_2 = new NamespacedKey(pluginInstance, "attachment2");
        COUNT_2 = new NamespacedKey(pluginInstance, "count2");
        HEALING_POINT = new NamespacedKey(pluginInstance, "healing_point");
        DROPPING_MULTIPLE = new NamespacedKey(pluginInstance, "dropping_multiple");
        FLAMMABLE = new NamespacedKey(pluginInstance, "flammable");
        RESISTANCE = new NamespacedKey(pluginInstance, "resistance");
        DUST_CONSUMER = new NamespacedKey(pluginInstance, "dust_consumer");
        INSOMNIA = new NamespacedKey(pluginInstance, "insomnia");

        ICE_CUBE = new NamespacedKey(pluginInstance, "ice_cube");
        SEXTANT = new NamespacedKey(pluginInstance, "sextant");
        MAGNIFIER = new NamespacedKey(pluginInstance, "magnifier");
        MIRROR = new NamespacedKey(pluginInstance, "mirror");
        BONE_ASHES = new NamespacedKey(pluginInstance, "bone_ashes");
        SLEEPLESS_PONY = new NamespacedKey(pluginInstance, "sleepless_pony");

        IMITATION_GOLDEN_FISH = new NamespacedKey(pluginInstance, "imitation_golden_fish");
        SPEAR_GRID = new NamespacedKey(pluginInstance, "spear_grid");

    }
}
