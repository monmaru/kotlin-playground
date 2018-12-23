package com.monmaru.fp

import arrow.core.andThen
import arrow.core.compose
import arrow.syntax.function.forwardCompose
import java.io.BufferedReader
import java.io.FileReader
import java.util.stream.Collectors

fun doComponse() {
    println(findMatches(allLines(source("grepInputText"))))

    val composed1 = findMatches compose allLines compose source
    println(composed1("grepInputText"))

    val composed2 = source forwardCompose allLines forwardCompose findMatches
    println(composed2("grepInputText"))

    val composed3 = source andThen allLines andThen findMatches
    println(composed3("grepInputText"))
}

val source = { name: String -> "data/$name" }

val allLines = { path: String ->
    val reader = BufferedReader(FileReader(path))
    reader.use { it.lines().collect(Collectors.toList()); }
}

val findMatches = { input: List<String> ->
    val regex = "[A-Z]{2}[0-9]{2}".toRegex()
        input.filter(regex::matches)
}