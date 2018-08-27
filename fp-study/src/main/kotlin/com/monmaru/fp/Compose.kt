package com.monmaru.fp

import java.io.BufferedReader
import java.io.FileReader
import java.util.stream.Collectors;

val source = { name: String -> "data/$name" }

val allLines = { path: String ->
    val reader = BufferedReader(FileReader(path))
    reader.use { it.lines().collect(Collectors.toList()); }
}

val findMatches = { input: List<String> ->
    val regex = "[A-Z]{2}[0-9]{2}".toRegex()
    input.filter(regex::matches)
}