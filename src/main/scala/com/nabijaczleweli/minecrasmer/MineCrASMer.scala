package com.nabijaczleweli.minecrasmer

import com.nabijaczleweli.minecrasmer.compat._
import com.nabijaczleweli.minecrasmer.item.ItemScoop
import com.nabijaczleweli.minecrasmer.proxy.IProxy
import com.nabijaczleweli.minecrasmer.reference.Container._
import com.nabijaczleweli.minecrasmer.reference.Reference._
import com.nabijaczleweli.minecrasmer.reference.{CompatLoader, Configuration, Container}
import com.nabijaczleweli.minecrasmer.util.ReflectionUtil
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.fml.common.{Mod, SidedProxy}

import scala.collection.JavaConversions._

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = DEPENDENCIES, modLanguage = "scala")
object MineCrASMer {
	@SidedProxy(clientSide = CLIENT_PROXY_PATH, serverSide = SERVER_PROXY_PATH)
	var proxy: IProxy = _

	@EventHandler
	def preInit(event: FMLPreInitializationEvent) {
		ReflectionUtil.setFieldInInstance(Container, "log", event.getModLog)
		Configuration load event.getSuggestedConfigurationFile
		CompatLoader identifyCompats classOf[ICompat].getPackage
		CompatLoader preLoadCompats event.getSide

		proxy.registerFluids()
		proxy.registerItemsAndBlocks()
		proxy.registerRenderers()
		proxy.registerGUIs()
		proxy.registerEntities()
	}

	@EventHandler
	def init(event: FMLInitializationEvent) {
		CompatLoader loadCompats event.getSide

		proxy.registerEvents()
		proxy.registerOreDict()
		proxy.registerOreGen()
		proxy.registerRecipes()
		proxy.registerModels()
	}

	@EventHandler
	def postInit(event: FMLPostInitializationEvent) {
		proxy.registerLoot()
	}

	@EventHandler
	def processIMCs(event: IMCEvent) {
		for(message <- event.getMessages)
			message.key match {
				case "register-scoop" if message.isNBTMessage =>  // This method of registering scoops requires the scoop item and fluid to be registered
					FluidRegistry getFluid (message.getNBTValue getString "fluid_name") match {
						case null =>
							log.warn(s"Unable to register scoop from ${message.getSender}: block of name '${message.getNBTValue getString "block_name"}' does not exist.")
						case fluid =>
							ItemScoop.colors += fluid -> (message.getNBTValue getInteger "color")
					}
				case _ =>
			}
	}
}
