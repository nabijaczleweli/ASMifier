package com.nabijaczleweli.minecrasmer.resource

import net.minecraft.client.resources.IResourceManager
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

@SideOnly(Side.CLIENT)
class AutoFormattingReloadableString(private val __key: String, val fmt: Any*) extends ReloadableString(__key) {
	override def reload(manager: IResourceManager) = {
		super.reload(manager)
		loaded = java.lang.String.format(loaded, fmt map unwrapArg: _*)
	}
}
