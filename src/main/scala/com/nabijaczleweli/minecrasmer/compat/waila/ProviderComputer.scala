package com.nabijaczleweli.minecrasmer.compat.waila

import java.util

import com.nabijaczleweli.minecrasmer.block.BlockComputerOn
import com.nabijaczleweli.minecrasmer.reference.Reference
import cpw.mods.fml.relauncher.{Side, SideOnly}
import mcp.mobius.waila.api.{IWailaConfigHandler, IWailaDataAccessor, IWailaDataProvider}
import net.minecraft.item.{ItemBlock, ItemStack}
import net.minecraft.util.StatCollector

@SideOnly(Side.CLIENT)
object ProviderComputer extends IWailaDataProvider {
	override def getWailaStack(accessor: IWailaDataAccessor, config: IWailaConfigHandler) = {
		val is = accessor.getStack.copy()
		is setItemDamage 3  // Facing to user to right-hand side
		is setStackDisplayName (StatCollector translateToLocal s"tile.${Reference.NAMESPACED_PREFIX}computer.neutral.name")
		is
	}

	override def getWailaHead(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		currenttip

	override def getWailaBody(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) = {
		currenttip add (StatCollector translateToLocal s"hud.${Reference.MOD_ID}:compat.waila.computer.state.${
			if(itemStack.getItem.asInstanceOf[ItemBlock].field_150939_a.isInstanceOf[BlockComputerOn.type]) "on" else "off"}.name")
		currenttip
	}

	override def getWailaTail(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		currenttip
}
