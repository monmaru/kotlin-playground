package com.monmaru.fp

import arrow.core.*
import arrow.data.*
import arrow.typeclasses.binding

fun main(args: Array<String>) {
    doOption()
    doEither()
    doState()
}

fun doOption() {
    println("----- Option Monad -----")
    println("division ${division(4, 2, 2)}")
    println("option division ${optionDivision(4, 2, 2)}")
    println("flatMap division ${flatMapDivision(4, 2, 2)}")
    println("comprehension division ${comprehensionDivision(4, 2, 2)}")
    println("----- Option Monad -----")
}

fun doEither() {
    println("----- Either Monad -----")
    Either.monad<String>().binding {
        val num1 = randomlyEither().bind()
        val num2 = randomlyEither().mapLeft { s -> s.toUpperCase() }.bind()
        num1 + num2
    }.fix().fold(::println, ::println)

    flatMapEitherDivision(3, 2, 4).fold(::println, ::println)
    eitherDivision(3, 2, 4).fold(::println, ::println)
    comprehensionEitherDivision(3, 2, 4).fold(::println, ::println)
    println("----- Either Monad -----")
}

fun doState() {
    println("----- State Monad -----")
    val (history: PriceLog, price: Double) = calculatePrice(100.0, 2.0).run(mutableListOf("Init" toT 15.0))
    println("Price: $price")
    println("::History::")
    history
            .map { (text, value) -> "$text\t|\t$value" }
            .forEach(::println)

    val bigPrice: Double = calculatePrice(100.0, 2.0).runA(mutableListOf("Init" toT 1000.0))
    println("bigPrice = $bigPrice")
    println("----- State Monad -----")
}