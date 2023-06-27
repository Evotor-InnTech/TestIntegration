package ru.evotor.testintegration.presentation.entities

import android.content.Context
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.evotor.integration.entities.receipt.position.SettlementMethodType
import ru.evotor.integration.entities.receipt.position.Tax
import ru.evotor.integration.entities.receipt.position.Type
import ru.evotor.testintegration.R
import java.math.BigDecimal

@Parcelize
data class PositionUi(
    val price: BigDecimal,
    val name: String,
    val measureName: String,
    val quantity: BigDecimal,
    val tax: Tax?,
    val commodityId: String?,
    val type: Type?,
    val priceWithDiscount: BigDecimal?,
    val mark: String?,
    val settlementMethodType: SettlementMethodType
) : Parcelable {

    val discount: BigDecimal?
        get() = priceWithDiscount?.let { price.minus(it) }

    companion object {
        fun getNewPosition(context: Context): PositionUi =
            PositionUi(
                price = BigDecimal(15.00),
                name = context.getString(R.string.default_position_name),
                measureName  = context.getString(R.string.default_measure_name),
                quantity = BigDecimal.ONE,
                tax = Tax.NO_VAT,
                commodityId = null,
                type = Type.NORMAL,
                priceWithDiscount = null,
                mark = null,
                settlementMethodType = SettlementMethodType.FULL
            )
    }
}