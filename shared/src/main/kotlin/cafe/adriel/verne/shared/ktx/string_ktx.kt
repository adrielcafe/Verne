package cafe.adriel.verne.shared.ktx

import java.text.Normalizer
import java.util.Locale

val String.normalized: String
    get() = Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("""[^\p{ASCII}]""".toRegex(), "")
        .replace("""[^a-zA-Z0-9\s]+""".toRegex(), "")
        .trim()
        .replace("""\s+""".toRegex(), " ")
        .toLowerCase(Locale.getDefault())
