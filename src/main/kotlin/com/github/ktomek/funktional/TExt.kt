package com.github.ktomek.funktional

/**
* Performs the given suspending [action] on the receiver and returns its result.
*
* Example:
* ```
* // within a coroutine scope
* val length: Int = "Hello".letCo { it.length }
* // length == 5
* ```
*/
suspend inline fun <T, reified R> T.letCo(action: suspend (T) -> R): R = action(this)

/**
 * Returns the receiver if non-null, or the result of [action] otherwise.
 *
 * Example:
 * ```
 * val value: String? = null
 * val result: String = value.orDefault { "default" }
 * // result == "default"
 * ```
 */
fun <T> T?.orDefault(action: () -> T): T = this ?: action()

/**
 * Returns the receiver if non-null, or the result of suspending [action] otherwise.
 *
 * Example:
 * ```
 * runBlocking {
 *   val value: String? = null
 *   val result: String = value.orDefaultCo { delay(100); "default" }
 *   // result == "default"
 * }
 * ```
 */
suspend fun <T> T?.orDefaultCo(action: suspend () -> T): T = this ?: action()

/**
 * Executes [action] if the receiver is null, then returns the receiver.
 *
 * Example:
 * ```
 * var sideEffect = false
 * val result: String? = null.onNull { sideEffect = true; "ignored" }
 * // sideEffect == true; result == null
 * ```
 */
fun <T> T?.onNull(action: () -> T): T? {
    if (this == null) action()
    return this
}

/**
 * Executes suspending [action] if the receiver is null, then returns the receiver.
 *
 * Example:
 * ```
 * runBlocking {
 *   var sideEffect = false
 *   val result: String? = null.onNullCo { delay(100); sideEffect = true; "ignored" }
 *   // sideEffect == true; result == null
 * }
 * ```
 */
suspend fun <T> T?.onNullCo(action: suspend () -> T): T? {
    if (this == null) action()
    return this
}

/**
 * Returns result of [some] if receiver is non-null, otherwise result of [none].
 *
 * Example:
 * ```
 * val result = "x".match({ "notNull" }, { "isNull" })
 * // result == "notNull"
 * ```
 */
fun <T, R> T?.match(some: (T) -> R, none: () -> R): R =
    if (this != null) some(this) else none()

/**
 * Returns result of suspending [some] if receiver is non-null, otherwise result of [none].
 *
 * Example:
 * ```
 * runBlocking {
 *   val result = "y".matchCo({ delay(10); "notNull" }, { delay(10); "isNull" })
 *   // result == "notNull"
 * }
 * ```
 */
suspend fun <T, R> T?.matchCo(some: suspend (T) -> R, none: suspend () -> R): R =
    if (this != null) some(this) else none()

/**
 * Executes [some] if receiver is non-null, otherwise [none], then returns the receiver.
 *
 * Example:
 * ```
 * var called = false
 * val result: String? = "z".matchAction({ called = true }, { /* no-op */ })
 * // called == true; result == "z"
 * ```
 */
fun <T> T?.matchAction(some: (T) -> Unit, none: () -> Unit): T? {
    if (this != null) some(this) else none()
    return this
}

/**
 * Executes suspending [some] if receiver is non-null, otherwise [none], then returns the receiver.
 *
 * Example:
 * ```
 * runBlocking {
 *   var called = false
 *   val result: String? = "w".matchCoAction({ delay(1); called = true }, { delay(1) })
 *   // called == true; result == "w"
 * }
 * ```
 */
suspend fun <T> T?.matchCoAction(some: suspend (T) -> Unit, none: suspend () -> Unit): T? {
    if (this != null) some(this) else none()
    return this
}

/**
 * Extension overload of [lift] for two nullable values:
 * applies [f] if both receiver and [t2] are non-null, or returns null.
 *
 * Example:
 * ```
 * val sum: Int? = 2.lift(3) { a, b -> a + b }
 * // sum == 5
 * ```
 */
@JvmName("liftExt2")
fun <T1, T2, R> T1?.lift(t2: T2?, f: (T1, T2) -> R): R? = lift(this, t2, f)

/**
 * Extension overload of [lift] for three nullable values: applies [f] if all are non-null, or returns null.
 *
 * Example:
 * ```
 * val prod: Int? = 2.lift(3, 4) { a, b, c -> a * b * c }
 * // prod == 24
 * ```
 */
@JvmName("liftExt3")
fun <T1, T2, T3, R> T1?.lift(t2: T2?, t3: T3?, f: (T1, T2, T3) -> R): R? = lift(this, t2, t3, f)
