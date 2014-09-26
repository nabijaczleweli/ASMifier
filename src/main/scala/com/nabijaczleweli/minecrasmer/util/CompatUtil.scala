package com.nabijaczleweli.minecrasmer.util

import com.nabijaczleweli.minecrasmer.compat.ICompat
import com.nabijaczleweli.minecrasmer.reference.{Reference, Configuration}
import cpw.mods.fml.common.Loader.isModLoaded
import com.nabijaczleweli.minecrasmer.util.StringUtils._

object CompatUtil {
	implicit class CompatConv(compat: ICompat) {
		def hasAllLoaded = {
			var allModsLoaded = true
			for(modid <- compat.getModIDs if allModsLoaded)
				if(!(modid == null || isModLoaded(modid)))
					allModsLoaded = false
			allModsLoaded
		}

		def getModList =
			if(compat.getModIDs.isEmpty)
				"Vanilla"
			else {
				val itr = {
					for(modid <- compat.getModIDs) yield
						if(modid == null)
							"Vanilla"
						else
							modid
				}.iterator
				var modids = new StringBuilder(itr.next())
				while(itr.hasNext)
					modids ++= {
						val modid = itr.next()
						if(itr.hasNext)
							s", $modid"
						else
							s" and $modid"
					}
				modids
			}

		def getModIds =
			if(compat.getModIDs.isEmpty)
				"Vanilla"
			else {
				var modids = new StringBuilder
				for(modid <- compat.getModIDs)
					modids ++= {
						if(modid == null)
							"Vanilla"
						else
							modid toUpper 0
					}
				modids
			}

		def shouldPreLoad = {
			val toRet = Configuration.config.get(Reference.CONFIG_COMPAT_CATEGORY, s"preLoad$getModIds", true, s"Should the compat with $getModList be preLoaded.").getBoolean
			Configuration.saveIfNeeded()
			toRet
		}

		def shouldLoad = {
			val toRet = Configuration.config.get(Reference.CONFIG_COMPAT_CATEGORY, s"load$getModIds", true, s"Should the compat with $getModList be loaded.").getBoolean
			Configuration.saveIfNeeded()
			toRet
		}
	}
}