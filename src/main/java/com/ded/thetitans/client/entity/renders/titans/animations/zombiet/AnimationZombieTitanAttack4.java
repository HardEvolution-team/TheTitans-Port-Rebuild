package com.ded.thetitans.client.entity.renders.titans.animations.zombiet;
import java.util.List;

import com.ded.thetitans.entity.EntityZombieTitan;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import thehippomaster.AnimationAPI.AIAnimation;
public class AnimationZombieTitanAttack4
extends AIAnimation
{
	private EntityZombieTitan entity;
	public AnimationZombieTitanAttack4(EntityZombieTitan test)
	{
		super(test);
		this.entity = test;
	}

	public int getAnimID()
	{
		return 9;
	}

	public boolean isAutomatic()
	{
		return true;
	}

	public int getDuration()
	{
		return 190;
	}

	public boolean continueExecuting()
	{
		return this.entity.getAnimTick() > this.getDuration() || this.entity.isStunned ? false : super.continueExecuting();
	}

	public void updateTask()
	{
		if ((this.entity.getAnimTick() < 40) && (this.entity.getAttackTarget() != null))
		{
			this.entity.getLookHelper().setLookPositionWithEntity(this.entity.getAttackTarget(), 5.0F, 40.0F);
		}

		if (this.entity.getAnimTick() >= 100 && this.entity.getAnimTick() <= 104)
		{
			double d8 = entity.isArmed() ? (1.75D * this.entity.getTitanSizeMultiplier()) : (0.375D * this.entity.getTitanSizeMultiplier());
			Vec3d vec3 = this.entity.getLook(1.0F);
			double dx = vec3.x * d8;
			double dz = vec3.z * d8;
			float f = (float)entity.getAttackValue(1.0F);
			int i = this.entity.getKnockbackAmount();
			this.entity.collideWithEntities(this.entity.body, this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.body.getEntityBoundingBox().expand(this.entity.getTitanSizeMultiplier(), 8.0D, this.entity.getTitanSizeMultiplier())));
			List<?> list11 = this.entity.world.getEntitiesWithinAABBExcludingEntity(this.entity, this.entity.getEntityBoundingBox().expand(this.entity.getTitanSizeMultiplier(), 1.0D, this.entity.getTitanSizeMultiplier()).offset(dx, -(this.entity.getTitanSizeMultiplier() * 0.5D), dz));
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


