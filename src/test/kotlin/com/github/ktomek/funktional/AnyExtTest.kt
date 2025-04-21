package com.github.ktomek.funktional

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class AnyExtTest {

    // ofType & ofTypeOrNull
    @Test
    fun `GIVEN Any of correct type WHEN ofType called THEN casts successfully`() {
        val any: Any = "Test"
        val s: String = any.asType()

        Assertions.assertEquals("Test", s)
    }

    @Test
    fun `GIVEN Any of wrong type WHEN ofType called THEN throws ClassCastException`() {
        val any: Any = 123

        assertFailsWith<ClassCastException> { any.asType<String>() }
    }

    @Test
    fun `GIVEN Any of correct type WHEN ofTypeOrNull called THEN casts successfully`() {
        val any: Any = "OK"

        val result: String? = any.asTypeOrNull()

        Assertions.assertEquals("OK", result)
    }

    @Test
    fun `GIVEN Any of wrong type WHEN ofTypeOrNull called THEN returns null`() {
        val any: Any = 123

        Assertions.assertNull(any.asTypeOrNull<String>())
    }
}
