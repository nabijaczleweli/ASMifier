package com.nabijaczleweli.minecrasmer.util

import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTTagCompound, NBTTagList, NBTTagString}
import net.minecraftforge.common.util.Constants.NBT

object NBTUtil {
	implicit class StringArraySaver(val strarr: Array[String]) extends AnyVal {
		def writeToNBT(nbt: NBTTagCompound, tagName: String) {
			val arrNBT = new NBTTagList
			for(str <- strarr)
				arrNBT appendTag new NBTTagString(str)
			nbt.setTag(tagName, arrNBT)
			nbt.setInteger(tagName + "Amount", strarr.length)
		}
	}

	implicit class ItemStackArraySaver(val isarr: Array[ItemStack]) extends AnyVal {
		def writeToNBT(nbt: NBTTagCompound, tagName: String) {
			val itemList = new NBTTagList
			for(idx <- 0 until isarr.length) {
				val stack = isarr(idx)
				if(stack != null) {
					val tag = new NBTTagCompound
					tag.setByte("item_index", idx.toByte)
					stack writeToNBT tag
					itemList appendTag tag
				}
			}
			nbt.setTag(tagName, itemList)
		}
	}

	implicit class GeneralNBTUtil(tag: NBTTagCompound) {
		def hasTag(tagName: String) =
			tag getTag tagName ne null

		def readStringArray(tagName: String) = {
			if(!hasTag(tagName))
				new Array[String](0)
			else {
				val arrNBT = tag.getTagList(tagName, NBT.TAG_STRING)

				val result = new Array[String](tag getInteger (tagName + "Amount"))
				for(i <- 0 until result.length)
					result(i) = arrNBT getStringTagAt i
				result
			}
		}

		def readItemStackArray(tagName: String, isarr: Array[ItemStack]) = {
			val tagList = tag.getTagList(tagName, NBT.TAG_COMPOUND)
			for(idx <- 0 until tagList.tagCount) {
				val tag = tagList getCompoundTagAt idx
				val slot = tag getByte "item_index"
				if(slot >= 0 && slot < isarr.length)
					isarr(slot) = ItemStack loadItemStackFromNBT tag
			}
		}
	}

	trait NBTReloadable {
		def readFromNBT(tag: NBTTagCompound): Unit

		def writeToNBT(tag: NBTTagCompound): Unit
	}
}
