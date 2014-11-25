package com.nabijaczleweli.minecrasmer.worldgen

import java.util.{Random, List => jList}

import com.nabijaczleweli.minecrasmer.entity.Villager._
import com.nabijaczleweli.minecrasmer.reference.Reference
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageCreationHandler
import net.minecraft.init.{Blocks, Items}
import net.minecraft.item.{ItemStack, ItemBlock}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.WeightedRandomChestContent
import net.minecraft.world.World
import net.minecraft.world.gen.structure.StructureVillagePieces.{PieceWeight, Start, Village}
import net.minecraft.world.gen.structure.{StructureBoundingBox, StructureComponent, StructureVillagePieces}
import net.minecraftforge.common.ChestGenHooks
import net.minecraftforge.oredict.OreDictionary

import scala.collection.JavaConversions._
import scala.util.{Random => sRandom}

/** HEAVILY based on `planetminecraft.com/blog/modding-trouble---adding-village-components-forge-164` */
class VillageComponentElectronicShop(villagePiece: StructureVillagePieces.Start, par2: Int, par3Random: Random, par4StructureBoundingBox: StructureBoundingBox, par5: Int) extends Village(villagePiece, par2) {
	import VillageComponentElectronicShop.random

	private var averageGroundLevel = -1
	private var hasMadeChest       = false
	private var isCarpetToggled    = random.nextBoolean()
	private var glass              = VillageComponentElectronicShop randomBlockFromOreDict "blockGlass"
	private var pane               = VillageComponentElectronicShop randomBlockFromOreDict "paneGlass"
	private var fenceCarpetColor   = random nextInt 16

	def this() =
		this(null, 0, null, null, 0)

	coordBaseMode = par5
	boundingBox = par4StructureBoundingBox

	protected override def func_143012_a(par1NBTTagCompound: NBTTagCompound) {
		super.func_143012_a(par1NBTTagCompound)
		par1NBTTagCompound.setBoolean("createdChest", hasMadeChest)
		par1NBTTagCompound.setBoolean("notCarpet", isCarpetToggled)
		par1NBTTagCompound.setTag("glass", glass writeToNBT new NBTTagCompound)
		par1NBTTagCompound.setTag("pane", pane writeToNBT new NBTTagCompound)
		par1NBTTagCompound.setInteger("carpetColor", fenceCarpetColor)
	}

	protected override def func_143011_b(par1NBTTagCompound: NBTTagCompound) {
		super.func_143011_b(par1NBTTagCompound)
		hasMadeChest = par1NBTTagCompound getBoolean "createdChest"
		isCarpetToggled = par1NBTTagCompound getBoolean "notCarpet"
		glass = ItemStack loadItemStackFromNBT (par1NBTTagCompound getCompoundTag "glass")
		pane = ItemStack loadItemStackFromNBT (par1NBTTagCompound getCompoundTag "pane")
		fenceCarpetColor = par1NBTTagCompound getInteger "carpetColor"
	}

