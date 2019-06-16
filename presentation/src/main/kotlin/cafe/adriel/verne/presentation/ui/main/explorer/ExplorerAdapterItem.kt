package cafe.adriel.verne.presentation.ui.main.explorer

import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import cafe.adriel.verne.domain.extension.getPathAfterBaseDir
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.presentation.R
import cafe.adriel.verne.presentation.extension.color
import cafe.adriel.verne.shared.extension.filesCount
import cafe.adriel.verne.shared.extension.formatMedium
import com.mikepenz.fastadapter.items.AbstractItem
import com.mikepenz.fastadapter.ui.utils.FastAdapterUIUtils
import kotlinx.android.synthetic.main.item_explorer.view.*
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date

class ExplorerAdapterItem(
    private val lifecycleScope: LifecycleCoroutineScope,
    val item: ExplorerItem,
    private val explorerRootFolder: File,
    private val searchResult: Boolean = false
) : AbstractItem<ExplorerAdapterItem.ViewHolder>() {

    override val layoutRes: Int
        get() = R.layout.item_explorer

    override val type: Int
        get() = R.id.vItemContentLayout

    override fun getViewHolder(v: View) = ViewHolder(v)

    override fun bindView(holder: ViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)
        with(holder.itemView) {
            val bgDrawable = FastAdapterUIUtils
                .getSelectableBackground(context, context.color(R.color.colorPrimaryAlpha), false)
            ViewCompat.setBackground(this, bgDrawable)
            vItemName.text = item.title
            when (item) {
                is ExplorerItem.Folder -> {
                    vItemIcon.setImageResource(R.drawable.ic_folder)
                    vItemDetails.text = context.resources.getQuantityString(R.plurals.files, 0, 0)
                    lifecycleScope.launch {
                        val filesCount = item.file.filesCount()
                        vItemDetails.text = context.resources
                            .getQuantityString(R.plurals.files, filesCount, filesCount)
                    }
                }
                is ExplorerItem.File -> {
                    if (searchResult) {
                        vItemPath.text = item.getPathAfterBaseDir(explorerRootFolder)
                        vItemPath.visibility = View.VISIBLE
                    }
                    vItemIcon.setImageResource(R.drawable.ic_file)
                    vItemDetails.text = Date(item.file.lastModified()).formatMedium()
                }
            }
        }
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
        with(holder.itemView) {
            vItemPath.text = ""
            vItemPath.visibility = View.GONE
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
