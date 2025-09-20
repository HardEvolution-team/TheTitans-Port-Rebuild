package com.ded.thetitans.client.entity.renders.titans.animations.zombiet;

import com.ded.thetitans.entity.EntityZombieTitan;
import thehippomaster.AnimationAPI.AIAnimation;
public class AnimationZombieTitanDeath
extends AIAnimation
{
	private EntityZombieTitan entity;
	public AnimationZombieTitanDeath(EntityZombieTitan test)
	{
		super(test);
		this.entity = test;
	}

	public int getAnimID()
	{
		return 10;
	}

	public boolean isAutomatic()
	{
		return true;
	}

	public int getDuration()
	{
		return 2000;
	}

	public boolean shouldExecute()
	{
		// Execute when the entity's deathTicks is greater than 0 and the entity is not alive
		// This is simpler logic than the old implementation
		return this.entity.deathTicks > 0 && !this.entity.isEntityAlive();
	}

	public boolean continueExecuting()
	{
		// Continue executing while deathTicks is greater than 0 and the entity is not alive
		// Also ensure we haven't exceeded the animation duration
		return this.entity.deathTicks > 0 && !this.entity.isEntityAlive() && this.entity.getAnimTick() < this.getDuration();
	}
}