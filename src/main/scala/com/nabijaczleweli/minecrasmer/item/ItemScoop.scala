package com.nabijaczleweli.minecrasmer.item

import com.nabijaczleweli.minecrasmer.MineCrASMer
import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.handler.ScoopHandler
import com.nabijaczleweli.minecrasmer.proxy.ClientProxy
import com.nabijaczleweli.minecrasmer.reference.Container.log
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.resource.ResourcesReloadedEvent
import com.nabijaczleweli.minecrasmer.util.StringUtils._
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.{Block, BlockAir}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemBucket, ItemStack}
import net.minecraft.util.{IIcon, MovingObjectPosition, StatCollector}
import net.minecraft.world.World
import net.minecraftforge.fluids.{Fluid, FluidContainerRegistry, IFluidBlock}

class ItemScoop(val contains: Block, val fluid: Fluid, val color: Int) extends ItemBucket(contains) {
	def this(block: Block with IFluidBlock, color: Int) =
		this(block, block.getFluid, color)

	def this(block: BlockAir) =
		this(block, null, 0)

	val empty = contains.isInstanceOf[BlockAir] || fluid == null
	protected lazy val containsDisplayName = Item getItemFromBlock contains match {
		case null =>
			log error s"Someone created a scoop with an unregistered block inside! Block name: ${contains.getUnlocalizedName}"
			"<null>"
		case it =>
			it getItemStackDisplayName new ItemStack(it)
	}
	@SideOnly(Side.CLIENT)
	final lazy val icons = new Array[IIcon](1)

	setUnlocalizedName(s"${Reference.NAMESPACED_PREFIX}scoop${if(empty) "Empty" else {contains.getUnlocalizedName substring 5 substring ":" toUpper 0}}")
	setCreativeTab(CreativeTabMineCrASMer)
	setMaxStackSize(1)
	if(!empty) {
		setContainerItem(Container.scoopEmpty)
		if(FMLCommonHandler.instance().getEffectiveSide.isClient)
			MineCrASMer.proxy.asInstanceOf[ClientProxy].scoopRenderQueue enqueue this
	}
	setTextureName(Reference.NAMESPACED_PREFIX + "scoop_empty")

	override def getItemStackDisplayName(is: ItemStack) =
		if(empty)
			super.getItemStackDisplayName(is)
		else
			ItemScoop.localizedName(0).format(containsDisplayName)

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

	override def getColorFromItemStack(is: ItemStack, pass: Int) =
		if(empty)
			super.getColorFromItemStack(is, pass)
		else
			color

	@SideOnly(Side.CLIENT)
	override def registerIcons(register: IIconRegister) {
		super.registerIcons(register)
		if(!empty)
			icons(0) = register registerIcon Reference.NAMESPACED_PREFIX + "scoop_mask"
	}
}

object ItemScoop {
	Container.eventBus register this

	val capacity = FluidContainerRegistry.BUCKET_VOLUME / 7

	@SideOnly(Side.CLIENT)
	private lazy val localizedName = new Array[String](1)

	@SubscribeEvent
	def onResourcesReloaded(event: ResourcesReloadedEvent) {
		localizedName(0) = StatCollector translateToLocal s"item.${Reference.NAMESPACED_PREFIX}scoopFilled.name"
	}
}
