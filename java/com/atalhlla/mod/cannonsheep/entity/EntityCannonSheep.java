package com.atalhlla.mod.cannonsheep.entity;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

import com.atalhlla.mod.cannonsheep.CannonSheepItems;
import com.atalhlla.util.minecraft.AITaskUtils;
import com.atalhlla.util.minecraft.EntityHelpers;

import cpw.mods.fml.common.FMLLog;

public class EntityCannonSheep extends EntitySheep {
	public static final String NAME = "cannon_sheep";

	// DataWatcher indices already used:
	// Entity: 0, 1, 2
	// EntityLivingBase: 6, 7, 8, 9
	// EntityAnimal: 12
	// EntitySheep: 16
	public static final int DATAWATCHER_INDEX_CANNON_FLAGS = 3;

	public static final int CANNON_FLAG_HAS_LOBOTOMIZER = 0x1;
	public static final int CANNON_FLAG_HAS_FEED_BAG = 0x2;

	public static final int CANNON_COUNT_CHARGES = 0xFF00;
	public static final int CANNON_COUNT_CHARGES_SHIFT = 8;
	public static final int CANNON_COUNT_SHELLS = 0xFF0000;
	public static final int CANNON_COUNT_SHELLS_SHIFT = 16;

	public static final String NBT_KEY_HAS_LOBOTOMIZER = "Has Lobotomizer";
	public static final String NBT_KEY_HAS_FEED_BAG = "Has Feed Bag";
	public static final String NBT_KEY_CHARGE_FUSE = "Charge Fuse";
	public static final String NBT_KEY_SHELL_FUSE = "Shell Fuse";

	protected AITaskUtils taskUtils = new AITaskUtils( this );

	protected EntityAIWander aiWander;
	protected EntityAILookIdle aiLookIdle;
	protected EntityAIWatchClosest aiWatchClosest;
	protected EntityAIEatGrass aiEatGrass;

	/*
	 * The following values need to be written to NBT:
	 * - hasLobotomizer
	 * - hasFeedBag
	 * - chargeFuseTicksLeft
	 * - shellFuseTicksLeft
	 * 
	 * The following values are on the data watcher:
	 * - hasLobotomizer
	 * - hasFeedBag
	 */

	public int chargeFuseTicksLeft = 0;
	public int shellFuseTicksLeft = 0;

	// TODO: Get this working on both client and server.
	public static void cannonizeSheep( EntityInteractEvent event ) {
		World world;
		EntitySheep oldSheep = (EntitySheep) event.target;
		EntityCannonSheep newSheep;

		if( oldSheep == null )
			return;

		if( oldSheep instanceof EntityCannonSheep )
			return;

		world = oldSheep.worldObj;
		newSheep = new EntityCannonSheep( world );

		newSheep.setLocationAndAngles( newSheep.posX, newSheep.posY, newSheep.posZ, newSheep.rotationYaw, newSheep.rotationPitch );
		newSheep.prevRotationYawHead = oldSheep.prevRotationYawHead;

		newSheep.setFleeceColor( oldSheep.getFleeceColor() );
		newSheep.setSheared( oldSheep.getSheared() );

		world.removeEntity( oldSheep );
		world.spawnEntityInWorld( newSheep );
	}

	public EntityCannonSheep( World world ) {
		super( world );

		this.initAI( world );
	}

	/**
	 * initAI searches the AI task list for relevant tasks so that they can be
	 * accessed by other methods later.
	 * 
	 * @param world
	 *            The world the Cannon Sheep is in.
	 */
	public void initAI( World world ) {
		aiEatGrass = (EntityAIEatGrass) taskUtils.getFirstTaskOfType( EntityAIEatGrass.class );
		aiWander = (EntityAIWander) taskUtils.getFirstTaskOfType( EntityAIWander.class );
		aiWatchClosest = (EntityAIWatchClosest) taskUtils.getFirstTaskOfType( EntityAIWatchClosest.class );
		aiLookIdle = (EntityAILookIdle) taskUtils.getFirstTaskOfType( EntityAILookIdle.class );
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();

		this.getEntityAttribute( SharedMonsterAttributes.maxHealth )
			.setBaseValue( 20.0D );
		this.getEntityAttribute( SharedMonsterAttributes.followRange )
			.setBaseValue( 32.0D );
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject( DATAWATCHER_INDEX_CANNON_FLAGS, new Integer(
			0x0 ) );
	}

