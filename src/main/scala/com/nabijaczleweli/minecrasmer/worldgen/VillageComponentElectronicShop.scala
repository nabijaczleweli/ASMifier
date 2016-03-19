package com.nabijaczleweli.minecrasmer.worldgen

import java.util.{List => jList, Random}

import com.nabijaczleweli.minecrasmer.entity.Villager._
import com.nabijaczleweli.minecrasmer.reference.Reference
import net.minecraft.init.{Blocks, Items}
import net.minecraft.item.{ItemBlock, ItemStack}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.{EnumFacing, WeightedRandomChestContent}
import net.minecraft.world.World
import net.minecraft.world.gen.structure.StructureVillagePieces.{PieceWeight, Start, Village}
import net.minecraft.world.gen.structure.{StructureBoundingBox, StructureComponent, StructureVillagePieces}
import net.minecraftforge.common.ChestGenHooks
import net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageCreationHandler
import net.minecraftforge.oredict.OreDictionary

import scala.collection.JavaConversions._
import scala.util.{Random => sRandom}

/** Heavily based on `planetminecraft.com/blog/modding-trouble---adding-village-components-forge-164` */
class VillageComponentElectronicShop(villagePiece: StructureVillagePieces.Start, par2: Int, par3Random: Random, par4StructureBoundingBox: StructureBoundingBox,
                                     par5: EnumFacing) extends Village(villagePiece, par2) {
	import com.nabijaczleweli.minecrasmer.worldgen.VillageComponentElectronicShop.random

	private var averageGroundLevel = -1
	private var hasMadeChest       = false
	private var isCarpetToggled    = random.nextBoolean()
	private var glass              = VillageComponentElectronicShop randomBlockFromOreDict "blockGlass"
	private var pane               = VillageComponentElectronicShop randomBlockFromOreDict "paneGlass"
	private var fenceCarpetColor   = random nextInt 16

	def this() =
		this(null, 0, null, null, EnumFacing.NORTH)

	coordBaseMode = par5
	boundingBox = par4StructureBoundingBox

	protected override def readStructureFromNBT(par1NBTTagCompound: NBTTagCompound) {
		super.readStructureFromNBT(par1NBTTagCompound)
		par1NBTTagCompound.setBoolean("createdChest", hasMadeChest)
		par1NBTTagCompound.setBoolean("notCarpet", isCarpetToggled)
		par1NBTTagCompound.setTag("glass", glass writeToNBT new NBTTagCompound)
		par1NBTTagCompound.setTag("pane", pane writeToNBT new NBTTagCompound)
		par1NBTTagCompound.setInteger("carpetColor", fenceCarpetColor)
	}

	protected override def writeStructureToNBT(par1NBTTagCompound: NBTTagCompound) {
		super.writeStructureToNBT(par1NBTTagCompound)
		hasMadeChest = par1NBTTagCompound getBoolean "createdChest"
		isCarpetToggled = par1NBTTagCompound getBoolean "notCarpet"
		ItemStack loadItemStackFromNBT (par1NBTTagCompound getCompoundTag "glass") match {
			case null =>
			case is =>
				glass = is
		}
		ItemStack loadItemStackFromNBT (par1NBTTagCompound getCompoundTag "pane") match {
			case null =>
			case is =>
				pane = is
		}
		fenceCarpetColor = par1NBTTagCompound getInteger "carpetColor"
	}

	def addComponentParts(world: World, random: Random, sbb: StructureBoundingBox): Boolean = {
		if(averageGroundLevel < 0) {
			averageGroundLevel = getAverageGroundLevel(world, sbb)
			if(averageGroundLevel < 0)
				return true
			boundingBox.offset(0, averageGroundLevel - boundingBox.maxY + 4, 0)
		}

		randomlyRareFillWithBlocks(world, sbb, 0, 2, 0, 6, 7, 6, Blocks.stonebrick.getDefaultState, false)  // Main house
		fillWithAir(world, sbb, 1, 3, 1, 5, 6, 5)  // Empty space
		placeDoorCurrentPosition(world, sbb, par3Random, 3, 3, 0, EnumFacing.NORTH)  // Door
		setBlockState(world, Blocks.stone_brick_stairs getStateFromMeta getMetadataWithOffset(Blocks.stone_brick_stairs, 3), 3, 2, -1, sbb)  // Doorsteps
		// Carpet
		for(x <- 1 until 6; z <- 1 until 6)
			if((((x + z) % 2) == 0) ^ isCarpetToggled)
				setBlockState(world, Blocks.carpet getStateFromMeta 0, x, 3, z, sbb)
			else
				setBlockState(world, Blocks.carpet getStateFromMeta 8, x, 3, z, sbb)
		// \Carpet
		val glassState = glass.getItem.asInstanceOf[ItemBlock].block getStateFromMeta glass.getItemDamage
		// Ceiling windows
		setBlockState(world, glassState, 2, 7, 2, sbb)
		setBlockState(world, glassState, 3, 7, 3, sbb)
		setBlockState(world, glassState, 4, 7, 4, sbb)
		setBlockState(world, glassState, 2, 7, 4, sbb)
		setBlockState(world, glassState, 4, 7, 2, sbb)
		// \Ceiling windows
		val paneState = pane.getItem.asInstanceOf[ItemBlock].block getStateFromMeta pane.getItemDamage
		// Wall windows
		randomlyRareFillWithBlocks(world, sbb, 0, 5, 2, 0, 5, 4, paneState, false)
		randomlyRareFillWithBlocks(world, sbb, 6, 5, 2, 6, 5, 4, paneState, false)
		// \Ceiling windows
		// Fence + Toppings + Floor
		setBlockState(world, Blocks.nether_brick_fence.getDefaultState, 3, 3, 4, sbb)
		setBlockState(world, Blocks.nether_brick_fence.getDefaultState, 4, 3, 5, sbb)
		setBlockState(world, Blocks.nether_brick_fence.getDefaultState, 2, 3, 5, sbb)
		setBlockState(world, Blocks.carpet getStateFromMeta fenceCarpetColor, 3, 4, 4, sbb)
		setBlockState(world, Blocks.carpet getStateFromMeta fenceCarpetColor, 4, 4, 5, sbb)
		setBlockState(world, Blocks.carpet getStateFromMeta fenceCarpetColor, 2, 4, 5, sbb)
		setBlockState(world, Blocks.heavy_weighted_pressure_plate.getDefaultState, 3, 3, 5, sbb)
		// \Fence \Toppings \Floor

		spawnVillagers(world, sbb, 3, 3, 5, 1)
		true
	}

	override def func_180779_c(default: Int, idx: Int) =  // getVillagerType
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

	def buildComponent(villagePiece: Start, pieces: jList[StructureComponent], random: Random, p1: Int, p2: Int, p3: Int, p4: EnumFacing, p5: Int) = {
		val structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p1, p2, p3, 0, 0, 0, widX, heiY, lenZ, p4)
		if((Village canVillageGoDeeper structureboundingbox) && (StructureComponent.findIntersecting(pieces, structureboundingbox) == null))
			new VillageComponentElectronicShop(villagePiece, p5, random, structureboundingbox, p4)
		else
			null
	}

	override def getVillagePieceWeight(random: Random, i: Int) =
		new PieceWeight(getComponentClass, 15, i + (random nextInt 3))

	override def buildComponent(villagePiece: StructureVillagePieces.PieceWeight, startPiece: StructureVillagePieces.Start, pieces: jList[StructureComponent],
	                            random: Random, p1: Int, p2: Int, p3: Int, facing: EnumFacing, p5: Int) =
		VillageComponentElectronicShop.buildComponent(startPiece, pieces, random, p1, p2, p3, facing, p5)

	override def getComponentClass =
		classOf[VillageComponentElectronicShop]

	private def randomBlockFromOreDict(name: String) =
		//noinspection ZeroIndexToHead
		sRandom shuffle (OreDictionary getOres name).toSeq take 1 map {is =>
			is.getItemDamage match {
				case OreDictionary.WILDCARD_VALUE =>
					is setItemDamage (random nextInt 16)
				case _ =>
			}
			is
		} apply 0
}
