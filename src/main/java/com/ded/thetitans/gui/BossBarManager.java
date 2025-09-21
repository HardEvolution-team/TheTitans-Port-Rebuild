package com.ded.thetitans.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ded.thetitans.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class BossBarManager
{
    private static final Minecraft MC = Minecraft.getMinecraft();
    private static final Map<IBossBar, GuiBossBarEntry> ENTRIES = new HashMap<IBossBar, GuiBossBarEntry>();
    private static boolean inWorld;
    private static long ticks;

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event)
    {
        if (inWorld)
        {
            if (MC.world == null)
            {
                inWorld = false;
                reset();
            }
            else
            {
                if (ticks % 10L == 0L)
                    tickQueue();

                ENTRIES.values().forEach(entry -> {
                    entry.onUpdate();
                });
            }
        }
        else if (MC.world != null)
            inWorld = true;

        ticks++;
    }

    // New event handler to cancel vanilla boss bars
    @SubscribeEvent
    public static void onRenderBossInfo(RenderGameOverlayEvent.BossInfo event) {
        // Cancel the vanilla boss bar rendering
        event.setCanceled(true);
    }

    private static void tickQueue()
    {
        List<Entity> entities = new ArrayList<Entity>(MC.world.loadedEntityList);
        List<GuiBossBarEntry> entries = new ArrayList<GuiBossBarEntry>(ENTRIES.values());

        for (GuiBossBarEntry entry : entries)
            if (entry.isDead())
                ENTRIES.remove(entry.getEntry());

        for (Entity entity : entities)
        {
            IBossBar bossBarCandidate = null;
            
            // Check if it's our custom IBossBar implementation
            if (entity instanceof IBossBar)
            {
                bossBarCandidate = (IBossBar) entity;
            }
            // Check if it's a vanilla boss entity that we need to wrap
            else if (entity instanceof EntityDragon || entity instanceof EntityWither)
            {
                // Check if we already have an adapter for this vanilla boss
                VanillaBossBarAdapter existingAdapter = null;
                for (IBossBar candidate : ENTRIES.keySet()) {
                    if (candidate instanceof VanillaBossBarAdapter) {
                        VanillaBossBarAdapter adapter = (VanillaBossBarAdapter) candidate;
                        if (adapter.getVanillaBoss().equals(entity)) {
                            existingAdapter = adapter;
                            break;
                        }
                    }
                }
                
                // Create a new adapter if one doesn't already exist
                if (existingAdapter == null) {
                    bossBarCandidate = new VanillaBossBarAdapter(entity);
                } else {
                    bossBarCandidate = existingAdapter;
                }
            }

            if (bossBarCandidate != null)
            {
                // Check if we already have an entry for this boss
                if (!ENTRIES.containsKey(bossBarCandidate))
                {
                    ENTRIES.put(bossBarCandidate, new GuiBossBarEntry(bossBarCandidate));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Pre event)
    {
        if (!event.getType().equals(ElementType.HOTBAR) || MC.world == null || MC.player == null)
            return;
        ScaledResolution res = new ScaledResolution(MC);
        List<GuiBossBarEntry> entries = new ArrayList<GuiBossBarEntry>(ENTRIES.values());
        GuiBossBarEntry entry;
        int size = entries.size();
        int finalSize = res.getScaledHeight() / 65;
        for (int i = 0, ii = 0; i < size && ii < finalSize; i++)
        {
            entry = entries.get(i);
            if (entry.canRender(MC.player))
            {
                ClientProxy.BOSSBARS.renderNewBar(ii, entry);
                ii++;
            }
        };
    }

    public static void reset()
    {
        ENTRIES.clear();
    }
}