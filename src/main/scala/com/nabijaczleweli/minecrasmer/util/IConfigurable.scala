package com.nabijaczleweli.minecrasmer.util

import net.minecraftforge.common.config.Configuration

trait IConfigurable {
	def load(config: Configuration): Unit
}
