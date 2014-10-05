package com.nabijaczleweli.minecrasmer.resource

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.resources.IResourceManager
import net.minecraft.util.StatCollector

import scala.math.ScalaNumber

@SideOnly(Side.CLIENT)
class ReloadableString(val key: String) extends IDoubleReloadable {
	protected var loaded: String = _


	override def reload(manager: IResourceManager) =
		loaded = StatCollector translateToLocal key

	def format(fmt: Any*) = // Stolen form StringLike
		java.lang.String.format(toString, fmt map unwrapArg: _*)


	override def equals(obj: Any) =
		obj match {
			case null =>
				false
			case rs: ReloadableString =>
				rs.loaded == loaded && rs.key == key
			case _ =>
				false
		}

	override def toString =
		loaded


	protected def unwrapArg(arg: Any) = // Stolen form StringLike
		arg match {
			case x: ScalaNumber => x.underlying
			case x => x.asInstanceOf[AnyRef]
		}
}

object ReloadableString {
	implicit def ReloadableStringToString(rs: ReloadableString) =
		rs.toString
}
