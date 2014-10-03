package com.nabijaczleweli.minecrasmer.item

import java.util

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.{Container, Reference}
import com.nabijaczleweli.minecrasmer.resource.ResourcesReloadedEvent
import com.nabijaczleweli.minecrasmer.util.IOreDictRegisterable
import com.nabijaczleweli.minecrasmer.util.StringUtils._
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.{IIcon, MathHelper, StatCollector}
import net.minecraftforge.oredict.OreDictionary

object ItemPlastic extends Item with IOreDictRegisterable {
	Container.eventBus register this

	private val subIconNames = Array[String]("monomer", "polymer", "plastic")

	@SideOnly(Side.CLIENT)
	private lazy val icons = new Array[IIcon](subIconNames.length)
	@SideOnly(Side.CLIENT)
	private lazy val localizedNames = new Array[String](subIconNames.length)

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

	@SideOnly(Side.CLIENT)
	override def getIconFromDamage(idx: Int) =
		icons(MathHelper.clamp_int(idx, 0, icons.length - 1))

	@SideOnly(Side.CLIENT)
	override def registerIcons(ir: IIconRegister) =
		for(i <- 0 until icons.length)
			icons(i) = ir registerIcon Reference.NAMESPACED_PREFIX + subIconNames(i)

	override def getItemStackDisplayName(is: ItemStack) =
		localizedNames(MathHelper.clamp_int(is.getItemDamage, 0, subIconNames.length))

	@SideOnly(Side.CLIENT)
	override def getSubItems(item: Item, tab: CreativeTabs, list: util.List[_]) =
		if(item.isInstanceOf[this.type])
			for(i <- 0 until icons.length)
				list.asInstanceOf[util.List[ItemStack]] add new ItemStack(item, 1, i)

	@SubscribeEvent
	def onResourcesReloaded(event: ResourcesReloadedEvent) {
		for(nameIdx <- subIconNames.indices)
			localizedNames(nameIdx) = StatCollector translateToLocal s"$getUnlocalizedName.${subIconNames(nameIdx)}.name"
	}

	override def registerOreDict() {
		val is = new ItemStack(this)
		for(i <- monomerDamage to plasticDamage) {
			is setItemDamage i
			OreDictionary.registerOre(oreDictName(i), is)
		}
	}
}
