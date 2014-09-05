package com.nabijaczleweli.minecrasmer.gui

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.gui.{FontRenderer, GuiScreen}
import net.minecraft.util.ResourceLocation
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11._

@SideOnly(Side.CLIENT)
object GUIComputer extends GuiScreen {
	val id = 0

	private final val resourceGUI   = new ResourceLocation("minecrasmer", "textures/gui/computer.png")
	private final val textureWidth  = 200
	private final val textureHeight = 150
	private final val screenContentOffsetX = 5
	private final val screenContentOffsetY = 5
	private final lazy val textureStartX = (width - textureWidth) / 2
	private final lazy val textureStartY = (height - textureHeight) / 2

	private final var lines: Array[String] = null

	fontRendererObj = null

	override def initGui() {
		super.initGui()
		Keyboard enableRepeatEvents true
		if(fontRendererObj == null) // Such that we don't get java.lang.ExceptionInInitializerError
			fontRendererObj = new FontRenderer(mc.gameSettings, new ResourceLocation("minecrasmer", "textures/gui/ascii.png"), mc.renderEngine, false)
		lines = Array("TEXT0", "TEXT1", "TEXT2", "", "", "", "")
	}

	override def drawScreen(mouseX: Int, mouseY: Int, f: Float) {
		drawDefaultBackground()

		glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
		mc.renderEngine bindTexture resourceGUI
		drawTexturedModalRect(textureStartX, textureStartY, 0, 0, textureWidth, textureHeight)

		drawLines()
		super.drawScreen(mouseX, mouseY, f)
	}

	override def onGuiClosed() {
		super.onGuiClosed()
		Keyboard enableRepeatEvents false
		lines = null
	}

	override def keyTyped(character: Char, modifiers: Int) {
		super.keyTyped(character, modifiers)
		if(lines != null) // Apparently keyTyped() is fired even after onGUIClosed()
			lines(3) += character
	}

	def drawLines() {
		var idx = 0
		for(str <- lines) {
			fontRendererObj.drawString(str, textureStartX + screenContentOffsetX, textureStartY + screenContentOffsetY + 8 * idx, 0xFFFFFF)
			idx += 1
		}
	}
}
