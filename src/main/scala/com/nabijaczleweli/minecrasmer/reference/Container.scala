package com.nabijaczleweli.minecrasmer.reference

import com.nabijaczleweli.minecrasmer.block.BlockLiquidCrystalFluid
import com.nabijaczleweli.minecrasmer.item.ItemScoop
import net.minecraft.block.{BlockAir, Block}
import net.minecraft.block.material.{MapColor, Material}
import net.minecraft.init.Blocks
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.fluids.Fluid
import org.apache.logging.log4j.LogManager

object Container {
	lazy val materialWrench = EnumHelper.addToolMaterial("Wrench", 1000, -1, 1000, 1, 0)
	lazy val materialComputer = new Material(MapColor.grayColor)

	lazy val log = LogManager getLogger Reference.MOD_ID

	lazy val liquidCrystal = new Fluid("liquidcrystal") setLuminosity 13 setViscosity Integer.MAX_VALUE setBlock BlockLiquidCrystalFluid.asInstanceOf[Block]

	lazy val scoopEmpty = new ItemScoop(Blocks.air.asInstanceOf[BlockAir])
	lazy val scoopLiquidCrystal = new ItemScoop(BlockLiquidCrystalFluid, 0x00FF00)
	var foreignScoops: List[ItemScoop] = Nil
}
