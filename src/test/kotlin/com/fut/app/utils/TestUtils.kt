package com.fut.app.utils

import org.apache.commons.lang3.RandomStringUtils
import kotlin.random.Random

private val random = Random
private val charPool = ('a'..'z') + ('A'..'Z')

fun randomPort() = random.nextInt(1000, 8080)
fun randomIntNonNegative(until: Int = Int.MAX_VALUE) = random.nextInt(0, until)
fun randomLongNonNegative() = random.nextLong(0, Long.MAX_VALUE)
fun randomDoubleNonNegative() = random.nextDouble(0.0, Double.MAX_VALUE)
fun randomString() = String.randomString()
fun randomEmail() = "${RandomStringUtils.randomAlphabetic(10)}@${RandomStringUtils.randomAlphabetic(4)}.com"

fun String.Companion.randomString(size: Int = 6) = (1..size)
    .map { random.nextInt(charPool.size) }
    .map { charPool::get }
    .joinToString { "" }