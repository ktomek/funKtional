package com.github.ktomek.funktional

import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import kotlin.test.junit5.JUnit5Asserter.fail

class TExtTest {

    // orDefault (sync)
    @Test
    fun `GIVEN non-null string WHEN orDefault called THEN returns original string`() {
        val value: String? = "Hello"

        val result = value.orDefault { "Default" }

        assertEquals("Hello", result)
    }

    @Test
    fun `GIVEN null string WHEN orDefault called THEN returns default string`() {
        val value: String? = null

        val result = value.orDefault { "Default" }

        assertEquals("Default", result)
    }

    // orDefault (suspend)
    @Test
    fun `GIVEN non-null string WHEN orDefault suspend called THEN returns original string`() = runTest {
        val value: String? = "Hi"

        val result = value.orDefaultCo {
            delay(10)
            fail("should not be called")
        }
        assertEquals("Hi", result)
    }

    @Test
    fun `GIVEN null string WHEN orDefault suspend called THEN returns default string`() = runTest {
        val value: String? = null

        val result = value.orDefaultCo {
            delay(10)
            "Default"
        }

        assertEquals("Default", result)
    }

    // onNull (sync)
    @Test
    fun `GIVEN null string WHEN onNull called THEN action is invoked and null returned`() {
        var called = false
        val value: String? = null

        val result = value.onNull { called = true }

        assertTrue(called)
        assertNull(result)
    }

    @Test
    fun `GIVEN non-null string WHEN onNull called THEN action is not invoked and original returned`() {
        val value: String? = "Hello"

        val result = value.onNull { fail("should not be called") }

        assertEquals("Hello", result)
    }

    // onNullCo (suspend)
    @Test
    fun `GIVEN null string WHEN onNull suspend called THEN action is invoked and null returned`() = runTest {
        var called = false
        val value: String? = null

        val result = value.onNullCo { called = true }

        assertTrue(called)
        assertNull(result)
    }

    @Test
    fun `GIVEN non-null string WHEN onNull suspend called THEN action is not invoked and original returned`() =
        runTest {
            val value: String? = "Hi"

            val result = value.onNullCo { fail() }

            assertEquals("Hi", result)
        }

    // letCo (suspend)
    @Test
    fun `GIVEN value WHEN let suspend called THEN maps value correctly`() = runTest {
        val result = "abc".letCo { it.length }

        assertEquals(3, result)
    }

    // match (sync)
    @Test
    fun `GIVEN non-null WHEN match sync called THEN calls not-null lambda`() {
        val result = "X".match({ "NN" }, { fail("should not be called") })

        assertEquals("NN", result)
    }

    @Test
    fun `GIVEN null WHEN match sync called THEN calls null lambda`() {
        val value: String? = null

        val result = value.match({ fail("should not be called") }, { "NF" })

        assertEquals("NF", result)
    }

    // match (suspend)
    @Test
    fun `GIVEN non-null WHEN match suspend called THEN calls not-null suspend lambda`() = runTest {
        val result = "Y".matchCo(
            some = {
                delay(5)
                "A"
            },
            none = {
                delay(5)
                fail("should not be called")
            }
        )

        assertEquals("A", result)
    }

    @Test
    fun `GIVEN null WHEN match suspend called THEN calls null suspend lambda`() = runTest {
        val value: Int? = null
        val result = value
            .matchCo(
                some = {
                    delay(5)
                    fail("should not be called")
                },
                none = {
                    delay(5)
                    2
                }
            )

        assertEquals(2, result)
    }

    // matchAction (sync)
    @Test
    fun `GIVEN non-null WHEN matchAction sync called THEN executes not-null action and returns original`() {
        var calledNotNull = false

        val result = "Z".matchAction(
            some = { calledNotNull = true },
            none = { fail("should not be called") }
        )

        assertTrue(calledNotNull)
        assertEquals("Z", result)
    }

    @Test
    fun `GIVEN null WHEN matchAction sync called THEN executes null action and returns null`() {
        var calledNull = false
        val value: String? = null

        val result = value.matchAction(
            some = { fail("should not be called") },
            none = { calledNull = true }
        )

        assertTrue(calledNull)
        assertNull(result)
    }

    // matchAction (suspend)
    @Test
    fun `GIVEN non-null WHEN matchAction suspend called THEN executes not-null suspend action and returns original`() =
        runTest {
            var called = false

            val result = "W".matchCoAction(
                some = {
                    delay(1)
                    called = true
                },
                none = { fail("should not be called") }
            )

            assertTrue(called)
            assertEquals("W", result)
        }

    @Test
    fun `GIVEN null WHEN matchAction suspend called THEN executes null suspend action and returns null`() = runTest {
        var called = false
        val value: Int? = null

        val result = value.matchCoAction(
            some = { fail("should not be called") },
            none = {
                delay(1)
                called = true
            }
        )

        assertTrue(called)
        assertNull(result)
    }

    @Test
    fun `GIVEN non-null receiver and non-null other WHEN two-arg extension lift called THEN returns mapping result`() {
        val a: Int? = 4
        val b: Int? = 5

        val result = a.lift(b) { x, y -> x + y }

        assertEquals(9, result)
    }

    @Test
    fun `GIVEN null receiver WHEN two-arg extension lift called THEN returns null`() {
        val a: Int? = null
        val b: Int? = 5

        val result = a.lift(b) { x, y -> x + y }

        assertNull(result)
    }

    @Test
    fun `GIVEN null other WHEN two-arg extension lift called THEN returns null`() {
        val a: Int? = 4
        val b: Int? = null

        val result = a.lift(b) { x, y -> x + y }

        assertNull(result)
    }
}
