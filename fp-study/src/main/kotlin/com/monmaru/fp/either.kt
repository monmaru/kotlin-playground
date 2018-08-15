package com.monmaru.fp

import arrow.core.*
import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.typeclasses.binding

fun randomlyEither(): Either<String, Int> = randomlyOption().toEither { "Nothing here" }

fun main(args: Array<String>) {
    Either.monad<String>().binding {
        val num1 = randomlyEither().bind()
        val num2 = randomlyEither().mapLeft { s -> s.toUpperCase() }.bind()
        num1 + num2
    }.fix().fold(::println, ::println)

    flatMapEitherDivision(3, 2, 4).fold(::println, ::println)
    eitherDivision(3, 2, 4).fold(::println, ::println)
    comprehensionEitherDivision(3, 2, 4).fold(::println, ::println)
}

fun eitherDivide(num: Int, den: Int): Either<String, Int> {
    val option = optionDivide(num, den)
    return when (option) {
        is Some -> Right(option.t)
        None -> Left("$num isn't divisible by $den")
    }
}

fun eitherDivision(a: Int, b: Int, den: Int): Either<String, Tuple2<Int, Int>> {
    val aDiv = eitherDivide(a, den)
    return when (aDiv) {
        is Right -> {
            val bDiv = eitherDivide(num = b, den = den)
            when (bDiv) {
                is Right -> Right(aDiv.getOrElse { 0 } toT bDiv.getOrElse { 0 })
                is Left -> bDiv as Either<String, Nothing>
            }
        }
        is Left -> aDiv as Either<String, Nothing>
    }
}

fun flatMapEitherDivision(a: Int, b: Int, den: Int): Either<String, Tuple2<Int, Int>> =
        eitherDivide(a, den).flatMap { aDiv ->
            eitherDivide(b, den).flatMap { bDiv ->
                Right(aDiv toT bDiv)
            }
        }

fun comprehensionEitherDivision(a: Int, b: Int, den: Int): Either<String, Tuple2<Int, Int>> =
        Either.monad<String>().binding {
            val aDiv = eitherDivide(a, den).bind()
            val bDiv = eitherDivide(b, den).bind()
            aDiv toT bDiv
        }.fix()


