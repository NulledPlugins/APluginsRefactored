/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.runelite.client.config.Config
 *  net.runelite.client.config.ConfigGroup
 *  net.runelite.client.config.ConfigItem
 *  net.runelite.client.config.ConfigSection
 *  net.runelite.client.config.Range
 */
package net.runelite.client.plugins.avorkath;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup(value="avork")
public interface AVorkathConfig
extends Config {
    @ConfigSection(keyName="delayConfig", name="Sleep Delay Configuration", description="Configure how the bot handles sleep delays", position=0)
    public static final String delayConfig = "delayConfig";
    @ConfigSection(keyName="pluginConfig", name="Plugin Configuration", description="", position=14)
    public static final String pluginConfig = "pluginConfig";

    @ConfigItem(keyName="key", name="License Key", description="", position=0)
    default public String key() {
        return "";
    }

    @Range(min=0, max=550)
    @ConfigItem(keyName="sleepMin", name="Sleep Min", description="", position=2, section="delayConfig")
    default public int sleepMin() {
        return 60;
    }

    @Range(min=0, max=550)
    @ConfigItem(keyName="sleepMax", name="Sleep Max", description="", position=3, section="delayConfig")
    default public int sleepMax() {
        return 350;
    }

    @Range(min=0, max=550)
    @ConfigItem(keyName="sleepTarget", name="Sleep Target", description="", position=4, section="delayConfig")
    default public int sleepTarget() {
        return 100;
    }

    @Range(min=0, max=550)
    @ConfigItem(keyName="sleepDeviation", name="Sleep Deviation", description="", position=5, section="delayConfig")
    default public int sleepDeviation() {
        return 10;
    }

    @ConfigItem(keyName="sleepWeightedDistribution", name="Sleep Weighted Distribution", description="Shifts the random distribution towards the lower end at the target, otherwise it will be an even distribution", position=6, section="delayConfig")
    default public boolean sleepWeightedDistribution() {
        return false;
    }

    @Range(min=0, max=10)
    @ConfigItem(keyName="tickDelayMin", name="Game Tick Min", description="", position=8, section="delayConfig")
    default public int tickDelayMin() {
        return 1;
    }

    @Range(min=0, max=10)
    @ConfigItem(keyName="tickDelayMax", name="Game Tick Max", description="", position=9, section="delayConfig")
    default public int tickDelayMax() {
        return 3;
    }

    @Range(min=0, max=10)
    @ConfigItem(keyName="tickDelayTarget", name="Game Tick Target", description="", position=10, section="delayConfig")
    default public int tickDelayTarget() {
        return 2;
    }

    @Range(min=0, max=10)
    @ConfigItem(keyName="tickDelayDeviation", name="Game Tick Deviation", description="", position=11, section="delayConfig")
    default public int tickDelayDeviation() {
        return 1;
    }

    @ConfigItem(keyName="tickDelayWeightedDistribution", name="Game Tick Weighted Distribution", description="Shifts the random distribution towards the lower end at the target, otherwise it will be an even distribution", position=12, section="delayConfig")
    default public boolean tickDelayWeightedDistribution() {
        return false;
    }

    @ConfigItem(keyName="supers", name="Super Pots", description="Enable to use Bastion potions for ranged, Super Combat for melee. Disable to use ranging potions for ranged, Combat potions for melee.", position=59)
    default public boolean supers() {
        return true;
    }

    @ConfigItem(keyName="useRanged", name="Ranged Mode", description="If disabled, uses melee", position=57)
    default public boolean useRanged() {
        return true;
    }

    @ConfigItem(keyName="useBlowpipe", name="Blowpipe", description="If disabled, will attempt to use Bolts", position=58)
    default public boolean useBlowpipe() {
        return false;
    }

    @ConfigItem(keyName="enableVorkath", name="Auto Prayer", description="Toggles Quick Prayers during Vorkath encounter", position=60)
    default public boolean enableVorkath() {
        return true;
    }

    @ConfigItem(keyName="AcidWalk", name="Acid Walk", description="", position=61)
    default public boolean AcidWalk() {
        return true;
    }

    @ConfigItem(keyName="Retaliate", name="Retaliate", description="", position=62)
    default public boolean Retaliate() {
        return true;
    }

    @ConfigItem(keyName="DodgeBomb", name="Dodge Bombs", description="", position=63)
    default public boolean DodgeBomb() {
        return true;
    }

    @ConfigItem(keyName="KillSpawns", name="Kill Undead Spawns", description="Casts crumble undead on undead spawns", position=64)
    default public boolean KillSpawns() {
        return true;
    }

    @ConfigItem(keyName="lootNames", name="Items to loot (separate with comma)", description="Provide partial or full names of items you'd like to loot.", position=65)
    default public String lootNames() {
        return "head,key,visage,hide,bone,rune,diamond,ore,bolt,seed,bar,uncut,jar,arrow,logs,dragon,grapes,manta,coins,battlestaff,wrath";
    }

    @ConfigItem(keyName="superantifire", name="Ext Super Antifire", description="Enable to use Extended Super Antifire. Disable to use regular antifire.", position=66)
    default public boolean superantifire() {
        return true;
    }

    @ConfigItem(keyName="antivenomplus", name="Anti Venom+", description="Enable to use Anti-venom+. Disable to use Antipoison++", position=67)
    default public boolean antivenomplus() {
        return true;
    }

    @ConfigItem(keyName="usePOHpool", name="Drink POH Pool", description="Enable to drink from POH pool to restore HP / Prayer.", position=68)
    default public boolean usePOHpool() {
        return true;
    }

    @ConfigItem(keyName="praypotAmount", name="Amount of Super Restores", description="Amount of super restores to withdraw from the bank", position=69)
    default public int praypotAmount() {
        return 2;
    }

    @ConfigItem(keyName="useRestores", name="Use Super Restores", description="Disable to use Prayer Potions", position=70)
    default public boolean useRestores() {
        return true;
    }

    @ConfigItem(keyName="onlytelenofood", name="Only Tele With No Food", description="Enable to only teleport out when you have 0 food and / or 0 restore pots. Disable to teleport out after every kill.", position=71)
    default public boolean onlytelenofood() {
        return false;
    }

    @ConfigItem(keyName="foodAmount", name="Amount of food 1", description="Amount of food to withdraw", position=79)
    default public int foodAmount() {
        return 17;
    }

    @ConfigItem(keyName="foodID", name="ID of food 1", description="ID of food to withdraw.", position=80)
    default public int foodID() {
        return 385;
    }

    @ConfigItem(keyName="foodAmount2", name="Amount of food 2", description="Amount of food to withdraw", position=81)
    default public int foodAmount2() {
        return 4;
    }

    @ConfigItem(keyName="foodID2", name="ID of food 2", description="ID of food to withdraw.", position=82)
    default public int foodID2() {
        return 3144;
    }

    default public boolean useDivines() {
        return false;
    }
}

