package cafe.adriel.verne.view.editor.typography

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.view.forEach
import androidx.fragment.app.FragmentManager
import cafe.adriel.krumbsview.util.tintDrawable
import cafe.adriel.verne.R
import cafe.adriel.verne.extension.colorFromAttr
import cafe.adriel.verne.extension.tagOf
import cafe.adriel.verne.model.FontFamily
import cafe.adriel.verne.util.AnalyticsUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import it.sephiroth.android.library.numberpicker.NumberPicker
import kotlinx.android.synthetic.main.fragment_typography.*
import org.koin.android.viewmodel.ext.android.viewModel

class TypographyDialogFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModel<TypographyViewModel>()

    companion object {
        fun show(fragmentManager: FragmentManager) {
            TypographyDialogFragment().show(fragmentManager,
                tagOf<TypographyDialogFragment>()
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_typography, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(vFontFamily) {
            context?.let {
                adapter = FontFamilyAdapter(it)
            }
            val selectedPosition = FontFamily.sortedValues.indexOfFirst { it == viewModel.fontFamily }
            setSelection(selectedPosition)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val fontFamily = FontFamily.sortedValues[position]
                    viewModel.fontFamily = fontFamily
                    AnalyticsUtil.logTypographyFontFamily(fontFamily.fontName)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) { }
            }
        }

        with(vFontSize) {
            forEach {
                when (it) {
                    is EditText -> it.isFocusable = false
                    is ImageButton -> {
                        it.setImageResource(R.drawable.ic_keyboard_arrow_up)
                        it.tintDrawable(colorFromAttr(android.R.attr.colorControlNormal))
                    }
                }
            }
            progress = viewModel.fontSize
            numberPickerChangeListener = object : NumberPicker.OnNumberPickerChangeListener {
                override fun onProgressChanged(numberPicker: NumberPicker, progress: Int, fromUser: Boolean) {
                    viewModel.fontSize = progress
                    AnalyticsUtil.logTypographyFontSize(progress)
                }
                override fun onStartTrackingTouch(numberPicker: NumberPicker) {
                }
                override fun onStopTrackingTouch(numberPicker: NumberPicker) {
                }
            }
        }

        with(vMarginSize) {
            forEach {
                when (it) {
                    is EditText -> it.isFocusable = false
                    is ImageButton -> {
                        it.setImageResource(R.drawable.ic_keyboard_arrow_up)
                        it.tintDrawable(colorFromAttr(android.R.attr.colorControlNormal))
                    }
                }
            }
            progress = viewModel.marginSize
            numberPickerChangeListener = object : NumberPicker.OnNumberPickerChangeListener {
                override fun onProgressChanged(numberPicker: NumberPicker, progress: Int, fromUser: Boolean) {
                    viewModel.marginSize = progress
                    AnalyticsUtil.logTypographyMarginSize(progress)
                }
                override fun onStartTrackingTouch(numberPicker: NumberPicker) {
                }
                override fun onStopTrackingTouch(numberPicker: NumberPicker) {
                }
            }
        }
    }
}
