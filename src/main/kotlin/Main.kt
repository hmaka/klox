package org.root

import Scanner
import java.io.File
import java.io.IO.readln
import kotlin.system.exitProcess

class Lox {
    private var hadError = false

    fun main(args: Array<String>) {
        when (args.size) {
            0 -> promptLoop()
            1 -> runFile(args.first())
            else -> error("Usage: <file-name> | ")
        }
        if (hadError) exitProcess(65)
    }

    private fun runFile(path: String) {
        val resource = Lox::class.java.getResource(path)
        val input = resource?.let { File(it.toURI()).readText() } ?: error("Input File Not Found")
        run(input)
    }

    fun promptLoop() {
        println("starting interactive shell")
        while (true) {
            val input = readln("> ")
            run(input)
            hadError = false
        }
    }

    private fun run(source: String) {
        val scanner = Scanner(source, this)
        val tokens = scanner.scanTokens().forEach { println(it) }
    }

    fun error(line: Int, message: String) {
        report(line, "", message)
        hadError = true
    }

    private fun report(line: Int, where: String, message: String) {
        System.err.println("[line $line] Error $where : $message")
    }

}