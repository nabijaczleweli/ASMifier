package com.nabijaczleweli.minecrasmer.util

import java.lang.reflect.Field

import com.google.common.base.Throwables
import com.google.common.reflect.ClassPath
import org.reflections.Reflections

import scala.collection.JavaConversions._
import scala.concurrent.util.Unsafe

object ReflectionUtil {
	@throws[NoSuchFieldException]("If the field isn\'t present in the class of instance class")
	@throws[NullPointerException]("If either `instance` or `name` are null")
	def setFieldInInstance(instance: AnyRef, name: String, newValue: Any) = {
		var cls: Class[_] = instance.getClass
		var field: Field = null
		try
			field = cls getField name
		catch {
			case _: NoSuchFieldException =>
		}
		while(field == null && cls != null) {
			try
				field = cls getDeclaredField name
			catch {
				case _: NoSuchFieldException =>
			}
			cls = cls.getSuperclass
		}
		if(field == null)
			throw new NoSuchFieldException(name)

		val offset = Unsafe.instance objectFieldOffset field
		Unsafe.instance.putObject(instance, offset, newValue)
	}

	/** Find subclasses of supplied superclass inside the supplied package using Reflections, if available, or hand-crafted class finding mechanism(tm).
	  *
	  * @param additionalFilters Additional class filters, like `{!_.isInterface)`; `Class[_]` is actually `Class[SuperType]`, but not cast.
	  */
	def subClassesInPackage[SuperType](superClass: Class[SuperType], pckg: Package, additionalFilters: Class[_] => Boolean) = {
		var classes: Iterable[Class[_ <: SuperType]] = Nil
		Compiler.disable()

		try
			classes = new Reflections(pckg.getName) getSubTypesOf superClass
		catch {
			case _: LinkageError => // No Reflections in classpath
				val classWrappers = ClassPath from getClass.getClassLoader getTopLevelClassesRecursive pckg.getName
				val tempClasses =
					classWrappers map {cw =>
						try
							Class.forName(cw.getName, false, getClass.getClassLoader) // Don't initialize those classes
						catch { // Don't crash on Client-side only classes
							case _: Throwable =>
								try
									Class.forName(cw.getName + '$', false, getClass.getClassLoader) // Also find objects; don't initialize them
								catch {
									case _: Throwable =>
										null
								}
						}
					} filter {_ != null}

				classes = tempClasses filter {superClass.isAssignableFrom} filter additionalFilters map {Class forName _.getName} map {_ asSubclass superClass}

			case t: Throwable =>
				Compiler.enable()
				throw Throwables propagate t
		}

		Compiler.enable()
		classes
	}
}
