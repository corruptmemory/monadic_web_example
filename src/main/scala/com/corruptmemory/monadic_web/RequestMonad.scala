package com.corruptmemory.monadic_web

abstract class RequestMonad {
  self =>
  import MaybeError.MaybeError
  import RequestMonad._
  def apply(request:Request):MaybeError
  def map(f:MaybeError => MaybeError):RequestMonad = new RequestMonad {
    def apply(request:Request):MaybeError = 
      self.apply(request).fold(fa = l => Left(l),
                               fb = r => f(Right(r)))
  }
  def flatMap(f:MaybeError => RequestMonad):RequestMonad = new RequestMonad {
    def apply(request:Request):MaybeError = 
      self.apply(request).fold(fa = l => Left(l),
                               fb = r => f(Right(r)).apply(request))
  }
}

object RequestMonad {
  import MaybeError.MaybeError
  type Request = Map[String,String]
  def apply(handler:Request=>MaybeError):RequestMonad = new RequestMonad {
    import MaybeError.MaybeError
    def apply(request:Request):MaybeError = handler(request)
  }
  def wrapResponse(response:MaybeError,wrapper:Request=>Response=>MaybeError):RequestMonad = new RequestMonad {
    import MaybeError.MaybeError
    def apply(request:Request):MaybeError = 
      response.fold(fa = l => Left(l),
                    fb = r => wrapper(request)(r))
  }
}

abstract class RequestReader[A] {
  self =>
  import RequestMonad._
  import RequestHelpers._
  def apply(request:Request):Result[A]
  
  def map[B](f:A => B):RequestReader[B] = new RequestReader[B] {
    def apply(request:Request):Result[B] = 
      self.apply(request).fold(fa = l => Left(l),
                               fb = r => Right(f(r)))
  }
  
  def flatMap[B](f:A => RequestReader[B]):RequestReader[B] = new RequestReader[B] {
    def apply(request:Request):Result[B] = 
      self.apply(request).fold(fa = l => Left(l),
                               fb = r => f(r).apply(request))
  }
}

object RequestHelpers {
  import RequestMonad._
  type Result[X] = Either[String,X]
  def read(key:String):RequestReader[String] = new RequestReader[String] {
    def apply(request:Request):Result[String] = request.get(key) match {
      case Some(x) => Right(x)
      case None => Left("Could not get value for:%s".format(key))
    }
  }
}
