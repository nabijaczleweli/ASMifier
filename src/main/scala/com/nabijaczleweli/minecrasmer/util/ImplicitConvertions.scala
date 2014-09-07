package com.nabijaczleweli.minecrasmer.util

import com.nabijaczleweli.minecrasmer.compat.ICompat
import cpw.mods.fml.common.Loader.isModLoaded

object ImplicitConvertions {
	implicit class CompatConv(compat: ICompat) {
		def hasAllLoaded = {
			var allModsLoaded = true
			for(modid <- compat.getModIDs if allModsLoaded)
				if(!(modid == null || isModLoaded(modid)))
					allModsLoaded = false
			allModsLoaded
		}
	}
}
