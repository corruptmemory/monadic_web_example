package com.corruptmemory.monadic_web

object MaybeError {
  type MaybeError = Either[String,Response]  
  class MaybeErrorW(r:MaybeError) {
    def fold2[Z](failure:String => Z,
        value:String => Z,
        stream:(StringBuilder => Unit) => Z):Z = {
      r.fold(fa = l => failure(l),
             fb = r => r match {
               case Value(s) => value(s)
               case Stream(s) => stream(s)
              })
    }
  }
  
  implicit def meW(r:MaybeError) = new MaybeErrorW(r)
  def err(message:String):Either[String,Response] = Left(message)
}