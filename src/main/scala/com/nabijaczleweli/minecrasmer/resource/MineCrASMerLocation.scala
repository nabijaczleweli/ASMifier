package com.nabijaczleweli.minecrasmer.resource

import com.nabijaczleweli.minecrasmer.reference.Reference
import net.minecraft.util.ResourceLocation

case class MineCrASMerLocation(path: String) extends ResourceLocation(Reference.MOD_ID, path)
