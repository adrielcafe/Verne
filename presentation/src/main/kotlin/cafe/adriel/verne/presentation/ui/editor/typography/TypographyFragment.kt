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
import androidx.lifecycle.Observer
import cafe.adriel.krumbsview.util.tintDrawable
import cafe.adriel.verne.presentation.R
import cafe.adriel.verne.presentation.extension.colorFromAttr
import cafe.adriel.verne.presentation.helper.AnalyticsHelper
import cafe.adriel.verne.presentation.model.FontFamily
import cafe.adriel.verne.presentation.model.TypographySettings
import cafe.adriel.verne.presentation.util.StateMachineView
import cafe.adriel.verne.shared.extension.tagOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import it.sephiroth.android.library.numberpicker.NumberPicker
import it.sephiroth.android.library.numberpicker.doOnProgressChanged
import kotlinx.android.synthetic.main.fragment_typography.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class TypographyFragment : BottomSheetDialogFragment(), StateMachineView<TypographyState> {

    private val viewModel by viewModel<TypographyViewModel>()
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

        initViews()

        viewModel.state.observe(this@TypographyFragment, Observer { onState(it) })
    }

    override fun onDestroy() {
        listener = null
        super.onDestroy()
    }

    override fun onState(state: TypographyState) {
        when (state) {
            is TypographyState.Init -> initSettings(state.settings)
            is TypographyState.SettingsChanged -> listener?.onSettingsChanged()
        }
    }

    private fun initSettings(settings: TypographySettings) {
        val selectedFontFamilyPosition = FontFamily.sortedValues
            .indexOfFirst { it == settings.fontFamily }

        vFontFamily.setSelection(selectedFontFamilyPosition)
        vFontSize.progress = settings.fontSize
        vMarginSize.progress = settings.marginSize
    }

    private fun initViews() {
        vFontFamily.apply {
            context?.apply {
                adapter = FontFamilyAdapter(this)
            }
            post {
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
                        onFontFamilySelected(FontFamily.sortedValues[position])

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
        }
        vFontSize.apply {
            setupNumberPicker(this)
            doOnProgressChanged { _, progress, fromUser ->
                if (fromUser) onFontSizeSelected(progress)
            }
        }
        vMarginSize.apply {
            setupNumberPicker(this)
            doOnProgressChanged { _, progress, fromUser ->
                if (fromUser) onMarginSizeSelected(progress)
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

    private fun onFontFamilySelected(fontFamily: FontFamily) {
        viewModel.setAction(TypographyAction.SetFontFamily(fontFamily))
        analyticsHelper.logTypographyFontFamily(fontFamily.name)
    }

    private fun onFontSizeSelected(fontSize: Int) {
        viewModel.setAction(TypographyAction.SetFontSize(fontSize))
        analyticsHelper.logTypographyFontSize(fontSize)
    }

    private fun onMarginSizeSelected(marginSize: Int) {
        viewModel.setAction(TypographyAction.SetMarginSize(marginSize))
        analyticsHelper.logTypographyMarginSize(marginSize)
    }
}
