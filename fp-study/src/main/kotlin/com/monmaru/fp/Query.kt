package com.monmaru.fp

import arrow.aql.instances.list.select.query
import arrow.aql.instances.list.select.value
import arrow.aql.instances.listk.select.select
import arrow.aql.instances.option.select.query
import arrow.aql.instances.option.select.select
import arrow.aql.instances.option.select.value
import arrow.aql.instances.sequence.select.query
import arrow.aql.instances.sequence.select.value
import arrow.aql.instances.sequencek.select.select
import arrow.core.None
import arrow.core.Option
import arrow.core.Some

fun doQuery() {
    println("----- Query Language -----")

    println("[select over List]")
    listOf(1, 2, 3).query {
        select { this + 1 }
    }.value().forEach(::println)

    println("[select over Option]")
    val opt = Option(1).query {
        select { this * 10 }
    }.value()

    when (opt) {
        is Some -> println(opt.t)
        is None -> println("None")
    }

    println("[select over Sequence]")
    sequenceOf(1, 2, 3, 4).query {
        select { this * 10 }
    }.value().toList().forEach(::println)

    println("----- Query Language -----")
}