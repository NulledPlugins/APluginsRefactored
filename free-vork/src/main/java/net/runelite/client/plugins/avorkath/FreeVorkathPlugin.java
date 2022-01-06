/*
 * Decompiled with CFR 0.151.
 *
 * Could not load the following classes:
 *  com.google.inject.Provides
 *  javax.inject.Inject
 *  net.runelite.api.ChatMessageType
 *  net.runelite.api.Client
 *  net.runelite.api.GameObject
 *  net.runelite.api.GameState
 *  net.runelite.api.MenuAction
 *  net.runelite.api.MenuEntry
 *  net.runelite.api.NPC
 *  net.runelite.api.Player
 *  net.runelite.api.Prayer
 *  net.runelite.api.Projectile
 *  net.runelite.api.Renderable
 *  net.runelite.api.Skill
 *  net.runelite.api.TileItem
 *  net.runelite.api.VarPlayer
 *  net.runelite.api.Varbits
 *  net.runelite.api.coords.LocalPoint
 *  net.runelite.api.coords.WorldArea
 *  net.runelite.api.coords.WorldPoint
 *  net.runelite.api.events.AnimationChanged
 *  net.runelite.api.events.ChatMessage
 *  net.runelite.api.events.ClientTick
 *  net.runelite.api.events.ConfigButtonClicked
 *  net.runelite.api.events.GameObjectDespawned
 *  net.runelite.api.events.GameObjectSpawned
 *  net.runelite.api.events.GameStateChanged
 *  net.runelite.api.events.GameTick
 *  net.runelite.api.events.ItemDespawned
 *  net.runelite.api.events.ItemSpawned
 *  net.runelite.api.events.NpcDespawned
 *  net.runelite.api.events.NpcSpawned
 *  net.runelite.api.events.ProjectileMoved
 *  net.runelite.api.events.ProjectileSpawned
 *  net.runelite.api.widgets.Widget
 *  net.runelite.api.widgets.WidgetInfo
 *  net.runelite.api.widgets.WidgetItem
 *  net.runelite.client.callback.ClientThread
 *  net.runelite.client.config.ConfigManager
 *  net.runelite.client.eventbus.Subscribe
 *  net.runelite.client.events.ConfigChanged
 *  net.runelite.client.game.ItemManager
 *  net.runelite.client.input.KeyManager
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.plugins.PluginDependency
 *  net.runelite.client.plugins.PluginDescriptor
 *  net.runelite.client.plugins.autils.AUtils
 *  net.runelite.client.plugins.autils.Spells
 *  net.runelite.client.ui.overlay.Overlay
 *  net.runelite.client.ui.overlay.OverlayManager
 *  net.runelite.client.ui.overlay.infobox.InfoBoxManager
 *  org.pf4j.Extension
 */
package net.runelite.client.plugins.avorkath;

import com.google.inject.Provides;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.autils.FreeUtils;
import net.runelite.client.plugins.autils.Spells;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import org.apache.commons.lang3.ArrayUtils;
import org.pf4j.Extension;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.*;

@Extension
@PluginDependency(value = FreeUtils.class)
@PluginDescriptor(name = "Free Vorkath", description = "Free Auto Vorkath.", tags = {"vorkath", "free", "plugins"}, enabledByDefault = false)
public class FreeVorkathPlugin extends Plugin {





    private static final List<Integer> vorkathRegions = Arrays.asList(7513, 7514, 7769, 7770);
    private final Set<Integer> O = Set.of(Integer.valueOf(21946), Integer.valueOf(9243));
    private final Set<Integer> rubyBoltIDs = Set.of(Integer.valueOf(21944), Integer.valueOf(9242));
    private boolean doingAcidWalk = true;
    private WorldArea c = new WorldArea(new WorldPoint(3082, 3485, 0), new WorldPoint(3100, 3502, 0));
    private WorldArea d = new WorldArea(new WorldPoint(2664, 3625, 0), new WorldPoint(2678, 3638, 0));
    private WorldArea e = new WorldArea(new WorldPoint(2635, 3668, 0), new WorldPoint(2652, 3684, 0));
    private WorldArea f = new WorldArea(new WorldPoint(2262, 4032, 0), new WorldPoint(2286, 4053, 0));
    private List<String> lootNamesList = new ArrayList<>();
    private List<TileItem> itemsToLoot = new ArrayList<>();
    private Player localPlayer;
    private MenuEntry k;
    private LocalPoint l;
    private LocalPoint m;
    private boolean notFrozen = true;
    private boolean zombieSpawnDead = true;
    private boolean deposited = false;
    private String[] lootNames;
    private int tickDelay;
    private NPC vorkathNpc;
    private List<WorldPoint> acidSpots = new ArrayList<WorldPoint>();
    private List<WorldPoint> spotsForAcidWalk = new ArrayList<WorldPoint>();
    private int amountOfAcidSpots = 0;
    private int ah = 3;
    private boolean U = false;



    public AVorkathState currentTask;
    public Instant o;
    public boolean running;


    @Inject
    ConfigManager configManager;
    @Inject
    private Client client;
    @Inject
    private AVorkathConfig config;
    @Inject
    private ClientThread clientThread;
    @Inject
    private ItemManager itemManager;
    @Inject
    private FreeUtils utils;
    @Inject
    private KeyManager keyManager;
    @Inject
    private InfoBoxManager infoBoxManager;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private FreeVorkathOverlay overlay;

