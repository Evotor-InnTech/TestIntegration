package ru.evotor.testintegration.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_login.*
import ru.evotor.integration.entities.credentials.Credentials
import ru.evotor.testintegration.R
import ru.evotor.testintegration.presentation.receipt.CreateReceiptFragment
import ru.evotor.testintegration.utils.openFragment
import ru.evotor.testintegration.utils.toStringIfNotEmpty

class LoginFragment : Fragment() {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        observeState()
    }

    private fun initListeners() {
        loginEditText.addTextChangedListener { editable ->
            viewModel.onLoginChanged(editable.toString())
        }
        passwordEditText.addTextChangedListener { editable ->
            viewModel.onPasswordChanged(editable.toString())
        }
        userIdEditText.addTextChangedListener { editable ->
            viewModel.onUserIdChanged(editable.toString())
        }
        innEditText.addTextChangedListener { editable ->
            viewModel.onInnChanged(editable?.toStringIfNotEmpty())
        }
        resetAuthCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onResetAuthorizationChecked(isChecked)
        }
        createReceiptButton.setOnClickListener {
            viewModel.getToken()
        }
    }

    private fun observeState() {
        viewModel.loginIsValidState.observe(viewLifecycleOwner) { isValid ->
            createReceiptButton.isEnabled = isValid
        }
        viewModel.tokenState.observe(viewLifecycleOwner) { token ->
            with(viewModel.currentLogin) {
                val credentials = Credentials(
                    token = token,
                    userId = userId,
                    inn = inn
                )
                openFragment(CreateReceiptFragment.newInstance(credentials, isResetAuthorization))
            }
        }
        viewModel.tokenErrorState.observe(viewLifecycleOwner) {
            Snackbar
                .make(requireView(), R.string.login_error, Snackbar.LENGTH_LONG)
                .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.error))
                .show()
        }
    }
}