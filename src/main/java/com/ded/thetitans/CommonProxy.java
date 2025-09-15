package com.ded.thetitans;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static com.ded.thetitans.Tags.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public abstract class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {}
    public void init(FMLInitializationEvent event) {}
}