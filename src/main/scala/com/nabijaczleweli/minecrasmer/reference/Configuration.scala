package com.nabijaczleweli.minecrasmer.reference

import java.io.File

import com.nabijaczleweli.minecrasmer.entity.tile.{TileEntityAdditionalCPU, TileEntityOverclocker, TileEntityComputer}
import com.nabijaczleweli.minecrasmer.worldgen.WorldGenLiquidCrystal
import net.minecraftforge.common.config.Configuration

object Configuration {
	private val toLoad = WorldGenLiquidCrystal :: TileEntityComputer :: TileEntityOverclocker :: TileEntityAdditionalCPU :: Nil

	var config: Configuration = _

	def load(configFile: File) {
		config = new Configuration(configFile)

		for(tl <- toLoad)
			tl load config

		saveIfNeeded()
	}

	def saveIfNeeded() =
		if(config.hasChanged)
			config.save()
}
