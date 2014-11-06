package com.nabijaczleweli.minecrasmer.item

import com.google.common.reflect.ClassPath
import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.util.IOreDictRegisterable
import net.minecraft.block.Block
import net.minecraft.item.ItemTool
import net.minecraftforge.oredict.OreDictionary

import scala.collection.JavaConversions._
import scala.reflect.runtime.ReflectionUtils

object ItemWrench extends ItemTool(0, Container.materialWrench, _ItemWrench.effectiveAgainst) with IOreDictRegisterable {
	val effectiveAgainst = _ItemWrench.effectiveAgainst

	setUnlocalizedName(Reference.NAMESPACED_PREFIX + "wrench")
	setCreativeTab(CreativeTabMineCrASMer)
	setTextureName(Reference.NAMESPACED_PREFIX + "wrench")
	setHarvestLevel("wrench", 3)

	override def registerOreDict() =
		OreDictionary.registerOre("toolWrench", this)
}

private object _ItemWrench {
	lazy val effectiveAgainst = {
		Compiler.disable() // Don't fruitlessly JIT-compile a lot of classes, we need only those that we'll get MODULE$ out of, not all from the package
		val classWrappers = ClassPath from getClass.getClassLoader getTopLevelClassesRecursive "com.nabijaczleweli.minecrasmer.block"
		val tempClasses = {
			for(cw <- classWrappers) yield
			  try
					Class.forName(cw.getName + '$', true, getClass.getClassLoader) // Find only objects
				catch {
				  case _: Throwable =>
					  null
				}
		} filter {_ != null}
		Compiler.enable();
		{{
			for(c <- (tempClasses filter classOf[Block].isAssignableFrom).toSet.asInstanceOf[Set[Class[_ <: Block]]]) yield
				try
					ReflectionUtils staticSingletonInstance c
				catch {
					case _: Throwable =>
						null
				}
		} filter {_ != null}}.asInstanceOf[Set[Block]] filter {block =>
			var eff = false
			for(meta <- 0 until 16 if !eff)
				eff = block.isToolEffective("wrench", 0)
			eff
		}
	}
}
