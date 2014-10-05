package com.nabijaczleweli.minecrasmer.creativetab

import com.nabijaczleweli.minecrasmer.block.BlockComputerOff
import com.nabijaczleweli.minecrasmer.reference.Container
import com.nabijaczleweli.minecrasmer.resource.{ReloadableString, ReloadableStrings, ResourcesReloadedEvent}
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object CreativeTabMineCrASMer extends CreativeTabs("ASMifier") {
	Container.eventBus register this

	@SideOnly(Side.CLIENT)
	lazy val translated = new ReloadableStrings(Future(List(new ReloadableString(super.getTranslatedTabLabel + ".name"))))

	override def getTabIconItem =
		Item getItemFromBlock BlockComputerOff

	@SideOnly(Side.CLIENT)
	override def getTranslatedTabLabel =
		translated(0)

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	def onResourcesReloaded(event: ResourcesReloadedEvent) {
		translated.reload()
	}
}
