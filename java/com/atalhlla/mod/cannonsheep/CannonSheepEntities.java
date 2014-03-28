package com.atalhlla.mod.cannonsheep;

import com.atalhlla.mod.cannonsheep.entity.EntityCannonSheep;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CannonSheepEntities {
	public static void registerEntities( FMLPreInitializationEvent event ) {
		CannonSheepMod.instance.utils.registerEntity( EntityCannonSheep.class );
	}
}
