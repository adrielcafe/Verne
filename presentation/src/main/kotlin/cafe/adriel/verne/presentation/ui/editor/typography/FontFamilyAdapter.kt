package cafe.adriel.verne.presentation.ui.editor.typography

import android.content.Context
import android.graphics.Typeface
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.util.contains
import cafe.adriel.verne.presentation.R
import cafe.adriel.verne.presentation.extension.font
import cafe.adriel.verne.presentation.model.FontFamily

class FontFamilyAdapter(context: Context) :
    ArrayAdapter<FontFamily>(context, R.layout.item_font_family, FontFamily.sortedValues) {

    private val typefaces = SparseArray<Typeface>(count)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val fontNameView = super.getView(position, convertView, parent) as TextView
        getItem(position)?.let { item ->
            fontNameView.text = item.fontName
            if (typefaces.contains(position)) {
                fontNameView.typeface = typefaces[position]
            } else {
                context.font(item.resId) { typeface ->
                    typefaces.put(position, typeface)
                    fontNameView.typeface = typeface
                }
            }
        }
        return fontNameView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
        getView(position, convertView, parent)
}
