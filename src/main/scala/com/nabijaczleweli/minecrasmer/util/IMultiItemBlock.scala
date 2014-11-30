package com.nabijaczleweli.minecrasmer.util

import net.minecraft.block.Block

trait IMultiItemBlock {this: Block =>
	val additionalModelNames: Array[String]
}
