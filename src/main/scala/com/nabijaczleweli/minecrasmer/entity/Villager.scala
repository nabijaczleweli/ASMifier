package com.nabijaczleweli.minecrasmer.entity

import com.nabijaczleweli.minecrasmer.reference.Reference
import com.nabijaczleweli.minecrasmer.reference.Reference._
import com.nabijaczleweli.minecrasmer.util.IConfigurable
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.common.registry.VillagerRegistry
import net.minecraftforge.fml.common.registry.VillagerRegistry.{VillagerCareer, VillagerProfession}

object Villager extends IConfigurable {
	val electronicsVillagerProfession = new VillagerProfession(Reference.NAMESPACED_PREFIX + "electronic", Reference.NAMESPACED_PREFIX + "textures/entity/villager/electronic.png")
	final var electronicsVillagerID = 4637

	override def load(config: Configuration) =
		electronicsVillagerID = config.getInt("electronicsVillagerID", CONFIG_ENTIRY_CATEGORY, electronicsVillagerID, 6, Int.MaxValue, "ID of the Electronics Vilager")

	def registerProfessions() {
		VillagerRegistry.instance register electronicsVillagerProfession
		new VillagerCareer(electronicsVillagerProfession, "electronic")
	}
}
