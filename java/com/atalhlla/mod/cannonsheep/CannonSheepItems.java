package com.atalhlla.mod.cannonsheep;

import com.atalhlla.mod.cannonsheep.item.ItemCannonSheepLobotomizer;
import com.atalhlla.mod.cannonsheep.item.ItemSheepCannonizer;
import com.atalhlla.util.minecraft.ModUtils;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CannonSheepItems {
	public static ItemSheepCannonizer sheepCannonizer;
	public static ItemCannonSheepLobotomizer cannonSheepLobotomizer;
	
	public static void initItems( FMLPreInitializationEvent event ) throws InstantiationException, IllegalAccessException {
		ModUtils utils = CannonSheepMod.instance.utils;
		sheepCannonizer = (ItemSheepCannonizer) utils.registerItem( ItemSheepCannonizer.class );
		cannonSheepLobotomizer = (ItemCannonSheepLobotomizer) utils.registerItem( ItemCannonSheepLobotomizer.class );
	}
}
