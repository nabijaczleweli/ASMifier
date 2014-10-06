package com.nabijaczleweli.minecrasmer.resource

import javax.annotation.Nonnull

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/** This class is here to satisfy the JVM on the Server side
  * <br />
  * To use a ReloadableString one would use `lazy val foo = new ReloadableString("bar")`,
  * but since ReloadableString is Client-side only the JVM would cry about instantiating
  * by throwing `java.lang.NoClassDefFoundError: [Lcom/nabijaczleweli/minecrasmer/resource/ReloadableString;`.
  * Because this class exists on both sides the above code becomes `lazy val foo = {val t = new ReloadableStrings;`
  * `t.strings = List(new ReloadableString("bar");t)` or `lazy val foo = new ReloadableStrings(Future(List(new ReloadableString("bar"))))`.
  */
class ReloadableStrings extends ISimpleReloadable {
	def this(@Nonnull fut: Future[List[AnyRef]]) = {
		this
		fut onSuccess {
			case list =>
				strings = list filter {obj => classOf[ReloadableString] isAssignableFrom obj.getClass}
		}
	}

	var strings: List[AnyRef] = Nil

	override def reload() =
		for(string <- strings)
			try {
				string.asInstanceOf[ISimpleReloadable].reload()
			} catch {
				case _: Throwable =>
			}

	def apply(idx: Int) = {
		if(idx >= strings.size)
			null
		else
			strings(idx)
	}.asInstanceOf[ReloadableString]
}
