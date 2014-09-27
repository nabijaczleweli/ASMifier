package com.nabijaczleweli.minecrasmer.util

import net.minecraft.nbt.{NBTTagString, NBTTagList, NBTTagCompound}

object NBTUtil {
	implicit class StringArraySaver(strarr: Array[String]) {
		def writeToNBT(nbt: NBTTagCompound, tagName: String) {
			val arrNBT = new NBTTagList
			for(str <- strarr)
				arrNBT appendTag new NBTTagString(str)
			nbt.setTag(tagName, arrNBT)
			nbt.setInteger(tagName + "Amount", strarr.length)
		}
	}

	implicit class GeneralNBTUtil(tag: NBTTagCompound) {
		def hasTag(tagName: String) =
			tag getTag tagName ne null

		def readStringArray(tagName: String) = {
			if(!hasTag(tagName))
				new Array[String](0)
			else {
				val arrNBT = tag.getTag(tagName).asInstanceOf[NBTTagList]

				val result = new Array[String](tag getInteger (tagName + "Amount"))
				for(i <- 0 until result.length)
					result(i) = arrNBT getStringTagAt i
				result
			}
		}
	}
}