package com.monmaru.fp

import arrow.core.*
import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.typeclasses.binding
import java.util.*

fun randomlyNull(): Int? {
    val random = Random()
    return if (random.nextBoolean()) random.nextInt() else null
}

fun randomlyOption(): Option<Int> = randomlyNull().toOption()

fun randomlyEither(): Either<String, Int> = randomlyOption().toEither { "Nothing here" }

fun divide(num: Int, den: Int): Int? {
    return if (num % den != 0) {
        null
    } else {
        num / den
    }
}

fun optionDivide(num: Int, den: Int): Option<Int> = divide(num, den).toOption()

fun main(args: Array<String>) {
    Either.monad<String>().binding {
        val num1 = randomlyEither().bind()
        val num2 = randomlyEither().mapLeft { s -> s.toUpperCase() }.bind()
        num1 + num2
    }.fix().fold(::println, ::println)
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

fun flatMapEitherDivision(a: Int, b: Int, den: Int): Either<String, Tuple2<Int, Int>> {
    return eitherDivide(a, den).flatMap { aDiv ->
        eitherDivide(b, den).flatMap { bDiv ->
            Right(aDiv toT bDiv)
        }
    }
}

//
fun comprehensionEitherDivision(a: Int, b: Int, den: Int): Either<String, Tuple2<Int, Int>> {
    return Either.monad<String>().binding {
        val aDiv = eitherDivide(a, den).bind()
        val bDiv = eitherDivide(b, den).bind()
        aDiv toT bDiv
    }.fix()
}

/*fun main(args: Array<String>) {
	eitherDivision(3, 2, 4).fold(::println, ::println)
}*/



