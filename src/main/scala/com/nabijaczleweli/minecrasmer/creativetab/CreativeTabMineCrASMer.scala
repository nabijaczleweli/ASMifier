package com.nabijaczleweli.minecrasmer.creativetab

import com.nabijaczleweli.minecrasmer.block.BlockComputerOff
import com.nabijaczleweli.minecrasmer.reference.Container
import com.nabijaczleweli.minecrasmer.resource.ResourcesReloadedEvent
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.util.StatCollector

object CreativeTabMineCrASMer extends CreativeTabs("ASMifier") {
	Container.eventBus register this

	@SideOnly(Side.CLIENT)
	lazy val translated = new Array[String](1)

	override def getTabIconItem =
		Item getItemFromBlock BlockComputerOff

	@SideOnly(Side.CLIENT)
	override def getTranslatedTabLabel =
		translated(0)

	@SubscribeEvent
	def onResourcesReloaded(event: ResourcesReloadedEvent) {
		translated(0) = StatCollector translateToLocal (super.getTranslatedTabLabel + ".name")
	}
}
