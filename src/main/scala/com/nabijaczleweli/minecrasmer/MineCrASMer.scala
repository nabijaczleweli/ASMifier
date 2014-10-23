package com.nabijaczleweli.minecrasmer

import com.google.common.reflect.ClassPath
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

@Mod(modid = MOD_ID, name = MOD_NAME, version = VERSION, dependencies = "after:appliedenergistics2;after:Waila;after:PneumaticCraft;after:MineFactoryReloaded;after:TConstruct;after:NotEnoughItems", modLanguage = "scala")
object MineCrASMer {
	@SidedProxy(clientSide = CLIENT_PROXY_PATH, serverSide = SERVER_PROXY_PATH)
	var proxy: IProxy = _

	lazy val compats = {
		Compiler.disable() // Don't fruitlessly JIT-compile a lot of classes, we need only those that we'll instantiate, not all from the package
		val classWrappers = ClassPath from getClass.getClassLoader getTopLevelClassesRecursive "com.nabijaczleweli.minecrasmer.compat"
		val tempClasses = {
			for(cw <- classWrappers) yield
				try
					Class.forName(cw.getName, true, getClass.getClassLoader)
				catch { // Don't crash on Client-side only classes
					case _: Throwable =>
						null
				}
		} ++ {
			for(cw <- classWrappers) yield
				try
					Class.forName(cw.getName + '$', true, getClass.getClassLoader) // Also find objects
				catch {
					case _: Throwable =>
						null
				}
		} filter {_ != null}
		Compiler.enable()
		val classes = (tempClasses filter classOf[ICompat].isAssignableFrom filter {!_.isInterface}).toList.asInstanceOf[List[Class[_ <: ICompat]]]
		Container.log info s"$MOD_NAME has identified ${classes.size} compats to load"
		for(c <- classes) yield
			try
				c.newInstance()
			catch {
				case _: Throwable =>
					try
						c getField "MODULE$" get null
					catch {
						case _: Throwable =>
							null
					}
			}
	}.asInstanceOf[List[ICompat]] filter {_ != null}

	@EventHandler
	def preInit(event: FMLPreInitializationEvent) {
		Configuration load event.getSuggestedConfigurationFile

		for(compat <- compats if compat.shouldPreLoad)
			if(compat.hasAllLoaded)
				compat preLoad event.getSide match {
					case Successful =>
						log info s"Successfully preloaded compat ${compat.getClass.getSimplestName}."
					case Empty =>
						log info s"Nothing to preload for compat ${compat.getClass.getSimplestName}."
					case Failed =>
						log info s"Preloading compat ${compat.getClass.getSimplestName} failed."
					case WrongSide =>
						log info s"Didn\'t preload compat ${compat.getClass.getSimplestName} on ${event.getSide}."
					case Completed() =>
						log warn s"Preloading compat ${compat.getClass.getSimplestName} has returned an unhandled result, but it still finished."
					case Uncompleted() =>
						log warn s"Preloading compat ${compat.getClass.getSimplestName} has returned an unhandled result, and it failed to finish."
				}
			else
				log info s"Could not find all mods for compat ${compat.getClass.getSimplestName}, hence its preloading failed."

		proxy.registerFluids()
		proxy.registerItemsAndBlocks()
		proxy.registerGUIs()
		proxy.registerEntities()
	}

	@EventHandler
	def init(event: FMLInitializationEvent) {
		for(compat <- compats if compat.shouldLoad)
			if(compat.hasAllLoaded)
				compat load event.getSide match {
					case Successful =>
						log info s"Successfully loaded compat ${compat.getClass.getSimplestName}."
					case Empty =>
						log info s"Nothing to load for compat ${compat.getClass.getSimplestName}."
					case Failed =>
						log info s"Loading compat ${compat.getClass.getSimplestName} failed."
					case WrongSide =>
						log info s"Didn\'t load compat ${compat.getClass.getSimplestName} on ${event.getSide}."
					case Completed() =>
						log warn s"Loading compat ${compat.getClass.getSimplestName} has returned an unhandled result, but it still finished."
					case Uncompleted() =>
						log warn s"Loading compat ${compat.getClass.getSimplestName} has returned an unhandled result, and it failed to finish."
				}
			else
				log info s"Could not find all mods for compat ${compat.getClass.getSimplestName}, hence its loading failed."

		proxy.registerEvents()
		proxy.registerOreDict()
		proxy.registerOreGen()
		proxy.registerRecipes()
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
						val itemStack = ItemStack loadItemStackFromNBT (message.getNBTValue getCompoundTag "itemstack")
						FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack(itemStack.getItem.asInstanceOf[ItemScoop].fluid.getName, ItemScoop.capacity), itemStack, new ItemStack(Container.scoopEmpty))

						val item = itemStack.getItem.asInstanceOf[ItemScoop]
						Container.foreignScoops ::= item
						ScoopHandler.scoops += item.contains -> item

						log info s"Successfully registered scoop with ${item.fluid.getName}."
					} catch {
						case exc: Throwable =>
							log.warn(s"Unable to register scoop from ${message.getSender}:", exc)
					}
				case _ =>
			}
	}
}
