package com.nabijaczleweli.minecrasmer.util

import scala.collection.mutable

object StringUtils {
	implicit class Utils(val string: String) extends AnyVal {
		def substring(toFind: String) =
			string match {
				case `toFind` =>
					""
				case str if str contains toFind =>
					str substring ((str indexOf toFind) + 1)
				case str =>
					str
			}

		def toUpper(idx: Int) = {
			val bldr = new mutable.StringBuilder(string)
			bldr.setCharAt(idx, bldr.charAt(idx).toUpper)
			bldr.toString()
		}

		def toLower(idx: Int) = {
			val bldr = new mutable.StringBuilder(string)
			bldr.setCharAt(idx, bldr.charAt(idx).toLower)
			bldr.toString()
		}

		def before(toFind: String) =
			string match {
				case `toFind` =>
					""
				case str if str contains toFind =>
					str.substring(0, str indexOf toFind)
				case str =>
					str
			}
	}
}
