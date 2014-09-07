package com.nabijaczleweli.minecrasmer.item

import java.util

import com.nabijaczleweli.minecrasmer.creativetab.CreativeTabMineCrASMer
import com.nabijaczleweli.minecrasmer.reference.Reference
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.{IIcon, MathHelper}

class ItemPCB extends Item {
	setUnlocalizedName("PCB")
	setCreativeTab(CreativeTabMineCrASMer)
	setHasSubtypes(true)

	override def getMaxDamage =
		0

	@SideOnly(Side.CLIENT)
	override def getIconFromDamage(idx: Int) =
		ItemPCB icons MathHelper.clamp_int(idx, 0, ItemPCB.icons.length - 1)

	@SideOnly(Side.CLIENT)
	override def registerIcons(ir: IIconRegister) {
		for(i <- 0 until ItemPCB.icons.length)
			ItemPCB icons i = ir registerIcon Reference.NAMESPACED_PREFIX + String.format(ItemPCB subIconNames i, "pcb_")
	}

	override def getItemStackDisplayName(is: ItemStack) =
		ItemPCB subDisplayName MathHelper.clamp_int(is.getItemDamage, 0, 15) format "PCB"

	@SideOnly(Side.CLIENT)
	override def getSubItems(item: Item, tab: CreativeTabs, list: util.List[_]) {
		if(item.isInstanceOf[ItemPCB])
			for(i <- 0 until ItemPCB.icons.length)
				list.asInstanceOf[util.List[ItemStack]] add new ItemStack(item, 1, i)
	}
}

object ItemPCB {
	private val subIconNames = Array[String]("%selements", "%snoelements", "%slcd", "lcd")
	private val subDisplayName = Array[String]("%s with elements", "%s without elements", "%s with LCD", "LCD")
	@SideOnly(Side.CLIENT)
	private val icons = new Array[IIcon](subIconNames.length)

	val fullPCBDamage  = 0
	val emptyPCBDamage = 1
	val PCBLCDDamage   = 2
	val LCDDamage      = 3
}
