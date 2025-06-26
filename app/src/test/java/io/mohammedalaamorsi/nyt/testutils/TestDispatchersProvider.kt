package io.mohammedalaamorsi.nyt.testutils

import io.mohammedalaamorsi.nyt.util.DispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher

/**
 * Test implementation of DispatchersProvider that uses StandardTestDispatcher
 * for all dispatchers to enable predictable testing behavior
 */
class TestDispatchersProvider(
    testDispatcher: CoroutineDispatcher = StandardTestDispatcher()
) : DispatchersProvider() {
    
    override val main: CoroutineDispatcher = testDispatcher
    override val io: CoroutineDispatcher = testDispatcher
    override val default: CoroutineDispatcher = testDispatcher
    override val unconfined: CoroutineDispatcher = testDispatcher
    override val immediate: CoroutineDispatcher = testDispatcher
}
