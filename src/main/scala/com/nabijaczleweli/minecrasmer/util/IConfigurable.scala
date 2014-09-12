package com.nabijaczleweli.minecrasmer.util

import net.minecraftforge.common.config

trait IConfigurable {
	def load(config: config.Configuration): Unit
}
