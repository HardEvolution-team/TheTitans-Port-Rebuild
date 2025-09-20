package com.ded.thetitans.client.entity.renders.titans.animations.zombiet;
import java.util.List;

import com.ded.thetitans.entity.EntityZombieTitan;
import net.minecraft.entity.Entity;


import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import thehippomaster.AnimationAPI.AIAnimation;
public class AnimationZombieTitanReformSword
extends AIAnimation
{
	private EntityZombieTitan entity;
	public AnimationZombieTitanReformSword(EntityZombieTitan test)
	{
		super(test);
		this.entity = test;
	}

	public int getAnimID()
	{
		return 2;
	}

	public boolean isAutomatic()
	{
		return true;
	}

	public int getDuration()
	{
		return 210;
	}

	public boolean continueExecuting()
	{
		return this.entity.getAnimTick() > this.getDuration() || this.entity.isStunned? false : super.continueExecuting();
	}

	public void updateTask()
	{
		if (this.entity.getAnimTick() == 160)
		{
			this.entity.setArmed(true);

		}

		if (this.entity.getAnimTick() == 50)
		{
			if (entity.isClient())
			{
				this.entity.shakeNearbyPlayerCameras(10D);

			}

			double d8 = 8.0D;
			Vec3d vec3 = this.entity.getLook(1.0F);
			double dx = vec3.x * d8;
			double dz = vec3.z * d8;
			float f = (float)entity.getAttackValue(1.0F);
			int i = this.entity.getKnockbackAmount();
			this.entity.collideWithEntities(this.entity.body, this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.body.getEntityBoundingBox().expand(16.0D, 8.0D, 16.0D)));
			List<?> list11 = this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.getEntityBoundingBox().expand(32.0D, 2.0D, 32.0D).offset(dx, 0.0D, dz));
			if ((list11 != null) && (!list11.isEmpty()))
			{
				for (int i1 = 0; i1 < list11.size(); i1++)
				{
					Entity entity1 = (Entity)list11.get(i1);
					if (this.entity.canAttackClass((Class<? extends EntityLivingBase>) entity1.getClass()))
					{
						this.entity.attackChoosenEntity(entity1, f, i);
					}
				}
			}


		}
	}
}


