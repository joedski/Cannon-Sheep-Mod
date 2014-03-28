package com.atalhlla.mod.cannonsheep.item;

import com.atalhlla.mod.cannonsheep.CannonSheepMod;

import net.minecraft.item.Item;

public class ItemCannonSheepLobotomizer extends Item {
	public static final String NAME = "cannon_sheep_lobotomizer";
	
	public ItemCannonSheepLobotomizer() {
		this.setCreativeTab( CannonSheepMod.creativeTabs );
		this.setTextureName( CannonSheepMod.MODID + ":" + NAME );
	}
}
