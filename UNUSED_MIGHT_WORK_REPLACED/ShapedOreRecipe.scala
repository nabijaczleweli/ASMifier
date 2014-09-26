package com.nabijaczleweli.minecrasmer.util.registration

import java.util

import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import net.minecraft.block.Block
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World
import net.minecraftforge.oredict.{RecipeSorter, OreDictionary}

class ShapedOreRecipe private() extends IRecipe {
	import ShapedOreRecipe._

	var filledGrid: Array[Array[AnyRef]] = _
	var output: ItemStack = _
	var width : Int       = _
	var height: Int       = _

	private[ShapedOreRecipe] def this(out: ItemStack, grid: List[String], replacements: Map[Char, AnyRef]) {
		this()
		width = {
			var max = 0
			for(line <- grid)
				max = max max line.length
			max
		}
		if(width > maxGridWidth || width == 0)
			throw new IllegalArgumentException(s"Illegal grid width for recipe $this")
		height = grid.length
		if(height > maxGridHeight || height == 0)
			throw new IllegalArgumentException(s"Illegal grid height for recipe $this")
		output = out.copy()
		filledGrid = Array.ofDim(width, height)

		val processedGrid = for(line <- grid) yield {
			var newline = line
			while(newline.length < width)
				newline += ' '
			newline
		}

		var y = 0
		for(line <- processedGrid) {
			var x = 0
			for(char <- line) {
				if(char != ' ')
					replacements get char match {
						case None =>
							throw new NoSuchElementException(s"Couldn\'t find \'$char\' replacement for recipe $this")
						case opt: Some[AnyRef] =>
							opt.get match {
								case is: ItemStack =>
									filledGrid(x)(y) = is
								case it: Item =>
									filledGrid(x)(y) = new ItemStack(it)
								case bl: Block =>
									filledGrid(x)(y) = new ItemStack(bl, 1, OreDictionary.WILDCARD_VALUE)
								case str: String =>
									filledGrid(x)(y) = OreDictionary getOres str
								case obj =>
									throw new IllegalArgumentException(s"Wrong replacement type(${obj.getClass.getName}) for recipe $this")
							}
					}
				x += 1
			}
			y += 1
		}
	}

	override def matches(inv: InventoryCrafting, world: World): Boolean = {
		for(startX <- 0 to maxGridHeight - height; startY <- 0 to maxGridHeight - height)
			if(checkMatch(inv, startX, startY))
				return true

		false
	}

	private def checkMatch(inv: InventoryCrafting, startX: Int, startY: Int): Boolean = {
		Container.log error s"checkMatch(_,$startX,$startY)"
		Container.log error s"I yield $output"
		for(_x <- 0 until width; _y <- 0 until height) {
			val x = _x - startX
			val y = _y - startY
			if(x >= 0 && y >= 0 && x < width && y < height) {
				Container.log error s"checking row $y,col $x"
				val slot = inv.getStackInRowAndColumn(x, y)
				Container.log error s"in slot: $slot"
				filledGrid(x)(y) match {
					case obj if obj != null && slot == null =>
						Container.log error s"I am not null($obj), but slot is"
						return false
					case null =>
						Container.log error s"I am null"
					case is: ItemStack =>
						Container.log error s"I am ItemStack:$is"
						if(!OreDictionary.itemMatches(is, slot, false))
							return false
						Container.log error s"and I match!"
					case ls: util.ArrayList[ItemStack@unchecked] =>
						Container.log error s"I am ArrayList of size ${ls.size()}"
						var notMatched = true
						val itr = ls.iterator()
						while(itr.hasNext && notMatched) {
							val t = itr.next()
							Container.log error s"trying to match stack to $t"
							notMatched = !OreDictionary.itemMatches(t, slot, false)
						}
						if(notMatched)
							return false
						Container.log error s"Some of me matched!"
				}
			}
		}
		true
	}

	override def getRecipeOutput =
		output

	override def getRecipeSize =
		width * height

	override def getCraftingResult(inv: InventoryCrafting) =
		output.copy()

	override def toString =
		s"${getClass.getSimpleName}[result:$output,width:$width,height:$height,grid:${{for(line <- filledGrid.toList) yield line.toList}.toString()}"
}

object _ShapedOreRecipe {
	private type objectsType = Any

	def apply(out: ItemStack, grid: List[String], replacements: Map[Char, AnyRef]): ShapedOreRecipe = {
		sanitize(out, grid, replacements)
		register()
		new ShapedOreRecipe(out, grid, replacements)
	}

	def apply(out: Item, grid: List[String], replacements: Map[Char, AnyRef]): ShapedOreRecipe =
		apply(new ItemStack(out), grid, replacements)

	def apply(out: Block, grid: List[String], replacements: Map[Char, AnyRef]): ShapedOreRecipe =
		apply(new ItemStack(out), grid, replacements)

	private[ShapedOreRecipe] final val maxGridWidth = 3
	private[ShapedOreRecipe] final val maxGridHeight = 3

	private var registered = false
	private def register() =
		if(!registered) {
			RecipeSorter.register(Reference.NAMESPACED_PREFIX + "shapedore", classOf[ShapedOreRecipe], RecipeSorter.Category.SHAPED, "after:forge:shapedore")
			registered = true
		}

	private def sanitize(out: AnyRef, grid: List[String], replacements: Map[Char, AnyRef]) =
		if(out == null || grid == null || replacements == null)
			Container.log error new NullPointerException("At least one argument passed to ShapedOreRecipe$.sanitize() is null! Thou shalt not do this!")
}
