package com.nabijaczleweli.minecrasmer.item

import java.util

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.resource.{ReloadableString, ReloadableStrings, ResourcesReloadedEvent}
import com.nabijaczleweli.minecrasmer.util.IOreDictRegisterable
import com.nabijaczleweli.minecrasmer.util.StringUtils._
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.MathHelper
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import net.minecraftforge.oredict.OreDictionary

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ItemPartialIron extends Item with IOreDictRegisterable {
	Container.eventBus register this

	private val subIconNames    = Array("half", "quarter")
	private val subOreDictNames = Array("dust", "dustTiny")

	/*@SideOnly(Side.CLIENT)
	private lazy val icons          = new Array[IIcon](subIconNames.length)*/
	@SideOnly(Side.CLIENT)
	private lazy val localizedNames = new ReloadableStrings(Future({subIconNames.indices map {idx => new ReloadableString(s"$getUnlocalizedName.${subIconNames(idx)}.name")}}.toList))

	val halfDamage = 0
	val quarterDamage = 1

	def oreDictName(dmg: Int) =
		(subOreDictNames(MathHelper.clamp_int(dmg, 0, subOreDictNames.length - 1)) toUpper 0) + "Iron"

	setUnlocalizedName(Reference.NAMESPACED_PREFIX + "smalliron")
	setCreativeTab(CreativeTabMineCrASMer)
	setHasSubtypes(true)

	override def getMaxDamage =
		0

	/*@SideOnly(Side.CLIENT)
	override def getIconFromDamage(idx: Int) =
		icons(MathHelper.clamp_int(idx, 0, icons.length - 1))

	@SideOnly(Side.CLIENT)
	override def registerIcons(ir: IIconRegister) =
		for(i <- 0 until icons.length)
			icons(i) = ir registerIcon Reference.NAMESPACED_PREFIX + "smalliron_" + subIconNames(i)*/

	override def getItemStackDisplayName(is: ItemStack) =
		localizedNames(MathHelper.clamp_int(is.getItemDamage, 0, subIconNames.length))

	@SideOnly(Side.CLIENT)
	override def getSubItems(item: Item, tab: CreativeTabs, list: util.List[_]) =
		if(item.isInstanceOf[this.type])
			for(i <- 0 until localizedNames.length)
				list.asInstanceOf[util.List[ItemStack]] add new ItemStack(item, 1, i)

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
}
