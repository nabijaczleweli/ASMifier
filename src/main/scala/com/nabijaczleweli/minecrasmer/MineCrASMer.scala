package com.nabijaczleweli.minecrasmer

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLPostInitializationEvent, FMLInitializationEvent, FMLPreInitializationEvent}
import com.nabijaczleweli.minecrasmer.reference.Reference

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, modLanguage = "scala")
object MineCrASMer {
	@EventHandler
	def preInit(event: FMLPreInitializationEvent) {

	}

	@EventHandler
	def init(event: FMLInitializationEvent) {

	}

	@EventHandler
	def postInit(event: FMLPostInitializationEvent) {

	}
}
