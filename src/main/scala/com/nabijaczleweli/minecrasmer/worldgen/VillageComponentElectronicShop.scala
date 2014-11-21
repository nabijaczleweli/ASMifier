package com.nabijaczleweli.minecrasmer.worldgen

import java.util.{Random, List => jList}

import com.nabijaczleweli.minecrasmer.entity.Villager._
import com.nabijaczleweli.minecrasmer.reference.Reference
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageCreationHandler
import net.minecraft.init.{Blocks, Items}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.WeightedRandomChestContent
import net.minecraft.world.World
import net.minecraft.world.gen.structure.StructureVillagePieces.{PieceWeight, Start, Village}
import net.minecraft.world.gen.structure.{StructureBoundingBox, StructureComponent, StructureVillagePieces}
import net.minecraftforge.common.ChestGenHooks

/** HEAVILY based on `planetminecraft.com/blog/modding-trouble---adding-village-components-forge-164` */
class VillageComponentElectronicShop(villagePiece: StructureVillagePieces.Start, par2: Int, par3Random: Random, par4StructureBoundingBox: StructureBoundingBox, par5: Int) extends Village {
	coordBaseMode = par5
	boundingBox = par4StructureBoundingBox

	private var averageGroundLevel = -1
	private var hasMadeChest = false

	protected override def func_143012_a(par1NBTTagCompound: NBTTagCompound) {
		super.func_143012_a(par1NBTTagCompound)
		par1NBTTagCompound.setBoolean("createdChest", hasMadeChest)
	}

	protected override def func_143011_b(par1NBTTagCompound: NBTTagCompound) {
		super.func_143011_b(par1NBTTagCompound)
		hasMadeChest = par1NBTTagCompound getBoolean "createdChest"
	}

	def addComponentParts(world: World, random: Random, sbb: StructureBoundingBox): Boolean = {
		if(averageGroundLevel < 0) {
			averageGroundLevel = getAverageGroundLevel(world, sbb)
			if(averageGroundLevel < 0)
				return true
			boundingBox.offset(0, averageGroundLevel - boundingBox.maxY + 4, 0)
		}
		fillWithBlocks(world, sbb, 0, 2, 0, 6, 3, 6, Blocks.cobblestone, Blocks.cobblestone, false)
		fillWithBlocks(world, sbb, 0, 4, 0, 6, 7, 6, Blocks.planks, Blocks.planks, false)
		fillWithBlocks(world, sbb, 1, 2, 1, 5, 2, 5, Blocks.planks, Blocks.planks, false)
		fillWithAir(world, sbb, 1, 3, 1, 5, 5, 5)
		placeBlockAtCurrentPosition(world, Blocks.stone_stairs, getMetadataWithOffset(Blocks.stone_stairs, 3), 4, 2, -1, sbb)
		placeDoorAtCurrentPosition(world, sbb, random, 4, 3, 0, getMetadataWithOffset(Blocks.wooden_door, 3))
		for(i <- 0 until 4) {
			placeBlockAtCurrentPosition(world, Blocks.log, 0, 0, i + 2, 0, sbb)
			placeBlockAtCurrentPosition(world, Blocks.log, 0, 6, i + 2, 0, sbb)
			placeBlockAtCurrentPosition(world, Blocks.log, 0, 0, i + 2, 6, sbb)
			placeBlockAtCurrentPosition(world, Blocks.log, 0, 6, i + 2, 6, sbb)
			if((i >= 1) && (i <= 4))
				placeBlockAtCurrentPosition(world, Blocks.log, 0, 3, i + 2, 1, sbb)
		}
		for(i <- 0 until 7) {
			placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 3), i, 6, -1, sbb)
			placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 3), i, 7, 0, sbb)
		}
		for(i <- 0 until 7) {
			placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 2), i, 6, 7, sbb)
			placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 2), i, 7, 6, sbb)
		}
		fillWithBlocks(world, sbb, 1, 4, 0, 2, 5, 0, Blocks.glass_pane, Blocks.glass_pane, false)
		placeBlockAtCurrentPosition(world, Blocks.wool, 14, 1, 3, 1, sbb)
		placeBlockAtCurrentPosition(world, Blocks.wool, 14, 2, 3, 1, sbb)
		placeBlockAtCurrentPosition(world, Blocks.oak_stairs, getMetadataWithOffset(Blocks.oak_stairs, 2) + 4, 3, 5, 2, sbb)
		for(i <- 0 until 2) {
			placeBlockAtCurrentPosition(world, Blocks.log, 0, i * 6, 4, 2, sbb)
			placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, i * 6, 4, 3, sbb)
			placeBlockAtCurrentPosition(world, Blocks.log, 0, i * 6, 4, 4, sbb)
		}
		placeBlockAtCurrentPosition(world, Blocks.planks, 1, 1, 3, 4, sbb)
		placeBlockAtCurrentPosition(world, Blocks.planks, 1, 2, 3, 4, sbb)
		placeBlockAtCurrentPosition(world, Blocks.planks, 1, 3, 3, 4, sbb)
		placeBlockAtCurrentPosition(world, Blocks.planks, 1, 3, 3, 5, sbb)
		for(i <- 0 until 4)
			placeBlockAtCurrentPosition(world, Blocks.wooden_slab, 1, i, 5, 5, sbb)
		placeBlockAtCurrentPosition(world, Blocks.torch, 0, 4, 5, 1, sbb)
		if(!hasMadeChest) {
			val i = getYWithOffset(1)
			val j = getXWithOffset(5, 5)
			val k = getZWithOffset(5, 5)
			if(sbb.isVecInside(j, i, k)) {
				hasMadeChest = true
				generateStructureChestContents(world, sbb, random, 5, 3, 5, ChestGenHooks getInfo VillageComponentElectronicShop.ELECTRONICS_CHEST getItems random, 1 + (random nextInt 3))
			}
		}
		spawnVillagers(world, sbb, 2, 3, 5, 1)
		true
	}

	override def getVillagerType(i: Int) =
		electronicsVillagerID
}

