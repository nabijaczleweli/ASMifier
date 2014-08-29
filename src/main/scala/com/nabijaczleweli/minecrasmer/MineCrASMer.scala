package com.nabijaczleweli.minecrasmer

import com.nabijaczleweli.minecrasmer.handler.BlocksHandler
import com.nabijaczleweli.minecrasmer.proxy.IProxy
import com.nabijaczleweli.minecrasmer.reference.Reference._
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.{Mod, SidedProxy}
import net.minecraftforge.common.MinecraftForge

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, modLanguage = "scala")
object MineCrASMer {
	@SidedProxy(clientSide = CLIENT_PROXY_PATH, serverSide = SERVER_PROXY_PATH)
	var proxy: IProxy = null

	@EventHandler
	def preInit(event: FMLPreInitializationEvent) {
		proxy.registerItemsAndBlocks()
		MinecraftForge.EVENT_BUS.register(BlocksHandler)
	}

	@EventHandler
	def init(event: FMLInitializationEvent) {

	}

	@EventHandler
	def postInit(event: FMLPostInitializationEvent) {

	}
}
