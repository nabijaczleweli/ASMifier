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

object ItemQuartz extends Item with IOreDictRegisterable {
	Container.eventBus register this

	private      val subIconNames    = Array("plate%s", "shards%s")
	private      val subNameNames    = Array("plate", "shards")
	private      val subOreDictNames = Array("plateQuartz", "shardsQuartz")
	@SideOnly(Side.CLIENT)
	private lazy val icons          = new Array[IIcon](subIconNames.length)
	@SideOnly(Side.CLIENT)
	private lazy val localizedNames = new ReloadableStrings(Future({
		                                                               for(nameIdx <- subIconNames.indices) yield
			                                                               new ReloadableString(s"$getUnlocalizedName.${subNameNames(nameIdx)}.name")
	                                                               }.toList))

	val plateDamage = 0
	val shardsDamage = 1

	setUnlocalizedName(Reference.NAMESPACED_PREFIX + "quartz")
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
			icons(i) = ir registerIcon Reference.NAMESPACED_PREFIX + subIconNames(i).format("_quartz")

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
		for(i <- plateDamage to shardsDamage) {
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
