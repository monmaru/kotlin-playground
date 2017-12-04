package sample

fun main(args: Array<String>) {
    println("Hello, world!")
    val name: String by lazy {
        println("初期化！")
        "world"
    }
    println("Hello, $name!")
}

fun whenFunc() {
    val ret1 = when(2) {
        1 -> "one"
        in 2..5 -> "two..five"
        else -> "unknown"
    }
    println(ret1)

    var target: Any = 2
    val ret2 = when(target) {
        is Double -> "Double"
        is Int -> "Int"
        else -> "Unknown"
    }
    println(ret2)

    val score = 70
    val grade = when {
        90 <= score -> 'A'
        80 <= score -> 'B'
        70 <= score -> 'C'
        60 <= score -> 'D'
        else        -> 'F'
    }
    println(grade)
}

// Build:
// kotlinc Main.kt -include-runtime -d HelloWorld.jar
// Run:
// java -jar ./HelloWorld.jar