package com.ded.thetitans;

import com.ded.thetitans.entity.EntityZombieTitan;
import com.ded.thetitans.entity.EntityGiantZombie;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.ded.thetitans.Tags.MODID;

@Mod(modid = MODID, version = Tags.VERSION, name = Tags.MODNAME, acceptedMinecraftVersions = "[1.12.2]")
public class MyMod {

    @SidedProxy(clientSide = "com.ded.thetitans.ClientProxy", serverSide = "com.ded.thetitans.CommonProxy")
    public static CommonProxy proxy;
    public static final String TEXTURES[] = {"default", "new"};
    public static final Logger LOGGER = LogManager.getLogger(MODID);


    private static int entityId = 0;

    public static final ResourceLocation genericTitanWhiteTexture32x64 = new ResourceLocation(Tags.MODID, "textures/entities/32x64_disintigration.png");
    public static final ResourceLocation genericTitanWhiteTexture64x64 = new ResourceLocation(Tags.MODID, "textures/entities/64x64_disintigration.png");


    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Entity registration (common to both sides)
        EntityRegistry.registerModEntity(
                new ResourceLocation(MODID, "giant_zombie"),
                EntityGiantZombie.class,
                "GiantZombie",
                entityId++,
                this,
                64, 1, true
        );
        EntityRegistry.registerModEntity(
                new ResourceLocation(MODID, "zombie_titan"),
                EntityZombieTitan.class,
                "ZombieTitan",
                entityId++,
                this,
                64, 1, true
        );



        EntityRegistry.registerEgg(new ResourceLocation(MODID, "giant_zombie"), 0x00FF00, 0xFF0000);
        EntityRegistry.registerEgg(new ResourceLocation(MODID, "zombie_titan"), 0x00AA00, 0xAA0000);
        MinecraftForge.EVENT_BUS.register(new SoundRegistry());
        proxy.preInit(event);
    }



    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventBusSubscriber(modid = MODID)
    public static class RegistrationHandler {
        /**
         * Регистрация предметов
         */
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
        }
    }
}