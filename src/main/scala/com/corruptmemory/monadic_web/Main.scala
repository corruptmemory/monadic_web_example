package com.corruptmemory.monadic_web

object Main {
  import MaybeError._
  import RequestMonad._
  import RequestHelpers._

  def resultDumper(r: MaybeError) {
    r.fold2(failure = f => println("*** ERROR: %s".format(f)),
      value = v => println(">>> Browser says: %s".format(v)),
      stream = s => {
        val sb = new StringBuilder
        s(sb)
        println("~~~ Streamed result: %s".format(sb.toString))
      })
  }

  def handler1: RequestReader[MaybeError] = {
    for {
      a <- read("arg")
    } yield a
  }

  def handler2: RequestReader[MaybeError] = {
    for {
      a <- read("arg")
    } yield Stream((sb:StringBuilder) => sb.append(a))
  }
  
  def responseWrapper(req:Request,res:Response):MaybeError = {
    res match {
      case Value(s) => "Wrapped!\n\n%s\n\nWrapped!".format(s)
      case s@Stream(_) => s
    }
  }
  
  def pipeline(handler:Request=>MaybeError,
               wrapper:Request=>Response=>MaybeError) = {
    for {
      r1 <- RequestMonad(handler)
      r2 <- wrapResponse(r1,wrapper)
    } yield r2
  }

  def main(args: Array[String]) {
    val processor = RequestMonad(handler1)
    resultDumper(processor(Map("arg" -> "woo hoo!")))
    resultDumper(processor(Map("arg1" -> "woo hoo!")))
    val processor1 = RequestMonad(handler2)
    resultDumper(processor1(Map("arg" -> "woo hoo!")))
    resultDumper(processor1(Map("arg1" -> "woo hoo!")))
    val processor2 = pipeline(handler1,(responseWrapper _).curried)
    resultDumper(processor2(Map("arg" -> "woo hoo!")))
    resultDumper(processor2(Map("arg1" -> "woo hoo!")))
  }

}