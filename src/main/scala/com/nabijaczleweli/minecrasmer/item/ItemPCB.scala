package com.nabijaczleweli.minecrasmer.item

import java.util

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.resource._
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.{IIcon, MathHelper}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object ItemPCB extends Item {
	Container.eventBus register this

	private val subIconNames = Array[String]("%selements", "%snoelements", "%slcd", "lcd")
	private val subNameNames = Array[String]("elements", "noelements", "withlcd", "lcd")
	@SideOnly(Side.CLIENT)
	private lazy val icons = new Array[IIcon](subIconNames.length)
	@SideOnly(Side.CLIENT)
	private lazy val localizedNames = new ReloadableStrings(Future({
		                                                               for(nameIdx <- subIconNames.indices) yield
			                                                               new AutoFormattingReloadableString(s"$getUnlocalizedName.${subNameNames(nameIdx)}.name", "PCB")
	                                                               }.toList))

	val fullPCBDamage  = 0
	val emptyPCBDamage = 1
	val PCBLCDDamage   = 2
	val LCDDamage      = 3

	setUnlocalizedName(Reference.NAMESPACED_PREFIX + "PCB")
	setCreativeTab(CreativeTabMineCrASMer)
	setHasSubtypes(true)

	override def getMaxDamage =
		0

	@SideOnly(Side.CLIENT)
	override def getIconFromDamage(idx: Int) =
		icons(MathHelper.clamp_int(idx, 0, icons.length - 1))

	@SideOnly(Side.CLIENT)
	override def registerIcons(ir: IIconRegister) {
		for(i <- 0 until icons.length)
			icons(i) = ir registerIcon Reference.NAMESPACED_PREFIX + subIconNames(i).format("pcb_")
	}

	override def getItemStackDisplayName(is: ItemStack) =
		localizedNames(MathHelper.clamp_int(is.getItemDamage, 0, subNameNames.length))

	@SideOnly(Side.CLIENT)
	override def getSubItems(item: Item, tab: CreativeTabs, list: util.List[_]) {
		if(item.isInstanceOf[this.type])
			for(i <- 0 until icons.length)
				list.asInstanceOf[util.List[ItemStack]] add new ItemStack(item, 1, i)
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	def onResourcesReloaded(event: ResourcesReloadedEvent) {
		localizedNames.reload()
	}
}
