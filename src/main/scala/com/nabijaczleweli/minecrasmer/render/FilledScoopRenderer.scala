/*package com.nabijaczleweli.minecrasmer.render

import com.nabijaczleweli.minecrasmer.item.ItemScoop
import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.client.renderer.{ItemRenderer, Tessellator}
import net.minecraft.item.ItemStack
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.IItemRenderer.ItemRenderType._
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper._
import net.minecraftforge.client.IItemRenderer.{ItemRenderType, ItemRendererHelper}
import org.lwjgl.opengl.GL11._

@SideOnly(Side.CLIENT)
object FilledScoopRenderer extends IItemRenderer {
	override def handleRenderType(item: ItemStack, `type`: ItemRenderType) =
		`type` != FIRST_PERSON_MAP

	override def shouldUseRenderHelper(`type`: ItemRenderType, item: ItemStack, helper: ItemRendererHelper) =
		helper != EQUIPPED_BLOCK && helper != BLOCK_3D && helper != INVENTORY_BLOCK

	override def renderItem(`type`: IItemRenderer.ItemRenderType, item: ItemStack, data: AnyRef*) {
		val itemScoop = item.getItem.asInstanceOf[ItemScoop]
		val mainIcon = itemScoop getIconFromDamage item.getItemDamage
		val containingIcon = itemScoop icons 0
		`type` match {
			case INVENTORY =>
				glEnable(GL_BLEND)
				glColor4f(1f, 1f, 1f, 1f)

				RenderItem.getInstance().renderIcon(0, 0, mainIcon, mainIcon.getIconWidth, mainIcon.getIconHeight)

				val color = itemScoop.getColorFromItemStack(item, 0)
				glColor4f((color >> 16 & 0xFF).toFloat / 255f, (color >> 8 & 0xFF).toFloat / 255f, (color & 0xFF).toFloat / 255f, 1)

				RenderItem.getInstance().renderIcon(0, 0, containingIcon, containingIcon.getIconWidth, containingIcon.getIconHeight)

				glDisable(GL_BLEND)
			case ENTITY =>
				glTranslated(-.5, 0, -0)
				ItemRenderer.renderItemIn2D(Tessellator.instance, containingIcon.getMaxU, containingIcon.getMinV, containingIcon.getMinU, containingIcon.getMaxV, containingIcon.getIconWidth, containingIcon.getIconHeight, .07f)
				glColor4f(1f, 1f, 1f, 1f)
				ItemRenderer.renderItemIn2D(Tessellator.instance, mainIcon.getMaxU, mainIcon.getMinV, mainIcon.getMinU, mainIcon.getMaxV, mainIcon.getIconWidth, mainIcon.getIconHeight, .065f)
				glTranslated(.5, 0, 0)
			case _ =>
				ItemRenderer.renderItemIn2D(Tessellator.instance, containingIcon.getMaxU, containingIcon.getMinV, containingIcon.getMinU, containingIcon.getMaxV, containingIcon.getIconWidth, containingIcon.getIconHeight, .07f)
				glColor4f(1f, 1f, 1f, 1f)
				ItemRenderer.renderItemIn2D(Tessellator.instance, mainIcon.getMaxU, mainIcon.getMinV, mainIcon.getMinU, mainIcon.getMaxV, mainIcon.getIconWidth, mainIcon.getIconHeight, .065f)
		}
	}
}*/