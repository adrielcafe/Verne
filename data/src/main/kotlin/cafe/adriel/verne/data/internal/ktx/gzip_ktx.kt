package cafe.adriel.verne.data.internal.ktx

import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

internal fun String.toGzip() =
    ByteArrayOutputStream(length).use { stream ->
        GZIPOutputStream(stream).use { it.write(toByteArray()) }
        stream.toByteArray()
    }

@OptIn(ExperimentalStdlibApi::class)
internal fun ByteArray.fromGzip() =
    GZIPInputStream(inputStream()).use { stream ->
        stream.readBytes().decodeToString()
    }
