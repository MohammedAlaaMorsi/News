package io.mohammedalaamorsi.nyt.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

open class DispatchersProvider {
    open val immediate: CoroutineDispatcher get() = Dispatchers.Main.immediate
    open val main: CoroutineDispatcher get() = Dispatchers.Main
    open val io: CoroutineDispatcher get() = Dispatchers.IO
    open val default: CoroutineDispatcher get() = Dispatchers.Default
    open val unconfined: CoroutineDispatcher get() = Dispatchers.Unconfined
}
