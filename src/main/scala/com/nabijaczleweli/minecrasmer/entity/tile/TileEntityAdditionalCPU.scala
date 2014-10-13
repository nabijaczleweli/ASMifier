package com.nabijaczleweli.minecrasmer.entity.tile

import com.nabijaczleweli.minecrasmer.computing.ComputerAccessory
import com.nabijaczleweli.minecrasmer.item.ItemCPU
import com.nabijaczleweli.minecrasmer.reference.Reference
import com.nabijaczleweli.minecrasmer.util.NBTUtil._
import com.nabijaczleweli.minecrasmer.util.{IConfigurable, SimpleDataProcessingTileEntity}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.oredict.OreDictionary
import org.apache.logging.log4j.core.helpers.Strings

class TileEntityAdditionalCPU extends SimpleDataProcessingTileEntity with ComputerAccessory with IInventory {
	val slots = new Array[ItemStack](1)
	var customName = ""

	def processors =
		TileEntityAdditionalCPU.processors

	def multiplier =
		((ItemCPU tier getStackInSlot(0)) + 1) * TileEntityAdditionalCPU.multiplierPerTier

	override def getSizeInventory =
		slots.length

	override def decrStackSize(id: Int, maxAmount: Int) =
		if(id >= slots.size || slots(id) == null)
			null
		else {
			val result = slots(id).copy()
			val toRemove = maxAmount min slots(id).stackSize

			result.stackSize = toRemove
			slots(id).stackSize -= toRemove
			result
		}

	override def closeInventory() =
		()

	override def getInventoryStackLimit =
		1

	override def markDirty() =
		super[SimpleDataProcessingTileEntity].markDirty()

	override def isItemValidForSlot(id: Int, is: ItemStack): Boolean = {
		val oresForIs = OreDictionary getOreIDs is map {OreDictionary.getOreName}
		for(oreForIs <- oresForIs)
			if(ItemCPU.subOreDictNames contains oreForIs)
				return true
		false
	}

	override def getStackInSlotOnClosing(id: Int) = {
		val stack = getStackInSlot(id)
		if(stack != null)
			setInventorySlotContents(id, null)
		stack
	}

	override def openInventory() =
		()

	override def setInventorySlotContents(id: Int, is: ItemStack) =
		if(id < slots.length)
			slots(id) = is

	override def isUseableByPlayer(player: EntityPlayer) =
		true

	override def getStackInSlot(id: Int) =
		if(id >= slots.length)
			null
		else
			slots(id)

	override def hasCustomInventoryName =
		Strings isNotEmpty customName

	override def getInventoryName =
		if(hasCustomInventoryName)
			customName
		else
			s"hud.${Reference.NAMESPACED_PREFIX}inventory.processor.name"

	override def writeToNBT(tag: NBTTagCompound) {
		super.writeToNBT(tag)
		slots.writeToNBT(tag, "slots")
	}

	override def readFromNBT(tag: NBTTagCompound) {
		super.readFromNBT(tag)
		tag.readItemStackArray("slots", slots)
	}
}

object TileEntityAdditionalCPU extends IConfigurable {
	final var processors = 1
	final var multiplierPerTier = 1.5F

	override def load(config: Configuration) {
		processors = config.getInt("TEAdditionalCPUProcessors", Reference.CONFIG_COMPUTE_CATEGORY, processors, 0, Int.MaxValue, "Amount of processors each TileEntityAdditionalCPU provides")
		multiplierPerTier = config.getFloat("TEAdditionalCPUMultiplierPerTier", Reference.CONFIG_COMPUTE_CATEGORY, multiplierPerTier, 0, 20F, "Multiplier of additional processor speed per processor tier")
	}
}
