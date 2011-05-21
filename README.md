# Monadic Web Example

This is a very simple example of using monads in Scala to demonstrate monadic computation and how using types one can significantly reduce the opportunity for bugs while making the intent of the code much more clear.

## Approach

I didn't want to get distracted by everything that is necessary in developing a Web application so I've abstracted out much of the detail.  Requests are simply Scala `Map`s and responses can either be `Value`s which just hold strings or `Stream`s that signal a request to directly stream data to the client.

## The Monads

In Scala objects that implement `map` and `flatMap` can be used in `for`-comprehensions, effectively meaning that such objects can be considered *monads*.  The example introduces two monads: `RequestMonad` and `RequestReader`.  In particular, both of these monads are example of *reader* monads in that they thread a common value through a chain of functions.

### The RequestMonad

The `RequestMonad` works as an intermediary between incoming requests and *handlers*. Handlers are any function with the signature `Request=>MaybeError`.  You can create a `RequestMonad` instance as follows:

    import MaybeError._
    import RequestMonad._
    
    def foo {
      val processor = RequestMonad(handler)
    }
    
Because `RequestMonad` is a monad it can be used in `for`-comprehensions as follows:

    import MaybeError._
    import RequestMonad._
    
    def processor(handler:Request=>MaybeError) =
      for (r <- RequestMonad(handler)) yield r

In this example `processor` is a "fully loaded" `RequestMonad` all ready for request data to be passed in:

    processor(Map("arg1"->"value1","arg2"->"value2"))
    
There's a useful monadic function defined on `RequestMonad`s called `wrapResponse` that provides an example of how doing "page layout" can be done monadically:

    def processor(handler:Request=>MaybeError,
                  wrapper:Request=>Response=>MaybeError) =
      for {
        r1 <- RequestMonad(handler)
        r2 <- wrapResponse(r1,wrapper)
      } yield r2

Later, when we get into more detail as to how this monad is constructed, we'll see that things like error conditions are automatically handled.

