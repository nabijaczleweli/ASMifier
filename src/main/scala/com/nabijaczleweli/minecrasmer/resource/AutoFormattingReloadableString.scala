package com.nabijaczleweli.minecrasmer.resource

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.resources.IResourceManager

@SideOnly(Side.CLIENT)
class AutoFormattingReloadableString(private val __key: String, val fmt: Any*) extends ReloadableString(__key) {
	override def reload(manager: IResourceManager) = {
		super.reload(manager)
		loaded = java.lang.String.format(loaded, fmt map unwrapArg: _*)
	}
}
