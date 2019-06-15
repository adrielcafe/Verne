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
import androidx.lifecycle.lifecycleScope
import cafe.adriel.krumbsview.util.tintDrawable
import cafe.adriel.verne.domain.interactor.settings.FontFamilySettingsInteractor
import cafe.adriel.verne.domain.interactor.settings.FontSizeSettingsInteractor
import cafe.adriel.verne.domain.interactor.settings.MarginSizeSettingsInteractor
import cafe.adriel.verne.presentation.R
import cafe.adriel.verne.presentation.extension.colorFromAttr
import cafe.adriel.verne.presentation.helper.AnalyticsHelper
import cafe.adriel.verne.presentation.model.FontFamily
import cafe.adriel.verne.shared.extension.tagOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import it.sephiroth.android.library.numberpicker.NumberPicker
import it.sephiroth.android.library.numberpicker.doOnProgressChanged
import kotlinx.android.synthetic.main.fragment_typography.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

class TypographyFragment : BottomSheetDialogFragment() {

    private val fontFamilyInteractor by inject<FontFamilySettingsInteractor>()
    private val fontSizeInteractor by inject<FontSizeSettingsInteractor>()
    private val marginSizeInteractor by inject<MarginSizeSettingsInteractor>()
    private val analyticsHelper by inject<AnalyticsHelper>()

    var listener: TypographyFragmentListener? = null

    companion object {
        fun show(fragmentManager: FragmentManager) {
            TypographyFragment().show(fragmentManager, tagOf<TypographyFragment>())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_typography, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vFontFamily.apply {
            context?.apply {
                adapter = FontFamilyAdapter(this)
            }
            post {
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        lifecycleScope.launch { onFontFamilySelected(FontFamily.sortedValues[position]) }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        }
        vFontSize.apply {
            setupNumberPicker(this)
            doOnProgressChanged { _, progress, fromUser ->
                if (fromUser) {
                    lifecycleScope.launch { onFontSizeSelected(progress) }
                }
            }
        }
        vMarginSize.apply {
            setupNumberPicker(this)
            doOnProgressChanged { _, progress, fromUser ->
                if (fromUser) {
                    lifecycleScope.launch { onMarginSizeSelected(progress) }
                }
            }
        }

        lifecycleScope.launch { loadSettings() }
    }

    override fun onDestroy() {
        listener = null
        super.onDestroy()
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

    private suspend fun loadSettings() {
        lifecycleScope.launch {
            val selectedFontFamilyPosition = FontFamily.sortedValues
                .indexOfFirst { fontFamily ->
                    val selectedFontFamily = FontFamily.valueOf(fontFamilyInteractor.get())
                    fontFamily == selectedFontFamily
                }

            vFontFamily.setSelection(selectedFontFamilyPosition)
            vFontSize.progress = fontSizeInteractor.get()
            vMarginSize.progress = marginSizeInteractor.get()
        }
    }

    private suspend fun onFontFamilySelected(selectedFontFamily: FontFamily) {
        lifecycleScope.launch {
            Timber.e("SAVE FONT FAMILY $selectedFontFamily")
            fontFamilyInteractor.set(selectedFontFamily.name)
            listener?.onSettingsChanged()
            analyticsHelper.logTypographyFontFamily(selectedFontFamily.name)
        }
    }

    private suspend fun onFontSizeSelected(selectedFontSize: Int) {
        lifecycleScope.launch {
            Timber.e("SAVE FONT SIZE $selectedFontSize")
            fontSizeInteractor.set(selectedFontSize)
            listener?.onSettingsChanged()
            analyticsHelper.logTypographyFontSize(selectedFontSize)
        }
    }

    private suspend fun onMarginSizeSelected(selectedMarginSize: Int) {
        lifecycleScope.launch {
            Timber.e("SAVE MARGIN SIZE $selectedMarginSize")
            marginSizeInteractor.set(selectedMarginSize)
            listener?.onSettingsChanged()
            analyticsHelper.logTypographyMarginSize(selectedMarginSize)
        }
    }
}
