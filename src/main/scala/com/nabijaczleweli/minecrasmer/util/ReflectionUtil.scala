package com.nabijaczleweli.minecrasmer.util

import java.lang.reflect.Field

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
}
