package com.nabijaczleweli.minecrasmer.entity.tile

import com.nabijaczleweli.minecrasmer.computing.{ComputerAccessory, MulticlockedComputer}
import com.nabijaczleweli.minecrasmer.reference.Reference
import com.nabijaczleweli.minecrasmer.util.NBTUtil._
import com.nabijaczleweli.minecrasmer.util.{IConfigurable, SimpleDataProcessingTileEntity}
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.config.Configuration

import scala.collection.mutable
import scala.util.Random

class TileEntityComputer extends SimpleDataProcessingTileEntity with MulticlockedComputer {
	var lines: Array[String] = Array("TEXT0", "TEXT1", "TEXT2", "", "", "", new Random().nextInt().toString)

	override val clockSpeed = TileEntityComputer.clocksPerTick

	override def updateEntity() {
		super.updateEntity()

		type searchingFor = TileEntity with ComputerAccessory
		val entities = mutable.Buffer[searchingFor]()
		for(x <- xCoord - 1 to xCoord + 1; y <- yCoord - 1 to yCoord + 1; z <- zCoord - 1 to zCoord + 1 if !(x == xCoord && y == yCoord && z == zCoord))
			worldObj.getTileEntity(x, y, z) match {
				case null =>
				case ent: searchingFor =>
					entities append ent
				case _ =>
			}

		var multiplier = 1F
		for(ent <- entities)
			ent match {
				case entity: TileEntityOverclocker =>
					multiplier += entity.multiplier
			}
		processorTick(multiplier)
		markDirty()
	}

	override def readFromNBT(tag: NBTTagCompound) {
		super[SimpleDataProcessingTileEntity].readFromNBT(tag)
		super[MulticlockedComputer].writeToNBT(tag)

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
