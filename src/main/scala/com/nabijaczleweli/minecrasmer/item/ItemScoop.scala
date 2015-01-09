package com.nabijaczleweli.minecrasmer.item

import com.nabijaczleweli.minecrasmer.MineCrASMer
import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.handler.ScoopHandler
import com.nabijaczleweli.minecrasmer.proxy.ClientProxy
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.resource.{ReloadableString, ReloadableStrings, ResourcesReloadedEvent}
import com.nabijaczleweli.minecrasmer.util.StringUtils._
import net.minecraft.block.{Block, BlockAir}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{ItemBucket, ItemStack}
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World
import net.minecraftforge.fluids.{Fluid, FluidContainerRegistry, IFluidBlock}
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ItemScoop(val contains: Block, val fluid: Fluid, val color: Int) extends ItemBucket(contains) {
	def this(block: Block with IFluidBlock, color: Int) =
		this(block, block.getFluid, color)

	def this(block: BlockAir) =
		this(block, null, 0)

	val empty = contains.isInstanceOf[BlockAir] || fluid == null

	setUnlocalizedName(s"${Reference.NAMESPACED_PREFIX}scoop_${if(empty) "empty" else {contains.getUnlocalizedName substring 5 substring ":" toLower 0}}")
	setCreativeTab(CreativeTabMineCrASMer)
	setMaxStackSize(1)
	if(!empty) {
		setContainerItem(Container.scoopEmpty)
		if(FMLCommonHandler.instance.getEffectiveSide.isClient)
			MineCrASMer.proxy.asInstanceOf[ClientProxy].scoopRenderQueue enqueue this
	}

	override def getItemStackDisplayName(is: ItemStack) =
		if(empty)
			super.getItemStackDisplayName(is)
		else
			ItemScoop localizedName 0 format contains.getLocalizedName

	override def onItemRightClick(is: ItemStack, world: World, player: EntityPlayer) = {
		val mop = getMovingObjectPositionFromPlayer(world, player, empty)
		if(mop == null)
			is
		else {
			val processedIs =
				mop.typeOfHit match {
					case MovingObjectPosition.MovingObjectType.MISS | MovingObjectPosition.MovingObjectType.ENTITY =>
						is
					case MovingObjectPosition.MovingObjectType.BLOCK =>
						if(empty) {
							val resultItemStack = ScoopHandler.fillScoop(world, mop)
							if(resultItemStack == null)
								is
							else
								resultItemStack
						} else {
							val resultItemStack = ScoopHandler.emptyScoop(world, mop, is)
							if(resultItemStack == null)
								is
							else
								resultItemStack
						}
				}
			if(player.capabilities.isCreativeMode)
				is
			else
				processedIs
		}
	}

	override def getMaxDamage =
		0

	override def getColorFromItemStack(is: ItemStack, layer: Int) =
		if(empty || layer == 0)
			super.getColorFromItemStack(is, layer)
		else
			color
}

object ItemScoop {
	Container.eventBus register this

	val capacity = FluidContainerRegistry.BUCKET_VOLUME / 7

	@SideOnly(Side.CLIENT)
	private lazy val localizedName = new ReloadableStrings(Future(List(new ReloadableString(s"item.${Reference.NAMESPACED_PREFIX}scoop_full.name"))))

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	def onResourcesReloaded(event: ResourcesReloadedEvent) {
		localizedName.reload()
	}
}
