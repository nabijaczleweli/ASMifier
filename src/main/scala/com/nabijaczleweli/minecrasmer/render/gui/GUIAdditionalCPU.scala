package com.nabijaczleweli.minecrasmer.render.gui

import com.nabijaczleweli.minecrasmer.container.ContainerAdditionalCPU
import com.nabijaczleweli.minecrasmer.resource.MineCrASMerLocation
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.resources.I18n
import net.minecraft.inventory.IInventory
import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import org.lwjgl.opengl.GL11._

@SideOnly(Side.CLIENT)
class GUIAdditionalCPU(container: ContainerAdditionalCPU) extends GuiContainer(container) {
	ySize = 109

	protected override def drawGuiContainerBackgroundLayer(f: Float, mouseX: Int, mouseY: Int) {
		glColor4f(1.0F, 1.0F, 1.0F, 1.0F)
		mc.getTextureManager bindTexture GUIAdditionalCPU.background
		drawTexturedModalRect((width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize)
	}

	protected override def drawGuiContainerForegroundLayer(mouseX: Int, mouseY: Int) { // Stolen from TileEntityHopper
		fontRendererObj.drawString(displayName(container.te), 5, 4, 0x404040)
		fontRendererObj.drawString(displayName(container.inventoryPlayer), 5, ySize - 92, 0x404040)
	}

	private def displayName(inv: IInventory) =
		if(inv.hasCustomName)
			inv.getName
		else
			I18n format inv.getName
}

object GUIAdditionalCPU {
	private val background = new MineCrASMerLocation("textures/gui/additional_cpu.png")

	val id = 1
}
