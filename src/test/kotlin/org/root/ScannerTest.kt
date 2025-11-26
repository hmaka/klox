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

    @Test
    fun testOperatorsAndPunctuation() {
        val lox = Lox()
        val source = "(){};,+-*!===<=>=!=<>./"
        val scanner = Scanner(source, lox)
        val tokens = scanner.scanTokens()

        val expectedTokens = listOf(
            Token(TokenType.LEFT_PAREN, "(", null, 1),
            Token(TokenType.RIGHT_PAREN, ")", null, 1),
            Token(TokenType.LEFT_BRACE, "{", null, 1),
            Token(TokenType.RIGHT_BRACE, "}", null, 1),
            Token(TokenType.SEMICOLON, ";", null, 1),
            Token(TokenType.COMMA, ",", null, 1),
            Token(TokenType.PLUS, "+", null, 1),
            Token(TokenType.MINUS, "-", null, 1),
            Token(TokenType.STAR, "*", null, 1),
            Token(TokenType.BANG_EQUAL, "!=", null, 1),
            Token(TokenType.EQUAL_EQUAL, "==", null, 1),
            Token(TokenType.LESS_EQUAL, "<=", null, 1),
            Token(TokenType.GREATER_EQUAL, ">=", null, 1),
            Token(TokenType.BANG_EQUAL, "!=", null, 1),
            Token(TokenType.LESS, "<", null, 1),
            Token(TokenType.GREATER, ">", null, 1),
            Token(TokenType.DOT, ".", null, 1),
            Token(TokenType.SLASH, "/", null, 1),
            Token(TokenType.EOF, "", null, 1)
        )

        assertTokensMatch(expectedTokens, tokens)
    }

    @Test
    fun testEmptyStringLiteral() {
        val lox = Lox()
        val source = "\"\""
        val scanner = Scanner(source, lox)
        val tokens = scanner.scanTokens()

        val expectedTokens = listOf(
            Token(TokenType.STRING, "\"\"", "", 1),
            Token(TokenType.EOF, "", null, 1)
        )

        assertTokensMatch(expectedTokens, tokens)
    }

    @Test
    fun testNonEmptyStringLiteral() {
        val lox = Lox()
        val source = "\"string\""
        val scanner = Scanner(source, lox)
        val tokens = scanner.scanTokens()

        val expectedTokens = listOf(
            Token(TokenType.STRING, "\"string\"", "string", 1),
            Token(TokenType.EOF, "", null, 1)
        )

        assertTokensMatch(expectedTokens, tokens)
    }

    @Test
    fun testWhitespaceAndNewlines() {
        val lox = Lox()
        val source = "space    tabs\nnewlines\n\n\n\nend"
        val scanner = Scanner(source, lox)
        val tokens = scanner.scanTokens()

        val expectedTokens = listOf(
            Token(TokenType.IDENTIFIER, "space", null, 1),
            Token(TokenType.IDENTIFIER, "tabs", null, 1),
            Token(TokenType.IDENTIFIER, "newlines", null, 2),
            Token(TokenType.IDENTIFIER, "end", null, 6),
            Token(TokenType.EOF, "", null, 6)
        )

        assertTokensMatch(expectedTokens, tokens)
    }

    @Test
    fun testNumberLiterals() {
        val lox = Lox()
        val source = "123\n123.456\n.456\n123."
        val scanner = Scanner(source, lox)
        val tokens = scanner.scanTokens()

        val expectedTokens = listOf(
            Token(TokenType.NUMBER, "123", 123.0, 1),
            Token(TokenType.NUMBER, "123.456", 123.456, 2),
            Token(TokenType.DOT, ".", null, 3),
            Token(TokenType.NUMBER, "456", 456.0, 3),
            Token(TokenType.NUMBER, "123", 123.0, 4),
            Token(TokenType.DOT, ".", null, 4),
            Token(TokenType.EOF, "", null, 4)
        )

        assertTokensMatch(expectedTokens, tokens)
    }

    @Test
    fun testIdentifiers() {
        val lox = Lox()
        val source = "andy formless fo _ _123 _abc ab123\nabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_"
        val scanner = Scanner(source, lox)
        val tokens = scanner.scanTokens()

        val expectedTokens = listOf(
            Token(TokenType.IDENTIFIER, "andy", null, 1),
            Token(TokenType.IDENTIFIER, "formless", null, 1),
            Token(TokenType.IDENTIFIER, "fo", null, 1),
            Token(TokenType.IDENTIFIER, "_", null, 1),
            Token(TokenType.IDENTIFIER, "_123", null, 1),
            Token(TokenType.IDENTIFIER, "_abc", null, 1),
            Token(TokenType.IDENTIFIER, "ab123", null, 1),
            Token(TokenType.IDENTIFIER, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_", null, 2),
            Token(TokenType.EOF, "", null, 2)
        )

        assertTokensMatch(expectedTokens, tokens)
    }

    @Test
    fun testKeywords() {
        val lox = Lox()
        val source = "and class else false for fun if nil or return super this true var while"
        val scanner = Scanner(source, lox)
        val tokens = scanner.scanTokens()

        val expectedTokens = listOf(
            Token(TokenType.AND, "and", null, 1),
            Token(TokenType.CLASS, "class", null, 1),
            Token(TokenType.ELSE, "else", null, 1),
            Token(TokenType.FALSE, "false", null, 1),
            Token(TokenType.FOR, "for", null, 1),
            Token(TokenType.FUN, "fun", null, 1),
            Token(TokenType.IF, "if", null, 1),
            Token(TokenType.NIL, "nil", null, 1),
            Token(TokenType.OR, "or", null, 1),
            Token(TokenType.RETURN, "return", null, 1),
            Token(TokenType.SUPER, "super", null, 1),
            Token(TokenType.THIS, "this", null, 1),
            Token(TokenType.TRUE, "true", null, 1),
            Token(TokenType.VAR, "var", null, 1),
            Token(TokenType.WHILE, "while", null, 1),
            Token(TokenType.EOF, "", null, 1)
        )

        assertTokensMatch(expectedTokens, tokens)
    }

    private fun assertTokensMatch(expected: List<Token>, actual: List<Token>) {
        assertEquals(expected.size, actual.size, "Token count mismatch")
        expected.forEachIndexed {
            index,
            expectedToken ->
            val actualToken = actual[index]
            assertEquals(expectedToken.type, actualToken.type, "Token type mismatch at index $index")
            assertEquals(expectedToken.lexeme, actualToken.lexeme, "Lexeme mismatch at index $index")
            assertEquals(expectedToken.literal, actualToken.literal, "Literal mismatch at index $index")
            assertEquals(expectedToken.line, actualToken.line, "Line mismatch at index $index")
        }
    }
}