	/*
	 * Currently this results in unrequited love, because Sheep do not care for
	 * Cannonized Sheep.
	 * 
	 * @see
	 * net.minecraft.entity.passive.EntityAnimal#canMateWith(net.minecraft.entity
	 * .passive.EntityAnimal)
	 */
	@Override
	public boolean canMateWith( EntityAnimal par1EntityAnimal ) {
		if( EntitySheep.class.isInstance( par1EntityAnimal ) && this.isInLove() && par1EntityAnimal.isInLove() ) {
			return true;
		}
		else {
			return super.canMateWith( par1EntityAnimal );
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT( NBTTagCompound par1NBTTagCompound ) {
		super.writeEntityToNBT( par1NBTTagCompound );

		par1NBTTagCompound.setBoolean( NBT_KEY_HAS_LOBOTOMIZER, this.getHasLobotomizer() );
		par1NBTTagCompound.setBoolean( NBT_KEY_HAS_FEED_BAG, this.getHasFeedBag() );
		par1NBTTagCompound.setInteger( NBT_KEY_CHARGE_FUSE, this.chargeFuseTicksLeft );
		par1NBTTagCompound.setInteger( NBT_KEY_SHELL_FUSE, this.shellFuseTicksLeft );
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT( NBTTagCompound par1NBTTagCompound ) {
		super.readEntityFromNBT( par1NBTTagCompound );

		this.setHasLobotomizer( par1NBTTagCompound.getBoolean( NBT_KEY_HAS_LOBOTOMIZER ) );
		this.setHasFeedBag( par1NBTTagCompound.getBoolean( NBT_KEY_HAS_FEED_BAG ) );
		this.chargeFuseTicksLeft = par1NBTTagCompound.getInteger( NBT_KEY_CHARGE_FUSE );
		this.shellFuseTicksLeft = par1NBTTagCompound.getInteger( NBT_KEY_SHELL_FUSE );
	}

	@Override
	protected boolean isMovementCeased() {
		return this.getHasLobotomizer() ? true : super.isMovementCeased();
	}

	@Override
	public boolean interact( EntityPlayer player ) {
		ItemStack currentItem = player.inventory.getCurrentItem();

		FMLLog.info( "EntityCannonSheep.interact: hi!  Sheep %d, here.  lobotomized = %b, feedbag = %b", this.getEntityId(), this.getHasLobotomizer(), this.getHasFeedBag() );

		if( currentItem == null )
			return super.interact( player );

		if( currentItem.getItem() == CannonSheepItems.cannonSheepLobotomizer ) {
			if( !this.worldObj.isRemote ) {
				// TODO: take item from player.
				this.setHasLobotomizer( true );
			}
			return true;
		}

		if( currentItem.getItem() == Items.flint_and_steel ) {
			FMLLog.info( "EntityCannonSheep.interact: Trying to use flint and steel!" );
			if( !this.worldObj.isRemote )
				this.fireZeMissiles();
			return true;
		}

		// TODO: Feed bag.

		return super.interact( player );
	}

	/*
	 * DataWatcher based properties.
	 * 
	 * The setters should never be called client side. (ie when world.isRemote
	 * == true)
	 */
	public boolean getHasLobotomizer() {
		int flags = this.dataWatcher.getWatchableObjectInt( DATAWATCHER_INDEX_CANNON_FLAGS );
		return (flags & CANNON_FLAG_HAS_LOBOTOMIZER) == CANNON_FLAG_HAS_LOBOTOMIZER;
	}

	public void setHasLobotomizer( boolean flagValue ) {
		int flags = this.dataWatcher.getWatchableObjectInt( DATAWATCHER_INDEX_CANNON_FLAGS );
		boolean oldFlagValue = (flags & CANNON_FLAG_HAS_LOBOTOMIZER) == CANNON_FLAG_HAS_LOBOTOMIZER;
		flags = flagValue ? flags | CANNON_FLAG_HAS_LOBOTOMIZER : flags & ~CANNON_FLAG_HAS_LOBOTOMIZER;
		this.dataWatcher.updateObject( DATAWATCHER_INDEX_CANNON_FLAGS, Integer.valueOf( flags ) );

		if( flagValue && !oldFlagValue ) {
			this.lobotomize();
		}
		else if( !flagValue && oldFlagValue ) {
			this.unlobotomize();
		}
	}

	public boolean getHasFeedBag() {
		int flags = this.dataWatcher.getWatchableObjectInt( DATAWATCHER_INDEX_CANNON_FLAGS );
		return (flags & CANNON_FLAG_HAS_FEED_BAG) == CANNON_FLAG_HAS_FEED_BAG;
	}

	public void setHasFeedBag( boolean flagValue ) {
		int flags = this.dataWatcher.getWatchableObjectInt( DATAWATCHER_INDEX_CANNON_FLAGS );
		boolean oldFlagValue = (flags & CANNON_FLAG_HAS_FEED_BAG) == CANNON_FLAG_HAS_FEED_BAG;
		flags = flagValue ? flags | CANNON_FLAG_HAS_FEED_BAG : flags & ~CANNON_FLAG_HAS_FEED_BAG;
		this.dataWatcher.updateObject( DATAWATCHER_INDEX_CANNON_FLAGS, Integer.valueOf( flags ) );

		if( flagValue && !oldFlagValue ) {
			this.addFeedBag();
		}
		else if( !flagValue && oldFlagValue ) {
			this.removeFeedBag();
		}
	}

	public void lobotomize() {
		this.tasks.removeTask( aiWander );
		this.tasks.removeTask( aiWatchClosest );
		this.tasks.removeTask( aiLookIdle );
	}

	public void unlobotomize() {
		this.tasks.addTask( 6, aiWander );
		this.tasks.addTask( 7, aiWatchClosest );
		this.tasks.addTask( 8, aiLookIdle );
	}

	public void addFeedBag() {
		this.tasks.removeTask( aiEatGrass );
	}

	public void removeFeedBag() {
		this.tasks.addTask( 5, aiEatGrass );
	}

	/*
	 * Cannon Sheep are only positively charged.
	 */
	public int getChargeCount() {
		return (this.dataWatcher.getWatchableObjectInt( DATAWATCHER_INDEX_CANNON_FLAGS ) & CANNON_COUNT_CHARGES) >> CANNON_COUNT_CHARGES_SHIFT;
	}

	public void setChargeCount( int count ) {
		if( count < 0 )
			count = 0;
		if( count > 255 )
			count = 255;

		int flags = this.dataWatcher.getWatchableObjectInt( DATAWATCHER_INDEX_CANNON_FLAGS );
		flags = (flags & ~CANNON_COUNT_CHARGES) | (count << CANNON_COUNT_CHARGES_SHIFT);
		this.dataWatcher.updateObject( DATAWATCHER_INDEX_CANNON_FLAGS, Integer.valueOf( flags ) );
	}

	public int getShellCount() {
		return (this.dataWatcher.getWatchableObjectInt( DATAWATCHER_INDEX_CANNON_FLAGS ) & CANNON_COUNT_SHELLS) >> CANNON_COUNT_SHELLS_SHIFT;
	}

	public void setShellCount( int count ) {
		if( count < 0 )
			count = 0;
		if( count > 255 )
			count = 255;

		int flags = this.dataWatcher.getWatchableObjectInt( DATAWATCHER_INDEX_CANNON_FLAGS );
		flags = (flags & ~CANNON_COUNT_SHELLS) | (count << CANNON_COUNT_SHELLS_SHIFT);
		this.dataWatcher.updateObject( DATAWATCHER_INDEX_CANNON_FLAGS, Integer.valueOf( flags ) );
	}

	@Override
	public ArrayList<ItemStack> onSheared( ItemStack item, IBlockAccess world, int x, int y, int z, int fortune ) {
		ArrayList<ItemStack> shearDrops = super.onSheared( item, world, x, y, z, fortune );
		// TODO: Drop Gunpowder on shearing.
		return shearDrops;
	}

	public void fireZeMissiles() {
		// TODO: But I am le tired :( (write code to fire the TNT.)
		FMLLog.info( "EntityCannonSheep/fireZeMissiles: Firing ze missiles!  (test fire.)" );

		worldObj.createExplosion( this, this.posX, this.posY, this.posZ, 0, true );

		Entity tnt = new EntityTNTPrimed(
			worldObj,
			this.posX,
			this.posY,
			this.posZ,
			this );
		EntityHelpers.throwEntity( tnt, 2F, this );
		worldObj.spawnEntityInWorld( tnt );
	}
}
