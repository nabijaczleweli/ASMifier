package com.nabijaczleweli.minecrasmer.compat

sealed abstract class CompatResult(val completed: Boolean)
case object Successful extends CompatResult(true)
case object Failed extends CompatResult(false)
case object WrongSide extends CompatResult(false)
