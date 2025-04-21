package com.github.ktomek.funktional

/**
 * Applies [f] to [t1] and [t2] if both are non-null, returning the result, or null otherwise.
 *
 * Example:
 * ```
 * val concatenated: String? = lift("foo", "bar") { s1, s2 -> s1 + s2 }
 * // concatenated == "foobar"
 * ```
 */
fun <T1, T2, R> lift(t1: T1?, t2: T2?, f: (T1, T2) -> R): R? =
    if (t1 != null && t2 != null) f(t1, t2) else null

/**
 * Applies [f] to [t1], [t2], and [t3] if all are non-null, returning the result, or null otherwise.
 *
 * Example:
 * ```
 * val sum: Int? = lift(1, 2, 3) { a, b, c -> a + b + c }
 * // sum == 6
 * ```
 */
fun <T1, T2, T3, R> lift(t1: T1?, t2: T2?, t3: T3?, f: (T1, T2, T3) -> R): R? =
    if (t1 != null && t2 != null && t3 != null) f(t1, t2, t3) else null
