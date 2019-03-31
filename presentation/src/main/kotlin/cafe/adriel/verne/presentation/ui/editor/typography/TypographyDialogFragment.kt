package cafe.adriel.verne.presentation.ui.editor.typography

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
import cafe.adriel.verne.presentation.R
import cafe.adriel.verne.presentation.extension.colorFromAttr
import cafe.adriel.verne.presentation.helper.AnalyticsHelper
import cafe.adriel.verne.presentation.helper.PreferencesHelper
import cafe.adriel.verne.presentation.model.FontFamily
import cafe.adriel.verne.shared.extension.launchMain
import cafe.adriel.verne.shared.extension.tagOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import it.sephiroth.android.library.numberpicker.NumberPicker
import kotlinx.android.synthetic.main.fragment_typography.*
import org.koin.android.ext.android.inject

class TypographyDialogFragment : BottomSheetDialogFragment() {

    private val preferencesHelper by inject<PreferencesHelper>()
    private val analyticsHelper by inject<AnalyticsHelper>()

    lateinit var listener: TypographyDialogFragmentListener

    companion object {
        fun show(fragmentManager: FragmentManager) {
            TypographyDialogFragment().show(fragmentManager, tagOf<TypographyDialogFragment>())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_typography, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFontFamily()
        initFontSize()
        initMarginSize()

        loadPreferences()
    }

    private fun initFontFamily() {
        with(vFontFamily) {
            context?.apply {
                adapter = FontFamilyAdapter(this)
            }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    onFontFamilySelected(FontFamily.sortedValues[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    private fun initFontSize() {
        with(vFontSize) {
            setupNumberPicker(this)
            numberPickerChangeListener = object : NumberPicker.OnNumberPickerChangeListener {
                override fun onProgressChanged(
                    numberPicker: NumberPicker,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    onFontSizeSelected(progress)
                }

                override fun onStartTrackingTouch(numberPicker: NumberPicker) {
                }

                override fun onStopTrackingTouch(numberPicker: NumberPicker) {
                }
            }
        }
    }

    private fun initMarginSize() {
        with(vMarginSize) {
            setupNumberPicker(this)
            numberPickerChangeListener = object : NumberPicker.OnNumberPickerChangeListener {
                override fun onProgressChanged(
                    numberPicker: NumberPicker,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    onMarginSizeSelected(progress)
                }

                override fun onStartTrackingTouch(numberPicker: NumberPicker) {
                }

                override fun onStopTrackingTouch(numberPicker: NumberPicker) {
                }
            }
        }
    }

    private fun setupNumberPicker(numberPickerView: NumberPicker) {
        numberPickerView.forEach { childView ->
            when (childView) {
                is EditText -> childView.isFocusable = false
                is ImageButton -> {
                    childView.setImageResource(R.drawable.ic_keyboard_arrow_up)
                    childView.tintDrawable(colorFromAttr(android.R.attr.colorControlNormal))
                }
            }
        }
    }

    private fun loadPreferences() = launchMain {
        val selectedFontFamilyPosition = FontFamily.sortedValues
            .indexOfFirst { fontFamily ->
                val selectedFontFamily = FontFamily.valueOf(preferencesHelper.getFontFamily())
                fontFamily == selectedFontFamily
            }

        vFontFamily.setSelection(selectedFontFamilyPosition)
        vFontSize.progress = preferencesHelper.getFontSize()
        vMarginSize.progress = preferencesHelper.getMarginSize()
    }

    private fun onFontFamilySelected(selectedFontFamily: FontFamily) = launchMain {
        preferencesHelper.setFontFamily(selectedFontFamily.name)
        listener.onPreferenceChanged()
        analyticsHelper.logTypographyFontFamily(selectedFontFamily.name)
    }

    private fun onFontSizeSelected(selectedFontSize: Int) = launchMain {
        preferencesHelper.setFontSize(selectedFontSize)
        listener.onPreferenceChanged()
        analyticsHelper.logTypographyFontSize(selectedFontSize)
    }

    private fun onMarginSizeSelected(selectedMarginSize: Int) = launchMain {
        preferencesHelper.setMarginSize(selectedMarginSize)
        listener.onPreferenceChanged()
        analyticsHelper.logTypographyMarginSize(selectedMarginSize)
    }
}
