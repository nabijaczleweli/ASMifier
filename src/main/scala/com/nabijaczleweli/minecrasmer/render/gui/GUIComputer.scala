package com.nabijaczleweli.minecrasmer.render.gui

import com.nabijaczleweli.minecrasmer.entity.tile.TileEntityComputer
import com.nabijaczleweli.minecrasmer.util.MineCrASMerLocation
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.gui.{FontRenderer, GuiScreen}
import net.minecraft.world.World
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11._

@SideOnly(Side.CLIENT)
object GUIComputer extends GuiScreen {
	val id = 0

	private final val resourceGUI   = new MineCrASMerLocation("textures/gui/computer.png")
	private final val textureWidth  = 200
	private final val textureHeight = 150
	private final val screenContentOffsetX = 5
	private final val screenContentOffsetY = 5
	private final lazy val textureStartX = (width - textureWidth) / 2
	private final lazy val textureStartY = (height - textureHeight) / 2

	private final var te: TileEntityComputer = _

	fontRendererObj = null

	def init(world: World, x: Int, y: Int, z: Int) {
		te = world.getTileEntity(x, y, z).asInstanceOf[TileEntityComputer]
	}

	override def initGui() {
		super.initGui()
		Keyboard enableRepeatEvents true
		if(fontRendererObj == null) // Such that we don't get java.lang.ExceptionInInitializerError
			fontRendererObj = new FontRenderer(mc.gameSettings, new MineCrASMerLocation("textures/gui/ascii.png"), mc.renderEngine, false)
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
		te = null
	}

	override def keyTyped(character: Char, modifiers: Int) {
		super.keyTyped(character, modifiers)
		if(te != null) // Apparently keyTyped() is fired even after onGUIClosed()
			te.lines(3) += character
	}

	def drawLines() {
		var idx = 0
		for(str <- te.lines) {
			fontRendererObj.drawString(str, textureStartX + screenContentOffsetX, textureStartY + screenContentOffsetY + 8 * idx, 0xFFFFFF)
			idx += 1
		}
	}

	override def doesGuiPauseGame() =
		false
}
