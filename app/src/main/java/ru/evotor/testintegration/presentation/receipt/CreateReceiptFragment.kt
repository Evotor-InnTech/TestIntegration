package ru.evotor.testintegration.presentation.receipt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_create_receipt.*
import ru.evotor.integration.entities.credentials.Credentials
import ru.evotor.integration.entities.device.Device
import ru.evotor.integration.entities.employee.Employee
import ru.evotor.integration.entities.receipt.OperationType
import ru.evotor.integration.entities.receipt.PaymentType
import ru.evotor.testintegration.R
import ru.evotor.testintegration.presentation.entities.PositionUi
import ru.evotor.testintegration.presentation.receipt.position.AddPositionBSDialogFragment
import ru.evotor.testintegration.utils.initAdapter
import ru.evotor.testintegration.utils.listenResult
import ru.evotor.testintegration.utils.toBigDecimalIfNotEmpty
import ru.evotor.testintegration.utils.toString
import ru.evotor.testintegration.utils.toStringIfNotEmpty
import ru.evotor.testintegration.utils.viewModels

class CreateReceiptFragment : Fragment() {

    companion object {
        private const val DEVICE_KEY = "device_key"
        private const val EMPLOYEE_KEY = "employee_key"
        private const val CREDENTIALS_KEY = "credentials_key"
        private const val RESET_AUTHORIZATION_KEY = "reset_authorization_key"

        fun newInstance(
            device: Device?,
            employee: Employee?,
            credentials: Credentials,
            resetAuthorization: Boolean
        ) = CreateReceiptFragment().apply {
            arguments = bundleOf(
                DEVICE_KEY to device,
                EMPLOYEE_KEY to employee,
                CREDENTIALS_KEY to credentials,
                RESET_AUTHORIZATION_KEY to resetAuthorization
            )
        }
    }

    private val viewModel by viewModels {
        CreateReceiptViewModel(
            device,
            employee,
            credentials,
            resetAuthorization
        )
    }

    private val device: Device? by lazy { arguments?.getParcelable(DEVICE_KEY) }
    private val employee: Employee? by lazy { arguments?.getParcelable(EMPLOYEE_KEY) }

    private val credentials: Credentials by lazy {
        arguments?.getParcelable<Credentials>(CREDENTIALS_KEY) ?: error("credentials is null")
    }

    private val resetAuthorization: Boolean by lazy {
        arguments?.getBoolean(RESET_AUTHORIZATION_KEY) ?: false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAddPositionDialogListener()
    }

    private fun setAddPositionDialogListener() {
        listenResult(AddPositionBSDialogFragment.ADD_BARCODE_KEY) { bundle ->
            bundle.getParcelable<PositionUi>(AddPositionBSDialogFragment.POSITION_ARG)
                ?.let { position ->
                    viewModel.addPosition(position)
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_create_receipt, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handlePaymentResult()
        initListeners()
        initPaymentTypeDropDownInput()
        setInputTexts()

        selectOperationType(viewModel.currentReceipt.operationType)
        shouldPrintReceiptSwitch.isChecked = viewModel.currentReceipt.shouldPrintReceipt

        observeState()
    }

    private fun handlePaymentResult() {
        viewModel.handlePaymentResult(requireActivity().activityResultRegistry) { result ->
            val snackbarColor = if (result.operationResult.success) {
                ContextCompat.getColor(requireContext(), R.color.success)
            } else {
                ContextCompat.getColor(requireContext(), R.color.error)
            }
            Snackbar
                .make(requireView(), result.operationResult.message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(snackbarColor)
                .show()
        }
    }

    private fun initListeners() {
        addPositonButton.setOnClickListener {
            AddPositionBSDialogFragment.show(childFragmentManager)
        }
        contactDetailsEditText.addTextChangedListener { editable ->
            viewModel.onContactDetailsChanged(editable.toString())
        }
        shouldPrintReceiptSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onShouldPrintReceiptChecked(isChecked)
        }
        paymentAddressEditText.addTextChangedListener { editable ->
            viewModel.onPaymentAddressChanged(editable.toString())
        }
        paymentPlaceEditText.addTextChangedListener { editable ->
            viewModel.onPaymentPlaceChanged(editable.toString())
        }
        discountEditText.addTextChangedListener { editable ->
            viewModel.onReceiptDiscountChanged(editable?.toBigDecimalIfNotEmpty())
        }
        mdlpEditText.addTextChangedListener { editable ->
            viewModel.onMdlpChanged(editable?.toStringIfNotEmpty())
        }
        sellReceiptUuidEditText.addTextChangedListener { editable ->
            viewModel.onSellReceiptUuidChanged(editable?.toStringIfNotEmpty())
        }
        sellRadioButton.setOnClickListener {
            selectOperationType(OperationType.SELL)
        }
        paybackRadioButton.setOnClickListener {
            selectOperationType(OperationType.PAYBACK)
        }
        toMkButton.setOnClickListener {
            viewModel.startMk()
        }
    }

    private fun selectOperationType(operationType: OperationType) {
        viewModel.onOperationTypeChecked(operationType)
        when (operationType) {
            OperationType.SELL -> onSellSelected()
            OperationType.PAYBACK -> onPaybackSelected()
        }
    }

    private fun onSellSelected() {
        operationTypeRadioGroup.check(R.id.sellRadioButton)
        sellReceiptUuidTextInput.isVisible = false
        sellReceiptUuidEditText.setText("")
    }

    private fun onPaybackSelected() {
        operationTypeRadioGroup.check(R.id.paybackRadioButton)
        sellReceiptUuidTextInput.isVisible = true
    }

    private fun initPaymentTypeDropDownInput() {
        val paymentTypes = PaymentType.values().map { it.toString(requireContext()) }
        paymentTypeEditText.initAdapter(paymentTypes) { value ->
            val paymentType = PaymentType.values().first { it.toString(requireContext()) == value }
            viewModel.onPaymentTypeSelected(paymentType)
        }
    }

    private fun setInputTexts() {
        with(viewModel.currentReceipt) {
            paymentTypeEditText.setText(paymentType.toString(requireContext()), false)
            contactDetailsEditText.setText(contactDetails)
            paymentAddressEditText.setText(paymentAddress)
            paymentPlaceEditText.setText(paymentPlace)
            receiptDiscount?.let { discountEditText.setText(it.toPlainString()) }
            mdlp?.let { mdlpEditText.setText(it) }
        }
    }

    private fun observeState() {
        viewModel.positionsState.observe(viewLifecycleOwner) { state ->
            setPositions(state.positions)
            toMkButton.isEnabled = state.isValid()
        }
    }

    private fun setPositions(positions: List<PositionUi>) {
        positionsViewGroup.removeAllViews()
        positions.forEach { position ->
            val positionLayout =
                layoutInflater.inflate(R.layout.item_position, positionsViewGroup, false)
            val nameTextView = positionLayout.findViewById<TextView>(R.id.nameTextView)
            val deleteImageView = positionLayout.findViewById<ImageView>(R.id.deleteImageView)
            nameTextView.text = position.name
            deleteImageView.setOnClickListener {
                viewModel.removePosition(position)
            }
            positionsViewGroup.addView(positionLayout)
        }
    }
}