package com.monmaru.fp

import arrow.core.*
import arrow.typeclasses.binding
import java.util.*

fun randomlyNull(): Int? {
    val random = Random()
    return if (random.nextBoolean()) random.nextInt() else null
}

fun randomlyOption(): Option<Int> = randomlyNull().toOption()

fun divide(num: Int, den: Int): Int? = if (num % den != 0) null else num / den

fun division(a: Int, b: Int, den: Int): Pair<Int, Int>? {
    val aDiv = divide(a, den)
    return when (aDiv) {
        is Int -> {
            val bDiv = divide(b, den)
            when (bDiv) {
                is Int -> aDiv to bDiv
                else -> null
            }
        }
        else -> null
    }
}

fun optionDivide(num: Int, den: Int): Option<Int> = divide(num, den).toOption()

fun optionDivision(a: Int, b: Int, den: Int): Option<Pair<Int, Int>> {
    val aDiv = optionDivide(a, den)
    return when (aDiv) {
        is Some -> {
            val bDiv = optionDivide(b, den)
            when (bDiv) {
                is Some -> Some(aDiv.t to bDiv.t)
                else -> None
            }
        }
        else -> None
    }
}

fun flatMapDivision(a: Int, b: Int, den: Int): Option<Pair<Int, Int>> {
    return optionDivide(a, den).flatMap { aDiv: Int ->
        optionDivide(b, den).flatMap { bDiv: Int ->
            Some(aDiv to bDiv)
        }
    }
}

fun comprehensionDivision(a: Int, b: Int, den: Int): Option<Pair<Int, Int>> {
    return Option.monad().binding {
        val aDiv: Int = optionDivide(a, den).bind()
        val bDiv: Int = optionDivide(b, den).bind()
        aDiv to bDiv
    }.fix()
}

fun main(args: Array<String>) {
    println("division ${division(4, 2, 2)}")
    println("option division ${optionDivision(4, 2, 2)}")
    println("flatMap division ${flatMapDivision(4, 2, 2)}")
    println("comprehension division ${comprehensionDivision(4, 2, 2)}")
}


/*
fun main(args: Array<String>) {
	Option.functor().map(1.toOption()) { i -> i + 1 }.ev()

	Option.applicative().ap(1.toOption(), Some { i: Int -> i + 1 }).ev()

	Option.monad().flatMap(1.toOption()) { i -> Some(i + 1) }
}*/
