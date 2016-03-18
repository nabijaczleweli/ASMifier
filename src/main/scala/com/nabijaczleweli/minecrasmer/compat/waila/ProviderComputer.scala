package com.nabijaczleweli.minecrasmer.compat.waila

import com.nabijaczleweli.minecrasmer.block.BlockComputerOn
import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityComputer
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.resource.{ReloadableString, ResourcesReloadedEvent}
import mcp.mobius.waila.api.{IWailaConfigHandler, IWailaDataAccessor, IWailaDataProvider}
import net.minecraft.item.{ItemBlock, ItemStack}
import net.minecraftforge.fml.common.Optional
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import java.util.{List => jList}

import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockPos
import net.minecraft.world.World


@SideOnly(Side.CLIENT)
@Optional.Interface(iface = "mcp.mobius.waila.api.IWailaDataProvider", modid = "Waila", striprefs = true)
object ProviderComputer extends IWailaDataProvider {
	Container.eventBus register this

	var PCName           = new ReloadableString(s"tile.${Reference.NAMESPACED_PREFIX}computer.neutral.name")
	var PCOffState       = new ReloadableString(s"hud.${Reference.NAMESPACED_PREFIX}compat.waila.computer.state.off.name")
	var PCOnState        = new ReloadableString(s"hud.${Reference.NAMESPACED_PREFIX}compat.waila.computer.state.on.name")
	var PCBaseClockSpeed = new ReloadableString(s"hud.${Reference.NAMESPACED_PREFIX}compat.waila.computer.clock.name")
	var PCCPUs           = new ReloadableString(s"hud.${Reference.NAMESPACED_PREFIX}compat.waila.computer.processors.name")
	var PCCPT            = new ReloadableString(s"hud.${Reference.NAMESPACED_PREFIX}compat.waila.computer.cpt.name")

	@Optional.Method(modid = "Waila")
	override def getWailaStack(accessor: IWailaDataAccessor, config: IWailaConfigHandler) = {
		val is = accessor.getStack.copy()
		is setItemDamage 3 // Facing to user to right-hand side
		is setStackDisplayName PCName
		is
	}

	@Optional.Method(modid = "Waila")
	override def getWailaHead(itemStack: ItemStack, currenttip: jList[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		currenttip

	@Optional.Method(modid = "Waila")
	override def getWailaBody(itemStack: ItemStack, currenttip: jList[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) = {
		val block = itemStack.getItem.asInstanceOf[ItemBlock].block
		val position = accessor.getPosition
		val world = accessor.getWorld
		val on = block.isInstanceOf[BlockComputerOn.type]
		lazy val te = world.getTileEntity(position).asInstanceOf[TileEntityComputer]
		lazy val CPUs = te.CPUs  // It might be expensive, so better cache it

		if(on) {
			currenttip add PCOnState
			currenttip add PCBaseClockSpeed.format(te.clockSpeed)
			currenttip add PCCPUs.format(collapseCPUs(CPUs))
			currenttip add PCCPT.format(te.clockSpeed * (CPUs map {CPU => (CPU._1 * CPU._2).floor.toInt}).sum)
		} else
			currenttip add PCOffState
		currenttip
	}

	@Optional.Method(modid = "Waila")
	override def getWailaTail(itemStack: ItemStack, currenttip: jList[String], accessor: IWailaDataAccessor, config: IWailaConfigHandler) =
		currenttip

	@Optional.Method(modid = "Waila")
	override def getNBTData(player: EntityPlayerMP, te: TileEntity, tag: NBTTagCompound, world: World, pos: BlockPos) =
		tag

	@SubscribeEvent
	def onResourcesReloaded(event: ResourcesReloadedEvent) = {
		PCName.reload()
		PCOffState.reload()
		PCOnState.reload()
		PCBaseClockSpeed.reload()
		PCCPUs.reload()
		PCCPT.reload()
	}

	def collapseCPUs(seq: Seq[(Int, Float)]) =
		seq.headOption match {
			case None =>
				"None"
			case Some(_) =>
				val sorted = seq.sorted
				collapseCPUsImpl(sorted.head, 1, sorted.slice(1, sorted.length))
		}

	def timesOrEmpty(amount: Int) =
		amount match {
			case 0 =>
				""
			case 1 =>
				""
			case amt =>
				s"*$amt"
		}

	def collapseCPUsImpl(last: (Int, Float), amount: Int, seq: Seq[(Int, Float)]): String =
		seq.headOption match {
			case None =>
				s"$last${timesOrEmpty(amount)}"
			case Some(head) =>
				head match {
					case `last` =>
						collapseCPUsImpl(last, amount + 1, seq.slice(1, seq.length))
					case hd =>
						s"$last${timesOrEmpty(amount)},${collapseCPUsImpl(hd, 1, seq.slice(1, seq.length))}"
				}
		}
}
