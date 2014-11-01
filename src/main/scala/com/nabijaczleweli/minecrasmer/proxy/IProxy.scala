package com.nabijaczleweli.minecrasmer.proxy

import com.nabijaczleweli.minecrasmer.util.IOreDictRegisterable

trait IProxy extends IOreDictRegisterable {
	def registerItemsAndBlocks(): Unit

	def registerGUIs(): Unit

	def registerEvents(): Unit

	def registerEntities(): Unit

	def registerFluids(): Unit

	def registerRenderers(): Unit

	def registerOreGen(): Unit

	def registerRecipes(): Unit

	def registerLoot(): Unit
}
