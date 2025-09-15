package com.ded.thetitans.mixin.minecraft;

import net.minecraft.entity.SharedMonsterAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = SharedMonsterAttributes.class)
public class MixinSharedMonsterAttributes {
    @ModifyConstant(method = "<clinit>", constant = @Constant(doubleValue = 1024.0D))
    private static double modifyMaxHealthLimit(double original) {
        return Double.MAX_VALUE;
    }
}
