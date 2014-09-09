package com.nabijaczleweli.minecrasmer.item

import java.util

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.Reference
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.{StatCollector, IIcon, MathHelper}
import com.nabijaczleweli.minecrasmer.util.StringUtils._

object ItemPlastic extends Item {
	private val subIconNames = Array[String]("monomer", "polymer", "plastic")

	@SideOnly(Side.CLIENT)
	private val icons = new Array[IIcon](subIconNames.length)

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
		StatCollector translateToLocal (getUnlocalizedName + '.' + subIconNames(MathHelper.clamp_int(is.getItemDamage, 0, subIconNames.length)) + ".name")

	@SideOnly(Side.CLIENT)
	override def getSubItems(item: Item, tab: CreativeTabs, list: util.List[_]) =
		if(item.isInstanceOf[this.type])
			for(i <- 0 until icons.length)
				list.asInstanceOf[util.List[ItemStack]] add new ItemStack(item, 1, i)
}
