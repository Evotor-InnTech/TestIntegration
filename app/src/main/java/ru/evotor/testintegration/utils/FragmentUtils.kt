package ru.evotor.testintegration.utils

import android.os.Bundle
import androidx.fragment.app.Fragment

fun Fragment.listenResult(key: String, action: (Bundle) -> Unit) {
    childFragmentManager.setFragmentResultListener(key, this) { _, result ->
        action(result)
    }
}