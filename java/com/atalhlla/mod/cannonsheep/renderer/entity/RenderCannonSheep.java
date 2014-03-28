package com.atalhlla.mod.cannonsheep.renderer.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderSheep;

@SideOnly( Side.CLIENT )
public class RenderCannonSheep extends RenderSheep {
	// TODO: Anything...?
	public RenderCannonSheep( ModelBase sheepModel, ModelBase sheepFleeceModel, float shadowSize ) {
		super( sheepModel, sheepFleeceModel, shadowSize );
	}
}
