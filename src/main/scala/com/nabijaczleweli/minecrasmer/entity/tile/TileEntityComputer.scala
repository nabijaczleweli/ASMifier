package com.nabijaczleweli.minecrasmer.entity.tile

import com.nabijaczleweli.minecrasmer.computing.{ComputerAccessory, MulticlockedComputer}
import com.nabijaczleweli.minecrasmer.reference.Reference
import com.nabijaczleweli.minecrasmer.util.NBTUtil._
import com.nabijaczleweli.minecrasmer.util.{IConfigurable, SimpleDataProcessingTileEntity}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.server.gui.IUpdatePlayerListBox
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.config.Configuration

import scala.collection.mutable.{Buffer => mBuffer}
import scala.util.Random

class TileEntityComputer extends SimpleDataProcessingTileEntity with MulticlockedComputer with IUpdatePlayerListBox {
	type accessoryType = TileEntity with ComputerAccessory
	var lines: Array[String] = Array("TEXT0", "TEXT1", "TEXT2", "", "", "", new Random().nextInt().toString)
	private val entities = mBuffer[accessoryType]()

	override def clockSpeed = TileEntityComputer.clocksPerTick

	override def CPUs =
		(1, nativeMultiplier) :: Nil ++ externals

	def nativeMultiplier = {
		readEntities()
		var mul = 1F
		(entities filter {_.isInstanceOf[TileEntityOverclocker]}).asInstanceOf[mBuffer[TileEntityOverclocker]] foreach {mul += _.multiplier}
		mul
	}

	def externals = {
		readEntities()
		(entities filter {_.isInstanceOf[TileEntityAdditionalCPU]}).asInstanceOf[mBuffer[TileEntityAdditionalCPU]] map {CPU => (CPU.processors, CPU.multiplier)}
	}

	override def update() {
		processorTick()
		entities.clear()
		markDirty()
	}

	private def readEntities() =
		if(entities.isEmpty)
			for(x <- -1 to 1; y <- -1 to 1; z <- -1 to 1)
				worldObj getTileEntity pos.add(x, y, z) match {
					case null =>
					case ent: accessoryType =>
						entities append ent
					case _ =>
				}

	override def readFromNBT(tag: NBTTagCompound) {
		super[SimpleDataProcessingTileEntity].readFromNBT(tag)
		super[MulticlockedComputer].readFromNBT(tag)

		lines = tag readStringArray "lines"
	}

	override def writeToNBT(tag: NBTTagCompound) {
		super[SimpleDataProcessingTileEntity].writeToNBT(tag)
		super[MulticlockedComputer].writeToNBT(tag)

		lines.writeToNBT(tag, "lines")
	}
}

object TileEntityComputer extends IConfigurable {
	final var clocksPerTick = 1

	override def load(config: Configuration) {
		clocksPerTick = config.getInt("TEComputerClocksPerTick", Reference.CONFIG_COMPUTE_CATEGORY, clocksPerTick, 0, Int.MaxValue, "Processor ticks per one update tick")
	}
}
