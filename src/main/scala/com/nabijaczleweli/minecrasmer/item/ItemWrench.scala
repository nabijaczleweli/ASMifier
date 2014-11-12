package com.nabijaczleweli.minecrasmer.item

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.util.IOreDictRegisterable
import net.minecraft.block.Block
import net.minecraft.item.ItemTool
import net.minecraftforge.oredict.OreDictionary
import org.reflections.Reflections

import scala.collection.JavaConversions._
import scala.reflect.runtime.ReflectionUtils

object ItemWrench extends ItemTool(0, Container.materialWrench, _ItemWrench.effectiveAgainst) with IOreDictRegisterable {
	def effectiveAgainst =
		_ItemWrench.effectiveAgainst

	setUnlocalizedName(Reference.NAMESPACED_PREFIX + "wrench")
	setTextureName(Reference.NAMESPACED_PREFIX + "wrench")
	setCreativeTab(CreativeTabMineCrASMer)
	setHarvestLevel("wrench", 3)

	override def registerOreDict() =
		OreDictionary.registerOre("toolWrench", this)
}

private object _ItemWrench {
	lazy val effectiveAgainst = {{
		for(c <- new Reflections("com.nabijaczleweli.minecrasmer.block") getSubTypesOf classOf[Block] filter {_.getSimpleName endsWith "$"}) yield
			try
				ReflectionUtils staticSingletonInstance c
			catch {
				case _: Throwable =>
					null
			}
		} filter {_ != null}}.toSet[Block] filter {block =>
			var eff = false
			for(meta <- 0 until 16 if !eff)
				eff = block.isToolEffective("wrench", 0)
			eff
		}
}
