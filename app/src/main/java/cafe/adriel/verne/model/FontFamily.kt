package cafe.adriel.verne.model

import androidx.annotation.FontRes
import cafe.adriel.verne.R

enum class FontFamily(val fontName: String, @FontRes val resId: Int) {

    ALICE("Alice", R.font.alice),
    ARVO("Arvo", R.font.arvo),
    BITTER("Bitter", R.font.bitter),
    FIRA_SANS("Fira Sans", R.font.fira_sans),
    LAILA("Laila", R.font.laila),
    LATO("Lato", R.font.lato),
    LORA("Lora", R.font.lora),
    MERRIWEATHER("Merriweather", R.font.merriweather),
    MONTSERRAT("Montserrat", R.font.montserrat),
    NOTO_SANS("Noto Sans", R.font.noto_sans),
    NOTO_SERIF("Noto Serif", R.font.noto_serif),
    OPEN_SANS("Open Sans", R.font.open_sans),
    OXYGEN("Oxygen", R.font.oxygen),
    PT_SANS("PT Sans", R.font.pt_sans),
    PT_SERIF("PT Serif", R.font.pt_serif),
    RALEWAY("Raleway", R.font.raleway),
    ROBOTO("Roboto", R.font.roboto),
    ROBOTO_SLAB("Roboto Slab", R.font.roboto_slab),
    SLABO_13PX("Slabo 13px", R.font.slabo_13px),
    SLABO_27PX("Slabo 27px", R.font.slabo_27px),
    SOURCE_SANS_PRO("Source Sans Pro", R.font.source_sans_pro),
    SOURCE_SERIF_PRO("Source Serif Pro", R.font.source_serif_pro),
    UBUNTU("Ubuntu", R.font.ubuntu);

    companion object {
        val sortedValues by lazy {
            values().sortedBy { it.fontName }
        }
    }
}
