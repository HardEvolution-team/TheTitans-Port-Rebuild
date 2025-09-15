package com.ded.thetitans.entity;

import com.ded.thetitans.SoundEvents;
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

public class EntityGiantZombie extends EntityZombie {

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
    private boolean hasPlayedCreationSound = false;
    private boolean hasPlayedRoarSound = false;
    private int livingSoundTimer = 0;
    private int gruntSoundTimer = 0;

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

            // Проигрываем звук создания при первой активации
            if (!hasPlayedCreationSound) {
                // Увеличиваем громкость и дальность звука до 1000 блоков
                LOGGER.info("Playing creation sound for Giant Zombie at position: {}, {}, {}", this.posX, this.posY, this.posZ);
                if (SoundEvents.ZOMBT_CREATION == null) {
                    LOGGER.error("ZOMBT_CREATION sound event is null when trying to play!");
                } else {
                    this.playSound(SoundEvents.ZOMBT_CREATION, 10.0F, 1.0F);
                }
                hasPlayedCreationSound = true;
            }

            float ticks = getSpawnTicks();
            if (ticks > 0) {
                setSpawnTicks(ticks - 1.0F);
                
                // Проигрываем звуки в зависимости от состояния
                playAnimationSounds();
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
                        
                        // Проигрываем звук рыка при завершении анимации
                        if (!hasPlayedRoarSound) {
                            // Увеличиваем громкость и дальность звука до 1000 блоков
                            LOGGER.info("Playing roar sound for Giant Zombie at position: {}, {}, {}", this.posX, this.posY, this.posZ);
                            if (SoundEvents.ZOMBT_GRUNT == null) {
                                LOGGER.error("ZOMBT_GRUNT sound event is null when trying to play!");
                            } else {
                                this.playSound(SoundEvents.ZOMBT_GRUNT, 10.0F, 1.0F);
                            }
                            hasPlayedRoarSound = true;
                        }
                        break;
                }
            }
        } else {
            // После завершения анимации периодически проигрываем звуки
            playLivingSounds();
        }
    }

    private void playAnimationSounds() {
        int state = getSpawnState();
        
        switch (state) {
            case STATE_RISING_SLOW:
            case STATE_RISING_FAST:
                // Проигрываем звуки grunt во время подъема
                gruntSoundTimer++;
                if (gruntSoundTimer >= 20) { // Каждые 20 тиков (1 секунда)
                    // Увеличиваем громкость и дальность звука до 1000 блоков
                    LOGGER.info("Playing grunt sound during rising animation for Giant Zombie at position: {}, {}, {}", this.posX, this.posY, this.posZ);
                    if (SoundEvents.ZOMBT_GRUNT == null) {
                        LOGGER.error("ZOMBT_GRUNT sound event is null when trying to play during rising animation!");
                    } else {
                        this.playSound(SoundEvents.ZOMBT_GRUNT, 10.0F, 1.0F);
                    }
                    gruntSoundTimer = 0;
                }
                break;
            case STATE_PAUSE:
                // Проигрываем звуки living во время паузы
                livingSoundTimer++;
                if (livingSoundTimer >= 30) { // Каждые 30 тиков (1.5 секунды)
                    // Увеличиваем громкость и дальность звука до 1000 блоков
                    LOGGER.info("Playing living sound during pause animation for Giant Zombie at position: {}, {}, {}", this.posX, this.posY, this.posZ);
                    if (SoundEvents.ZOMBT_GRUNT == null) {
                        LOGGER.error("ZOMBT_GRUNT sound event is null when trying to play during pause animation!");
                    } else {
                        this.playSound(SoundEvents.ZOMBT_GRUNT, 10.0F, 1.0F);
                    }
                    livingSoundTimer = 0;
                }
                break;
        }
    }

    private void playLivingSounds() {
        // Периодически проигрываем звуки после завершения анимации
        livingSoundTimer++;
        if (livingSoundTimer >= 100) { // Каждые 100 тиков (5 секунд)
            // Увеличиваем громкость и дальность звука до 1000 блоков
            LOGGER.info("Playing periodic grunt sound for Giant Zombie at position: {}, {}, {}", this.posX, this.posY, this.posZ);
            if (SoundEvents.ZOMBT_GRUNT == null) {
                LOGGER.error("ZOMBT_GRUNT sound event is null when trying to play periodically!");
            } else {
                this.playSound(SoundEvents.ZOMBT_GRUNT, 10.0F, 1.0F);
            }
            livingSoundTimer = 0;
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
        compound.setBoolean("HasPlayedCreationSound", this.hasPlayedCreationSound);
        compound.setBoolean("HasPlayedRoarSound", this.hasPlayedRoarSound);
    }
    @Override public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setSpawnState(compound.getInteger("SpawnState"));
        this.setSpawnTicks(compound.getFloat("SpawnTicks"));
        this.hasPlayedCreationSound = compound.getBoolean("HasPlayedCreationSound");
        this.hasPlayedRoarSound = compound.getBoolean("HasPlayedRoarSound");
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
    
    // Play random grunt sounds periodically when alive
    @Override
    protected SoundEvent getAmbientSound() {
        LOGGER.info("Getting ambient sound for Giant Zombie at position: {}, {}, {}", this.posX, this.posY, this.posZ);
        if (SoundEvents.ZOMBT_CREATION == null) {
            LOGGER.error("ZOMBT_CREATION sound event is null!");
            return null;
        }
        return SoundEvents.ZOMBT_CREATION;
    }
    
    // Play random grunt sounds when hurt
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        LOGGER.info("Getting hurt sound for Giant Zombie at position: {}, {}, {}", this.posX, this.posY, this.posZ);
        if (SoundEvents.ZOMBT_GRUNT == null) {
            LOGGER.error("ZOMBT_GRUNT sound event is null!");
            return null;
        }
        return SoundEvents.ZOMBT_GRUNT;
    }
    
    // Play death sound
    @Override
    protected SoundEvent getDeathSound() {
        LOGGER.info("Getting death sound for Giant Zombie at position: {}, {}, {}", this.posX, this.posY, this.posZ);
        if (SoundEvents.ZOMBT_DEATH == null) {
            LOGGER.error("ZOMBT_DEATH sound event is null!");
            return null;
        }
        return SoundEvents.ZOMBT_DEATH;
    }
    
    // Play step sounds
    @Override
    protected SoundEvent getStepSound() {
        LOGGER.info("Getting step sound for Giant Zombie at position: {}, {}, {}", this.posX, this.posY, this.posZ);
        if (SoundEvents.CHOMP == null) {
            LOGGER.error("CHOMP sound event is null!");
            return null;
        }
        return SoundEvents.CHOMP;
    }
}