package ru.evotor.testintegration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import ru.evotor.integration.Integration
import ru.evotor.integration.IntegrationImpl
import ru.evotor.integration.entities.credentials.v2.Credentials_V2
import ru.evotor.integration.entities.device.v2.Device_V2
import ru.evotor.integration.entities.employee.v2.Employee_V2
import ru.evotor.integration.entities.receipt.OperationType_V1
import ru.evotor.integration.entities.receipt.Receipt_V1
import ru.evotor.integration.entities.receipt.position.Position_V1
import ru.evotor.integration.entities.receipt.position.Type_V1
import ru.evotor.integration.entities.receipt.v2.PaymentType_V2
import ru.evotor.integration.entities.receipt.v2.Receipt_V2
import java.math.BigDecimal
import java.util.*

class MainActivity : AppCompatActivity() {

    private val integration: Integration by lazy { IntegrationImpl() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        payment_v1_btn.setOnClickListener {
            startPaymentV1()
        }

        sell_v2_btn.setOnClickListener {
            startSellV2()
        }

        payback_v2_btn.setOnClickListener {
            startPaybackV2()
        }

        integration.handlePaymentResult(activityResultRegistry) {
            text.text = it.operationResult.message
        }
    }

    private fun startPaymentV1() {
        integration.startPaymentV1(
            Receipt_V1(
                uuid = UUID.randomUUID().toString(),
                positions = listOf(
                    Position_V1(
                        price = BigDecimal(15.00),
                        name = "Сок",
                        measureName = "шт",
                        quantity = BigDecimal.ONE,
                        tax = "NO_VAT",
                        commodityId = "5a7b8ebd-dfa4-454c-abf7-0c9b0dbefc7c",
                        type = Type_V1.NORMAL,
                        priceWithDiscount = BigDecimal(5.00)
                    )
                ),
                operationType = OperationType_V1.SELL,
                receiptDiscount = BigDecimal(4.00),
                paymentAddress = "Невский пр.",
                paymentPlace = "Невский пр.",
                clientEmail = "test@test.ru",
                shouldPrintReceipt = true
            )
        )
    }

    private fun startSellV2() {
        integration.startSellV2(
            credentials = Credentials_V2(
                token = "Bearer ...",
                userId = "..."
            ),
            receipt = Receipt_V2(
                uuid = "4ecf2074-46ca-46ec-a473-3b54e4da7456",
                positions = listOf(
                    Position_V1(
                        price = BigDecimal(15.00),
                        name = "Сок",
                        measureName = "шт",
                        quantity = BigDecimal.ONE,
                        tax = "NO_VAT",
                        commodityId = "5a7b8ebd-dfa4-454c-abf7-0c9b0dbefc7c",
                        type = Type_V1.NORMAL,
                        priceWithDiscount = BigDecimal(5.00)
                    )
                ),
                clientEmail = "test@test.ru",
                clientPhone = null,
                paymentType = PaymentType_V2.CASH,
                shouldPrintReceipt = true,
                paymentAddress = "Невский пр.",
                paymentPlace = "Невский пр.",
                receiptDiscount = BigDecimal(4.00)
            ),
            resetAuthorization = true
        )
    }

    private fun startPaybackV2() {
        integration.startPaybackV2(
            credentials = Credentials_V2(
                token = "Bearer ...",
                userId = "..."
            ),
            receipt = Receipt_V2(
                uuid = UUID.randomUUID().toString(),
                positions = listOf(
                    Position_V1(
                        price = BigDecimal(15.00),
                        name = "Сок",
                        measureName = "шт",
                        quantity = BigDecimal.ONE,
                        tax = "NO_VAT",
                        commodityId = "5a7b8ebd-dfa4-454c-abf7-0c9b0dbefc7c",
                        type = Type_V1.NORMAL,
                        priceWithDiscount = BigDecimal(5.00)
                    )
                ),
                clientEmail = "test@test.ru",
                clientPhone = null,
                paymentType = PaymentType_V2.CASH,
                shouldPrintReceipt = true,
                paymentAddress = "Невский пр.",
                paymentPlace = "Невский пр.",
                receiptDiscount = BigDecimal(4.00)
            ),
            sellReceiptUuid = "4ecf2074-46ca-46ec-a473-3b54e4da7456"
        )
    }
}