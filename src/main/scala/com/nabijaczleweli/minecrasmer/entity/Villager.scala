package com.nabijaczleweli.minecrasmer.entity

import com.nabijaczleweli.minecrasmer.reference.Reference._
import com.nabijaczleweli.minecrasmer.util.IConfigurable
import net.minecraftforge.common.config.Configuration

object Villager extends IConfigurable {
	final var electronicsVillagerID = 4637

	override def load(config: Configuration) =
		electronicsVillagerID = config.getInt("electronicsVillagerID", CONFIG_ENTIRY_CATEGORY, electronicsVillagerID, 6, Int.MaxValue, "ID of the Electronics Vilager")
}
