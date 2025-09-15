package com.ded.thetitans.mixin;


import com.ded.thetitans.Tags;
import com.google.common.collect.ImmutableMap;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.jetbrains.annotations.Nullable;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.Name(Tags.MODNAME + "Core")
@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
@IFMLLoadingPlugin.SortingIndex(Integer.MIN_VALUE)
public class TheTitansEarlyMixinLoader implements IFMLLoadingPlugin, IEarlyMixinLoader {
    public static final Map<String, Boolean> modMixinsConfig = new ImmutableMap.Builder<String, Boolean>()
            .build();

    @Override
    public List<String> getMixinConfigs() {
        String[] configs = {
                "mixins.thetitans.minecraft.json"
        };
        return Arrays.asList(configs);
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public @Nullable String getSetupClass() {
        return null;
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}