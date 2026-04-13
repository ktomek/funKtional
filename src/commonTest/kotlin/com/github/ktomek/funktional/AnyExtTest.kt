package com.github.ktomek.funktional

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class AnyExtTest {

    @Test
    fun `GIVEN Any of correct type WHEN ofType called THEN casts successfully`() {
        val any: Any = "Test"
        val s: String = any.asType()

        assertEquals("Test", s)
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

        assertEquals("OK", result)
    }

    @Test
    fun `GIVEN Any of wrong type WHEN ofTypeOrNull called THEN returns null`() {
        val any: Any = 123

        assertNull(any.asTypeOrNull<String>())
    }
}
