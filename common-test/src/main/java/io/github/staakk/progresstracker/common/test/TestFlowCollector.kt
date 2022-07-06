package io.github.staakk.progresstracker.common.test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope

@OptIn(ExperimentalCoroutinesApi::class)
fun <E> TestScope.collectAll(flow: Flow<E>, dispatcher: TestDispatcher): Pair<List<E>, Job> {
    val elements = mutableListOf<E>()
    val collectJob = launch(dispatcher) { flow.toList(elements) }
    return elements to collectJob
}
