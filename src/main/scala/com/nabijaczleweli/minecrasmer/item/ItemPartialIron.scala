package com.nabijaczleweli.minecrasmer.item

import java.util

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.resource.{MineCrASMerLocation, ReloadableString, ReloadableStrings, ResourcesReloadedEvent}
import com.nabijaczleweli.minecrasmer.util.StringUtils._
import com.nabijaczleweli.minecrasmer.util.{IMultiModelItem, IOreDictRegisterable}
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.model.{ModelBakery, ModelResourceLocation}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.MathHelper
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import net.minecraftforge.oredict.OreDictionary

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ItemPartialIron extends Item with IMultiModelItem with IOreDictRegisterable {
	Container.eventBus register this

	private val subIconNames    = Array("half", "quarter")
	private val subOreDictNames = Array("dust", "dustTiny")

	@SideOnly(Side.CLIENT)
	private lazy val localizedNames =
		new ReloadableStrings(Future({subIconNames.indices map {idx => new ReloadableString(s"${super.getUnlocalizedName}.${subIconNames(idx)}.name")}}.toList))

	val halfDamage = 0
	val quarterDamage = 1

	def oreDictName(dmg: Int) =
		(subOreDictNames(MathHelper.clamp_int(dmg, 0, subOreDictNames.length - 1)) toUpper 0) + "Iron"

	setUnlocalizedName(Reference.NAMESPACED_PREFIX + "smalliron")
	setCreativeTab(CreativeTabMineCrASMer)
	setHasSubtypes(true)

	override def getMaxDamage =
		0

	override def getItemStackDisplayName(is: ItemStack) =
		localizedNames(MathHelper.clamp_int(is.getItemDamage, 0, subIconNames.length))

	@SideOnly(Side.CLIENT)
	override def getSubItems(item: Item, tab: CreativeTabs, list: util.List[ItemStack]) =
		if(item.isInstanceOf[this.type])
			for(i <- 0 until localizedNames.length)
				list.asInstanceOf[util.List[ItemStack]] add new ItemStack(item, 1, i)

	override def getUnlocalizedName(stack: ItemStack) =
		super.getUnlocalizedName + '.' + subIconNames(stack.getMetadata)

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	def onResourcesReloaded(event: ResourcesReloadedEvent) {
		localizedNames.reload()
	}

	override def registerOreDict() {
		val is = new ItemStack(this)
		for(i <- halfDamage to quarterDamage) {
			is setItemDamage i
			OreDictionary.registerOre(oreDictName(i), is)
		}
	}

	@SideOnly(Side.CLIENT)
	override def registerModels() {
		val cleanUnlocalizedName = super.getUnlocalizedName substring "."
		for(i <- halfDamage to quarterDamage) {
			Minecraft.getMinecraft.getRenderItem.getItemModelMesher.register(this, i, new ModelResourceLocation(cleanUnlocalizedName + '_' + subIconNames(i), "inventory"))
			ModelBakery.registerItemVariants(this, MineCrASMerLocation("smalliron_" + subIconNames(i)))
		}
	}
}
