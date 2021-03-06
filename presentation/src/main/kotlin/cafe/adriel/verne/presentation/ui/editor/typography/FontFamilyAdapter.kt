package cafe.adriel.verne.presentation.ui.editor.typography

import android.content.Context
import android.graphics.Typeface
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.util.contains
import androidx.lifecycle.LifecycleCoroutineScope
import cafe.adriel.verne.presentation.R
import cafe.adriel.verne.presentation.extension.font
import cafe.adriel.verne.presentation.model.FontFamily
import kotlinx.coroutines.launch

internal class FontFamilyAdapter(context: Context, private val lifecycleScope: LifecycleCoroutineScope) :
    ArrayAdapter<FontFamily>(context, R.layout.item_font_family, FontFamily.sortedValues) {

    private val typefaces by lazy { SparseArray<Typeface>(count) }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val fontNameView = super.getView(position, convertView, parent) as TextView
        getItem(position)?.let { item ->
            fontNameView.text = item.fontName
            if (typefaces.contains(position)) {
                fontNameView.typeface = typefaces[position]
            } else {
                lifecycleScope.launch {
                    context.font(item.resId)?.let { typeface ->
                        typefaces.put(position, typeface)
                        fontNameView.typeface = typeface
                    }
                }
            }
        }
        return fontNameView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
        getView(position, convertView, parent)
}
