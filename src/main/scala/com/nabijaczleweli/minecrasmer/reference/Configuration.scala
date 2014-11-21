package com.nabijaczleweli.minecrasmer.reference

import java.io.File

import com.nabijaczleweli.minecrasmer.entity.{Villager, EntityItemShredder}
import com.nabijaczleweli.minecrasmer.entity.tile.{TileEntityAdditionalCPU, TileEntityOverclocker, TileEntityComputer}
import com.nabijaczleweli.minecrasmer.worldgen.WorldGenLiquidCrystal
import net.minecraftforge.common.config.Configuration

object Configuration {
	private val toLoad = WorldGenLiquidCrystal :: TileEntityComputer :: TileEntityOverclocker :: TileEntityAdditionalCPU :: EntityItemShredder :: Villager :: Nil

	private var theConfig: Configuration = _

	def config =
		theConfig

	def load(configFile: File) {
		theConfig = new Configuration(configFile)

		for(tl <- toLoad)
			tl load config

		saveIfNeeded()
	}

	def saveIfNeeded() =
		if(config.hasChanged)
			config.save()
}
