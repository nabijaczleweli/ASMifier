package com.nabijaczleweli.minecrasmer.reference

import com.nabijaczleweli.minecrasmer.compat._
import com.nabijaczleweli.minecrasmer.reference.Container._
import com.nabijaczleweli.minecrasmer.reference.Reference._
import com.nabijaczleweli.minecrasmer.util.CompatUtil._
import com.nabijaczleweli.minecrasmer.util.ReflectionUtil
import com.nabijaczleweli.minecrasmer.util.StringUtils._
import net.minecraftforge.fml.relauncher.Side

import scala.collection.immutable.HashMap
import scala.collection.mutable.ListBuffer
import scala.reflect.runtime.ReflectionUtils

object CompatLoader {
	import com.nabijaczleweli.minecrasmer.reference.CompatState.{CompatState, DISABLED, ERRORED, INITIALIZED, LOADED, NOMODS, PREINITIALIZED}

	private var compats            : List[ICompat]             = Nil
	private var compatLoadingStates: Map[ICompat, CompatState] = HashMap.empty
	private var namedCompats       : Map[String, ICompat]      = HashMap.empty

	def identifyCompats(pckg: Package) {
		val classes = ReflectionUtil.subClassesInPackage(classOf[ICompat], pckg, {!_.isInterface})
		log info s"$MOD_NAME has identified ${classes.size} compats to load"

		for(c <- classes) {
			var instance: ICompat = null
			try
				instance = c.newInstance
			catch {
				case _: Throwable =>
					try
						instance = (ReflectionUtils staticSingletonInstance c).asInstanceOf[ICompat]
					catch {
						case t: Throwable =>
							log warn s"Cannot instantiate nor get instance of compat \'$c\'. Cause: $t"
					}
			}

			if(instance != null) {
				compats :+= instance
				compatLoadingStates += instance -> LOADED
				namedCompats += c.getSimplestName -> instance
			}
		}

		sortCompats()
	}

	def preLoadCompats(side: Side) =
		doLoadCompats({_ preLoad side}, {_.shouldPreLoad}, side, Nil, PREINITIALIZED, "preload")

	def loadCompats(side: Side) =
		doLoadCompats({_ load side}, {_.shouldLoad}, side, NOMODS :: DISABLED :: Nil, INITIALIZED, "load")

	def getCompatByName(name: String) =
		namedCompats get name

	def getCompatStatus(name: String) =
		getCompatByName(name) map {compatLoadingStates.apply}

	private def doLoadCompats(invoke: ICompat => CompatResult, shouldLoad: ICompat => Boolean, side: Side, excludedStates: Seq[CompatState], finalState: CompatState, baseWord: String) =
		for(compat <- compats if !(excludedStates contains compatLoadingStates(compat)))
			if(shouldLoad(compat))
				if(compat.hasAllLoaded)
					invoke(compat) match {
						case Successful =>
							log info s"Successfully ${baseWord}ed compat ${compat.getClass.getSimplestName}."
							compatLoadingStates += compat -> finalState
						case Empty =>
							log info s"Nothing to $baseWord for compat ${compat.getClass.getSimplestName}."
							compatLoadingStates += compat -> finalState
						case Failed =>
							log info s"${baseWord toUpper 0}ing compat ${compat.getClass.getSimplestName} failed."
							compatLoadingStates += compat -> ERRORED
						case WrongSide =>
							log info s"Didn\'t $baseWord compat ${compat.getClass.getSimplestName} on $side."
							compatLoadingStates += compat -> finalState // Next stage might be used on this side
						case Completed() =>
							log warn s"${baseWord toUpper 0}ing compat ${compat.getClass.getSimplestName} has returned an unhandled result, but it still finished."
							compatLoadingStates += compat -> finalState
						case Uncompleted() =>
							log warn s"${baseWord toUpper 0}ing compat ${compat.getClass.getSimplestName} has returned an unhandled result, and it failed to finish."
							compatLoadingStates += compat -> ERRORED
					}
				else {
					log info s"Could not find all mods for compat ${compat.getClass.getSimplestName}, hence its ${baseWord}ing failed."
					compatLoadingStates += compat -> NOMODS
				}
			else
				compatLoadingStates += compat -> DISABLED

	private def sortCompats() {
		val sizeToCompat = new ListBuffer[(Int, ICompat)]
		for(compat <- compats)
			sizeToCompat prepend compat.getModIDs.size -> compat

		val sizes = sizeToCompat groupBy {_._1} mapValues {_ map {_._2}}
		for((_, compatList) <- sizes) { // In-place sort
			val sorted = compatList sortWith {(left, right) => (left.getModIDs mkString "").size < (right.getModIDs mkString "").size} // Sort by collective length of modIDs
			compatList.clear()
			compatList ++= sorted
		}
	}
}

object CompatState extends Enumeration {
	type CompatState = Value

	val LOADED         = Value("Loaded")
	val PREINITIALIZED = Value("Preinitialized")
	val INITIALIZED    = Value("Initialized")
	val AVAILABLE      = Value("Available")
	val ERRORED        = Value("Errored")
	val NOMODS         = Value("No mods")
	val DISABLED       = Value("Disabled")
}