package com.nabijaczleweli.minecrasmer.reference

import com.nabijaczleweli.minecrasmer.block.BlockLiquidCrystalFluid
import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.item.ItemScoop
import com.nabijaczleweli.minecrasmer.util.IOreDictRegisterable
import cpw.mods.fml.common.eventhandler.EventBus
import net.minecraft.block.{Block, BlockAir}
import net.minecraft.block.material.{MapColor, Material}
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.oredict.OreDictionary
import org.apache.logging.log4j.LogManager

object Container extends IOreDictRegisterable {
	lazy val materialWrench = EnumHelper.addToolMaterial("Wrench", 1000, -1, 1000, 1, 0)
	lazy val materialComputer = new Material(MapColor.grayColor)

	lazy val log = LogManager getLogger Reference.MOD_ID

	lazy val liquidCrystal = new Fluid("liquidcrystal") setLuminosity 13 setViscosity Integer.MAX_VALUE setBlock (BlockLiquidCrystalFluid: Block)

	lazy val socketCPU = new Item setUnlocalizedName s"${Reference.NAMESPACED_PREFIX}CPUSocket" setCreativeTab CreativeTabMineCrASMer setTextureName s"${Reference.NAMESPACED_PREFIX}socket_cpu"
	lazy val stoneRod = new Item setUnlocalizedName s"${Reference.NAMESPACED_PREFIX}rodStone" setCreativeTab CreativeTabMineCrASMer setTextureName s"${Reference.NAMESPACED_PREFIX}rod_stone"
	lazy val scoopEmpty = new ItemScoop(Blocks.air.asInstanceOf[BlockAir])
	lazy val scoopLiquidCrystal = new ItemScoop(BlockLiquidCrystalFluid, 0x00FF00)
	var foreignScoops: List[ItemScoop] = Nil

	val eventBus = new EventBus


	// Only for classless Items!
	override def registerOreDict() =
		OreDictionary.registerOre("rodStone", stoneRod)
}
