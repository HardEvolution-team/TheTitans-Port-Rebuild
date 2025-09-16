package com.ded.thetitans.client.entity.renders;

import com.ded.thetitans.entity.EntityGiantZombie;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelGiantZombie extends ModelZombie {

    public ModelGiantZombie() {
        this(0.0F, false);
    }

    public ModelGiantZombie(float modelSize, boolean p_i1168_2_) {
        super(modelSize, p_i1168_2_);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

        if (entityIn instanceof EntityGiantZombie) {
            EntityGiantZombie giant = (EntityGiantZombie) entityIn;

            if (giant.getSpawnState() != EntityGiantZombie.STATE_NORMAL) {
                float animationProgress = 0.0F;
                float ticks = giant.getSpawnTicks() - giant.getLastPartialTickTime();
                
                switch (giant.getSpawnState()) {
                    case EntityGiantZombie.STATE_RISING_SLOW:
                        animationProgress = MathHelper.clamp((EntityGiantZombie.DURATION_RISING_SLOW - ticks) / EntityGiantZombie.DURATION_RISING_SLOW, 0.0F, 1.0F);
                        break;
                    case EntityGiantZombie.STATE_PAUSE:
                        animationProgress = 1.0F;
                        break;
                    case EntityGiantZombie.STATE_RISING_FAST:
                        animationProgress = 1.0F + MathHelper.clamp((EntityGiantZombie.DURATION_RISING_FAST - ticks) / EntityGiantZombie.DURATION_RISING_FAST, 0.0F, 1.0F);
                        break;
                }

                float armCrawlMotion = MathHelper.sin(animationProgress * (float)Math.PI * 2.0F) * 0.7F;
                float legCrawlMotion = MathHelper.sin(animationProgress * (float)Math.PI * 2.0F + 0.5F) * 0.5F; // Ноги двигаются с небольшим сдвигом фазы
                float bodySway = MathHelper.sin(animationProgress * (float)Math.PI * 4.0F) * 0.1F;
                float headBob = MathHelper.sin(animationProgress * (float)Math.PI * 2.0F + 0.3F) * 0.05F;
                
                this.bipedLeftArm.rotateAngleX = -1.2F + armCrawlMotion * 0.6F;
                this.bipedLeftArm.rotateAngleZ = -armCrawlMotion * 0.5F;
                this.bipedLeftArm.rotateAngleY = -0.8F;
                
                // Правая рука движется в противофазе с разведением наружу
                this.bipedRightArm.rotateAngleX = -1.2F - armCrawlMotion * 0.6F;
                this.bipedRightArm.rotateAngleZ = armCrawlMotion * 0.5F;
                this.bipedRightArm.rotateAngleY = 0.8F;
                
                this.bipedLeftLeg.rotateAngleX = legCrawlMotion * 0.4F;
                this.bipedRightLeg.rotateAngleX = -legCrawlMotion * 0.4F;
                
                this.bipedBody.rotateAngleX = armCrawlMotion * 0.15F;
                this.bipedBody.rotateAngleY = bodySway * 0.2F;
                
                this.bipedHead.rotateAngleX = armCrawlMotion * 0.3F + headBob;
                this.bipedHead.rotateAngleY = bodySway * 0.1F;
                
                if (giant.getSpawnState() == EntityGiantZombie.STATE_PAUSE) {
                    float headYaw = MathHelper.sin(ageInTicks * 0.1F) * 0.5F;
                    this.bipedHead.rotateAngleY = headYaw;
                }

            }
        }
    }
}