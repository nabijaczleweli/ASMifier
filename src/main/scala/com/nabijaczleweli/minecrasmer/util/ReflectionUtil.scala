package com.nabijaczleweli.minecrasmer.util

import scala.reflect.runtime.universe._
import scala.reflect.runtime.currentMirror

object ReflectionUtil {
	def setValInInstance(instance: AnyRef, valName: String, newValue: Any) {
		val instanceMirror = currentMirror reflect instance
		val field = (instanceMirror.symbol.toType decl TermName(valName)).asTerm.accessed.asTerm
		val fieldMirror = instanceMirror reflectField field
		fieldMirror set newValue
	}
}
