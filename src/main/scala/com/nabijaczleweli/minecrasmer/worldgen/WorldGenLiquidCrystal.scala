package com.nabijaczleweli.minecrasmer.worldgen

import java.util.Random

import com.nabijaczleweli.minecrasmer.block.BlockLiquidCrystalFluid
import com.nabijaczleweli.minecrasmer.reference.Reference
import com.nabijaczleweli.minecrasmer.util.IConfigurable
import cpw.mods.fml.common.IWorldGenerator
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider
import net.minecraftforge.common.config.Configuration

object WorldGenLiquidCrystal extends IWorldGenerator with IConfigurable {
	var treshold = 5
	var bigVeinProbability = 300
	var baseGenerationLevel = 30
	var offLevelMax = 3

	private var chunksBeforeGenerating = 0

	override def generate(random: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkProvider, chunkProvider: IChunkProvider) =
		world.getBiomeGenForCoords(chunkX, chunkZ).biomeName match {
			case "Hell" | "Sky" =>
			case _ =>
				if(chunksBeforeGenerating == 0)
					if(random.nextInt(bigVeinProbability) == 0) {
						val baseY = baseGenerationLevel + (if(random.nextBoolean()) -random.nextInt(offLevelMax) else random nextInt offLevelMax) max 2
						val baseX = chunkX * 16 + random.nextInt(14) + 1
						val baseZ = chunkZ * 16 + random.nextInt(14) + 1
						world.setBlock(baseX, baseY, baseZ, BlockLiquidCrystalFluid, 8, 1 | 2)
						world.setBlock(baseX + 1, baseY, baseZ, BlockLiquidCrystalFluid, 2, 1 | 2)
						world.setBlock(baseX - 1, baseY, baseZ, BlockLiquidCrystalFluid, 2, 1 | 2)
						world.setBlock(baseX, baseY, baseZ + 1, BlockLiquidCrystalFluid, 2, 1 | 2)
						world.setBlock(baseX, baseY, baseZ - 1, BlockLiquidCrystalFluid, 2, 1 | 2)
					} else {
						val yLevel = baseGenerationLevel + (if(random.nextBoolean()) -random.nextInt(offLevelMax) else random nextInt offLevelMax)
						world.setBlock(chunkX * 16 + random.nextInt(16), yLevel max 2, chunkZ * 16 + random.nextInt(16), BlockLiquidCrystalFluid, random.nextInt(4), 1 | 2)
					}

				if(chunksBeforeGenerating < 1)
					chunksBeforeGenerating = treshold
				else
					chunksBeforeGenerating -= 1
		}

	override def load(config: Configuration) {
		baseGenerationLevel = config.get(Reference.CONFIG_WORLDGEN_CATEGORY, "baseGenLiquidCrystalLvl", baseGenerationLevel, s"Base level of generation; 2..60; default: $baseGenerationLevel", 2, 60).getInt
		bigVeinProbability = config.get(Reference.CONFIG_WORLDGEN_CATEGORY, "propGenLiquidCrystalBigVein", bigVeinProbability,
		                                s"Probability of generating a big vein (1/x); 0..${Int.MaxValue}; default: $bigVeinProbability", 0, Int.MaxValue).getInt
		offLevelMax = config.get(Reference.CONFIG_WORLDGEN_CATEGORY, "deviationGenLiquidCrystalMax", offLevelMax, s"Maximal deviation of level of generation; 0..50; default: $offLevelMax", 0, 50).getInt
		treshold = config.get(Reference.CONFIG_WORLDGEN_CATEGORY, "chunkGenLiquidCrystalTreshold", treshold, s"Amount of chunks of which generation will happen (1/x); 1..500; default: $treshold").getInt

		chunksBeforeGenerating = new Random() nextInt treshold
	}
}
