package com.nabijaczleweli.minecrasmer

import com.nabijaczleweli.minecrasmer.compat.waila.Waila
import com.nabijaczleweli.minecrasmer.compat._
import com.nabijaczleweli.minecrasmer.handler.ScoopHandler
import com.nabijaczleweli.minecrasmer.item.ItemScoop
import com.nabijaczleweli.minecrasmer.proxy.IProxy
import com.nabijaczleweli.minecrasmer.reference.Container._
import com.nabijaczleweli.minecrasmer.reference.Reference._
import com.nabijaczleweli.minecrasmer.reference.{Configuration, Container}
import com.nabijaczleweli.minecrasmer.util.CompatUtil._
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.{Mod, SidedProxy}
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids._

import scala.collection.JavaConversions._

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = "after:appliedenergistics2;after:Waila", modLanguage = "scala")
object MineCrASMer {
	@SidedProxy(clientSide = CLIENT_PROXY_PATH, serverSide = SERVER_PROXY_PATH)
	var proxy: IProxy = _

	val compats = new AE2 :: new Vanilla :: new Waila :: Nil

	@EventHandler
	def preInit(event: FMLPreInitializationEvent) {
		Configuration load event.getSuggestedConfigurationFile

		for(compat <- compats if compat.shouldPreLoad)
			if(compat.hasAllLoaded)
				(compat preLoad event.getSide: CompatResult) match {
					case Successful =>
						log info s"Successfully preloaded compat ${compat.getClass.getSimpleName}."
					case Failed =>
						log info s"Preloading compat ${compat.getClass.getSimpleName} failed."
					case WrongSide =>
						log info s"Didn\'t preload compat ${compat.getClass.getSimpleName} on ${event.getSide}."
				}
			else
				log info s"Could not find all mods for compat ${compat.getClass.getSimpleName}, hence its preloading failed."

		proxy.registerFluids()
		proxy.registerItemsAndBlocks()
		proxy.registerGUIs()
		proxy.registerEntities()
	}

	@EventHandler
	def init(event: FMLInitializationEvent) {
		for(compat <- compats if compat.shouldLoad)
			if(compat.hasAllLoaded)
				(compat load event.getSide: CompatResult) match {
					case Successful =>
						log info s"Successfully loaded compat ${compat.getClass.getSimpleName}."
					case Failed =>
						log info s"Loading compat ${compat.getClass.getSimpleName} failed."
					case WrongSide =>
						log info s"Didn\'t load compat ${compat.getClass.getSimpleName} on ${event.getSide}."
				}
			else
				log info s"Could not find all mods for compat ${compat.getClass.getSimpleName}, hence its loading failed."

		proxy.registerEvents()
		proxy.registerOreDict()
		proxy.registerOreGen()
	}

	@EventHandler
	def postInit(event: FMLPostInitializationEvent) {
		proxy.registerRenderers()
	}

	@EventHandler
	def processIMCs(event: IMCEvent) {
		for(message <- event.getMessages)
			message.key match {
				case "register-scoop" if message.isNBTMessage =>  // This method of registering scoops requires the scoop item and fluid to be registered
					try {
						val nbt = message.getNBTValue
						val itemStack = ItemStack loadItemStackFromNBT (nbt getCompoundTag "itemstack")
						FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack(itemStack.getItem.asInstanceOf[ItemScoop].fluid.getName, ItemScoop.capacity), itemStack, new ItemStack(Container.scoopEmpty))

						val item = itemStack.getItem.asInstanceOf[ItemScoop]
						Container.foreignScoops ::= item
						ScoopHandler.scoops += item.contains -> item

						log info s"Successfully registered scoop with ${item.fluid.getName}."
					} catch {
						case exc: Throwable =>
							log warn s"Unable to register scoop from ${message.getSender}, exception type: $exc"
					}
				case _ =>
			}
	}
}
