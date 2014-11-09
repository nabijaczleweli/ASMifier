package com.nabijaczleweli.minecrasmer.item

import java.util

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.resource.{ReloadableString, ReloadableStrings, ResourcesReloadedEvent}
import com.nabijaczleweli.minecrasmer.util.IOreDictRegisterable
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.{IIcon, MathHelper}
import net.minecraftforge.oredict.OreDictionary

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ItemCPU extends Item with IOreDictRegisterable {
	Container.eventBus register this

	val subOreDictNames = Array("processorTier0", "processorTier1", "processorTier2")
	private      val subIconNames   = Array("%selementary", "%ssimple", "%sgood")
	private      val subNameNames   = Array("elementary", "simple", "good")
	@SideOnly(Side.CLIENT)
	private lazy val icons          = new Array[IIcon](subIconNames.length)
	@SideOnly(Side.CLIENT)
	private lazy val localizedNames = new ReloadableStrings(Future({subIconNames.indices map {idx => new ReloadableString(s"$getUnlocalizedName.${subNameNames(idx)}.name")}}.toList))

	val elementaryDamage = 0
	val simpleDamage     = 1
	val goodDamage       = 2

	setUnlocalizedName(Reference.NAMESPACED_PREFIX + "CPU")
	setCreativeTab(CreativeTabMineCrASMer)
	setHasSubtypes(true)

	override def getMaxDamage =
		0

	@SideOnly(Side.CLIENT)
	override def getIconFromDamage(idx: Int) =
		icons(MathHelper.clamp_int(idx, 0, icons.length - 1))

	@SideOnly(Side.CLIENT)
	override def registerIcons(ir: IIconRegister) =
		for(i <- 0 until icons.length)
			icons(i) = ir registerIcon Reference.NAMESPACED_PREFIX + subIconNames(i).format("cpu_")

	override def getItemStackDisplayName(is: ItemStack) =
		localizedNames(MathHelper.clamp_int(is.getItemDamage, 0, localizedNames.length))

	@SideOnly(Side.CLIENT)
	override def getSubItems(item: Item, tab: CreativeTabs, list: util.List[_]) =
		if(item.isInstanceOf[this.type])
			for(i <- 0 until icons.length)
				list.asInstanceOf[util.List[ItemStack]] add new ItemStack(item, 1, i)

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

	def oreDictName(dmg: Int) =
		subOreDictNames(MathHelper.clamp_int(dmg, 0, subOreDictNames.length - 1))

	def tier(is: ItemStack) = {
		var temp = -1
		for(name <- OreDictionary getOreIDs is map {OreDictionary.getOreName} if temp == -1)
			temp = subOreDictNames indexOf name
		temp + 1
	}
}
