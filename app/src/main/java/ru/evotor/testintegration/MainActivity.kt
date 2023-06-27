package ru.evotor.testintegration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.evotor.testintegration.presentation.login.LoginFragment
import ru.evotor.testintegration.repository.RepositoryModule
import ru.evotor.testintegration.utils.openFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RepositoryModule.init(applicationContext)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            openFragment(LoginFragment(), false)
        }
    }
}