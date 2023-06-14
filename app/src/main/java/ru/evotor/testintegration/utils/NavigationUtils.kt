package ru.evotor.testintegration.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import ru.evotor.testintegration.R

fun FragmentActivity.openFragment(fragment: Fragment, addToBackStack: Boolean = true) {
    val transaction = supportFragmentManager.beginTransaction()
    if (addToBackStack) {
        transaction.addToBackStack(null)
    }
    transaction
        .replace(R.id.container, fragment)
        .commit()
}

fun Fragment.openFragment(fragment: Fragment, addToBackStack: Boolean = true) {
    requireActivity().openFragment(fragment, addToBackStack)
}