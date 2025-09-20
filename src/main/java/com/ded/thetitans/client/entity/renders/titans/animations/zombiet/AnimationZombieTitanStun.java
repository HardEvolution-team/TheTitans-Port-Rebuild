package com.ded.thetitans.client.entity.renders.titans.animations.zombiet;

import com.ded.thetitans.entity.EntityZombieTitan;
import thehippomaster.AnimationAPI.AIAnimation;
public class AnimationZombieTitanStun
extends AIAnimation
{
	private EntityZombieTitan entity;
	public AnimationZombieTitanStun(EntityZombieTitan test)
	{
		super(test);
		this.entity = test;
	}

	public int getAnimID()
	{
		return 8;
	}

	public boolean isAutomatic()
	{
		return true;
	}

	public int getDuration()
	{
		return 180;
	}

	public boolean continueExecuting()
	{
		return this.entity.getAnimTick() > 180 ? false : super.continueExecuting();
	}
}


