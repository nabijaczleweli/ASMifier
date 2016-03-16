package com.nabijaczleweli.minecrasmer.item

import java.util

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.resource.{MineCrASMerLocation, ReloadableString, ReloadableStrings, ResourcesReloadedEvent}
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

object ItemCPU extends Item with IMultiModelItem with IOreDictRegisterable {
	Container.eventBus register this

	val subOreDictNames = Array("processorTier0", "processorTier1", "processorTier2")
	private      val subIconNames   = Array("%selementary", "%ssimple", "%sgood")
	private      val subNameNames   = Array("elementary", "simple", "good")
	@SideOnly(Side.CLIENT)
	private lazy val localizedNames = new ReloadableStrings(Future({subIconNames.indices map {idx => new ReloadableString(s"${super.getUnlocalizedName}.${subNameNames(idx)}.name")}}.toList))

	val elementaryDamage = 0
	val simpleDamage     = 1
	val goodDamage       = 2

	setUnlocalizedName(Reference.NAMESPACED_PREFIX + "CPU")
	setCreativeTab(CreativeTabMineCrASMer)
	setHasSubtypes(true)

	override def getMaxDamage =
		0

	override def getItemStackDisplayName(is: ItemStack) =
		localizedNames(MathHelper.clamp_int(is.getItemDamage, 0, localizedNames.length))

	@SideOnly(Side.CLIENT)
	override def getSubItems(item: Item, tab: CreativeTabs, list: util.List[ItemStack]) =
		if(item.isInstanceOf[this.type])
			for(i <- 0 until localizedNames.length)
				list.asInstanceOf[util.List[ItemStack]] add new ItemStack(item, 1, i)

	override def getUnlocalizedName(stack: ItemStack) =
		"item." + Reference.NAMESPACED_PREFIX + '.' + subNameNames(stack.getMetadata)

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	def onResourcesReloaded(event: ResourcesReloadedEvent) {
		localizedNames.reload()
	}

	override def registerOreDict() {
		val is = new ItemStack(this)
		for(i <- elementaryDamage to goodDamage) {
			is setItemDamage i
			OreDictionary.registerOre(oreDictName(i), is)
		}
	}

	@SideOnly(Side.CLIENT)
	override def registerModels() {
		for(i <- elementaryDamage to goodDamage) {
			Minecraft.getMinecraft.getRenderItem.getItemModelMesher.register(this, i, new ModelResourceLocation(Reference.NAMESPACED_PREFIX + subIconNames(i).format("cpu_"), "inventory"))
			ModelBakery.registerItemVariants(this, MineCrASMerLocation(subIconNames(i).format("cpu_")))
		}
	}

	def oreDictName(dmg: Int) =
		subOreDictNames(MathHelper.clamp_int(dmg, 0, subOreDictNames.length - 1))

	def tier(is: ItemStack) =
		if(is == null || is .getItem == null)
			-1
		else {
			var temp = -1
			for(name <- OreDictionary getOreIDs is map {OreDictionary.getOreName} if temp == -1)
				temp = subOreDictNames indexOf name
			temp + 1
		}
}
