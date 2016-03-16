package com.nabijaczleweli.minecrasmer.util

import com.nabijaczleweli.minecrasmer.util.StringUtils._
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.crafting.IRecipe
import net.minecraftforge.fluids.IFluidBlock
import net.minecraftforge.fml.common.registry.GameRegistry

object RegistrationUtils {
	implicit class IRecipeUtils(val recipe: IRecipe) extends AnyVal {
		@inline
		def register() =
			GameRegistry addRecipe recipe
	}

	implicit class BlockUtils(val block: Block) extends AnyVal {
		@inline
		def register() =
			GameRegistry.registerBlock(block, block.getUnlocalizedName substring ":")
	}

	implicit class FluidBlockUtils(val block: Block with IFluidBlock) extends AnyVal {
		@inline
		def register() =
			GameRegistry.registerBlock(block, null, block.getUnlocalizedName substring ":")
	}

	implicit class ItemUtils(val item: Item) extends AnyVal {
		@inline
		def register() =
			GameRegistry.registerItem(item, item.getUnlocalizedName substring ":")
	}
}
