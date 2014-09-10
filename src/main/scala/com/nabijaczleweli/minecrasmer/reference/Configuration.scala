package com.nabijaczleweli.minecrasmer.reference

import java.io.File

import com.nabijaczleweli.minecrasmer.worldgen.WorldGenLiquidCrystal
import net.minecraftforge.common.config.Configuration

object Configuration {
	var config: Configuration = _

	def load(configFile: File) {
		config = new Configuration(configFile)

		WorldGenLiquidCrystal.baseGenerationLevel = config.get(Reference.CONFIG_WORLDGEN_CATEGORY, "baseGenLiquidCrystalLvl", WorldGenLiquidCrystal.baseGenerationLevel,
		                                                       s"Base level of generation; 2..60; default: ${WorldGenLiquidCrystal.baseGenerationLevel}", 2, 60).getInt
		WorldGenLiquidCrystal.bigVeinProbability  = config.get(Reference.CONFIG_WORLDGEN_CATEGORY, "propGenLiquidCrystalBigVein", WorldGenLiquidCrystal.bigVeinProbability,
		                                                       s"Probability of generating a big vein (1/x); 0..${Int.MaxValue}; default: ${WorldGenLiquidCrystal.bigVeinProbability}", 0, Int.MaxValue).getInt
		WorldGenLiquidCrystal.offLevelMax         = config.get(Reference.CONFIG_WORLDGEN_CATEGORY, "deviationGenLiquidCrystalMax", WorldGenLiquidCrystal.offLevelMax,
		                                                       s"Maximal deviation of level of generation; 0..50; default: ${WorldGenLiquidCrystal.offLevelMax}", 0, 50).getInt
		WorldGenLiquidCrystal.treshold            = config.get(Reference.CONFIG_WORLDGEN_CATEGORY, "chunkGenLiquidCrystalTreshold", WorldGenLiquidCrystal.treshold,
		                                                       s"Amount of chunks of which generation will happen (1/x); 1..500; default: ${WorldGenLiquidCrystal.treshold}").getInt

		saveIfNeeded()
	}

	def saveIfNeeded() =
		if(config.hasChanged)
			config.save()
}
