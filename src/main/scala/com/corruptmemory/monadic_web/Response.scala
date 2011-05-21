package com.corruptmemory.monadic_web

sealed trait Response
case class Value(value:String) extends Response
case class Stream(stream:StringBuilder => Unit) extends Response