    private static void setRenderableVisible(Renderable renderable, boolean bl) {
        Method method;
        try {
            method = renderable.getClass().getMethod("setHidden", Boolean.TYPE);
        } catch (NoSuchMethodException noSuchMethodException) {
            return;
        }
        try {
            method.invoke(renderable, bl);
            return;
        } catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
            return;
        }
    }

    public static boolean atPOH(Client client) {
        return Arrays.stream(client.getMapRegions()).anyMatch(vorkathRegions::contains);
    }

    @Provides
    AVorkathConfig getConfig(ConfigManager configManager) {
        return (AVorkathConfig) configManager.getConfig(AVorkathConfig.class);
    }

    private void resetPlugin() {
        this.itemsToLoot.clear();
        this.lootNamesList.clear();
        this.lootNames = this.config.lootNames().toLowerCase().split("\\s*,\\s*");
        if (!this.config.lootNames().isBlank()) {
            this.lootNamesList.addAll(Arrays.asList(this.lootNames));
        }
        this.overlayManager.remove(this.overlay);
        this.running = false;
        this.currentTask = null;
        this.tickDelay = 0;
        this.U = false;
        this.notFrozen = true;
        this.zombieSpawnDead = true;
        this.l = null;
        this.m = null;
        this.deposited = false;
        this.o = null;
    }

    @Subscribe
    private void onConfigButtonClicked(ConfigButtonClicked configButtonClicked) {
        if (configButtonClicked.getGroup().equalsIgnoreCase("avork")) return;
    }

    private void setSpell(WidgetInfo widgetInfo) {
        Widget w;
        if ((w = this.client.getWidget(widgetInfo)) == null) {
            return;
        }
        this.client.setSelectedSpellName("<col=00ff00>" + w.getName() + "</col>");
        this.client.setSelectedSpellWidget(widgetInfo.getId());
        this.client.setSelectedSpellChildIndex(-1);
    }

    public void onConfigChanged(ConfigChanged configChanged) {
        if (!configChanged.getGroup().equals("avork")) {
            return;
        }
        this.resetPlugin();
    }

    private long getSleepDelay() {
        return this.utils.randomDelay(this.config.sleepWeightedDistribution(), this.config.sleepMin(), this.config.sleepMax(), this.config.sleepDeviation(), this.config.sleepTarget());
    }

    private int getTickDelay() {
        return (int) this.utils.randomDelay(this.config.tickDelayWeightedDistribution(), this.config.tickDelayMin(), this.config.tickDelayMax(), this.config.tickDelayDeviation(), this.config.tickDelayTarget());
    }

    protected void startUp() {
        this.resetPlugin();
        if (!this.running) {
            this.running = true;
            this.o = Instant.now();
            this.currentTask = null;
            this.k = null;
            this.itemsToLoot.clear();
            this.overlayManager.add(this.overlay);
            return;
        }
        this.running = false;
        this.resetPlugin();
        this.overlayManager.remove(this.overlay);
    }

    protected void shutDown() {
        this.resetPlugin();
    }

    private void findBank() {
        GameObject gameObject = this.utils.findNearestBankNoDepositBoxes();
        if (gameObject == null) return;
        this.clientThread.invoke(() -> this.openBankAction(gameObject));
    }

    private void lootItems(List<TileItem> tileItem) {
        TileItem t;
        if ((t = this.getClosestTileItem(tileItem)) == null) return;

        this.clientThread.invoke(() -> this.lootItemAction(t));
    }

    private TileItem getClosestTileItem(List<TileItem> list) {
        TileItem tileItem = list.get(0);
        int n = tileItem.getTile().getWorldLocation().distanceTo(this.localPlayer.getWorldLocation());
        Iterator<TileItem> iterator = list.iterator();
        while (iterator.hasNext()) {
            TileItem tileItem2 = iterator.next();
            int n2 = tileItem2.getTile().getWorldLocation().distanceTo(this.localPlayer.getWorldLocation());
            if (n2 >= n) continue;
            tileItem = tileItem2;
            n = n2;
        }
        return tileItem;
    }

    public WidgetItem getPrayerPotionWidget() {
        WidgetItem widgetItem = PrayerRestoreType.PRAYER_POTION.getItemFromInventory(this.client);
        if (widgetItem != null) {
            return widgetItem;
        }
        widgetItem = PrayerRestoreType.SANFEW_SERUM.getItemFromInventory(this.client);
        if (widgetItem == null) return PrayerRestoreType.SUPER_RESTORE.getItemFromInventory(this.client);
        return widgetItem;
    }

    public WidgetItem getFoodWidget() {
        WidgetItem widgetItem = this.utils.getInventoryWidgetItem(this.config.foodID());
        if (widgetItem == null) return widgetItem;
        return widgetItem;
    }

    public WidgetItem getRangedItemWidget() {
        WidgetItem widgetItem = PrayerRestoreType.RANGED.getItemFromInventory(this.client);
        if (widgetItem == null) return widgetItem;
        return widgetItem;
    }

    public WidgetItem getCombatItemWidget() {
        WidgetItem widgetItem = PrayerRestoreType.COMBAT.getItemFromInventory(this.client);
        if (widgetItem == null) return widgetItem;
        return widgetItem;
    }

    public WidgetItem getAntifireWidget() {
        WidgetItem widgetItem = PrayerRestoreType.ANTIFIRE.getItemFromInventory(this.client);
        if (widgetItem == null) return widgetItem;
        return widgetItem;
    }

    public WidgetItem getAntivenomWidget() {
        WidgetItem widgetItem = PrayerRestoreType.ANTIVENOM.getItemFromInventory(this.client);
        if (widgetItem == null) return widgetItem;
        return widgetItem;
    }

    public AVorkathState calcTask() {
        if (this.tickDelay > 0) {
            return AVorkathState.TIMEOUT;
        }
        if (!this.utils.isBankOpen()) return this.getTask();
        return this.getBankTask();
    }

    @Nullable
    NPC getSleepingVorkathNpc() {
        return this.utils.findNearestNpc(new int[]{8059});
    }

    private boolean atVorky() {
        return ArrayUtils.contains(this.client.getMapRegions(), 9023);
    }

    private AVorkathState getTask() {
        if (!this.zombieSpawnDead && !this.atVorky()) {
            this.zombieSpawnDead = true;
        }
        if (!this.notFrozen && !this.atVorky()) {
            this.notFrozen = true;
        }
        if (this.utils.findNearestNpc(new int[]{8059}) != null && this.atVorky()) {
            this.spotsForAcidWalk.clear();
            this.acidSpots.clear();
        }
        if (FreeVorkathPlugin.atPOH(this.client) && this.utils.inventoryContains(this.rubyBoltIDs) && !this.utils.isItemEquipped(this.rubyBoltIDs) && this.config.useRanged() && !this.config.useBlowpipe()) {
            return AVorkathState.EQUIP_RUBIES;
        }
        if (!this.atVorky() && this.utils.inventoryContains(this.rubyBoltIDs) && !this.utils.isItemEquipped(this.rubyBoltIDs) && this.config.useRanged() && !this.config.useBlowpipe()) {
            return AVorkathState.EQUIP_RUBIES;
        }
        if (this.atVorky()) {
            FreeVorkathPlugin freeVorkathPlugin = this;
            if (freeVorkathPlugin.getNpcHP(freeVorkathPlugin.vorkathNpc, 750) > 265) {
                FreeVorkathPlugin freeVorkathPlugin2 = this;
                if (freeVorkathPlugin2.getNpcHP(freeVorkathPlugin2.vorkathNpc, 750) <= 750 && this.utils.inventoryContains(this.rubyBoltIDs) && !this.utils.isItemEquipped(this.rubyBoltIDs) && this.acidSpots.isEmpty() && this.config.useRanged() && !this.config.useBlowpipe()) {
                    return AVorkathState.EQUIP_RUBIES;
                }
            }
        }
        if (this.atVorky()) {
            FreeVorkathPlugin freeVorkathPlugin = this;
            if (freeVorkathPlugin.getNpcHP(freeVorkathPlugin.vorkathNpc, 750) < 265 && this.utils.inventoryContains(this.O) && !this.utils.isItemEquipped(this.O) && this.acidSpots.isEmpty() && this.config.useRanged() && !this.config.useBlowpipe()) {
                return AVorkathState.EQUIP_DIAMONDS;
            }
        }
        if (this.client.getVar(Varbits.QUICK_PRAYER) == 1 && !this.atVorky()) {
            return AVorkathState.DEACTIVATE_PRAY;
        }
        if (FreeVorkathPlugin.atPOH(this.client) && this.client.getBoostedSkillLevel(Skill.PRAYER) < this.client.getRealSkillLevel(Skill.PRAYER) && this.config.usePOHpool()) {
            return AVorkathState.DRINK_POOL;
        }
        if (!this.itemsToLoot.isEmpty() && !this.utils.inventoryFull() && this.atVorky()) {
            return AVorkathState.LOOT_ITEMS;
        }
        if (this.utils.inventoryContains(22124) && this.itemsToLoot.isEmpty() && !FreeVorkathPlugin.atPOH(this.client) && this.atVorky() && !this.config.onlytelenofood()) {
            return AVorkathState.WALK_SECOND;
        }
        if (!this.utils.inventoryContains(this.config.foodID()) && this.itemsToLoot.isEmpty() && !FreeVorkathPlugin.atPOH(this.client) && this.atVorky()) {
            return AVorkathState.WALK_SECOND;
        }
        if (this.utils.inventoryContains(this.config.foodID()) && this.utils.inventoryFull() && !this.itemsToLoot.isEmpty() && !FreeVorkathPlugin.atPOH(this.client) && this.atVorky()) {
            return AVorkathState.EAT_FOOD;
        }
        if (this.getPrayerPotionWidget() == null && this.atVorky()) {
            return AVorkathState.WALK_SECOND;
        }
        if (FreeVorkathPlugin.atPOH(this.client)) {
            return AVorkathState.TELE_EDGE;
        }
        if (this.localPlayer.getWorldArea().intersectsWith(this.c) && this.deposited) {
            return AVorkathState.WALK_FIRST;
        }
        if (this.localPlayer.getWorldArea().intersectsWith(this.c) && !this.deposited) {
            return AVorkathState.FIND_BANK;
        }
        if (this.localPlayer.getWorldArea().intersectsWith(this.d)) {
            return AVorkathState.WALK_THIRD;
        }
        if (this.localPlayer.getWorldArea().intersectsWith(this.e)) {
            return AVorkathState.USE_BOAT;
        }
        if (this.localPlayer.getWorldArea().intersectsWith(this.f)) {
            return AVorkathState.JUMP_OBSTACLE;
        }
        if (!this.acidSpots.isEmpty() && this.atVorky()) {
            return AVorkathState.ACID_WALK;
        }
        if (!this.notFrozen && this.atVorky()) {
            return AVorkathState.HANDLE_BOMB;
        }
        if (!this.zombieSpawnDead && this.atVorky()) {
            return AVorkathState.HANDLE_ICE;
        }
        if (this.config.antivenomplus() && this.client.getVar(VarPlayer.IS_POISONED) > 0 && this.atVorky()) {
            return AVorkathState.DRINK_ANTIVENOM;
        }
        if (this.client.getBoostedSkillLevel(Skill.RANGED) == this.client.getRealSkillLevel(Skill.RANGED) && this.atVorky() && this.config.useRanged()) {
            return AVorkathState.DRINK_RANGE;
        }
        if (this.client.getBoostedSkillLevel(Skill.STRENGTH) == this.client.getRealSkillLevel(Skill.STRENGTH) && this.atVorky() && !this.config.useRanged()) {
            return AVorkathState.DRINK_COMBAT;
        }
        if (this.config.superantifire() && this.client.getVarbitValue(6101) == 0 && this.atVorky()) {
            return AVorkathState.DRINK_ANTIFIRE;
        }
        if (!this.config.superantifire() && this.client.getVarbitValue(3981) == 0 && this.atVorky()) {
            return AVorkathState.DRINK_ANTIFIRE;
        }
        if (this.client.getVar(Varbits.QUICK_PRAYER) == 0 && this.atVorky()) {
            return AVorkathState.ACTIVATE_PRAY;
        }
        if (this.utils.findNearestNpc(new int[]{8059}) != null && this.atVorky() && this.itemsToLoot.isEmpty()) {
            return AVorkathState.WAKE_VORKATH;
        }
        if (this.localPlayer.getWorldLocation().distanceTo(this.vorkathNpc.getWorldArea()) <= 1 && this.config.useRanged()) {
            return AVorkathState.MOVE_AWAY;
        }
        if (!this.notFrozen) return AVorkathState.TIMEOUT;
        if (!this.zombieSpawnDead) return AVorkathState.TIMEOUT;
        if (this.utils.findNearestNpc(new int[]{8061}) == null) return AVorkathState.TIMEOUT;
        if (!this.acidSpots.isEmpty()) return AVorkathState.TIMEOUT;
        if (this.vorkathNpc == null) return AVorkathState.TIMEOUT;
        if (this.client.getLocalPlayer().getInteracting() == this.vorkathNpc) return AVorkathState.TIMEOUT;
        if (!this.atVorky()) return AVorkathState.TIMEOUT;
        return AVorkathState.ATTACK_VORKATH;
    }

    private AVorkathState getBankTask() {
        if (!this.deposited) {
            this.utils.depositAll();
            this.deposited = true;
            return AVorkathState.DEPOSIT_ITEMS;
        }
        if (!this.utils.inventoryContains(2444) && this.config.useRanged() && !this.config.supers()) {
            return AVorkathState.WITHDRAW_RANGED;
        }
        if (!this.utils.inventoryContains(22461) && this.config.useRanged() && this.config.supers()) {
            return AVorkathState.WITHDRAW_RANGED;
        }
        if (!this.utils.inventoryContains(12695) && !this.config.useRanged()) {
            return AVorkathState.WITHDRAW_COMBAT;
        }
        if (this.config.antivenomplus() && !this.utils.inventoryContains(12913)) {
            return AVorkathState.WITHDRAW_VENOM;
        }
        if (!this.config.antivenomplus() && !this.utils.inventoryContains(5952)) {
            return AVorkathState.WITHDRAW_VENOM;
        }
        if (this.config.superantifire() && !this.utils.inventoryContains(22209)) {
            return AVorkathState.WITHDRAW_ANTIFIRE;
        }
        if (!this.config.superantifire() && !this.utils.inventoryContains(2452)) {
            return AVorkathState.WITHDRAW_ANTIFIRE;
        }
        if (!this.utils.inventoryContains(12791)) {
            return AVorkathState.WITHDRAW_POUCH;
        }
        if (!this.utils.inventoryContains(3024) && !this.utils.inventoryContains(2434)) {
            return AVorkathState.WITHDRAW_RESTORES;
        }
        if (!this.utils.inventoryContains(8013)) {
            return AVorkathState.WITHDRAW_TELES;
        }
        if (!this.utils.inventoryContains(this.O) && this.config.useRanged() && !this.config.useBlowpipe()) {
            return AVorkathState.WITHDRAW_BOLTS;
        }
        if (!this.utils.inventoryContains(this.config.foodID())) {
            return AVorkathState.WITHDRAW_FOOD1;
        }
        if (!this.utils.inventoryContains(this.config.foodID2())) {
            return AVorkathState.WITHDRAW_FOOD2;
        }
        if (!this.localPlayer.getWorldArea().intersectsWith(this.c)) return AVorkathState.TIMEOUT;
        if (!this.utils.inventoryContains(this.config.foodID2())) return AVorkathState.TIMEOUT;
        if (!this.deposited) return AVorkathState.TIMEOUT;
        return AVorkathState.WALK_FIRST;
    }

    @Subscribe
    private void onGameTick(GameTick gameTick) {
        this.localPlayer = this.client.getLocalPlayer();
        if (this.client == null) return;
        if (this.localPlayer == null) return;
        if (!this.running) {
            return;
        }
        if (!this.client.isResized()) {
            this.utils.sendGameMessage("client must be set to resizable mode");
            return;
        }
        this.currentTask = this.calcTask();
        //this.localPlayerLocation = this.localPlayer.getLocalLocation();
        this.utils.setMenuEntry(null);

        WidgetItem wi;
        GameObject obj;
        NPC npc;
        WorldPoint loc;
        LocalPoint lLoc;


        switch (this.currentTask) {
            case TIMEOUT: {
                this.utils.handleRun(30, 20);
                --this.tickDelay;
                return;
            }
            case MOVE_AWAY: {
                this.utils.walk(new WorldPoint(this.localPlayer.getWorldLocation().getX(), this.localPlayer.getWorldLocation().getY() - 3, this.localPlayer.getWorldLocation().getPlane()));
                return;
            }
            case TELE_EDGE: {
                this.utils.useDecorativeObject(13523, MenuAction.GAME_OBJECT_FIRST_OPTION.getId(), this.getSleepDelay());
                this.tickDelay = this.getTickDelay();
                return;
            }
            case EQUIP_RUBIES: {
                wi = this.utils.getInventoryWidgetItem(this.rubyBoltIDs);
                if (gameTick == null) return;
                this.clientThread.invoke(() -> this.equipItemAction((WidgetItem) wi));
                return;
            }
            case DRINK_POOL: {
                obj = this.utils.findNearestGameObject(new int[]{29240, 29241});
                this.utils.useGameObjectDirect(obj, this.getSleepDelay(), MenuAction.GAME_OBJECT_FIRST_OPTION.getId());
                this.tickDelay = this.getTickDelay();
                return;
            }
            case EQUIP_DIAMONDS: {
                wi = this.utils.getInventoryWidgetItem(this.O);
                if (gameTick == null) return;
                this.clientThread.invoke(() -> this.useItemSecondOption(wi));
                return;
            }
            case ACID_WALK: {
                this.doAcidWalk();
                if (this.ah < 3) return;
                if (this.doingAcidWalk) {
                    this.utils.walk(this.spotsForAcidWalk.get(1));
                    this.doingAcidWalk = false;
                }
                if (this.doingAcidWalk) return;
                this.utils.walk(this.spotsForAcidWalk.get(this.spotsForAcidWalk.size() - 1));
                this.doingAcidWalk = true;
                return;
            }
            case ATTACK_VORKATH: {
                if (!this.atVorky()) return;
                this.utils.attackNPCDirect(this.vorkathNpc);
                return;
            }
            case HANDLE_ICE: {
                npc = this.utils.findNearestNpc(new String[]{"Zombified Spawn"});
                WorldPoint worldPoint = this.client.getLocalPlayer().getWorldLocation();
                LocalPoint.fromWorld(this.client, worldPoint);
                this.setSpell(Spells.CRUMBLE_UNDEAD.getInfo());
                this.clientThread.invoke(() -> this.castSpellOnNPC(npc));
                return;
            }
            case HANDLE_BOMB: {
                loc = this.client.getLocalPlayer().getWorldLocation();
                lLoc = LocalPoint.fromWorld(this.client, loc);
                this.l = new LocalPoint(lLoc.getX() + 256, lLoc.getY());
                this.m = new LocalPoint(lLoc.getX() - 256, lLoc.getY());
                if (lLoc.distanceTo(this.m) <= 1) {
                    this.notFrozen = true;
                    this.zombieSpawnDead = true;
                }
                if (lLoc.distanceTo(this.l) <= 1) {
                    this.notFrozen = true;
                    this.zombieSpawnDead = true;
                }
                if (lLoc.getX() < 6208) {
                    this.utils.walk(this.l);
                    this.tickDelay = this.getTickDelay();
                    this.notFrozen = true;
                    this.zombieSpawnDead = true;
                    return;
                }
                this.utils.walk(this.m);
                this.tickDelay = this.getTickDelay();
                this.notFrozen = true;
                this.zombieSpawnDead = true;
                return;
            }
            case DEACTIVATE_PRAY: {
                this.clientThread.invoke(this::deactivateQuickPrayers3);
                this.tickDelay = this.getTickDelay();
                return;
            }
            case ACTIVATE_PRAY: {
                this.clientThread.invoke(this::activateQuickPrayers2);
                return;
            }
            case WITHDRAW_COMBAT: {
                if (!this.config.supers()) {
                    this.utils.withdrawItem(9739);
                } else {
                    this.utils.withdrawItem(12695);
                }
                this.tickDelay = this.getTickDelay();
                return;
            }
            case WITHDRAW_RANGED: {
                if (!this.config.supers()) {
                    this.utils.withdrawItem(2444);
                } else {
                    this.utils.withdrawItem(22461);
                }
                this.tickDelay = this.getTickDelay();
                return;
            }
            case WAKE_VORKATH: {
                this.clientThread.invoke(this::attackVorkathAction);
                this.tickDelay = this.getTickDelay();
                return;
            }
            case CLOSE_BANK: {
                this.utils.closeBank();
                this.tickDelay = this.getTickDelay();
                return;
            }
            case WITHDRAW_VENOM: {
                if (this.config.antivenomplus()) {
                    this.utils.withdrawItem(12913);
                }
                if (!this.config.antivenomplus()) {
                    this.utils.withdrawItem(5952);
                }
                this.tickDelay = this.getTickDelay();
                return;
            }
            case WITHDRAW_ANTIFIRE: {
                if (this.config.superantifire()) {
                    this.utils.withdrawItem(22209);
                }
                if (!this.config.superantifire()) {
                    this.utils.withdrawItem(2452);
                }
                this.tickDelay = this.getTickDelay();
                return;
            }
            case WITHDRAW_POUCH: {
                this.utils.withdrawItem(12791);
                this.tickDelay = this.getTickDelay();
                return;
            }
            case WITHDRAW_RESTORES: {
                if (this.config.useRestores()) {
                    this.utils.withdrawItemAmount(3024, this.config.praypotAmount());
                } else {
                    this.utils.withdrawItemAmount(2434, this.config.praypotAmount());
                }
                this.tickDelay = this.getTickDelay();
                return;
            }
            case WITHDRAW_TELES: {
                this.utils.withdrawItemAmount(8013, 10);
                this.tickDelay = this.getTickDelay();
                return;
            }
            case WITHDRAW_BOLTS: {
                if (this.utils.bankContains(21946, 1)) {
                    this.utils.withdrawAllItem(21946);
                }
                if (!this.utils.bankContains(21946, 1) && this.utils.bankContains(9243, 1)) {
                    this.utils.withdrawAllItem(9243);
                }
                this.tickDelay = this.getTickDelay();
                return;
            }
            case WITHDRAW_FOOD1: {
                this.utils.withdrawItemAmount(this.config.foodID(), this.config.foodAmount());
                this.tickDelay = this.getTickDelay();
                return;
            }
            case WITHDRAW_FOOD2: {
                this.utils.withdrawItemAmount(this.config.foodID2(), this.config.foodAmount2());
                this.tickDelay = this.getTickDelay();
                return;
            }
            case MOVING: {
                this.utils.handleRun(30, 20);
                this.tickDelay = this.getTickDelay();
                return;
            }
            case DRINK_ANTIVENOM: {
                wi = this.getAntivenomWidget();
                if (gameTick == null) return;
                this.clientThread.invoke(() -> this.drinkPotion5((WidgetItem) wi));
                return;
            }
            case DRINK_COMBAT: {
                wi = this.getCombatItemWidget();
                if (gameTick == null) return;
                this.clientThread.invoke(() -> this.drinkPotion6((WidgetItem) wi));
                return;
            }
            case EAT_FOOD: {
                wi = this.getFoodWidget();
                if (gameTick == null) return;
                this.clientThread.invoke(() -> this.useItemFirstOption((WidgetItem) wi));
                return;
            }
            case DRINK_RANGE: {
                wi = this.getRangedItemWidget();
                if (gameTick == null) return;
                this.clientThread.invoke(() -> this.drinkPotion4((WidgetItem) wi));
                return;
            }
            case DRINK_ANTIFIRE: {
                wi = this.getAntifireWidget();
                if (gameTick == null) return;
                this.clientThread.invoke(() -> this.drinkPotion3((WidgetItem) wi));
                return;
            }
            case WALK_FIRST: {
                this.clientThread.invoke(this::teleToHouse3);
                this.deposited = false;
                this.tickDelay = this.getTickDelay();
                return;
            }
            case WALK_SECOND: {
                this.clientThread.invoke(this::teleToHouse1);
                this.tickDelay = this.getTickDelay();
                return;
            }
            case WALK_THIRD: {
                this.utils.walk(new WorldPoint(2643, 3676, 0));
                this.tickDelay = this.getTickDelay();
                return;
            }
            case USE_BOAT: {
                obj = this.utils.findNearestGameObject(new int[]{29917});
                this.utils.useGameObjectDirect(obj, this.getSleepDelay(), MenuAction.GAME_OBJECT_FIRST_OPTION.getId());
                this.tickDelay = this.getTickDelay();
                return;
            }
            case FIND_BANK: {
                this.findBank();
                this.tickDelay = this.getTickDelay();
                return;
            }
            case DEPOSIT_ITEMS: {
                this.tickDelay = this.getTickDelay();
                return;
            }
            case WITHDRAW_ITEMS: {
                this.tickDelay = this.getTickDelay();
                return;
            }
            case LOOT_ITEMS: {
                FreeVorkathPlugin freeVorkathPlugin = this;
                freeVorkathPlugin.lootItems(freeVorkathPlugin.itemsToLoot);
                return;
            }
            case JUMP_OBSTACLE: {
                this.utils.useGameObject(31990, 3, this.getSleepDelay());
                this.tickDelay = this.getTickDelay();
                return;
            }
        }
    }

    @Subscribe
    private void onItemSpawned(ItemSpawned itemSpawned1) {
        TileItem itemSpawned = itemSpawned1.getItem();
        String string = this.client.getItemDefinition(itemSpawned.getId()).getName().toLowerCase();
        if (!this.lootNamesList.stream().anyMatch(string.toLowerCase()::contains)) return;
        if (itemSpawned.getId() == 1751) return;
        this.itemsToLoot.add(itemSpawned);
    }

    @Subscribe
    private void onItemDespawned(ItemDespawned itemDespawned) {
        this.itemsToLoot.remove(itemDespawned.getItem());
    }

    public int getPrayerRestoreAmount(WidgetItem widgetItem, int n) {
        if (PrayerRestoreType.PRAYER_POTION.containsId(widgetItem.getId())) {
            return 7 + (int) Math.floor((double) n * 0.25);
        }
        if (PrayerRestoreType.SANFEW_SERUM.containsId(widgetItem.getId())) {
            return 4 + (int) Math.floor((double) n * 0.0);
        }
        if (!PrayerRestoreType.SUPER_RESTORE.containsId(widgetItem.getId())) return 0;
        return 8 + (int) Math.floor((double) n * 0.25);
    }

    private int getNpcHP(NPC nPC, Integer n) {
        if (nPC == null) return -1;
        if (nPC.getName() == null) {
            return -1;
        }
        int n2 = nPC.getHealthScale();
        int n3 = nPC.getHealthRatio();
        if (n3 < 0) return -1;
        if (n2 <= 0) return -1;
        if (n != null) return (int) ((float) (n * n3 / n2) + 0.5f);
        return -1;
    }

    @Subscribe
    private void onClientTick(ClientTick clientTick) {
        if (this.acidSpots.size() == this.amountOfAcidSpots) return;
        if (this.acidSpots.size() == 0) {
            this.spotsForAcidWalk.clear();
        } else {
            this.doAcidWalk();
        }
        this.amountOfAcidSpots = this.acidSpots.size();
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged animationChanged1) {
        Widget widget;
        if (!this.config.enableVorkath()) return;
        if (this.vorkathNpc == null) return;
        Actor animationChanged;
        if ((animationChanged = animationChanged1.getActor()).getAnimation() == 7950 && animationChanged.getName().contains("Vorkath") && (widget = this.client.getWidget(0xA0000F)) != null) {
            //this.widgetBounds = widget.getBounds();
        }
        if (animationChanged.getAnimation() != 7949) return;
        if (!animationChanged.getName().contains("Vorkath")) return;
        if (this.client.getVar(Varbits.QUICK_PRAYER) != 1) return;
        this.clientThread.invoke(this::deactivateQuickPrayersAction2);
    }

    @Subscribe
    private void onProjectileSpawned(ProjectileSpawned projectileSpawned1) {
        if (this.client.getGameState() != GameState.LOGGED_IN) return;
        if (!this.config.DodgeBomb()) return;
        Projectile projectileSpawned = projectileSpawned1.getProjectile();
        WorldPoint worldPoint = this.client.getLocalPlayer().getWorldLocation();
        LocalPoint lp = LocalPoint.fromWorld(this.client, worldPoint);
        if (projectileSpawned.getId() == 1481) {
            this.notFrozen = false;
        }
        if (projectileSpawned.getId() != 395) return;
        this.zombieSpawnDead = false;
        if (this.client.getLocalPlayer().getInteracting() == null) return;
        this.utils.walk(lp);
    }

    @Subscribe
    private void onProjectileMoved(ProjectileMoved projectileMoved1) {
        if (!this.config.enableVorkath()) return;
        Projectile projectile = projectileMoved1.getProjectile();
        LocalPoint projectileMoved = projectileMoved1.getPosition();
        WorldPoint.fromLocal((Client) this.client, (LocalPoint) projectileMoved);
        this.client.getLocalPlayer().getLocalLocation();
        WorldPoint worldPoint = this.client.getLocalPlayer().getWorldLocation();
        LocalPoint lp = LocalPoint.fromWorld((Client) this.client, (WorldPoint) worldPoint);
        if (projectile.getId() == 1483) {
            FreeVorkathPlugin freeVorkathPlugin = this;
            freeVorkathPlugin.addAcidSpot(WorldPoint.fromLocal((Client) freeVorkathPlugin.client, (LocalPoint) projectileMoved));
        }
        if (projectile.getId() == 395) {
            this.zombieSpawnDead = false;
            if (this.client.getLocalPlayer().getInteracting() != null) {
                this.utils.walk(lp);
            }
        }
        if (projectile.getId() != 1481) return;
        this.notFrozen = false;
    }

    public String getItemInTag(int n) {
        String string = this.configManager.getConfiguration("inventorytags", "item_" + n);
        if (string == null) return "";
        if (!string.isEmpty()) return string;
        return "";
    }

    @Subscribe
    private void onNpcSpawned(NpcSpawned npcSpawned) {
        NPC nPC = npcSpawned.getNpc();
        if (nPC.getName() == null) {
            return;
        }
        if (nPC.getName().equals("Vorkath")) {
            this.vorkathNpc = npcSpawned.getNpc();
        }
        if (!this.config.KillSpawns()) return;
        if (!nPC.getName().equals("Zombified Spawn")) return;
        this.zombieSpawnDead = false;
    }

    @Subscribe
    private void onNpcDespawned(NpcDespawned npcDespawned1) {
        if (!this.config.enableVorkath()) return;
        NPC npcDespawned;
        if ((npcDespawned = npcDespawned1.getNpc()).getName() == null) {
            return;
        }
        if (!npcDespawned.getName().equals("Vorkath")) return;
        this.vorkathNpc = null;
        if (this.client.getVar(Varbits.QUICK_PRAYER) != 1) return;
        this.clientThread.invoke(this::deactivateQuickPrayersAction1);
    }

    @Subscribe
    private void onGameStateChanged(GameStateChanged gameStateChanged) {
        this.itemsToLoot.clear();
        switch (gameStateChanged.getGameState()) {
            case CONNECTION_LOST:
            case LOGGING_IN:
            case HOPPING: {
                this.resetPlugin();
                break;
            }
        }
        if (gameStateChanged.getGameState() != GameState.LOADING) return;
        if (!this.U) return;
        this.resetPlugin();
    }

    @Subscribe
    private void onChatMessage(ChatMessage chatMessage) {
        if (!this.config.enableVorkath()) return;
        if (chatMessage.getType() != ChatMessageType.GAMEMESSAGE) {
            return;
        }
        Widget object = this.client.getWidget(0xA0000F);
        String string0 = "Your prayers have been disabled!";
        String string = "You have been poisoned by venom!";
        String string2 = "You have been poisoned!";
        String string3 = "You have been frozen!";
        String string4 = "The spawn violently explodes, unfreezing you as it does so.";
        String string5 = "You become unfrozen as you kill the spawn.";
        if (chatMessage.getMessage().equals(string0) || chatMessage.getMessage().contains(string0)) {
            this.clientThread.invoke(this::activateQuickPrayersAction);
        }
        if (chatMessage.getMessage().equals(string3)) {
            this.notFrozen = false;
            this.zombieSpawnDead = false;
        }
        final WidgetItem wi;
        if (chatMessage.getMessage().equals(string) && (wi = this.getAntivenomWidget()) != null) {
            this.clientThread.invoke(() -> this.drinkPotion2(wi));
        }

        final WidgetItem wi2;
        if (chatMessage.getMessage().equals(string2) && (wi2 = this.getAntivenomWidget()) != null && this.config.antivenomplus()) {
            this.clientThread.invoke(() -> this.drinkPotion1((WidgetItem) wi2));
        }
        if (!chatMessage.getMessage().equals(string4)) {
            if (!chatMessage.getMessage().equals(string5)) return;
        }
        this.notFrozen = true;
        this.zombieSpawnDead = true;
        if (!this.config.Retaliate()) return;
        if (!this.atVorky()) return;
        this.utils.attackNPCDirect(this.vorkathNpc);
    }

    private void addAcidSpot(WorldPoint worldPoint) {
        if (this.acidSpots.contains(worldPoint)) return;
        this.acidSpots.add(worldPoint);
    }

    private void doAcidWalk() {
        this.spotsForAcidWalk.clear();
        if (this.vorkathNpc == null) {
            return;
        }
        int[][][] nArrayArray = new int[][][]{new int[][]{{0, 1}, {0, -1}}, new int[][]{{1, 0}, {-1, 0}}};
        ArrayList<WorldPoint> arrayList = new ArrayList<WorldPoint>();
        double d = 99.0;
        WorldPoint worldPoint = this.client.getLocalPlayer().getWorldLocation();
        WorldPoint worldPoint2 = this.vorkathNpc.getWorldLocation();
        int n = worldPoint2.getX() + 14;
        int n2 = worldPoint2.getX() - 8;
        int n3 = worldPoint2.getY() - 1;
        int n4 = worldPoint2.getY() - 8;
        for (int i = -1; i < 2; ++i) {
            for (int j = -1; j < 2; ++j) {
                WorldPoint worldPoint3 = new WorldPoint(worldPoint.getX() + i, worldPoint.getY() + j, worldPoint.getPlane());
                if (this.acidSpots.contains(worldPoint3) || worldPoint3.getY() < n4 || worldPoint3.getY() > n3)
                    continue;
                for (int k = 0; k < 2; ++k) {
                    WorldPoint worldPoint4;
                    int n5;
                    double d2 = 0; //TODO: may need to be >= 2
                    double d3 = Math.abs(i) + Math.abs(j);
                    if (d2 < 2.0) {
                        d3 += (double) (Math.abs(j * nArrayArray[k][0][0]) + Math.abs(i * nArrayArray[k][0][1]));
                    }
                    if (k == 0) {
                        d3 += 0.5;
                    }
                    ArrayList<WorldPoint> arrayList2 = new ArrayList<WorldPoint>();
                    arrayList2.add(worldPoint3);
                    for (n5 = 1; n5 < 25 && !this.acidSpots.contains(worldPoint4 = new WorldPoint(worldPoint3.getX() + n5 * nArrayArray[k][0][0], worldPoint3.getY() + n5 * nArrayArray[k][0][1], worldPoint3.getPlane())) && worldPoint4.getY() >= n4 && worldPoint4.getY() <= n3 && worldPoint4.getX() >= n2 && worldPoint4.getX() <= n; ++n5) {
                        arrayList2.add(worldPoint4);
                    }
                    for (n5 = 1; n5 < 25 && !this.acidSpots.contains(worldPoint4 = new WorldPoint(worldPoint3.getX() + n5 * nArrayArray[k][1][0], worldPoint3.getY() + n5 * nArrayArray[k][1][1], worldPoint3.getPlane())) && worldPoint4.getY() >= n4 && worldPoint4.getY() <= n3 && worldPoint4.getX() >= n2 && worldPoint4.getX() <= n; ++n5) {
                        arrayList2.add(worldPoint4);
                    }
                    if (!(arrayList2.size() >= this.ah && d3 < d) && (d3 != d || arrayList2.size() <= arrayList.size()))
                        continue;
                    arrayList = arrayList2;
                    d = d3;
                }
            }
        }
        if (d != 99.0) {
            this.spotsForAcidWalk = arrayList;
        }
    }

    @Subscribe
    private void onGameObjectSpawned(GameObjectSpawned gameObjectSpawned) {
        if (!this.config.enableVorkath()) return;
        GameObject o;
        if ((o = gameObjectSpawned.getGameObject()).getId() != 30032) {
            if (o.getId() != 32000) return;
        }

        this.addAcidSpot(o.getWorldLocation());
    }

    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned gameObjectDespawned) {
        if (!this.config.enableVorkath()) return;
        GameObject o;
        if ((o = gameObjectDespawned.getGameObject()).getId() != 30032) {
            if (o.getId() != 32000) return;
        }

        this.acidSpots.remove(o.getWorldLocation());
    }

    public void activatePrayer(WidgetInfo widgetInfo) {
        Widget w;
        if ((w = this.client.getWidget(widgetInfo)) == null) {
            return;
        }

        if (this.client.getBoostedSkillLevel(Skill.PRAYER) <= 0) {
            return;
        }
        this.clientThread.invoke(() -> this.activatePrayerAction(w));
    }

    private void activatePrayerAction(Widget widget) {
        this.client.invokeMenuAction("Activate", widget.getName(), 1, MenuAction.CC_OP.getId(), widget.getItemId(), widget.getId());
    }

    private void drinkPotion1(WidgetItem widgetItem) {
        this.client.invokeMenuAction("Drink", "<col=ff9040>Potion", widgetItem.getId(), MenuAction.ITEM_FIRST_OPTION.getId(), widgetItem.getIndex(), WidgetInfo.INVENTORY.getId());
    }

    private void drinkPotion2(WidgetItem widgetItem) {
        this.client.invokeMenuAction("Drink", "<col=ff9040>Potion", widgetItem.getId(), MenuAction.ITEM_FIRST_OPTION.getId(), widgetItem.getIndex(), WidgetInfo.INVENTORY.getId());
    }

    private void activateQuickPrayersAction() {
        this.client.invokeMenuAction("Activate", "Quick-prayers", 1, MenuAction.CC_OP.getId(), -1, 0xA0000F);
    }

    private void deactivateQuickPrayersAction1() {
        this.client.invokeMenuAction("Deactivate", "Quick-prayers", 1, MenuAction.CC_OP.getId(), -1, 0xA0000F);
    }

    private void deactivateQuickPrayersAction2() {
        this.client.invokeMenuAction("Deactivate", "Quick-prayers", 1, MenuAction.CC_OP.getId(), -1, 0xA0000F);
    }

    private void teleToHouse1() {
        this.client.invokeMenuAction("", "", 8013, MenuAction.ITEM_FIRST_OPTION.getId(), this.utils.getInventoryWidgetItem(8013).getIndex(), WidgetInfo.INVENTORY.getId());
    }

    private void teleToHouse3() {
        this.client.invokeMenuAction("", "", 8013, MenuAction.ITEM_THIRD_OPTION.getId(), this.utils.getInventoryWidgetItem(8013).getIndex(), WidgetInfo.INVENTORY.getId());
    }

    private void drinkPotion3(WidgetItem widgetItem) {
        this.client.invokeMenuAction("Drink", "<col=ff9040>Potion", widgetItem.getId(), MenuAction.ITEM_FIRST_OPTION.getId(), widgetItem.getIndex(), WidgetInfo.INVENTORY.getId());
    }

    private void drinkPotion4(WidgetItem widgetItem) {
        this.client.invokeMenuAction("Drink", "<col=ff9040>Potion", widgetItem.getId(), MenuAction.ITEM_FIRST_OPTION.getId(), widgetItem.getIndex(), WidgetInfo.INVENTORY.getId());
    }

    private void useItemFirstOption(WidgetItem widgetItem) {
        this.client.invokeMenuAction("", "", widgetItem.getId(), MenuAction.ITEM_FIRST_OPTION.getId(), widgetItem.getIndex(), WidgetInfo.INVENTORY.getId());
    }

    private void drinkPotion6(WidgetItem widgetItem) {
        this.client.invokeMenuAction("Drink", "<col=ff9040>Potion", widgetItem.getId(), MenuAction.ITEM_FIRST_OPTION.getId(), widgetItem.getIndex(), WidgetInfo.INVENTORY.getId());
    }

    private void drinkPotion5(WidgetItem widgetItem) {
        this.client.invokeMenuAction("Drink", "<col=ff9040>Potion", widgetItem.getId(), MenuAction.ITEM_FIRST_OPTION.getId(), widgetItem.getIndex(), WidgetInfo.INVENTORY.getId());
    }

    private void attackVorkathAction() {
        this.client.invokeMenuAction("", "", this.utils.findNearestNpc(new String[]{"Vorkath"}).getIndex(), MenuAction.NPC_FIRST_OPTION.getId(), 0, 0);
    }

    private void activateQuickPrayers2() {
        this.client.invokeMenuAction("Activate", "Quick-prayers", 1, MenuAction.CC_OP.getId(), -1, 0xA0000F);
    }

    private void deactivateQuickPrayers3() {
        this.client.invokeMenuAction("Deactivate", "Quick-prayers", 1, MenuAction.CC_OP.getId(), -1, 0xA0000F);
    }

    private void castSpellOnNPC(NPC nPC) {
        this.client.invokeMenuAction("", "", nPC.getIndex(), MenuAction.SPELL_CAST_ON_NPC.getId(), nPC.getLocalLocation().getX(), nPC.getLocalLocation().getY());
    }

    private void useItemSecondOption(WidgetItem widgetItem) {
        this.client.invokeMenuAction("", "", widgetItem.getId(), MenuAction.ITEM_SECOND_OPTION.getId(), widgetItem.getIndex(), WidgetInfo.INVENTORY.getId());
    }

    private void equipItemAction(WidgetItem widgetItem) {
        this.client.invokeMenuAction("", "", widgetItem.getId(), MenuAction.ITEM_SECOND_OPTION.getId(), widgetItem.getIndex(), WidgetInfo.INVENTORY.getId());
    }

    private void lootItemAction(TileItem tileItem) {
        this.client.invokeMenuAction("", "", tileItem.getId(), MenuAction.GROUND_ITEM_THIRD_OPTION.getId(), tileItem.getTile().getSceneLocation().getX(), tileItem.getTile().getSceneLocation().getY());
    }

    private void openBankAction(GameObject gameObject) {
        this.client.invokeMenuAction("", "", gameObject.getId(), this.utils.getBankMenuOpcode(gameObject.getId()), gameObject.getSceneMinLocation().getX(), gameObject.getSceneMinLocation().getY());
    }
}

