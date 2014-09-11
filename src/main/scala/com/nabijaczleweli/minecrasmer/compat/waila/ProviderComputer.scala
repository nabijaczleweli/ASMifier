package com.nabijaczleweli.minecrasmer.compat.waila

import java.util

import com.nabijaczleweli.minecrasmer.block.BlockComputerOn
import com.nabijaczleweli.minecrasmer.reference.Reference
import cpw.mods.fml.relauncher.{Side, SideOnly}
import mcp.mobius.waila.api.{IWailaConfigHandler, IWailaDataAccessor, IWailaDataProvider}
import net.minecraft.item.{ItemBlock, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.StatCollector

@SideOnly(Side.CLIENT)
object ProviderComputer extends IWailaDataProvider {
	final val displayTag = {
		val t = new NBTTagCompound
		t.setString("Name", StatCollector translateToLocal s"tile.${Reference.NAMESPACED_PREFIX}computer.neutral.name")
		t
	}
	final val rootTag = {
		val t = new NBTTagCompound
		t.setTag("display", displayTag)
		t
	}

	override def getWailaStack(accessor: IWailaDataAccessor, config: IWailaConfigHandler) = {
		val is = accessor.getStack.copy()
		is setItemDamage 3  // Facing to user to right-hand side

		var setRoot = false
		var setDisplay = false
		if(!is.hasTagCompound)
			setRoot = true
		else
			if(!is.stackTagCompound.hasKey("display"))
				setDisplay = true

		if(setRoot)
			is.stackTagCompound = rootTag
		else
			if(setDisplay)
				is.stackTagCompound.setTag("display", displayTag)

		is
	}

	override def getWailaHead(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		currenttip

	override def getWailaBody(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) = {
		currenttip add (StatCollector translateToLocal s"hud.minecrasmer:compat.waila.computer.state.${if(itemStack.getItem.asInstanceOf[ItemBlock].field_150939_a.isInstanceOf[BlockComputerOn.type]) "on" else "off"}.name")
		currenttip
	}

	override def getWailaTail(itemStack: ItemStack, currenttip: util.List[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		currenttip
}
