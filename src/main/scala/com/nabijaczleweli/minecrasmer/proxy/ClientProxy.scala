package com.nabijaczleweli.minecrasmer.proxy

import com.nabijaczleweli.minecrasmer.item.ItemScoop
import com.nabijaczleweli.minecrasmer.render.FilledScoopRenderer
import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraftforge.client.MinecraftForgeClient

import scala.collection.mutable

@SideOnly(Side.CLIENT)
class ClientProxy extends CommonProxy {
	val scoopRenderQueue = mutable.Queue[ItemScoop]()

	override def registerRenderers() {
		super.registerRenderers()
		while(scoopRenderQueue.size != 0)
			MinecraftForgeClient.registerItemRenderer(scoopRenderQueue.dequeue(), FilledScoopRenderer)
	}
}
