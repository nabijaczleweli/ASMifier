package com.nabijaczleweli.minecrasmer.compat

trait ICompat {
	def getModIDs: List[String]

	def preLoad: Boolean

	def load: Boolean
}
