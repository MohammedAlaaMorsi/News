package io.mohammedalaamorsi.nyt.util

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Test

class DispatchersProviderTest {

    @Test
    fun `should provide correct default dispatchers`() {
        // Given
        val dispatchersProvider = DispatchersProvider()

        // Then
        assertThat(dispatchersProvider.main).isEqualTo(Dispatchers.Main)
        assertThat(dispatchersProvider.immediate).isEqualTo(Dispatchers.Main.immediate)
        assertThat(dispatchersProvider.io).isEqualTo(Dispatchers.IO)
        assertThat(dispatchersProvider.default).isEqualTo(Dispatchers.Default)
        assertThat(dispatchersProvider.unconfined).isEqualTo(Dispatchers.Unconfined)
    }

    @Test
    fun `should allow overriding dispatchers in test implementation`() {
        // Given
        val testDispatcher = StandardTestDispatcher()
        val testDispatchersProvider = object : DispatchersProvider() {
            override val main = testDispatcher
            override val io = testDispatcher
            override val default = testDispatcher
        }

        // Then
        assertThat(testDispatchersProvider.main).isEqualTo(testDispatcher)
        assertThat(testDispatchersProvider.io).isEqualTo(testDispatcher)
        assertThat(testDispatchersProvider.default).isEqualTo(testDispatcher)
        assertThat(testDispatchersProvider.immediate).isEqualTo(Dispatchers.Main.immediate)
        assertThat(testDispatchersProvider.unconfined).isEqualTo(Dispatchers.Unconfined)
    }
}
