package com.atalhlla.mod.cannonsheep.item;

import com.atalhlla.mod.cannonsheep.CannonSheepMod;

import net.minecraft.item.Item;

public class ItemSheepCannonizer extends Item {
	public static final String NAME = "sheep_cannonizer";

	public ItemSheepCannonizer() {
		this.setCreativeTab( CannonSheepMod.creativeTabs );
		this.setTextureName( CannonSheepMod.MODID + ":" + NAME );
	}
}
