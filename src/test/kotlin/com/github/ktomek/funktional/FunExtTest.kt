package com.github.ktomek.funktional

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FunExtTest {

    // –– Extension three‑arg lift(T1?.lift(T2?, T3?, f)) ––

    @Test
    fun `GIVEN three non-null inputs WHEN three-arg extension lift called THEN returns mapping result`() {
        val a: Int? = 1
        val b: Int? = 2
        val c: Int? = 3

        val result = a.lift(b, c) { x, y, z -> x * y * z }

        Assertions.assertEquals(6, result)
    }

    @Test
    fun `GIVEN null receiver WHEN three-arg extension lift called THEN returns null`() {
        val a: Int? = null
        val b: Int? = 2
        val c: Int? = 3

        val result = a.lift(b, c) { x, y, z -> x * y * z }

        Assertions.assertNull(result)
    }

    @Test
    fun `GIVEN one null among others WHEN three-arg extension lift called THEN returns null`() {
        val a: Int? = 1
        val b: Int? = null
        val c: Int? = 3

        val result = a.lift(b, c) { x, y, z -> x * y * z }

        Assertions.assertNull(result)
    }

    // –– Top‑level two‑arg lift(t1, t2, f) ––

    @Test
    fun `GIVEN non-null parameters WHEN top-level two-arg lift called THEN returns mapping result`() {
        val x: String? = "foo"
        val y: String? = "bar"

        val result = lift(x, y) { s1, s2 -> s1 + s2 }

        Assertions.assertEquals("foobar", result)
    }

    @Test
    fun `GIVEN one null parameter WHEN top-level two-arg lift called THEN returns null`() {
        val x: String? = null
        val y: String? = "bar"

        val result = lift(x, y) { s1, s2 -> s1 + s2 }

        Assertions.assertNull(result)
    }

    // –– Top‑level three‑arg lift(t1, t2, t3, f) ––

    @Test
    fun `GIVEN non-null parameters WHEN top-level three-arg lift called THEN returns mapping result`() {
        val x: Int? = 2
        val y: Int? = 3
        val z: Int? = 4

        val result = lift(x, y, z) { a, b, c -> a + b + c }

        Assertions.assertEquals(9, result)
    }

    @Test
    fun `GIVEN one null among parameters WHEN top-level three-arg lift called THEN returns null`() {
        val x: Int? = 2
        val y: Int? = null
        val z: Int? = 4

        val result = lift(x, y, z) { a, b, c -> a + b + c }

        Assertions.assertNull(result)
    }
}
