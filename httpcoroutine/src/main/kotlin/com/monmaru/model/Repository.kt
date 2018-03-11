package com.monmaru.model

data class Repository (
        val owner: String,
        val title: String,
        val url: String,
        val description: String,
        val language: String,
        val star: String
)