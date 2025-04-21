package com.github.ktomek.funktional

/**
 * Casts the receiver to type [R], or throws a [ClassCastException] if it is not of that type.
 *
 * Example:
 * ```
 * val any: Any = "hello"
 * val str: String = any.asType()
 * // str == "hello"
 * ```
 */
inline fun <reified R> Any.asType(): R = this as R

/**
 * Safely casts the receiver to type [R], returning null if it is not of that type.
 *
 * Example:
 * ```
 * val any: Any = 123
 * val str: String? = any.asTypeOrNull<String>()
 * // str == null
 * ```
 */
inline fun <reified R> Any.asTypeOrNull(): R? = this as? R
