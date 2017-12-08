package fizzbuzz

fun main(args: Array<String>) {
    for (i in 1..100) {
        val num = when {
            i % 15 == 0 -> "FizzBuzz"
            i % 3 == 0  -> "Fizz"
            i % 5 == 0  -> "Buzz"
            else        -> "$i"
        }
        println(num)
    }
}
