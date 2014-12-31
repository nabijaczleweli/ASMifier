package com.nabijaczleweli.minecrasmer.item

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.util.{IOreDictRegisterable, ReflectionUtil}
import net.minecraft.block.Block
import net.minecraft.item.ItemTool
import net.minecraftforge.oredict.OreDictionary

import scala.collection.JavaConversions._
import scala.reflect.runtime.ReflectionUtils

object ItemWrench extends ItemTool(0, Container.materialWrench, _ItemWrench.effectiveAgainst) with IOreDictRegisterable {
	def effectiveAgainst =
		_ItemWrench.effectiveAgainst

	setUnlocalizedName(Reference.NAMESPACED_PREFIX + "wrench")
	setCreativeTab(CreativeTabMineCrASMer)
	setHarvestLevel("wrench", 3)

	override def registerOreDict() =
		OreDictionary.registerOre("toolWrench", this)
}

private object _ItemWrench {
	lazy val effectiveAgainst = {
		ReflectionUtil.subClassesInPackage(classOf[Block], Package getPackage "com.nabijaczleweli.minecrasmer.block", {_.getSimpleName endsWith "$"}) map {
			try
				ReflectionUtils.staticSingletonInstance
			catch {
				case _: Throwable =>
					null
			}
		} filter {_ != null}}.toSet.asInstanceOf[Set[Block]] filter {block =>
			var eff = false
			for(meta <- 0 until 16 if !eff)
				try
					eff = block.isToolEffective("wrench", block getStateFromMeta meta)
				catch {
					case _: NullPointerException =>
				}
			eff
		}
}
