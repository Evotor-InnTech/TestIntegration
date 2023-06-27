package ru.evotor.testintegration.presentation.entities

import android.content.Context
import ru.evotor.integration.entities.receipt.OperationType
import ru.evotor.integration.entities.receipt.PaymentType
import ru.evotor.testintegration.R
import java.math.BigDecimal

data class ReceiptUi(
    val positions: List<PositionUi>,
    val contactDetails: String,
    val paymentType: PaymentType,
    val shouldPrintReceipt: Boolean,
    val operationType: OperationType,
    val paymentAddress: String,
    val paymentPlace: String,
    val receiptDiscount: BigDecimal?,
    val mdlp: String?,
    val sellReceiptUuid: String?
) {

    companion object {
        fun getNewReceipt(context: Context): ReceiptUi =
            ReceiptUi(
                positions = listOf(PositionUi.getNewPosition(context)),
                contactDetails = context.getString(R.string.default_receipt_contact_details),
                paymentType = PaymentType.CASH,
                shouldPrintReceipt = true,
                operationType = OperationType.SELL,
                paymentAddress = context.getString(R.string.default_receipt_payment_address),
                paymentPlace = context.getString(R.string.default_receipt_payment_address),
                receiptDiscount = BigDecimal(4.00),
                mdlp = null,
                sellReceiptUuid = null
            )
    }
}