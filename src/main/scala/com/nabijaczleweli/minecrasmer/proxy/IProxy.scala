package com.nabijaczleweli.minecrasmer.proxy

trait IProxy {
	def registerItemsAndBlocks(): Unit

	def registerOreDict(): Unit
}