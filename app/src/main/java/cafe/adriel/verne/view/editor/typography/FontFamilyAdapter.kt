package cafe.adriel.verne.view.editor.typography

import android.content.Context
import android.graphics.Typeface
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.util.contains
import cafe.adriel.verne.R
import cafe.adriel.verne.extension.font
import cafe.adriel.verne.model.FontFamily

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
                context.font(item.resId) {
                    typefaces.put(position, it)
                    fontNameView.typeface = it
                }
            }
        }
        return fontNameView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup) =
        getView(position, convertView, parent)
}
