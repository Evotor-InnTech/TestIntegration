package ru.evotor.testintegration.presentation.receipt

import android.content.Context
import androidx.activity.result.ActivityResultRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import ru.evotor.integration.Integration
import ru.evotor.integration.entities.TransactionResult
import ru.evotor.integration.entities.credentials.Credentials
import ru.evotor.integration.entities.device.Device
import ru.evotor.integration.entities.receipt.OperationType
import ru.evotor.integration.entities.receipt.PaymentType
import ru.evotor.integration.entities.receipt.Receipt
import ru.evotor.integration.entities.receipt.position.Position
import ru.evotor.testintegration.presentation.entities.PositionUi
import ru.evotor.testintegration.presentation.entities.ReceiptUi
import ru.evotor.testintegration.repository.RepositoryModule
import java.math.BigDecimal
import java.util.UUID

data class PositionsState(
    val positions: List<PositionUi>
) {

    fun isValid(): Boolean =
        positions.isNotEmpty()
}

class ReceiptCreationViewModel(
    private val device: Device?,
    private val credentials: Credentials,
    private val resetAuthorization: Boolean,
    private val integration: Integration = RepositoryModule.integration,
    context: Context = RepositoryModule.applicationContext
) : ViewModel() {

    var currentReceipt: ReceiptUi = ReceiptUi.getNewReceipt(context)
        private set(value) {
            field = value
            _positionsState.value = PositionsState(value.positions)
        }

    private val _positionsState = MutableLiveData<PositionsState>()
    val positionsState: LiveData<PositionsState> = _positionsState.distinctUntilChanged()

    fun addPosition(position: PositionUi) {
        val positions = currentReceipt.positions.toMutableList()
        positions.add(position)
        currentReceipt = currentReceipt.copy(positions = positions)
    }

    fun removePosition(position: PositionUi) {
        val positions = currentReceipt.positions.toMutableList()
        positions.remove(position)
        currentReceipt = currentReceipt.copy(positions = positions)
    }

    fun onOperationTypeChecked(operationType: OperationType) {
        currentReceipt = currentReceipt.copy(operationType = operationType)
    }

    fun onContactDetailsChanged(contactDetails: String) {
        currentReceipt = currentReceipt.copy(contactDetails = contactDetails)
    }

    fun onShouldPrintReceiptChecked(isChecked: Boolean) {
        currentReceipt = currentReceipt.copy(shouldPrintReceipt = isChecked)
    }

    fun onPaymentTypeSelected(paymentType: PaymentType) {
        currentReceipt = currentReceipt.copy(paymentType = paymentType)
    }

    fun onPaymentAddressChanged(paymentAddress: String) {
        currentReceipt = currentReceipt.copy(paymentAddress = paymentAddress)
    }

    fun onPaymentPlaceChanged(paymentPlace: String) {
        currentReceipt = currentReceipt.copy(paymentPlace = paymentPlace)
    }

    fun onReceiptDiscountChanged(receiptDiscount: BigDecimal?) {
        currentReceipt = currentReceipt.copy(receiptDiscount = receiptDiscount)
    }

    fun onMdlpChanged(mdlp: String?) {
        currentReceipt = currentReceipt.copy(mdlp = mdlp)
    }

    fun onSellReceiptUuidChanged(sellReceiptUuid: String?) {
        currentReceipt = currentReceipt.copy(sellReceiptUuid = sellReceiptUuid)
    }

    fun startMk() {
        when (currentReceipt.operationType) {
            OperationType.SELL -> startSell()
            OperationType.PAYBACK -> startPayback()
        }
    }

    private fun startSell() {
        with(currentReceipt) {
            integration.startSell(
                credentials = credentials,
                receipt = toReceipt(),
                device = device,
                resetAuthorization = resetAuthorization
            )
        }
    }

    private fun startPayback() {
        with(currentReceipt) {
            integration.startPayback(
                credentials = credentials,
                receipt = toReceipt(),
                device = device,
                sellReceiptUuid = sellReceiptUuid,
                resetAuthorization = resetAuthorization
            )
        }
    }

    fun handlePaymentResult(
        registry: ActivityResultRegistry,
        callback: (TransactionResult) -> Unit
    ) {
        integration.handlePaymentResult(registry, callback)
    }

    private fun ReceiptUi.toReceipt(): Receipt {
        var clientEmail: String? = null
        var clientPhone: String? = null
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(contactDetails).matches()) {
            clientEmail = contactDetails
        } else {
            clientPhone = contactDetails
        }
        return Receipt(
            uuid = UUID.randomUUID().toString(),
            positions = positions.map { it.toPosition() },
            clientEmail = clientEmail,
            clientPhone = clientPhone,
            paymentType = paymentType,
            shouldPrintReceipt = shouldPrintReceipt,
            paymentAddress = paymentAddress,
            paymentPlace = paymentPlace,
            receiptDiscount = receiptDiscount,
            mdlp = mdlp
        )
    }

    private fun PositionUi.toPosition(): Position =
        Position(
            price = price,
            name = name,
            measureName = measureName,
            quantity = quantity,
            tax = tax,
            commodityId = commodityId,
            type = type,
            priceWithDiscount = priceWithDiscount,
            mark = mark,
            settlementMethodType = settlementMethodType
        )
}