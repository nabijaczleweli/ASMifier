package com.nabijaczleweli.minecrasmer.util

import scala.collection.mutable

object StringUtils {
	implicit class Utils(string: String) {
		def substring(toFind: String) =
			string match {
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
	}
}
