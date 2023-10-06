package ru.evotor.testintegration.presentation.receipt.position

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bs_add_position.*
import ru.evotor.integration.entities.receipt.position.SettlementMethodType
import ru.evotor.integration.entities.receipt.position.Tax
import ru.evotor.integration.entities.receipt.position.Type
import ru.evotor.testintegration.R
import ru.evotor.testintegration.presentation.entities.PositionUi
import ru.evotor.testintegration.utils.initAdapter
import ru.evotor.testintegration.utils.roundForTwoDigits
import ru.evotor.testintegration.utils.toBigDecimal
import ru.evotor.testintegration.utils.toBigDecimalIfNotEmpty
import ru.evotor.testintegration.utils.toString
import ru.evotor.testintegration.utils.toStringIfNotEmpty
import java.math.BigDecimal

class AddPositionBSDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val ADD_BARCODE_KEY = "add_position_key"
        const val POSITION_ARG = "position_arg"

        fun show(manager: FragmentManager) {
            val bsDialog = AddPositionBSDialogFragment()
            bsDialog.show(manager, bsDialog.tag)
        }
    }

    private var currentPosition: PositionUi? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        activity?.let {
            val dialog = BottomSheetDialog(it, R.style.BottomSheetDialogStyle)

            dialog.setOnShowListener {
                val bottomSheet =
                    dialog.findViewById<View>(R.id.design_bottom_sheet)
                bottomSheet?.let {
                    val behavior = BottomSheetBehavior.from(bottomSheet)
                    behavior.peekHeight = bottomSheet.height
                    bottomSheet.parent.requestLayout()
                }
            }

            return dialog
        }
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.bs_add_position, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (currentPosition == null) {
            currentPosition = PositionUi.getNewPosition(requireContext())
        }

        initListeners()
        initDropDownInputs()
        setInputTexts()
    }

    private fun initListeners() {
        nameEditText.addTextChangedListener { editable ->
            currentPosition = currentPosition?.copy(name = editable.toString())
        }
        priceEditText.addTextChangedListener { editable ->
            currentPosition = currentPosition?.copy(price = editable.toBigDecimal())
        }
        measureEditText.addTextChangedListener { editable ->
            currentPosition = currentPosition?.copy(measureName = editable.toString())
        }
        quantityEditText.addTextChangedListener { editable ->
            currentPosition = currentPosition?.copy(quantity = editable.toBigDecimal())
        }
        commodityIdEditText.addTextChangedListener { editable ->
            currentPosition = currentPosition?.copy(commodityId = editable?.toStringIfNotEmpty())
        }
        discountEditText.addTextChangedListener { editable ->
            currentPosition = currentPosition?.copy(
                priceWithDiscount = calculatePriceWithDiscount(editable?.toBigDecimalIfNotEmpty())
            )
        }
        markEditText.addTextChangedListener { editable ->
            currentPosition = currentPosition?.copy(mark = editable?.toStringIfNotEmpty())
        }

        addButton.setOnClickListener {
            onAddButtonClick()
        }
        cancelButton.setOnClickListener {
            dismiss()
        }
    }

    private fun calculatePriceWithDiscount(positionDiscount: BigDecimal?): BigDecimal? {
        val discount = positionDiscount ?: return null
        val quantity = currentPosition?.quantity ?: return null
        return currentPosition?.price?.minus(discount)?.multiply(quantity)?.roundForTwoDigits()
    }


    private fun onAddButtonClick() {
        setFragmentResult(
            ADD_BARCODE_KEY,
            bundleOf(POSITION_ARG to currentPosition)
        )
        dismiss()
    }

    private fun initDropDownInputs() {
        initTaxDropDownInput()
        initInventoryTypeDropDownInput()
        initSettlementMethodTypeDropDownInput()
    }

    private fun initTaxDropDownInput() {
        val taxes = Tax.values().map { it.toString(requireContext()) }
        taxEditText.initAdapter(taxes) { value ->
            val tax = Tax.values().first { it.toString(requireContext()) == value }
            currentPosition = currentPosition?.copy(tax = tax)
        }
    }

    private fun initInventoryTypeDropDownInput() {
        val types = Type.values().map { it.toString(requireContext()) }
        typeEditText.initAdapter(types) { value ->
            val type = Type.values().first { it.toString(requireContext()) == value }
            currentPosition = currentPosition?.copy(type = type)
        }
    }

    private fun initSettlementMethodTypeDropDownInput() {
        val settlementMethodTypes =
            SettlementMethodType.values().map { it.toString(requireContext()) }
        settlementMethodTypeEditText.initAdapter(settlementMethodTypes) { value ->
            val settlementMethodType =
                SettlementMethodType.values().first { it.toString(requireContext()) == value }
            currentPosition = currentPosition?.copy(settlementMethodType = settlementMethodType)
        }
    }

    private fun setInputTexts() {
        currentPosition?.apply {
            nameEditText.setText(name)
            priceEditText.setText(price.toPlainString())
            measureEditText.setText(measureName)
            quantityEditText.setText(quantity.toPlainString())
            tax?.let { taxEditText.setText(it.toString(requireContext()), false) }
            commodityId?.let { commodityIdEditText.setText(it) }
            type?.let { typeEditText.setText(it.toString(requireContext()), false) }
            discount?.let { discountEditText.setText(it.toPlainString()) }
            mark?.let { markEditText.setText(it) }
            settlementMethodTypeEditText.setText(settlementMethodType.toString(requireContext()), false)
        }
    }
}