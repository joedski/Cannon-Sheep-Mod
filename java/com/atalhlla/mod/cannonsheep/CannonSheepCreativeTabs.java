package com.atalhlla.mod.cannonsheep;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CannonSheepCreativeTabs extends CreativeTabs {
	
	public CannonSheepCreativeTabs( String tabLabel ) {
		super( tabLabel );
	}

	@SideOnly( Side.CLIENT )
	@Override
	public Item getTabIconItem() {
		return Item.getItemFromBlock( Blocks.tnt );
	}
}
