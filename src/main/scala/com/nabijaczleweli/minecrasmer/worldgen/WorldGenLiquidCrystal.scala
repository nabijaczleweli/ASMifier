package com.nabijaczleweli.minecrasmer.worldgen

import java.util.Random

import com.nabijaczleweli.minecrasmer.block.BlockLiquidCrystalFluid
import com.nabijaczleweli.minecrasmer.reference.Reference
import com.nabijaczleweli.minecrasmer.util.IConfigurable
import net.minecraft.util.BlockPos
import net.minecraft.world.chunk.IChunkProvider
import net.minecraft.world.{World, WorldType}
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.common.IWorldGenerator

object WorldGenLiquidCrystal extends IWorldGenerator with IConfigurable {
	var treshold            = 5
	var bigVeinProbability  = 300
	var baseGenerationLevel = 30
	var offLevelMax         = 3

	private var chunksBeforeGenerating = 0

	override def generate(random: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkProvider, chunkProvider: IChunkProvider) =
		if(world.getWorldInfo.getTerrainType != WorldType.FLAT)
			world.getBiomeGenForCoords(new BlockPos(chunkX * 16, 64, chunkZ * 16)).biomeName match {
				case "Hell" | "Sky" =>
				case _ =>
					if(chunksBeforeGenerating == 0)
						if(random.nextInt(bigVeinProbability) == 0) {
							val base = new BlockPos(chunkX * 16 + random.nextInt(14) + 1, baseGenerationLevel + (if(random.nextBoolean()) -random.nextInt(offLevelMax) else random nextInt offLevelMax) max 2,
							                        chunkX * 16 + random.nextInt(14) + 1)
							generate(world, base, 8)
							generate(world, base.offsetNorth, 8)
							generate(world, base.offsetSouth, 8)
							generate(world, base.offsetWest, 8)
							generate(world, base.offsetEast, 8)
						} else {
							val yLevel = baseGenerationLevel + (if(random.nextBoolean()) -random.nextInt(offLevelMax) else random nextInt offLevelMax)
							generate(world, new BlockPos(chunkX * 16 + random.nextInt(16), yLevel max 2, chunkZ * 16 + random.nextInt(16)), random nextInt 4)
						}

					if(chunksBeforeGenerating < 1)
						chunksBeforeGenerating = treshold
					else
						chunksBeforeGenerating -= 1
			}

	private def generate(world: World, pos: BlockPos, amt: Int) =
		world.setBlockState(pos, BlockLiquidCrystalFluid getStateFromMeta amt, 1 | 2)

	override def load(config: Configuration) {
		baseGenerationLevel = config.getInt("baseGenLiquidCrystalLvl", Reference.CONFIG_WORLDGEN_CATEGORY, baseGenerationLevel, 2, 60, "Base level of generation")
		bigVeinProbability = config.getInt("propGenLiquidCrystalBigVein", Reference.CONFIG_WORLDGEN_CATEGORY, bigVeinProbability, 0, Int.MaxValue, "Probability of generating a big vein (1/x)")
		offLevelMax = config.getInt("deviationGenLiquidCrystalMax", Reference.CONFIG_WORLDGEN_CATEGORY, offLevelMax, 0, 50, "Maximal deviation of level of generation")
		treshold = config.getInt("chunkGenLiquidCrystalTreshold", Reference.CONFIG_WORLDGEN_CATEGORY, treshold, 0, 500, "Amount of chunks of which generation will happen (1/x)")

		chunksBeforeGenerating = new Random() nextInt treshold
	}
}
