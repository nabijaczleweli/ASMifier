package com.nabijaczleweli.minecrasmer

import com.nabijaczleweli.minecrasmer.compat.{Vanilla, AE2}
import com.nabijaczleweli.minecrasmer.proxy.IProxy
import com.nabijaczleweli.minecrasmer.reference.Container._
import com.nabijaczleweli.minecrasmer.reference.Reference._
import com.nabijaczleweli.minecrasmer.util.ImplicitConvertions._
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.{Mod, SidedProxy}

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = "after:appliedenergistics2", modLanguage = "scala")
object MineCrASMer {
	@SidedProxy(clientSide = CLIENT_PROXY_PATH, serverSide = SERVER_PROXY_PATH)
	var proxy: IProxy = null

	val compats = new AE2 :: new Vanilla :: Nil

	@EventHandler
	def preInit(event: FMLPreInitializationEvent) {
		proxy.registerFluids()
		proxy.registerItemsAndBlocks()
		proxy.registerGUIs()
		proxy.registerEntities()

		for(compat <- compats)
			if(compat.hasAllLoaded)
				if(compat.preLoad)
					log info s"Successfully preloaded compat ${compat.getClass.getSimpleName}."
				else
					log info s"Preloading compat ${compat.getClass.getSimpleName} failed."
			else
				log info s"Could not find all mods for ${compat.getClass.getSimpleName}, hence its preloading failed."
	}

	@EventHandler
	def init(event: FMLInitializationEvent) {
		proxy.registerEvents()
		proxy.registerOreDict()

		for(compat <- compats)
			if(compat.hasAllLoaded)
				if(compat.load)
					log info s"Successfully loaded compat ${compat.getClass.getSimpleName}."
				else
					log info s"Loading compat ${compat.getClass.getSimpleName} failed."
			else
				log info s"Could not find all mods for ${compat.getClass.getSimpleName}, hence its loading failed."
	}

	@EventHandler
	def postInit(event: FMLPostInitializationEvent) {

	}
}
