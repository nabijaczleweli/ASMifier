package com.nabijaczleweli.minecrasmer.compat.nei

import java.util.{ArrayList => jArrayList, List => jList}

import codechicken.nei.recipe.{GuiRecipe, ICraftingHandler, IUsageHandler}
import codechicken.nei.{NEIServerUtils, PositionedStack}
import com.nabijaczleweli.minecrasmer.item.{ItemScoop, ItemQuartz}
import com.nabijaczleweli.minecrasmer.reference.{Reference, Container => mContainer}
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import net.minecraftforge.fluids.FluidStack
import org.lwjgl.opengl.GL11

import scala.collection.mutable.{ListMap => mListMap, Map => mMap, MutableList => mList}

/** This is based on IC2's NEICraftingInWorldManager */
class NEIMineCrASMerInWorldCraftingManager extends ICraftingHandler with IUsageHandler {
	private final val details: mMap[ItemStackWrapper, String] = new mListMap
	private final val offsets                                 = new mList[ItemStackWrapper]
	private final val outputs                                 = new mList[PositionedStack]
	private[nei]  var target : ItemStack                      = _

	// Explicit return type or AbstractMethodError
	override def getUsageHandler(inputId: String, ingredients: Object*): IUsageHandler =
		this

	// Explicit return type or AbstractMethodError
	override def getRecipeHandler(outputId: String, results: Object*): ICraftingHandler =
		if(results.length > 0 && results(0).isInstanceOf[ItemStack]) {
			val inst = new NEIMineCrASMerInWorldCraftingManager
			inst.target = results(0).asInstanceOf[ItemStack]
			inst.addRecipes()
			inst
		} else
			this

	override def keyTyped(gui: GuiRecipe, keyChar: Char, keyCode: Int, recipe: Int) =
		false

	override def getOtherStacks(recipetype: Int) =
		new jArrayList

	override def getResultStack(recipe: Int) =
		outputs(recipe)

	override def onUpdate() =
		()

	override def handleTooltip(gui: GuiRecipe, currenttip: jList[String], recipe: Int) =
		currenttip

	override def recipiesPerPage =
		1

	override def getOverlayHandler(gui: GuiContainer, recipe: Int) =
		null

	override def numRecipes =
		offsets.size

	override def mouseClicked(gui: GuiRecipe, button: Int, recipe: Int) =
		false

	override def drawBackground(recipe: Int) =
		GL11.glColor4f(1, 1, 1, 1)

	override def getRecipeName =
		StatCollector translateToLocal s"hud.${Reference.NAMESPACED_PREFIX}compat.nei.inworld.label.name"

	override def getOverlayRenderer(gui: GuiContainer, recipe: Int) =
		null

	override def getIngredientStacks(recipe: Int) =
		new jArrayList

	override def drawForeground(recipe: Int) =
		if(outputs.size > recipe)
			Minecraft.getMinecraft.fontRenderer.drawSplitString(details(offsets(recipe)), 10, 25, 150, 0)

	override def hasOverlay(gui: GuiContainer, container: Container, recipe: Int) =
		false

	override def handleItemTooltip(gui: GuiRecipe, stack: ItemStack, currenttip: jList[String], recipe: Int) =
		currenttip

	private def addRecipe(wrapper: ItemStackWrapper, msg: String) {
		val newStack = wrapper.is.copy
		newStack.stackSize = 1
		if(NEIServerUtils.areStacksSameTypeCrafting(newStack, target)) {
			offsets += wrapper
			outputs += new PositionedStack(newStack, 75, 4)
			details += wrapper -> msg
		}
	}

	private def addRecipes() {
		addRecipe(new ItemStack(ItemQuartz, 1, ItemQuartz.cleanShardsDamage), NEI.cleanQuartzShardsDescription)
		addRecipe(new ItemStack(ItemQuartz, 1, ItemQuartz.shardsDamage), NEI.quartzShardsDescription)
		for(scoop <- mContainer.scoopLiquidCrystal :: mContainer.foreignScoops)
			addRecipe(new ItemStack(scoop), NEI.scoopsDescription.replace("$$$", new FluidStack(scoop.fluid, ItemScoop.capacity).getLocalizedName).replace("%%%", scoop.contains.getLocalizedName))
	}
}