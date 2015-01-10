package com.nabijaczleweli.minecrasmer.util

import net.minecraft.nbt.{NBTTagByte, NBTBase, NBTTagCompound}

class ConstructibleNBTTagCompound(map: Map[String, NBTBase]) extends NBTTagCompound {
	for(kv <- map)
		setTag(kv._1, kv._2)
}

class NBTTagBool(value: Boolean) extends NBTTagByte(if(value) 1 else 0)
