package io.github.staakk.progresstracker

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.*
import java.util.concurrent.ArrayBlockingQueue

class ATest {

    @Test
    fun test() {
        val a = arrayOf(1, 3, 5)

        assertEquals(8, solution(a))
    }

    fun solution(input: Array<Int>) =
        input.flatMap { x ->
            input.map { y ->
                f(x, y)
            }
        }.sum()

    fun f(x: Int, y: Int): Int = (x xor y).countOneBits()
}

class LruCache(private val capacity: Int) {

    private val elements = mutableMapOf<Int, Int>()

    private val keysQueue: Queue<Int> = LinkedList()

    operator fun set(key: Int, value: Int) {
        elements[key] = value
        keysQueue.add(key)

        if (keysQueue.size > capacity) {
            val removedKey = keysQueue.remove()
            elements.remove(removedKey)
        }
    }

    operator fun get(key: Int): Int {
        return elements[key] ?: -1
    }
}