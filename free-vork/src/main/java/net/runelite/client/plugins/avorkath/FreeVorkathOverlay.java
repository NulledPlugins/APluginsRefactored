/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.openosrs.client.ui.overlay.components.table.TableAlignment
 *  com.openosrs.client.ui.overlay.components.table.TableComponent
 *  javax.inject.Inject
 *  javax.inject.Singleton
 *  net.runelite.api.Client
 *  net.runelite.api.MenuAction
 *  net.runelite.client.plugins.Plugin
 *  net.runelite.client.ui.overlay.OverlayMenuEntry
 *  net.runelite.client.ui.overlay.OverlayPanel
 *  net.runelite.client.ui.overlay.OverlayPosition
 *  net.runelite.client.ui.overlay.components.TitleComponent
 *  net.runelite.client.util.ColorUtil
 */
package net.runelite.client.plugins.avorkath;

import com.openosrs.client.ui.overlay.components.table.TableAlignment;
import com.openosrs.client.ui.overlay.components.table.TableComponent;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.util.ColorUtil;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;

@Singleton
class FreeVorkathOverlay
extends OverlayPanel {
    private static final Logger logger = LoggerFactory.getLogger(FreeVorkathOverlay.class);
    private final FreeVorkathPlugin plugin;
    private final AVorkathConfig config;
    String a;
    private String status = "Starting...";

    @Inject
    private FreeVorkathOverlay(Client client, FreeVorkathPlugin freeVorkathPlugin, AVorkathConfig aVorkathConfig) {
        super((Plugin)freeVorkathPlugin);
        this.setPosition(OverlayPosition.BOTTOM_LEFT);
        this.plugin = freeVorkathPlugin;
        this.config = aVorkathConfig;
        this.getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "airs overlay"));
    }

    public Dimension render(Graphics2D graphics2D) {
        if (this.plugin.o == null || !this.plugin.running) {
            logger.debug("Overlay conditions not met, not starting overlay");
            return null;
        }
        TableComponent tableComponent = new TableComponent();
        tableComponent.setColumnAlignments(new TableAlignment[]{TableAlignment.LEFT, TableAlignment.RIGHT});
        Duration duration = Duration.between(this.plugin.o, Instant.now());
        this.a = duration.toHours() < 1L ? "mm:ss" : "HH:mm:ss";
        tableComponent.addRow(new String[]{"Time running:", DurationFormatUtils.formatDuration(duration.toMillis(), this.a)});
        if (this.plugin.currentTask != null && !this.plugin.currentTask.name().equals("TIMEOUT")) {
            this.status = this.plugin.currentTask.name();
        }
        tableComponent.addRow(new String[]{"Status:", this.status});
        TableComponent tc = new TableComponent();
        tc.setColumnAlignments(new TableAlignment[]{TableAlignment.LEFT, TableAlignment.RIGHT});
        if (tableComponent.isEmpty()) return super.render(graphics2D);
        this.panelComponent.setBackgroundColor(ColorUtil.fromHex((String)"#121212"));
        this.panelComponent.setPreferredSize(new Dimension(200, 200));
        this.panelComponent.setBorder(new Rectangle(5, 5, 5, 5));
        this.panelComponent.getChildren().add(TitleComponent.builder().text("Free Vorkath").color(ColorUtil.fromHex((String)"#40C4FF")).build());
        this.panelComponent.getChildren().add(tableComponent);
        this.panelComponent.getChildren().add(tc);
        return super.render(graphics2D);
    }
}

