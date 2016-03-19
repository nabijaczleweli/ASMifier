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
import net.minecraft.util.{ITickable, ChatComponentText, ChatComponentTranslation}
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.common.util.Constants.NBT
import net.minecraftforge.oredict.OreDictionary
import org.apache.logging.log4j.core.helpers.Strings

class TileEntityAdditionalCPU extends SimpleDataProcessingTileEntity with ComputerAccessory with IInventory with ITickable {
	val slots = new Array[ItemStack](1)
	private var customName = ""

	def processors =
		TileEntityAdditionalCPU.processors

	def multiplier =
		((ItemCPU tier getStackInSlot(0)) + 1) * TileEntityAdditionalCPU.multiplierPerTier

	def setCustomName(name: String) {
		customName = name
		markDirty()
	}

	override def getSizeInventory =
		slots.length

	override def decrStackSize(id: Int, maxAmount: Int) =
		if(id >= slots.length || slots(id) == null)
			null
		else {
			val result = slots(id).copy()
			val toRemove = maxAmount min slots(id).stackSize

			result.stackSize = toRemove
			slots(id).stackSize -= toRemove
			result
		}

	override def closeInventory(playerIn: EntityPlayer) =
		()

	override def getInventoryStackLimit =
		1

	override def isItemValidForSlot(id: Int, is: ItemStack): Boolean = {
		val oresForIs = OreDictionary getOreIDs is map {OreDictionary.getOreName}
		for(oreForIs <- oresForIs)
			if(ItemCPU.subOreDictNames contains oreForIs)
				return true
		false
	}

	override def removeStackFromSlot(id: Int) = {
		val stack = getStackInSlot(id)
		if(stack != null)
			setInventorySlotContents(id, null)
		stack
	}

	override def openInventory(playerIn: EntityPlayer) =
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

	override def hasCustomName =
		Strings isNotEmpty customName

	override def getName =
		if(hasCustomName)
			customName
		else
			s"hud.${Reference.NAMESPACED_PREFIX}inventory.processor.name"

	override def getDisplayName =
		if(hasCustomName)
			new ChatComponentText(getName)
		else
			new ChatComponentTranslation(getName)

	override def writeToNBT(tag: NBTTagCompound) {
		super.writeToNBT(tag)
		slots.writeToNBT(tag, "slots")
		if(hasCustomName)
			tag.setString("display_name", customName)
	}

	override def readFromNBT(tag: NBTTagCompound) {
		super.readFromNBT(tag)
		tag.readItemStackArray("slots", slots)
		if(tag.hasKey("display_name", NBT.TAG_STRING))
			customName = tag getString "display_name"
	}

	override def getField(id: Int) =
		0

	override def clear() =
		slots(0) = null

	override def getFieldCount =
		0

	override def setField(id: Int, value: Int) =
		()

	override def update() =
		()
}

object TileEntityAdditionalCPU extends IConfigurable {
	final var processors        = 1
	final var multiplierPerTier = 1.5F

	override def load(config: Configuration) {
		processors = config.getInt("TEAdditionalCPUProcessors", Reference.CONFIG_COMPUTE_CATEGORY, processors, 0, Int.MaxValue,
		                           "Amount of processors each TileEntityAdditionalCPU provides")
		multiplierPerTier = config.getFloat("TEAdditionalCPUMultiplierPerTier", Reference.CONFIG_COMPUTE_CATEGORY, multiplierPerTier, 0, 20F,
		                                    "Multiplier of additional processor speed per processor tier")
	}
}
