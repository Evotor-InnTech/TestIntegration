package ru.evotor.testintegration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import ru.evotor.integration.Integration
import ru.evotor.integration.IntegrationImpl
import ru.evotor.integration.entities.receipt.OperationType_V1
import ru.evotor.integration.entities.receipt.Receipt_V1
import ru.evotor.integration.entities.receipt.position.Position_V1
import ru.evotor.integration.entities.receipt.position.Type_V1
import java.math.BigDecimal
import java.util.*

class MainActivity : AppCompatActivity() {

    private val integration: Integration by lazy { IntegrationImpl() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            integration.startPayment(
                Receipt_V1(
                    uuid = UUID.randomUUID().toString(),
                    positions = listOf(
                        Position_V1(
                            price = BigDecimal(15),
                            name = "Сок",
                            measureName = "шт",
                            quantity = BigDecimal.ONE,
                            tax = "NO_VAT",
                            commodityId = "5a7b8ebd-dfa4-454c-abf7-0c9b0dbefc7c",
                            type = Type_V1.NORMAL,
                            priceWithDiscount = BigDecimal(13.00)
                        )
                    ),
                    operationType = OperationType_V1.SELL,
                    receiptDiscount = BigDecimal(4.00)
                )
            )
        }

        integration.handlePaymentResult(activityResultRegistry) {
            text.text = it.operationResult.message
        }
    }
}