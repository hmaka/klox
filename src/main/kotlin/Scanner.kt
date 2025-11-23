import TokenType.*
import org.root.Lox

class Scanner(val source: String, private val lox: Lox) {
    private val tokens = mutableListOf<Token>()
    private val sourceAsChars = source.toCharArray()

    private var start = 0
    private var current = 0
    private var line = 1

    fun scanTokens(): List<Token> {
        while (isAtEnd().not()) {
            start = current
            scanToken()
        }
        tokens.add(Token(TokenType.EOF, "", null, line))

        return tokens
    }

    private fun scanToken() {
        val c: Char = advance()
        when (c) {
            '(' -> addToken(LEFT_PAREN)
            ')' -> addToken(RIGHT_PAREN)
            '{' -> addToken(LEFT_BRACE)
            '}' -> addToken(RIGHT_BRACE)
            ',' -> addToken(COMMA)
            '.' -> addToken(DOT)
            '-' -> addToken(MINUS)
            '+' -> addToken(PLUS)
            ';' -> addToken(SEMICOLON)
            '*' -> addToken(STAR)
            '?' -> addToken(QUESTION)
            ':' -> addToken(COLON)
            '!' -> addToken(if (match('=')) BANG_EQUAL else BANG)
            '=' -> addToken(if (match('=')) EQUAL_EQUAL else EQUAL)
            '<' -> addToken(if (match('=')) LESS_EQUAL else LESS)
            '>' -> addToken(if (match('=')) GREATER_EQUAL else GREATER)
            '/' -> {
                // A comment goes until the end of the line.
                if (match('/')) while (isAtEnd().not() && peek() != '\n') advance()
                else addToken(SLASH)
            }

            ' ',
            '\r',
            '\t',
                -> also { }// do nothing
            '\n' -> line += 1
            '"' -> string()
            else if (c.isDigit()) -> number()
            else if (c.isLetter() || c == '_') -> identifier()
            else -> lox.error(line, "Unexpected character.")
        }
    }

    private fun identifier() {
        while (peek()?.isAlphaNumeric() == true) {
            advance()
        }

        val text = source.substring(start, current)
        val type = isReservedWord(text) ?: IDENTIFIER
        addToken(type)
    }

    fun Char.isAlphaNumeric(): Boolean {
        return this.isLetterOrDigit() || this == '_'
    }

    private fun isReservedWord(word: String): TokenType? {
        return when (word) {
            "and" -> AND
            "class" -> CLASS
            "else" -> ELSE
            "false" -> FALSE
            "for" -> FOR
            "fun" -> FUN
            "if" -> IF
            "nil" -> NIL
            "or" -> OR
            "return" -> RETURN
            "super" -> SUPER
            "this" -> THIS
            "true" -> TRUE
            "var" -> VAR
            "while" -> WHILE
            else -> null
        }
    }

    private fun string() {
        while (peek() != '"' && isAtEnd().not()) {
            if (peek() == '\n') line += 1
            advance()
        }
        // Advance past trailing '"'
        advance()

        addToken(STRING, source.substring(start + 1, current - 1))

    }

    private fun number() {
        while (peek()?.isDigit() == true) advance()

        if (peek() == '.' && peekNext()?.isDigit() == true) {
            advance()
            while (peek()?.isDigit() == true) advance()
        }

        addToken(NUMBER, source.substring(start, current))

    }

    private fun peekNext(): Char? {
        return sourceAsChars.getOrNull(current + 1)
    }

    private fun match(expected: Char): Boolean {
        if (isAtEnd()) return false
        if (sourceAsChars[current] != expected) return false;

        current += 1
        return true;
    }

    private fun peek(): Char? {
        return sourceAsChars.getOrNull(current)
    }

    private fun addToken(type: TokenType, literal: Any? = null) {
        val lexeme = source.substring(start,current)
        tokens.add(Token(type, lexeme, literal, line))
    }

    private fun advance(): Char {
        current += 1
        return sourceAsChars[current - 1]
    }


    private fun isAtEnd(): Boolean {
        return current >= sourceAsChars.size
    }

}
