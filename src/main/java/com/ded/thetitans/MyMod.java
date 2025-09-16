package com.ded.thetitans;

import com.ded.thetitans.client.entity.renders.RenderGiantZombie;
import com.ded.thetitans.entity.EntityGiantZombie;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.ded.thetitans.Tags.MODID;

@Mod(modid = MODID, version = Tags.VERSION, name = Tags.MODNAME, acceptedMinecraftVersions = "[1.12.2]")
public class MyMod {

    @SidedProxy(clientSide = "com.ded.thetitans.ClientProxy", serverSide = "com.ded.thetitans.CommonProxy")
    public static CommonProxy proxy;

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    private static int entityId = 0;


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
        EntityRegistry.registerEgg(new ResourceLocation(MODID, "giant_zombie"), 0x00FF00, 0xFF0000);
        MinecraftForge.EVENT_BUS.register(new SoundRegistry());
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        // Регистрация рецептов (если нужно)
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        // Регистрация предметов (если нужно)
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        // Регистрация блоков (если нужно)
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        // All sounds are automatically registered by Forge through the @GameRegistry.ObjectHolder annotations in SoundEvents class
        LOGGER.info("Sound events registered through Forge's automatic registration");
    }


    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // Пост-инициализация
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        // Регистрация серверных команд (если нужно)
    }
}