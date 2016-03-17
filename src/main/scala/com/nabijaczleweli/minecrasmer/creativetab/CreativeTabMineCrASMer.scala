package com.nabijaczleweli.minecrasmer.creativetab

import com.nabijaczleweli.minecrasmer.block.BlockComputerOff
import com.nabijaczleweli.minecrasmer.reference.Container
import com.nabijaczleweli.minecrasmer.resource.{ReloadableString, ReloadableStrings, ResourcesReloadedEvent}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

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
