package com.ded.thetitans;

import com.ded.thetitans.client.entity.renders.RenderGiantZombie;
import com.ded.thetitans.client.entity.renders.RenderZombieTitan;
import com.ded.thetitans.entity.EntityGiantZombie;
import com.ded.thetitans.entity.EntityZombieTitan;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import static com.ded.thetitans.Tags.MODID;

@Mod.EventBusSubscriber(modid = MODID, value = Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityGiantZombie.class, RenderGiantZombie::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityZombieTitan.class, RenderZombieTitan::new);
        System.out.println("Registered renderer for GiantZombie"); // Debug log to confirm
        System.out.println("Registered renderer for ZombieTitan"); // Debug log to confirm
    }
}