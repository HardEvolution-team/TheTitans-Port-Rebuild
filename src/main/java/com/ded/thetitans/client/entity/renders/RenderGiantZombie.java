package com.ded.thetitans.client.entity.renders;

import com.ded.thetitans.entity.EntityGiantZombie;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderGiantZombie extends RenderLiving<EntityGiantZombie> {
    private static final ResourceLocation ZOMBIE_TEXTURE = new ResourceLocation("textures/entity/zombie/zombie.png");
    private final float scale = 10.0F;

    public RenderGiantZombie(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelGiantZombie(), 0.5F * 10.0F);
    }



    @Override
    protected ResourceLocation getEntityTexture(EntityGiantZombie entity) {
        return ZOMBIE_TEXTURE;
    }

    @Override
    protected void preRenderCallback(EntityGiantZombie entity, float partialTickTime) {
        GlStateManager.scale(this.scale, this.scale, this.scale);
        entity.setLastPartialTickTime(partialTickTime);

        int state = entity.getSpawnState();
        if (state == EntityGiantZombie.STATE_NORMAL) {
            return;
        }

        float entityHeight = entity.height;
        final float INITIAL_MODEL_OFFSET_Y = entityHeight * 1.1F - 10.0F;

        float currentAnimationProgress = 0.0F;

        switch (state) {
            case EntityGiantZombie.STATE_RISING_SLOW:
                float slowTicks = entity.getSpawnTicks() - partialTickTime;
                currentAnimationProgress = MathHelper.clamp((EntityGiantZombie.DURATION_RISING_SLOW - slowTicks) / EntityGiantZombie.DURATION_RISING_SLOW, 0.0F, 0.3F);
                break;
            case EntityGiantZombie.STATE_PAUSE:
                currentAnimationProgress = 0.3F;
                break;
            case EntityGiantZombie.STATE_RISING_FAST:
                float fastTicks = entity.getSpawnTicks() - partialTickTime;
                float fastProgress = MathHelper.clamp((EntityGiantZombie.DURATION_RISING_FAST - fastTicks) / EntityGiantZombie.DURATION_RISING_FAST, 0.0F, 1.0F);
                float easedProgress = fastProgress * fastProgress * (3.0F - 2.0F * fastProgress);
                currentAnimationProgress = 0.3F + easedProgress * 0.7F;
                break;
        }

        float yOffset = INITIAL_MODEL_OFFSET_Y * (1.0F - currentAnimationProgress);

        GlStateManager.translate(0.0F, yOffset / this.scale, 0.0F);
    }
}