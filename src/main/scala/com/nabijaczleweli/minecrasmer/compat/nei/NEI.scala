package com.nabijaczleweli.minecrasmer.compat.nei

import com.nabijaczleweli.minecrasmer.compat.{Empty, ICompat}
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.resource.{MineCrASMerLocation, ResourcesReloadedEvent}
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.Minecraft
import net.minecraft.util.StatCollector

object NEI extends ICompat {
	Container.eventBus register this

	private var quartzShardsDescription = ""
	private var cleanQuartzShardsDescription = ""

	def getCleanQuartzShardsDescription =
		cleanQuartzShardsDescription

	def getQuartzShardsDescription =
		quartzShardsDescription

	private var active: Side = null

	override def getModIDs =
		"NotEnoughItems" :: Nil

	override def preLoad(side: Side) = {
		active = side
		Empty
	}

	override def load(side: Side) = {
		Container.log info s"NEI plugin will be automatically loaded by NEI itself on the world load."
		Empty
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	def onResourcesReloaded(event: ResourcesReloadedEvent) {
		def streamForKey(key: String) =
			Minecraft.getMinecraft.getResourceManager.getResource(new MineCrASMerLocation(StatCollector translateToLocal s"hud.${Reference.NAMESPACED_PREFIX}compat.nei.inworld.$key.description.name")).getInputStream

		val quartzShardsStream = streamForKey("quartzshards")
		val cleanQuartzShardsStream = streamForKey("cleanquartzshards")

		var tempArray = new Array[Char](quartzShardsStream.available())
		for(idx <- tempArray.indices)
			tempArray(idx) = quartzShardsStream.read().toChar
		quartzShardsDescription = new String(tempArray)

		tempArray = new Array[Char](cleanQuartzShardsStream.available())
		for(idx <- tempArray.indices)
			tempArray(idx) = cleanQuartzShardsStream.read().toChar
		cleanQuartzShardsDescription = new String(tempArray)
	}
}
