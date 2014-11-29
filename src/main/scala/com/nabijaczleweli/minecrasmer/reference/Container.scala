package com.nabijaczleweli.minecrasmer.reference

import com.nabijaczleweli.minecrasmer.block.BlockLiquidCrystalFluid
import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.item.ItemScoop
import com.nabijaczleweli.minecrasmer.util.IOreDictRegisterable
import net.minecraft.block.material.{MapColor, Material}
import net.minecraft.block.{Block, BlockAir}
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fml.common.eventhandler.EventBus
import net.minecraftforge.oredict.OreDictionary
import org.apache.logging.log4j.LogManager

object Container extends IOreDictRegisterable {
	import com.nabijaczleweli.minecrasmer.reference.Reference.NAMESPACED_PREFIX

	lazy val materialWrench   = EnumHelper.addToolMaterial("Wrench", 1000, -1, 1000, 1, 0)
	lazy val materialComputer = new Material(MapColor.grayColor)

	val log = LogManager.getRootLogger // Set by the mod instance in PreInit; needs not to be lazy (reflection)

	lazy val liquidCrystal = new Fluid("liquidcrystal") setLuminosity 13 setViscosity Integer.MAX_VALUE setBlock (BlockLiquidCrystalFluid: Block) setUnlocalizedName s"${NAMESPACED_PREFIX}liquidcrystal"

	lazy val socketCPU          = new Item setUnlocalizedName s"${NAMESPACED_PREFIX}CPUSocket" setCreativeTab CreativeTabMineCrASMer
	lazy val stoneRod           = new Item setUnlocalizedName s"${NAMESPACED_PREFIX}rodStone" setCreativeTab CreativeTabMineCrASMer
	lazy val scoopEmpty         = new ItemScoop(Blocks.air.asInstanceOf[BlockAir])
	lazy val scoopLiquidCrystal = new ItemScoop(BlockLiquidCrystalFluid, 0x00FF00)
	var foreignScoops: List[ItemScoop] = Nil

	val eventBus = new EventBus


	// Only for classless Items!
	override def registerOreDict() =
		OreDictionary.registerOre("rodStone", stoneRod)
}
