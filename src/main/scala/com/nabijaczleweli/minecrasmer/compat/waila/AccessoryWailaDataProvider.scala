package com.nabijaczleweli.minecrasmer.compat.waila

import java.util.{List => jList}

import com.nabijaczleweli.minecrasmer.computing.ComputerAccessory
import mcp.mobius.waila.api.{IWailaConfigHandler, IWailaDataAccessor, IWailaDataProvider}
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.fml.common.Optional
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

@SideOnly(Side.CLIENT)
@Optional.Interface(iface = "mcp.mobius.waila.api.IWailaDataProvider", modid = "Waila", striprefs = true)
trait AccessoryWailaDataProvider[-T <: TileEntity with ComputerAccessory] extends IWailaDataProvider {
	protected def getWailaBodyImpl(world: World, currenttip: jList[String], te: T): jList[String]

	@Optional.Method(modid = "Waila")
	override def getWailaStack(accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		null

	@Optional.Method(modid = "Waila")
	override def getWailaHead(itemStack: ItemStack, currenttip: jList[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		currenttip

	@Optional.Method(modid = "Waila")
	override def getWailaBody(itemStack: ItemStack, currenttip: jList[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		getWailaBodyImpl(accessor.getWorld, currenttip, accessor.getWorld.getTileEntity(accessor.getPosition.func_178782_a).asInstanceOf[T])

	@Optional.Method(modid = "Waila")
	override def getWailaTail(itemStack: ItemStack, currenttip: jList[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		currenttip
}