object VillageComponentElectronicShop extends IVillageCreationHandler {
	final val ELECTRONICS_CHEST = Reference.NAMESPACED_PREFIX + "village_electronics"

	private val widX = 9
	private val heiY = 7
	private val lenZ = 11
	private val villageForwarder = new Village() {
		override def addComponentParts(world: World, rand: Random, sbb: StructureBoundingBox) =
			false

		@inline
		def canVillageDeeperGo(sbb: StructureBoundingBox) =
			Village canVillageGoDeeper sbb
	}

	ChestGenHooks getInfo ELECTRONICS_CHEST addItem new WeightedRandomChestContent(Items.sugar, 0, 1, 4, 85)
	ChestGenHooks getInfo ELECTRONICS_CHEST addItem new WeightedRandomChestContent(Items.book, 0, 1, 2, 35)
	ChestGenHooks getInfo ELECTRONICS_CHEST addItem new WeightedRandomChestContent(Items.flint_and_steel, 0, 1, 1, 2)

	def buildComponent(villagePiece: Start, pieces: jList[_], random: Random, p1: Int, p2: Int, p3: Int, p4: Int, p5: Int) = {
		val structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p1, p2, p3, 0, 0, 0, widX, heiY, lenZ, p4)
		if((villageForwarder canVillageDeeperGo structureboundingbox) && (StructureComponent.findIntersecting(pieces, structureboundingbox) == null))
			new VillageComponentElectronicShop(villagePiece, p5, random, structureboundingbox, p4)
		else
			null
	}

	override def getVillagePieceWeight(random: Random, i: Int) =
		new PieceWeight(getComponentClass, 15, i + (random nextInt 3))

	override def buildComponent(villagePiece: PieceWeight, startPiece: Start, pieces: jList[_], random: Random, p1: Int, p2: Int, p3: Int, p4: Int, p5: Int) =
		VillageComponentElectronicShop.buildComponent(startPiece, pieces, random, p1, p2, p3, p4, p5)

	override def getComponentClass =
		classOf[VillageComponentElectronicShop]
}
