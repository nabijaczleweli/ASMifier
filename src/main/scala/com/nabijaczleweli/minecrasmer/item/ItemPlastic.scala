package com.nabijaczleweli.minecrasmer.item

import java.util

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.resource.{MineCrASMerLocation, ReloadableString, ReloadableStrings, ResourcesReloadedEvent}
import com.nabijaczleweli.minecrasmer.util.{IMultiModelItem, IOreDictRegisterable}
import com.nabijaczleweli.minecrasmer.util.StringUtils._
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

object ItemPlastic extends Item with IMultiModelItem with IOreDictRegisterable {
	Container.eventBus register this

	private val subIconNames = Array[String]("monomer", "polymer", "plastic")

	@SideOnly(Side.CLIENT)
	private lazy val localizedNames =
		new ReloadableStrings(Future({subIconNames.indices map {idx => new ReloadableString(s"${super.getUnlocalizedName}.${subIconNames(idx)}.name")}}.toList))

	val monomerDamage = 0
	val polymerDamage = 1
	val plasticDamage = 2

	def oreDictName(dmg: Int) =
		"material" + (subIconNames(MathHelper.clamp_int(dmg, 0, ItemPlastic.subIconNames.length - 1)) toUpper 0)

	setUnlocalizedName(Reference.NAMESPACED_PREFIX + "plastic")
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
		for(i <- monomerDamage to plasticDamage) {
			is setItemDamage i
			OreDictionary.registerOre(oreDictName(i), is)
		}
	}

	@SideOnly(Side.CLIENT)
	override def registerModels() {
		val cleanUnlocalizedName = super.getUnlocalizedName substring "."
		for(i <- monomerDamage to plasticDamage) {
			Minecraft.getMinecraft.getRenderItem.getItemModelMesher.register(this, i, new ModelResourceLocation(cleanUnlocalizedName + '_' + subIconNames(i), "inventory"))
			ModelBakery.registerItemVariants(this, MineCrASMerLocation("plastic_" + subIconNames(i)))
		}
	}
}
