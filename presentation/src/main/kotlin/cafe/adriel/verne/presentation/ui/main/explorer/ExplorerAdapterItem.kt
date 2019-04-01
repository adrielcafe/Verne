package cafe.adriel.verne.presentation.ui.main.explorer

import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import cafe.adriel.verne.domain.extension.getPathAfterBaseDir
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.presentation.R
import cafe.adriel.verne.presentation.extension.color
import cafe.adriel.verne.shared.extension.filesCount
import cafe.adriel.verne.shared.extension.formatMedium
import cafe.adriel.verne.shared.extension.launchMain
import com.mikepenz.fastadapter.commons.utils.FastAdapterUIUtils
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.item_explorer.view.*
import java.io.File
import java.util.Date

class ExplorerAdapterItem(
    val item: ExplorerItem,
    private val explorerRootFolder: File,
    private val searchResult: Boolean = false
) : AbstractItem<ExplorerAdapterItem, ExplorerAdapterItem.ViewHolder>() {

    override fun getLayoutRes() = R.layout.item_explorer

    override fun getType() = layoutRes

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
                    vItemDetails.text = context.resources.getQuantityString(R.plurals.files, 0, 0)
                    launchMain {
                        val filesCount = item.file.filesCount()
                        vItemDetails.text = context.resources.getQuantityString(R.plurals.files, filesCount, filesCount)
                    }

                    vItemIcon.setImageResource(R.drawable.ic_folder)
                }
                is ExplorerItem.File -> {
                    vItemDetails.text = Date(item.file.lastModified()).formatMedium()
                    if (searchResult) {
                        vItemPath.text = item.getPathAfterBaseDir(explorerRootFolder)
                        vItemPath.visibility = View.VISIBLE
                    }

                    vItemIcon.setImageResource(R.drawable.ic_file)
                }
            }
        }
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)
        with(holder.itemView) {
            vItemPath.text = ""
            vItemPath.visibility = android.view.View.GONE
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
