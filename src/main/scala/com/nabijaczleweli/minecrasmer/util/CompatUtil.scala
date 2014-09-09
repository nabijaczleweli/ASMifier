package com.nabijaczleweli.minecrasmer.util

import com.nabijaczleweli.minecrasmer.compat.ICompat
import com.nabijaczleweli.minecrasmer.reference.Configuration
import cpw.mods.fml.common.Loader.isModLoaded

object CompatUtil {
	implicit class CompatConv(compat: ICompat) {
		def hasAllLoaded = {
			var allModsLoaded = true
			for(modid <- compat.getModIDs if allModsLoaded)
				if(!(modid == null || isModLoaded(modid)))
					allModsLoaded = false
			allModsLoaded
		}

		def shouldPreLoad = {
			val toRet = Configuration.config.get("compatibility", s"preLoad${
				if(compat.getModIDs.isEmpty)
					"Vanilla"
				else {
					var modids = new StringBuilder
					for(modid <- compat.getModIDs)
						modids ++= (if(modid == null) "Vanilla" else modid)
					modids
				}
			}", true, s"Should the compat with ${
				if(compat.getModIDs.isEmpty)
					"Vanilla"
				else
					for(modid <- compat.getModIDs) yield
						if(modid == null)
							"Vanilla"
						else
							modid
			} be preLoaded.").getBoolean
			Configuration.saveIfNeeded()
			toRet
		}

		def shouldLoad = {
			val toRet = Configuration.config.get("compatibility", s"load${
				if(compat.getModIDs.isEmpty)
					"Vanilla"
				else {
					var modids = new StringBuilder
					for(modid <- compat.getModIDs)
						modids ++= (if(modid == null) "Vanilla" else modid)
					modids
				}
			}", true, s"Should the compat with ${
				if(compat.getModIDs.isEmpty)
					"Vanilla"
				else
					for(modid <- compat.getModIDs) yield
						if(modid == null)
							"Vanilla"
						else
							modid
			} be loaded.").getBoolean
			Configuration.saveIfNeeded()
			toRet
		}
	}
}
