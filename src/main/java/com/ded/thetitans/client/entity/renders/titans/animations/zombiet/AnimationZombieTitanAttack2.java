package com.ded.thetitans.client.entity.renders.titans.animations.zombiet;
import java.util.List;

import com.ded.thetitans.entity.EntityZombieTitan;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import thehippomaster.AnimationAPI.AIAnimation;


public class AnimationZombieTitanAttack2
extends AIAnimation
{
	private EntityZombieTitan entity;
	public AnimationZombieTitanAttack2(EntityZombieTitan test)
	{
		super(test);
		this.entity = test;
	}

	public int getAnimID()
	{
		return 6;
	}

	public boolean isAutomatic()
	{
		return true;
	}

	public int getDuration()
	{
		return 150;
	}

	public boolean continueExecuting()
	{
		return this.entity.getAnimTick() > this.getDuration() || this.entity.isStunned ? false : super.continueExecuting();
	}

	public void updateTask()
	{
		if ((this.entity.getAnimTick() < 60) && (this.entity.getAttackTarget() != null))
		{
			this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
		}

		if ((this.entity.getAnimTick() == 60) || (this.entity.getAnimTick() == 104))
		{
			float f = (float)entity.getAttackValue(1.0F);
			int i = 0;
			this.entity.collideWithEntities(this.entity.body, this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.body.getEntityBoundingBox().expand(this.entity.getTitanSizeMultiplier() * 1.5D, 8.0D, this.entity.getTitanSizeMultiplier() * 1.5D).offset(0, -8, 0)));
			List<?> list11 = this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.getEntityBoundingBox().expand(this.entity.getTitanSizeMultiplier() * 1.5D, 8.0D, this.entity.getTitanSizeMultiplier() * 1.5D).offset(0, -8, 0));
			if ((list11 != null) && (!list11.isEmpty()))
			{
				for (int i1 = 0; i1 < list11.size(); i1++)
				{
					Entity entity1 = (Entity)list11.get(i1);
					if (this.entity.canAttackClass((Class<? extends EntityLivingBase>) entity1.getClass()))
					{
						entity1.motionY += 1.0F + entity.getRNG().nextFloat() + entity.getRNG().nextFloat();
						this.entity.attackChoosenEntity(entity1, f, i);
					}
				}
			}

			if (entity.isClient())
			{
				this.entity.shakeNearbyPlayerCameras(10D);
			}
		}
	}
}


