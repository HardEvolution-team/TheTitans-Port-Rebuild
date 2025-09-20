package com.ded.thetitans.client.entity.renders.titans.animations.zombiet;

import com.ded.thetitans.SoundRegistry;
import com.ded.thetitans.entity.EntityZombieTitan;
import thehippomaster.AnimationAPI.AIAnimation;

public class AnimationZombieTitanCreation
		extends AIAnimation
{
	private EntityZombieTitan entity;

	public AnimationZombieTitanCreation(EntityZombieTitan test)
	{
		super(test);
		this.entity = test;
	}

	public int getAnimID()
	{
		return 13;
	}

	public boolean isAutomatic()
	{
		return true;
	}

	public int getDuration()
	{
		return 520;
	}

	public void startExecuting()
	{
		super.startExecuting();
		this.entity.playSound(SoundRegistry.TITAN_BIRTH, 1000.0F, 1.0F);
	}

	public boolean continueExecuting()
	{
		return this.entity.getAnimTick() < this.getDuration();
	}

	public void updateTask()
	{
		this.entity.motionX = 0D;
		this.entity.motionZ = 0D;
		if (this.entity.motionY > 0D)
			this.entity.motionY = 0D;
		if (entity.isClient())
		{
			if (this.entity.getAnimTick() == 10)
				this.entity.playSound(SoundRegistry.TITAN_RUMBLE, this.entity.getTitanSizeMultiplier(), 1F);
			if (this.entity.getAnimTick() == 160)
				this.entity.playSound(SoundRegistry.TITAN_ZOMBIE_CREATION, this.entity.getTitanSizeMultiplier(), this.entity.isChild() ? 1.5F : 1F);
			if (this.entity.getAnimTick() == 300)
				this.entity.playSound(SoundRegistry.TITAN_QUAKE, this.entity.getTitanSizeMultiplier(), 1F);
			if (this.entity.getAnimTick() == 420)
				this.entity.playSound(SoundRegistry.BIGZOMBIE_LIVING, this.entity.getTitanSizeMultiplier(), 1F);
		}
	}
}