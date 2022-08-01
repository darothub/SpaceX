package com.mindera.rocketscience.domain

interface Mapper<T, R> {
    fun toModel(value: T): R
}