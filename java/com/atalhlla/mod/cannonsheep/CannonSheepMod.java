package com.atalhlla.mod.cannonsheep;

import com.atalhlla.mod.cannonsheep.proxy.CommonProxy;
import com.atalhlla.util.minecraft.ModUtils;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod( modid = CannonSheepMod.MODID, version = CannonSheepMod.VERSION, name = CannonSheepMod.MODNAME )
public class CannonSheepMod {
	public static final String MODID = "cannonsheep";
	public static final String VERSION = "a0.0";
	public static final String MODNAME = "Cannon Sheep";

	@Instance( MODID )
	public static CannonSheepMod instance;

	@SidedProxy( clientSide = "com.atalhlla.mod.cannonsheep.proxy.ClientProxy", serverSide = "com.atalhlla.mod.cannonsheep.proxy.CommonProxy" )
	public static CommonProxy proxy;
	
	public static CannonSheepCreativeTabs creativeTabs = new CannonSheepCreativeTabs( "Cannon Sheep" );

	public ModUtils utils = new ModUtils( this );
	
	@EventHandler
	public void preInit( FMLPreInitializationEvent event ) throws InstantiationException, IllegalAccessException {
		CannonSheepItems.initItems( event );
		CannonSheepEntities.registerEntities( event );
		proxy.registerRenderers();
	}
	
	@EventHandler
	public void init( FMLInitializationEvent event ) {
		CannonSheepRecipes.initRecipes( event );
		CannonSheepEventHandler.register();
	}
}
