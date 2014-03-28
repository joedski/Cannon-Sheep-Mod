package com.atalhlla.util.minecraft;

import java.lang.reflect.Field;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModUtils {
	public Object modInstance;

	public ModUtils( Object modInstance ) {
		this.modInstance = modInstance;
	}

	public String getThingName( Class<?> thingClass ) {
		Field nameField;
		String name = null;

		try {
			nameField = thingClass.getField( "NAME" );
		}
		catch( SecurityException e ) {
			e.printStackTrace();
			nameField = null;
		}
		catch( NoSuchFieldException e ) {
			e.printStackTrace();
			nameField = null;
		}

		if( nameField != null ) {
			try {
				name = (String) nameField.get( null );
			}
			catch( IllegalArgumentException e ) {
				e.printStackTrace();
				nameField = null;
			}
			catch( IllegalAccessException e ) {
				e.printStackTrace();
				nameField = null;
			}
		}

		if( nameField == null ) {
			name = thingClass.getSimpleName();
		}

		return name;
	}

	@SuppressWarnings( { "rawtypes", "unchecked" } )
	public void registerEntity( Class entityClass, int primaryEggColor, int secondaryEggColor ) {
		String name = getThingName( entityClass );
		int entityID = EntityRegistry.findGlobalUniqueEntityId();

		EntityRegistry.registerGlobalEntityID( entityClass, name, entityID );
		EntityRegistry.registerModEntity( entityClass, name, entityID, modInstance, 64, 1, true );
		EntityList.entityEggs.put( Integer.valueOf( entityID ), new EntityList.EntityEggInfo(
			entityID,
			primaryEggColor,
			secondaryEggColor ) );
	}

	/**
	 * Registers an entity, using random colors for the spawn egg.
	 * 
	 * @param entityClass
	 *            The class of the entity to register.
	 */
	@SuppressWarnings( { "rawtypes" } )
	public void registerEntity( Class entityClass ) {
		String name = getThingName( entityClass );
		long seed = name.hashCode();
		Random rand = new Random( seed );

		registerEntity( entityClass, rand.nextInt( 0xFFFFFF ), rand.nextInt( 0xFFFFFF ) );
	}

	public Block registerBlock( Class<? extends Block> blockClass ) throws InstantiationException, IllegalAccessException {
		String name;
		Block block = null;
		block = blockClass.newInstance();
		name = getThingName( blockClass );
		block.setBlockName( name );
		GameRegistry.registerBlock( block, name );

		return block;
	}

	public Item registerItem( Class<? extends Item> itemClass ) throws InstantiationException, IllegalAccessException {
		String name;
		Item item = null;
		item = itemClass.newInstance();
		name = getThingName( itemClass );
		item.setUnlocalizedName( name );
		GameRegistry.registerItem( item, name );

		return item;
	}
}
