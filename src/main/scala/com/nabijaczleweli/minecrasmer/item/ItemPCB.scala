package com.nabijaczleweli.minecrasmer.item

import java.util

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.resource._
import com.nabijaczleweli.minecrasmer.util.IMultiModelItem
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.model.{ModelBakery, ModelResourceLocation}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.MathHelper
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ItemPCB extends Item with IMultiModelItem {
	Container.eventBus register this

	private      val subIconNames   = Array("%selements", "%snoelements", "%slcd", "lcd")
	private      val subNameNames   = Array("elements", "noelements", "withlcd", "lcd")
	@SideOnly(Side.CLIENT)
	private lazy val localizedNames = new ReloadableStrings(Future({subIconNames.indices map {idx => new AutoFormattingReloadableString(s"${super.getUnlocalizedName}.${subNameNames(idx)}.name", "PCB")}}.toList))

	val fullPCBDamage  = 0
	val emptyPCBDamage = 1
	val PCBLCDDamage   = 2
	val LCDDamage      = 3

	setUnlocalizedName(Reference.NAMESPACED_PREFIX + "PCB")
	setCreativeTab(CreativeTabMineCrASMer)
	setHasSubtypes(true)

	override def getMaxDamage =
		0

	override def getItemStackDisplayName(is: ItemStack) =
		localizedNames(MathHelper.clamp_int(is.getItemDamage, 0, subNameNames.length))

	@SideOnly(Side.CLIENT)
	override def getSubItems(item: Item, tab: CreativeTabs, list: util.List[ItemStack]) {
		if(item.isInstanceOf[this.type])
			for(i <- 0 until localizedNames.length)
				list.asInstanceOf[util.List[ItemStack]] add new ItemStack(item, 1, i)
	}

	override def getUnlocalizedName(stack: ItemStack) =
		"item." + Reference.NAMESPACED_PREFIX + '.' + subNameNames(stack.getMetadata)

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	def onResourcesReloaded(event: ResourcesReloadedEvent) {
		localizedNames.reload()
	}

	@SideOnly(Side.CLIENT)
	override def registerModels() {
		for(i <- fullPCBDamage to LCDDamage) {
			Minecraft.getMinecraft.getRenderItem.getItemModelMesher.register(this, i, new ModelResourceLocation(Reference.NAMESPACED_PREFIX + subIconNames(i).format("pcb_"), "inventory"))
			ModelBakery.registerItemVariants(this, MineCrASMerLocation(subIconNames(i).format("pcb_")))
		}
	}
}
