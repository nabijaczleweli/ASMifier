package com.nabijaczleweli.minecrasmer.compat.nei

import java.io.{BufferedReader, InputStreamReader}

import com.nabijaczleweli.minecrasmer.compat.{Empty, ICompat}
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.resource.{MineCrASMerLocation, ResourcesReloadedEvent}
import net.minecraft.client.Minecraft
import net.minecraft.util.StatCollector
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

object NEI extends ICompat {
	Container.eventBus register this

	private[nei] var quartzShardsDescription      = ""
	private[nei] var cleanQuartzShardsDescription = ""
	private[nei] var scoopsDescription            = ""
	private[nei] var active: Side                 = null

	override def getModIDs =
		"NotEnoughItems" :: Nil

	override def preLoad(side: Side) = {
		active = side
		Empty
	}

	override def load(side: Side) = {
		if(active != null)
			Container.log info s"NEI plugin will be automatically loaded by NEI itself on the world load."
		else
			Container.log info s"NEI plugin will NOT be loaded, since NEI compat is inactive."
		Empty
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	def onResourcesReloaded(event: ResourcesReloadedEvent) {
		def streamForKey(key: String) =
			Minecraft.getMinecraft.getResourceManager.getResource(MineCrASMerLocation(
				StatCollector translateToLocal s"hud.${Reference.NAMESPACED_PREFIX}compat.nei.inworld.$key.description.name")).getInputStream
		def bufferForKey(key: String) =
			new BufferedReader(new InputStreamReader(streamForKey(key), "UTF-8"))
		def lineForKey(key: String, name: String) = {
			var buf: BufferedReader = null
			var ret: String = ""
			try {
				buf = bufferForKey(key)
				ret = buf.readLine()
			} catch {
				case exc: Exception =>
					Container.log warn s"Loading display message for $name failed: $exc"
			} finally {
				if(buf != null)
					buf.close()
			}
			ret
		}

		if(active != null) {
			quartzShardsDescription = lineForKey("quartzshards", "Quartz Shards")
			cleanQuartzShardsDescription = lineForKey("cleanquartzshards", "Clean Quartz Shards")
			scoopsDescription = lineForKey("scoops", "Scoops")
		}
	}
}
