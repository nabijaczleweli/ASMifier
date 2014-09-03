package com.nabijaczleweli.minecrasmer.gui

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.gui.GuiScreen
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11._

@SideOnly(Side.CLIENT)
object GUIComputer extends GuiScreen {
	val id = 0

	private final val resourceGUI   = new ResourceLocation("minecrasmer", "textures/gui/computer.png")
	private final val textureWidth  = 200
	private final val textureHeight = 150
	private final lazy val textureStartX = (width - textureWidth) / 2
	private final lazy val textureStartY = (height - textureHeight) / 2

	override def drawScreen(mouseX: Int, mouseY: Int, f: Float) {
		drawDefaultBackground()

		glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
		mc.renderEngine bindTexture resourceGUI
		drawTexturedModalRect(textureStartX, textureStartY, 0, 0, textureWidth, textureHeight)

		super.drawScreen(mouseX, mouseY, f)
	}
}
