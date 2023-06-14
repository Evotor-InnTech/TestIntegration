package ru.evotor.testintegration.utils

import android.content.Context
import ru.evotor.integration.entities.receipt.PaymentType
import ru.evotor.integration.entities.receipt.position.SettlementMethodType
import ru.evotor.integration.entities.receipt.position.Tax
import ru.evotor.integration.entities.receipt.position.Type
import ru.evotor.testintegration.R

fun PaymentType.toString(context: Context) =
    when (this) {
        PaymentType.CASH -> context.getString(R.string.payment_type_cash)
        PaymentType.ELECTRON -> context.getString(R.string.payment_type_electron)
        PaymentType.TAP_ON_PHONE -> context.getString(R.string.payment_type_tap_on_phone)
    }

fun Tax.toString(context: Context) =
    when (this) {
        Tax.NO_VAT -> context.getString(R.string.tax_without_vat)
        Tax.VAT_10 -> context.getString(R.string.tax_vat_10)
        Tax.VAT_20 -> context.getString(R.string.tax_vat_20)
        Tax.VAT_0 -> context.getString(R.string.tax_vat_0)
        Tax.VAT_20_120 -> context.getString(R.string.tax_vat_20_120)
        Tax.VAT_10_110 -> context.getString(R.string.tax_vat_10_110)
    }

fun Type.toString(context: Context) =
    when (this) {
        Type.NORMAL -> context.getString(R.string.inventory_type_normal)
        Type.ALCOHOL_MARKED -> context.getString(R.string.inventory_type_alcohol_marked)
        Type.ALCOHOL_NOT_MARKED -> context.getString(R.string.inventory_type_alcohol_not_marked)
        Type.TOBACCO_MARKED -> context.getString(R.string.inventory_type_tobacco_marked)
        Type.SHOES_MARKED -> context.getString(R.string.inventory_type_shoe_marked)
        Type.MEDICINE_MARKED -> context.getString(R.string.inventory_type_medicines_marked)
        Type.SERVICE -> context.getString(R.string.inventory_type_service)
        Type.PERFUME_MARKED -> context.getString(R.string.inventory_type_perfume_marked)
        Type.LIGHT_INDUSTRY_MARKED -> context.getString(R.string.inventory_type_light_industry_marked)
        Type.PHOTOS_MARKED -> context.getString(R.string.inventory_type_photos_marked)
        Type.TYRES_MARKED -> context.getString(R.string.inventory_type_tires_marked)
    }

fun SettlementMethodType.toString(context: Context) =
    when (this) {
        SettlementMethodType.FULL -> context.getString(R.string.settlement_method_type_full)
        SettlementMethodType.FULL_PREPAYMENT -> context.getString(R.string.settlement_method_type_full_prepayment)
        SettlementMethodType.PARTIAL -> context.getString(R.string.settlement_method_type_partial)
        SettlementMethodType.PARTIAL_PREPAYMENT -> context.getString(R.string.settlement_method_type_partial_prepayment)
        SettlementMethodType.ADVANCE_PAYMENT -> context.getString(R.string.settlement_method_type_advance_payment)
        SettlementMethodType.LEND -> context.getString(R.string.settlement_method_type_lend)
        SettlementMethodType.LOAN_PAYMENT -> context.getString(R.string.settlement_method_type_loan_payment)
    }