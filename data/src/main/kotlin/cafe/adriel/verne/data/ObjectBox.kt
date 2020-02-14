package cafe.adriel.verne.data

import android.content.Context
import cafe.adriel.verne.data.model.MyObjectBox
import cafe.adriel.verne.domain.model.VerneConfig
import io.objectbox.BoxStore
import io.objectbox.BoxStoreBuilder
import io.objectbox.DebugFlags
import io.objectbox.android.AndroidObjectBrowser

internal object ObjectBox {

    fun init(context: Context, config: VerneConfig): BoxStore =
        MyObjectBox.builder()
            .androidContext(context.applicationContext)
            .directory(config.databaseDir)
            .also { builder ->
                if (config.releaseBuild.not())
                    builder.enableDebugMode()
            }
            .build()
            .also { store ->
                if (config.releaseBuild.not())
                    store.enableObjectBrowser(context)
            }

    private fun BoxStoreBuilder.enableDebugMode() {
        debugRelations()
        debugFlags(
            DebugFlags.LOG_TRANSACTIONS_READ
            or DebugFlags.LOG_TRANSACTIONS_WRITE
            or DebugFlags.LOG_QUERIES
            or DebugFlags.LOG_QUERY_PARAMETERS
            or DebugFlags.LOG_ASYNC_QUEUE
            or DebugFlags.LOG_CACHE_HITS
            or DebugFlags.LOG_CACHE_ALL
        )
    }

    private fun BoxStore.enableObjectBrowser(context: Context) {
        AndroidObjectBrowser(this).start(context)
    }
}
