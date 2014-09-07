package com.nabijaczleweli.minecrasmer.worldgen

import java.util.Random

import com.nabijaczleweli.minecrasmer.block.BlockLiquidCrystalFluid
import cpw.mods.fml.common.IWorldGenerator
import net.minecraft.world.World
import net.minecraft.world.chunk.IChunkProvider

object WorldGenLiquidCrystal extends IWorldGenerator {
	var treshold = 5
	var bigVeinProbability = 300
	var baseGenerationLevel = 30
	var offLevelMax = 3

	private var generatedChunks = 0L

	override def generate(random: Random, chunkX: Int, chunkZ: Int, world: World, chunkGenerator: IChunkProvider, chunkProvider: IChunkProvider) {
		generatedChunks %= treshold

		if(generatedChunks == 0)
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

		generatedChunks += 1
	}
}
