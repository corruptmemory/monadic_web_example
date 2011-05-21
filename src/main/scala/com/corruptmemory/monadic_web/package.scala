package com.corruptmemory

package object monadic_web {
  import MaybeError._
  import RequestMonad._
  import RequestHelpers._
  implicit def string2ME(s:String):MaybeError = Right(Value(s))
  implicit def value2ME(v:Value):MaybeError = Right(v)
  implicit def func2ME(f:StringBuilder => Unit):MaybeError = Right(Stream(f))
  implicit def stream2ME(s:Stream):MaybeError = Right(s)
  implicit def reader2Arg(s:RequestReader[MaybeError]):Request => MaybeError = {
    (r:Request) => {
      s(r).fold(fa = l => Left(l),
                fb = r => r)
    }
  }
}