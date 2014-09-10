package com.nabijaczleweli.minecrasmer.proxy

trait IProxy {
	def registerItemsAndBlocks(): Unit

	def registerOreDict(): Unit

	def registerGUIs(): Unit

	def registerEvents(): Unit

	def registerEntities(): Unit

	def registerFluids(): Unit

	def registerRenderers(): Unit
}
