package com.nabijaczleweli.minecrasmer.util

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity

class SimpleDataProcessingTileEntity extends TileEntity {
	override def onDataPacket(net: NetworkManager, pkt: S35PacketUpdateTileEntity) =
		readFromNBT(pkt.getNbtCompound)

	override def getDescriptionPacket = {
		val tag = new NBTTagCompound
		writeToNBT(tag)
		new S35PacketUpdateTileEntity(pos, 0, tag)
	}
}
