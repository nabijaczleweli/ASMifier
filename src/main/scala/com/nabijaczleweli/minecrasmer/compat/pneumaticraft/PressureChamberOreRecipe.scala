package com.nabijaczleweli.minecrasmer.compat.pneumaticraft

import net.minecraft.block.Block
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.oredict.OreDictionary
import pneumaticCraft.api.recipe.IPressureChamberRecipe

import scala.collection.mutable.{HashMap => mHashMap}


class PressureChamberOreRecipe(_input: Array[AnyRef], output: Array[ItemStack]) extends IPressureChamberRecipe {
	private lazy val input =
		try
			for(in <- _input) yield
				in match {
					case it: Item =>
						new ItemStack(it)
					case bl: Block =>
						new ItemStack(bl, 1, OreDictionary.WILDCARD_VALUE)
					case is: ItemStack =>
						is
					case str: String =>
						(str, 1)
					case (sth, amt: Int) =>
						sth match {
							case it: Item =>
								new ItemStack(it, amt)
							case bl: Block =>
								new ItemStack(bl, amt, OreDictionary.WILDCARD_VALUE)
							case str: String =>
								(str, amt)
						}
				}
		catch {
			case thr: Throwable =>
				throw new IllegalArgumentException(s"Invalid pressure chamber ore recipe: ${_input mkString ", "} yielding ${output mkString ", "}.", thr)
		}

	override def getCraftingPressure =
		2.5F

	override def craftRecipe(inputStacks: Array[ItemStack], removedStacks: Array[ItemStack]) =
		output

	override def isValidRecipe(inputStacks: Array[ItemStack]) =
		if(inputStacks.length < input.length)
			null
		else {
			val inputCpy = input.toBuffer
			val idxToAmount = new mHashMap[Int, Int]
			inputStacks filter {stack =>
				var genIdx = 0
				inputCpy indexWhere {
					case is: ItemStack =>
						OreDictionary.itemMatches(stack, is, false)
					case (str: AnyRef, _) =>
						var anyMatch = false
						for(is <- OreDictionary getOres str.asInstanceOf[String] if !anyMatch)
							anyMatch = OreDictionary.itemMatches(stack, is, false)
						anyMatch
					case _ =>
						false
				} match {
					case -1 =>
						false
					case idx =>
						val amount = inputCpy(idx) match {
							case is: ItemStack =>
								stack.stackSize / is.stackSize
							case (_, amt: Any) =>
								amt.asInstanceOf[Int]
							case _ =>
								-1
						}
						inputCpy remove idx
						idxToAmount += genIdx -> amount
						genIdx += 1
						true
				}
			} match {
				case a if a.isEmpty =>
					null
				case stacks => {
					for(idx <- stacks.indices) yield {
						val newStack = stacks(idx).copy
						newStack.stackSize = idxToAmount.getOrElse(idx, newStack.stackSize)
						newStack
					}
				}.toArray
			}
		}

	override def shouldRemoveExactStacks() =
		false
}
