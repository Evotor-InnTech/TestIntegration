package ru.evotor.testintegration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import ru.evotor.integration.Integration
import ru.evotor.integration.IntegrationImpl
import ru.evotor.integration.entities.credentials.Credentials
import ru.evotor.integration.entities.receipt.PaymentType
import ru.evotor.integration.entities.receipt.Receipt
import ru.evotor.integration.entities.receipt.position.Position
import ru.evotor.integration.entities.receipt.position.Tax
import ru.evotor.integration.entities.receipt.position.Type
import java.math.BigDecimal
import java.util.*

class MainActivity : AppCompatActivity() {

    private val integration: Integration by lazy { IntegrationImpl() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sell_btn.setOnClickListener {
            startSell()
        }

        payback_btn.setOnClickListener {
            startPayback()
        }

        integration.handlePaymentResult(activityResultRegistry) {
            text.text = it.operationResult.message
        }
    }

    private fun startSell() {
        integration.startSell(
            credentials = Credentials(
                token = "Bearer ...",
                userId = "..."
            ),
            receipt = Receipt(
                uuid = "4ecf2074-46ca-46ec-a473-3b54e4da7456",
                positions = listOf(
                    Position(
                        price = BigDecimal(15.00),
                        name = "Сок",
                        measureName = "шт",
                        quantity = BigDecimal.ONE,
                        tax = Tax.NO_VAT,
                        commodityId = "5a7b8ebd-dfa4-454c-abf7-0c9b0dbefc7c",
                        type = Type.NORMAL,
                        priceWithDiscount = BigDecimal(5.00),
                        mark = null
                    )
                ),
                clientEmail = "test@test.ru",
                clientPhone = null,
                paymentType = PaymentType.CASH,
                shouldPrintReceipt = true,
                paymentAddress = "Невский пр.",
                paymentPlace = "Невский пр.",
                receiptDiscount = BigDecimal(4.00),
                mdlp = "12121212121212",
                extra = hashMapOf("test" to "test")
            ),
            resetAuthorization = true
        )
    }

    private fun startPayback() {
        integration.startPayback(
            credentials = Credentials(
                token = "Bearer ...",
                userId = "..."
            ),
            receipt = Receipt(
                uuid = UUID.randomUUID().toString(),
                positions = listOf(
                    Position(
                        price = BigDecimal(15.00),
                        name = "Сок",
                        measureName = "шт",
                        quantity = BigDecimal.ONE,
                        tax = Tax.NO_VAT,
                        commodityId = "5a7b8ebd-dfa4-454c-abf7-0c9b0dbefc7c",
                        type = Type.NORMAL,
                        priceWithDiscount = BigDecimal(5.00),
                        mark = null
                    )
                ),
                clientEmail = "test@test.ru",
                clientPhone = null,
                paymentType = PaymentType.CASH,
                shouldPrintReceipt = true,
                paymentAddress = "Невский пр.",
                paymentPlace = "Невский пр.",
                receiptDiscount = BigDecimal(4.00)
            ),
            sellReceiptUuid = "4ecf2074-46ca-46ec-a473-3b54e4da7456"
        )
    }
}