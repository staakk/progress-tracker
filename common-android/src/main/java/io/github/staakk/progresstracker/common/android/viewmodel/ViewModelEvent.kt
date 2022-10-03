package io.github.staakk.progresstracker.common.android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import timber.log.Timber

typealias Action<T> = suspend T.() -> Unit

abstract class ViewModelEvent<T : ViewModel>(val action: Action<T>)

fun <T : ViewModel> T.viewModelDispatch(event: ViewModelEvent<T>) {
    Timber.v("dispatch($event)")
    with(event) {
        viewModelScope.launch {
            action()
        }
    }
}