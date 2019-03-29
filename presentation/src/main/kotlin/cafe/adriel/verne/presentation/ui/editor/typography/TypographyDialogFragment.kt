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
import cafe.adriel.verne.interactor.preference.FontFamilyPreferenceInteractor
import cafe.adriel.verne.interactor.preference.FontSizePreferenceInteractor
import cafe.adriel.verne.interactor.preference.MarginSizePreferenceInteractor
import cafe.adriel.verne.presentation.R
import cafe.adriel.verne.presentation.extension.colorFromAttr
import cafe.adriel.verne.presentation.helper.AnalyticsHelper
import cafe.adriel.verne.presentation.model.FontFamily
import cafe.adriel.verne.shared.extension.launchMain
import cafe.adriel.verne.shared.extension.tagOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import it.sephiroth.android.library.numberpicker.NumberPicker
import kotlinx.android.synthetic.main.fragment_typography.*
import org.koin.android.ext.android.inject

class TypographyDialogFragment : BottomSheetDialogFragment() {

    private val analyticsHelper by inject<AnalyticsHelper>()
    private val fontFamily by inject<FontFamilyPreferenceInteractor>()
    private val fontSize by inject<FontSizePreferenceInteractor>()
    private val marginSize by inject<MarginSizePreferenceInteractor>()

    lateinit var listener: TypographyDialogFragmentListener

    companion object {
        fun show(fragmentManager: FragmentManager) {
            TypographyDialogFragment().show(fragmentManager, tagOf<TypographyDialogFragment>())
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
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    onFontFamilySelected(FontFamily.sortedValues[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
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
            numberPickerChangeListener = object : NumberPicker.OnNumberPickerChangeListener {
                override fun onProgressChanged(numberPicker: NumberPicker, progress: Int, fromUser: Boolean) {
                    onFontSizeSelected(progress)
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
            numberPickerChangeListener = object : NumberPicker.OnNumberPickerChangeListener {
                override fun onProgressChanged(numberPicker: NumberPicker, progress: Int, fromUser: Boolean) {
                    onMarginSizeSelected(progress)
                }

                override fun onStartTrackingTouch(numberPicker: NumberPicker) {
                }

                override fun onStopTrackingTouch(numberPicker: NumberPicker) {
                }
            }
        }

        loadPreferences()
    }

    private fun loadPreferences() = launchMain {
        val selectedFontFamilyPosition = FontFamily.sortedValues
            .indexOfFirst {
                val selectedFontFamily = FontFamily.valueOf(fontFamily.get())
                it ==  selectedFontFamily
            }

        vFontFamily.setSelection(selectedFontFamilyPosition)
        vFontSize.progress = fontSize.get()
        vMarginSize.progress = marginSize.get()
    }

    private fun onFontFamilySelected(selectedFontFamily: FontFamily) = launchMain {
        val fontName = selectedFontFamily.fontName
        fontFamily.set(fontName)

        listener.onPreferencesChanged()
        analyticsHelper.logTypographyFontFamily(fontName)
    }

    private fun onFontSizeSelected(selectedFontSize: Int) = launchMain {
        fontSize.set(selectedFontSize)

        listener.onPreferencesChanged()
        analyticsHelper.logTypographyFontSize(selectedFontSize)
    }

    private fun onMarginSizeSelected(selectedMarginSize: Int) = launchMain {
        marginSize.set(selectedMarginSize)

        listener.onPreferencesChanged()
        analyticsHelper.logTypographyMarginSize(selectedMarginSize)
    }
}
