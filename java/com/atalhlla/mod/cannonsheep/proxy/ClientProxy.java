package com.atalhlla.mod.cannonsheep.proxy;

import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.model.ModelSheep2;

import com.atalhlla.mod.cannonsheep.entity.EntityCannonSheep;
import com.atalhlla.mod.cannonsheep.renderer.entity.RenderCannonSheep;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerRenderers() {
		super.registerRenderers();

		RenderingRegistry.registerEntityRenderingHandler( EntityCannonSheep.class, new RenderCannonSheep(
			new ModelSheep2(), // ModelSheep2 is the actual (shorn) sheep
			new ModelSheep1(), // ModelSheep1 is the fleece.
			0.7F ) );
	}
}
