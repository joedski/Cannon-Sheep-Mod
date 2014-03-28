package com.atalhlla.mod.cannonsheep;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

import com.atalhlla.mod.cannonsheep.entity.EntityCannonSheep;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CannonSheepEventHandler {

	public static void register() {
		MinecraftForge.EVENT_BUS.register( new CannonSheepEventHandler() );
	}

	/*
	 * Event Dispatches
	 * 
	 * These functions filter the events Forge emits. They check constraints and
	 * return early if those are violated. Grouping is possible, but should only
	 * be done where it makes sense.
	 */
	@SubscribeEvent
	public void onSheepInteract( EntityInteractEvent event ) {
		// Target constraints
		if( !(event.target instanceof EntitySheep) )
			return;
		if( event.target instanceof EntityCannonSheep )
			return;

		// Player constraints
		ItemStack currentItemStack = event.entityPlayer.inventory.getCurrentItem();
		if( currentItemStack == null )
			return;

		// Event Dispatch
		if( currentItemStack.getItem() == CannonSheepItems.sheepCannonizer )
			cannonizeSheep( event );
	}

	/*
	 * Actual Event Handlers
	 */
	public void cannonizeSheep( EntityInteractEvent event ) {
		boolean isRemote = event.target.worldObj.isRemote;

		if( isRemote )
			return;

		EntityCannonSheep.cannonizeSheep( event );
	}
}
