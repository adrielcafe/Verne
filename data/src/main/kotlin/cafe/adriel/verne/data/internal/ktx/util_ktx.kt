package cafe.adriel.verne.data.internal.ktx

import android.annotation.SuppressLint
import com.google.firebase.firestore.util.Util

@SuppressLint("RestrictedApi")
internal fun generateFirebaseId(): String = Util.autoId()
