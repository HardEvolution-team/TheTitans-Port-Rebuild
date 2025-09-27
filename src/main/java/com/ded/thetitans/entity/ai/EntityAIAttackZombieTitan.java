package com.ded.thetitans.entity.ai;

import com.ded.thetitans.entity.EntityZombieTitan;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIAttackZombieTitan extends EntityAIBase {
    private EntityZombieTitan zombie;

    public EntityAIAttackZombieTitan(EntityZombieTitan titan) {
        this.zombie = titan;
        this.setMutexBits(3); // Блокирует движение (1) и look/target (2)
    }

    @Override
    public boolean shouldExecute() {
        // Don't execute if the entity is dead, in death animation, or already animating
        if (this.zombie.getHealth() <= 0 || this.zombie.getAnimID() == 14 || this.zombie.getAnimID() != 0) {
            return false;
        }
        
        // Execute only if there's a target and not currently animating
        return this.zombie.getAttackTarget() != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        // Don't continue if the entity is dead or in death animation
        if (this.zombie.getHealth() <= 0 || this.zombie.getAnimID() == 14) {
            return false;
        }
        
        // Continue only if there's a target and currently animating
        return this.zombie.getAttackTarget() != null && this.zombie.getAnimID() != 0;
    }

    @Override
    public void startExecuting() {
        // Only start new animation if not already animating
        if (this.zombie.getAnimID() == 0) {
            // Выбор атаки по расстоянию
            double distSq = this.zombie.getDistanceSq(this.zombie.getAttackTarget());
            int chosenID;
            if (distSq > 250000.0D) { // >500 блоков — дальняя
                int[] farAttacks = {2, 6};
                chosenID = farAttacks[this.zombie.getRNG().nextInt(farAttacks.length)];
            } else { // Ближняя
                int[] closeAttacks = {1, 3};
                chosenID = closeAttacks[this.zombie.getRNG().nextInt(closeAttacks.length)];
            }

            this.zombie.setAnimID(chosenID);
            this.zombie.setAnimTick(0);
            // System.out.println("AttackAI started with ID: " + chosenID); // Дебаг
        }
    }

    @Override
    public void updateTask() {
        if (this.zombie.getAttackTarget() != null) {
            this.zombie.getLookHelper().setLookPositionWithEntity(
                    this.zombie.getAttackTarget(), 30.0F, 30.0F
            );
        }
    }

    @Override
    public void resetTask() {
        // Only reset if not in death animation
        if (this.zombie.getAnimID() != 14) {
            this.zombie.setAnimID(0);
            this.zombie.setAnimTick(0);
        }
    }
}