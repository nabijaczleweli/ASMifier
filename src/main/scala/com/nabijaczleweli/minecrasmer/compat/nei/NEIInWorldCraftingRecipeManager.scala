package com.nabijaczleweli.minecrasmer.compat.nei

import java.util

import codechicken.nei.PositionedStack
import codechicken.nei.api.{IOverlayHandler, IRecipeOverlayRenderer}
import codechicken.nei.recipe.{GuiRecipe, ICraftingHandler, IUsageHandler}
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import net.minecraft.item.ItemStack

object NEIInWorldCraftingRecipeManager extends IUsageHandler with ICraftingHandler {
	override def getUsageHandler(inputId: String, ingredients: AnyRef*): IUsageHandler = ???

	override def getRecipeHandler(outputId: String, results: AnyRef*): ICraftingHandler = ???

	override def keyTyped(gui: GuiRecipe, keyChar: Char, keyCode: Int, recipe: Int): Boolean = ???

	override def getOtherStacks(recipetype: Int): util.List[PositionedStack] = ???

	override def getResultStack(recipe: Int): PositionedStack = ???

	override def onUpdate(): Unit = ???

	override def handleTooltip(gui: GuiRecipe, currenttip: util.List[String], recipe: Int): util.List[String] = ???

	override def recipiesPerPage(): Int = ???

	override def getOverlayHandler(gui: GuiContainer, recipe: Int): IOverlayHandler = ???

	override def numRecipes(): Int = ???

	override def mouseClicked(gui: GuiRecipe, button: Int, recipe: Int): Boolean = ???

	override def drawBackground(recipe: Int): Unit = ???

	override def getRecipeName: String = ???

	override def getOverlayRenderer(gui: GuiContainer, recipe: Int): IRecipeOverlayRenderer = ???

	override def getIngredientStacks(recipe: Int): util.List[PositionedStack] = ???

	override def drawForeground(recipe: Int): Unit = ???

	override def hasOverlay(gui: GuiContainer, container: Container, recipe: Int): Boolean = ???

	override def handleItemTooltip(gui: GuiRecipe, stack: ItemStack, currenttip: util.List[String], recipe: Int): util.List[String] = ???
}
