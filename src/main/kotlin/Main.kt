package org.example

fun main(args: Array<String>) {
    println("Arguments: ${args.joinToString()}")

    when (args.size) {
        0 -> println("starting interactive shell")
        1 -> println("starting program from file")
        else -> error("Usage: <file-name> | ")
    }
//    while (true){
//        val input = readln()
//        println("ouput: " + input)
//    }

}