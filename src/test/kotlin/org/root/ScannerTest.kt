package org.root

import Scanner
import Token
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class ScannerTest {

    @Test
    fun testEmptySource() {
        val lox = Lox()
        val scanner = Scanner("", lox)
        val tokens = scanner.scanTokens()
        val expectedTokens = listOf(
            Token(TokenType.EOF, "", null, 1)
        )
        assertTokensMatch(expectedTokens, tokens)
    }

}