	def addComponentParts(world: World, random: Random, sbb: StructureBoundingBox): Boolean = {
		if(averageGroundLevel < 0) {
			averageGroundLevel = getAverageGroundLevel(world, sbb)
			if(averageGroundLevel < 0)
				return true
			boundingBox.offset(0, averageGroundLevel - boundingBox.maxY + 4, 0)
		}

		fillWithBlocks(world, sbb, 0, 2, 0, 6, 7, 6, Blocks.stonebrick, Blocks.stonebrick, false) // Main house
		fillWithAir(world, sbb, 1, 3, 1, 5, 6, 5) // Empty space
		placeDoorAtCurrentPosition(world, sbb, par3Random, 3, 3, 0, getMetadataWithOffset(Blocks.wooden_door, 1)) // Door
		placeBlockAtCurrentPosition(world, Blocks.stone_brick_stairs, getMetadataWithOffset(Blocks.stone_brick_stairs, 3), 3, 2, -1, sbb) // Doorsteps
		// Carpet
		for(x <- 1 until 6; z <- 1 until 6)
			if((((x + z) % 2) == 0) ^ isCarpetToggled)
				placeBlockAtCurrentPosition(world, Blocks.carpet, 0, x, 3, z, sbb)
			else
				placeBlockAtCurrentPosition(world, Blocks.carpet, 8, x, 3, z, sbb)
		// \Carpet
		val glassBlock = glass.getItem.asInstanceOf[ItemBlock].field_150939_a
		// Ceiling windows
		placeBlockAtCurrentPosition(world, glassBlock, glass.getItemDamage, 2, 7, 2, sbb)
		placeBlockAtCurrentPosition(world, glassBlock, glass.getItemDamage, 3, 7, 3, sbb)
		placeBlockAtCurrentPosition(world, glassBlock, glass.getItemDamage, 4, 7, 4, sbb)
		placeBlockAtCurrentPosition(world, glassBlock, glass.getItemDamage, 2, 7, 4, sbb)
		placeBlockAtCurrentPosition(world, glassBlock, glass.getItemDamage, 4, 7, 2, sbb)
		// \Ceiling windows
		val paneBlock = pane.getItem.asInstanceOf[ItemBlock].field_150939_a
		// Wall windows
		fillWithMetadataBlocks(world, sbb, 0, 5, 2, 0, 5, 4, paneBlock, pane.getItemDamage, paneBlock, pane.getItemDamage, false)
		fillWithMetadataBlocks(world, sbb, 6, 5, 2, 6, 5, 4, paneBlock, pane.getItemDamage, paneBlock, pane.getItemDamage, false)
		// \Ceiling windows
		// Fence + Toppings + Floor
		placeBlockAtCurrentPosition(world, Blocks.nether_brick_fence, 0, 3, 3, 4, sbb)
		placeBlockAtCurrentPosition(world, Blocks.nether_brick_fence, 0, 4, 3, 5, sbb)
		placeBlockAtCurrentPosition(world, Blocks.nether_brick_fence, 0, 2, 3, 5, sbb)
		placeBlockAtCurrentPosition(world, Blocks.carpet, fenceCarpetColor, 3, 4, 4, sbb)
		placeBlockAtCurrentPosition(world, Blocks.carpet, fenceCarpetColor, 4, 4, 5, sbb)
		placeBlockAtCurrentPosition(world, Blocks.carpet, fenceCarpetColor, 2, 4, 5, sbb)
		placeBlockAtCurrentPosition(world, Blocks.heavy_weighted_pressure_plate, 0, 3, 3, 5, sbb)
		// \Fence \Toppings \Floor

		spawnVillagers(world, sbb, 3, 3, 5, 1)
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
	private val random = new Random

	ChestGenHooks getInfo ELECTRONICS_CHEST addItem new WeightedRandomChestContent(Items.sugar, 0, 1, 4, 85)
	ChestGenHooks getInfo ELECTRONICS_CHEST addItem new WeightedRandomChestContent(Items.book, 0, 1, 2, 35)
	ChestGenHooks getInfo ELECTRONICS_CHEST addItem new WeightedRandomChestContent(Items.flint_and_steel, 0, 1, 1, 2)

	def buildComponent(villagePiece: Start, pieces: jList[_], random: Random, p1: Int, p2: Int, p3: Int, p4: Int, p5: Int) = {
		val structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p1, p2, p3, 0, 0, 0, widX, heiY, lenZ, p4)
		if((Village canVillageGoDeeper structureboundingbox) && (StructureComponent.findIntersecting(pieces, structureboundingbox) == null))
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

	private def randomBlockFromOreDict(name: String) =
		sRandom shuffle (OreDictionary getOres name).toSeq take 1 map { is =>
			is.getItemDamage match {
				case OreDictionary.WILDCARD_VALUE =>
					is setItemDamage (random nextInt 16)
				case _ =>
			}
			is
		} apply 0
}
