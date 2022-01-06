/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.runelite.api.Client
 *  net.runelite.api.widgets.WidgetInfo
 *  net.runelite.api.widgets.WidgetItem
 */
package net.runelite.client.plugins.avorkath;

import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;

import java.util.Arrays;
import java.util.Iterator;

public enum PrayerRestoreType {
    PRAYER_POTION(143, 141, 139, 2434),
    SUPER_RESTORE(3030, 3028, 3026, 3024, 24605, 24603, 24601, 24598),
    RANGED(22470, 22467, 22464, 22461, 173, 171, 169, 2444),
    ANTIFIRE(2458, 2456, 2454, 2452, 22218, 22215, 22212, 22209),
    ANTIVENOM(5958, 5956, 5954, 5952, 12919, 12917, 12915, 12913),
    SANFEW_SERUM(10931, 10929, 10927, 10925),
    COMBAT(12701, 12699, 12697, 12695, 9745, 9743, 9741, 9739);

    public int[] ItemIDs;

    private PrayerRestoreType(int ... nArray) {
        this.ItemIDs = nArray;
    }

    public final boolean containsId(int n) {
        return Arrays.stream(this.ItemIDs).anyMatch(arg_0 -> PrayerRestoreType.containsId(n, arg_0));
    }

    public final WidgetItem getItemFromInventory(Client client) {
        WidgetItem widgetItem = null;
        Widget object;
        if ((object = client.getWidget(WidgetInfo.INVENTORY)) == null) {
            return null;
        }
        Iterator i = object.getWidgetItems().iterator();
        WidgetItem finalWidgetItem = widgetItem;
        do {
            if (!i.hasNext()) return null;
            widgetItem = (WidgetItem)i.next();
        } while (!Arrays.stream(this.ItemIDs).anyMatch(arg_0 -> PrayerRestoreType.getItemFromInventory(finalWidgetItem, arg_0)));
        return widgetItem;
    }

    private static boolean getItemFromInventory(WidgetItem widgetItem, int n) {
        if (n != widgetItem.getId()) return false;
        return true;
    }

    private static boolean containsId(int n, int n2) {
        if (n2 != n) return false;
        return true;
    }
}

