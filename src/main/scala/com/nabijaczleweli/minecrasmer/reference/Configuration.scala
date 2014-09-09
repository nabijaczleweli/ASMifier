package com.nabijaczleweli.minecrasmer.reference

import java.io.File

import net.minecraftforge.common.config.Configuration

object Configuration {
	var config: Configuration = _

	def load(configFile: File) {
		config = new Configuration(configFile)
	}

	def saveIfNeeded() =
		if(config.hasChanged)
			config.save()
}
