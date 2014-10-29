package com.nabijaczleweli.minecrasmer.util;

import java.lang.reflect.InvocationTargetException;

public class JavaUtils {
	public static Object getModuleFromClass(String classPartialPath) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
		return Class.forName("com.nabijaczleweli.minecrasmer." + classPartialPath).getField("MODULE$").get(null);
	}

	public static Object getValFromModule(Object module, String valName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		return module.getClass().getMethod(valName).invoke(module);
	}
}
