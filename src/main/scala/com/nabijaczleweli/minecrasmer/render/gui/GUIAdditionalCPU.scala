package com.nabijaczleweli.minecrasmer.render.gui

import com.nabijaczleweli.minecrasmer.container.ContainerAdditionalCPU
import com.nabijaczleweli.minecrasmer.resource.MineCrASMerLocation
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.client.gui.inventory.GuiContainer
import org.lwjgl.opengl.GL11._

@SideOnly(Side.CLIENT)
class GUIAdditionalCPU(container: ContainerAdditionalCPU) extends GuiContainer(container) {
	xSize = GUIAdditionalCPU.xSize
	ySize = GUIAdditionalCPU.ySize

	override def drawGuiContainerBackgroundLayer(f: Float, mouseX: Int, mouseY: Int) {
		glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
		mc.getTextureManager bindTexture GUIAdditionalCPU.background
		drawTexturedModalRect((width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize)
	}
}

object GUIAdditionalCPU {
	private val background = new MineCrASMerLocation("textures/gui/additional_cpu.png")

	val id = 1

	val xSize = 176
	val ySize = 109
}
