package com.nabijaczleweli.minecrasmer.compat

import cpw.mods.fml.relauncher.Side

trait ICompat {
	def getModIDs: List[String]

	def preLoad(side: Side): CompatResult

	def load(side: Side): CompatResult
}
