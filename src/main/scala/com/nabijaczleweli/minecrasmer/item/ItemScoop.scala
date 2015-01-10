package com.nabijaczleweli.minecrasmer.item

import java.util.{List => jList}

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.handler.ScoopHandler
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.resource.{ReloadableString, ReloadableStrings, ResourcesReloadedEvent}
import com.nabijaczleweli.minecrasmer.util.ConstructibleNBTTagCompound
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.{NBTTagCompound, NBTTagString}
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World
import net.minecraftforge.fluids.{Fluid, FluidContainerRegistry, FluidRegistry}
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.collection.immutable.HashMap
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ItemScoop extends Item {
	@SideOnly(Side.CLIENT)
	private lazy val localizedNames = new ReloadableStrings(Future(List(new ReloadableString(s"item.${Reference.NAMESPACED_PREFIX}scoop_full.name"),
	                                                                    new ReloadableString(s"item.${Reference.NAMESPACED_PREFIX}scoop_empty.name"))))
	val capacity = FluidContainerRegistry.BUCKET_VOLUME / 7
	val emptyColor = 0x3a4d55 // Picked straight from the image file
	var colors: Map[Fluid, Int] = HashMap(Container.liquidCrystal -> 0x00ff00)
	val fluidKey = s"${Reference.NAMESPACED_PREFIX}fluid_name"


	Container.eventBus register this
	setUnlocalizedName(s"${Reference.NAMESPACED_PREFIX}scoop")
	setCreativeTab(CreativeTabMineCrASMer)
	setMaxStackSize(1)

	private def actualTagCompound(is: ItemStack) = {
		if(!is.hasTagCompound)
			is setTagCompound new NBTTagCompound
		is.getTagCompound
	}

	def empty(is: ItemStack) =
		!(actualTagCompound(is) hasKey fluidKey)

	def contains(is: ItemStack) =
		fluid(is) match {
			case null =>
				null
			case fld =>
				fld.getBlock
		}

	def fluid(is: ItemStack) =
		FluidRegistry getFluid (actualTagCompound(is) getString fluidKey)

	def scoopWith(fluid: Fluid) =
		new ItemStack(this) ensuring {is => is setTagCompound new ConstructibleNBTTagCompound(Map(ItemScoop.fluidKey -> new NBTTagString(fluid.getName))); is.hasTagCompound}

	def scoopEmpty =
		new ItemStack(this)


	override def getItemStackDisplayName(is: ItemStack) =
		if(empty(is))
			localizedNames(1)
		else
			localizedNames(0) format contains(is).getLocalizedName

	override def onItemRightClick(is: ItemStack, world: World, player: EntityPlayer) = {
		val mop = getMovingObjectPositionFromPlayer(world, player, empty(is))
		if(mop == null)
			is
		else {
			val processedIs =
				mop.typeOfHit match {
					case MovingObjectPosition.MovingObjectType.MISS | MovingObjectPosition.MovingObjectType.ENTITY =>
						is
					case MovingObjectPosition.MovingObjectType.BLOCK =>
						if(empty(is)) {
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
		if(layer == 0)
			super.getColorFromItemStack(is, layer)
		else if(empty(is))
			emptyColor
		else
			colors get fluid(is) match {
				case Some(color) =>
					color
				case None =>
					0xff00ff
			}

	@SideOnly(Side.CLIENT)
	override def getSubItems(itemIn: Item, tab: CreativeTabs, subItems: jList[_]) = {
		assume(itemIn == this)
		val items = subItems.asInstanceOf[jList[ItemStack]]

		for(flu <- colors.keys)
			items add scoopWith(flu)
		items add scoopEmpty
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	def onResourcesReloaded(event: ResourcesReloadedEvent) {
		localizedNames.reload()
	}
}
