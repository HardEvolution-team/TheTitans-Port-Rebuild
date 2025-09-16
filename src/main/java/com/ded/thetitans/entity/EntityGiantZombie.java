package com.ded.thetitans.entity;

import com.ded.thetitans.SoundRegistry;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class EntityGiantZombie extends TitanBase {

    private static final Logger LOGGER = LogManager.getLogger("TheTitans|SoundPlayback");

    public static final DataParameter<Integer> SPAWN_STATE = EntityDataManager.createKey(EntityGiantZombie.class, DataSerializers.VARINT);
    public static final DataParameter<Float> SPAWN_TICKS = EntityDataManager.createKey(EntityGiantZombie.class, DataSerializers.FLOAT);
    // Состояния
    public static final int STATE_RISING_SLOW = 1; // Медленный подъем
    public static final int STATE_PAUSE = 2;       // Пауза
    public static final int STATE_RISING_FAST = 3; // Быстрый подъем
    public static final int STATE_NORMAL = 4;      // Обычное поведение

    // Длительность этапов в тиках (20 тиков = 1 секунда)
    public static final int DURATION_RISING_SLOW = 40;
    public static final int DURATION_PAUSE = 60;
    public static final int DURATION_RISING_FAST = 80;

    public final float SIZE = 30.0F;
    private float lastPartialTickTime;

    public EntityGiantZombie(World worldIn) {
        super(worldIn);
        this.setSize(0.6F * SIZE, 1.95F * SIZE);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(SPAWN_STATE, STATE_RISING_SLOW);
        this.dataManager.register(SPAWN_TICKS, 0.0F);
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        IEntityLivingData superLivingData = super.onInitialSpawn(difficulty, livingdata);

        // Хитбокс на поверхности, модель смещается в рендере
        this.setSpawnState(STATE_RISING_SLOW);
        this.setSpawnTicks((float)DURATION_RISING_SLOW);
        this.setNoGravity(true);
        this.noClip = true;
        
        // Play the birth sound when the titan spawns
        this.playBirthSound();

        return superLivingData;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (getSpawnState() != STATE_NORMAL) {
            this.motionX = 0;
            this.motionY = 0;
            this.motionZ = 0;
            this.setNoGravity(true);
            this.noClip = true;
            this.navigator.clearPath();
            
            // Отключаем AI во время анимации
            this.setNoAI(true);

            float ticks = getSpawnTicks();
            if (ticks > 0) {
                setSpawnTicks(ticks - 1.0F);
            } else {
                switch (getSpawnState()) {
                    case STATE_RISING_SLOW:
                        setSpawnState(STATE_PAUSE);
                        setSpawnTicks((float)DURATION_PAUSE);
                        break;
                    case STATE_PAUSE:
                        setSpawnState(STATE_RISING_FAST);
                        setSpawnTicks((float)DURATION_RISING_FAST);
                        break;
                    case STATE_RISING_FAST:
                        setSpawnState(STATE_NORMAL);
                        this.setNoGravity(false);
                        this.noClip = false;
                        // Включаем AI после завершения анимации
                        this.setNoAI(false);
                        break;
                }
            }
        }
    }

    public int getSpawnState() { return this.dataManager.get(SPAWN_STATE); }
    public void setSpawnState(int state) { this.dataManager.set(SPAWN_STATE, state); }
    public float getSpawnTicks() { return this.dataManager.get(SPAWN_TICKS); }
    public void setSpawnTicks(float ticks) { this.dataManager.set(SPAWN_TICKS, ticks); }
    public void setLastPartialTickTime(float partialTickTime) { this.lastPartialTickTime = partialTickTime; }
    public float getLastPartialTickTime() { return this.lastPartialTickTime; }
    @Override public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("SpawnState", this.getSpawnState());
        compound.setFloat("SpawnTicks", this.getSpawnTicks());
    }
    @Override public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setSpawnState(compound.getInteger("SpawnState"));
        this.setSpawnTicks(compound.getFloat("SpawnTicks"));
    }
    @Override protected boolean canDespawn() { return false; }
    
    // Make the giant zombie immune to burning in sunlight
    @Override
    protected boolean shouldBurnInDay() {
        return false;
    }
    
    @Override protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1000.0D);
    }
    

    @Override
    protected SoundEvent getAmbientSound() {return SoundRegistry.TITAN_ZOMBIE_ROAR;}

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {return SoundRegistry.TITAN_ZOMBIE_ROAR;}

    @Override
    protected SoundEvent getDeathSound() {return SoundRegistry.TITAN_ZOMBIE_DEATH;}
    
    @Override
    protected SoundEvent getStepSound() {return SoundRegistry.TITAN_STEP;}
}