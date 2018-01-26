package com.danielecampogiani.demo

import org.mockito.Mockito

inline fun <reified T> mockk() = Mockito.mock(T::class.java)!!

fun <T> any(): T {
    Mockito.any<T>()
    return uninitialized()
}

private fun <T> uninitialized(): T = null as T