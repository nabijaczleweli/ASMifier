package com.nabijaczleweli.minecrasmer.item

import java.util

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.Reference
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.{IIcon, MathHelper}

class ItemPlastic extends Item {
	setUnlocalizedName("plastic")
	setCreativeTab(CreativeTabMineCrASMer)
	setHasSubtypes(true)

	override def getMaxDamage =
		0

	@SideOnly(Side.CLIENT)
	override def getIconFromDamage(idx: Int) =
		ItemPlastic icons MathHelper.clamp_int(idx, 0, ItemPlastic.icons.length - 1)

	@SideOnly(Side.CLIENT)
	override def registerIcons(ir: IIconRegister) =
		for(i <- 0 until ItemPlastic.icons.length)
			ItemPlastic icons i = ir registerIcon Reference.NAMESPACED_PREFIX + ItemPlastic.subIconNames(i)

	override def getItemStackDisplayName(is: ItemStack) =
		ItemPlastic subDisplayName MathHelper.clamp_int(is.getItemDamage, 0, ItemPlastic.subDisplayName.length)

	@SideOnly(Side.CLIENT)
	override def getSubItems(item: Item, tab: CreativeTabs, list: util.List[_]) =
		if(item.isInstanceOf[ItemPlastic])
			for(i <- 0 until ItemPlastic.icons.length)
				list.asInstanceOf[util.List[ItemStack]] add new ItemStack(item, 1, i)
}

object ItemPlastic {
	private val subIconNames = Array[String]("monomer", "polymer", "plastic")
	private val subDisplayName = Array[String]("Monomer", "Polymer", "Plastic")

	@SideOnly(Side.CLIENT)
	private val icons = new Array[IIcon](subIconNames.length)

	val monomerDamage = 0
	val polymerDamage = 1
	val plasticDamage = 2
	def oreDictName(dmg: Int) =
		"material" + subDisplayName(MathHelper.clamp_int(dmg, 0, ItemPlastic.subDisplayName.length - 1))
}
