package cafe.adriel.verne.view.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import cafe.adriel.krumbsview.model.Krumb
import cafe.adriel.verne.R
import cafe.adriel.verne.extension.getFragment
import cafe.adriel.verne.extension.tagOf
import cafe.adriel.verne.model.ExplorerItem
import cafe.adriel.verne.util.AnalyticsUtil
import cafe.adriel.verne.view.BaseActivity
import cafe.adriel.verne.view.main.explorer.ExplorerFragment
import cafe.adriel.verne.view.main.explorer.listener.ExplorerFragmentListener
import cafe.adriel.verne.view.main.settings.SettingsFragment
import com.ferfalk.simplesearchview.SimpleSearchView
import com.ferfalk.simplesearchview.SimpleSearchViewListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.viewModel

class MainActivity : BaseActivity<MainViewState>(),
    ExplorerFragmentListener {

    override val viewModel by viewModel<MainViewModel>()

    private var backPressedTwice = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(vToolbar)

        with(vBreadcrumbs) {
            setOnPreviousItemClickListener {
                getExplorerFragment()?.backToPreviousFolder()
            }
        }
        with(vSearch){
            setTextColor(Color.WHITE)
            setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    getExplorerFragment()?.search(newText?.trim() ?: "")
                    AnalyticsUtil.logTextSearch()
                    return false
                }
                override fun onQueryTextCleared(): Boolean {
                    return false
                }
            })
            setOnSearchViewListener(object : SimpleSearchViewListener() {
                override fun onSearchViewShown() {
                    getExplorerFragment()?.setSearchModeEnabled(true)
                }
                override fun onSearchViewClosed() {
                    getExplorerFragment()?.setSearchModeEnabled(false)
                }
            })
        }

        vAppVersion.text = viewModel.appVersion

        supportFragmentManager.commit {
            replace(R.id.vContent, getExplorerFragment() ?: ExplorerFragment(),
                tagOf<ExplorerFragment>()
            )
            replace(R.id.vSettings, getSettingsFragment() ?: SettingsFragment(),
                tagOf<SettingsFragment>()
            )
        }
    }

    override fun onBackPressed() {
        val backToPreviousFolder = getExplorerFragment()?.backToPreviousFolder() ?: false
        when {
            vDrawer.isDrawerOpen(GravityCompat.END) -> vDrawer.closeDrawer(GravityCompat.END)
            vSearch.isSearchOpen -> vSearch.closeSearch()
            backToPreviousFolder -> vBreadcrumbs.removeLastItem()
            backPressedTwice -> super.onBackPressed()
            else -> launch {
                backPressedTwice = true
                Snackbar.make(vRoot, R.string.press_back_exit, Snackbar.LENGTH_SHORT).show()
                delay(2000)
                backPressedTwice = false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (vSearch.onActivityResult(requestCode, resultCode, data)) {
            AnalyticsUtil.logVoiceSearch()
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onAttachFragment(fragment: Fragment) {
        when(fragment){
            is ExplorerFragment -> fragment.listener = this
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val searchMenuItem = menu.findItem(R.id.action_search)
        vSearch.post {
            with(vSearch) {
                val menuItemSize = resources.getDimensionPixelSize(R.dimen.design_navigation_icon_size)
                revealAnimationCenter.x -= menuItemSize * menu.size
                setMenuItem(searchMenuItem)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when(item?.itemId){
        R.id.action_settings -> {
            vDrawer.openDrawer(GravityCompat.END)
            true
        }
        else -> false
    }

    override fun onStateUpdated(state: MainViewState) {
        // TODO
    }

    override fun onPrintHtml(fileName: String, html: String) {
        originalContext?.apply {
            viewModel.printHtml(this, fileName, html)
            AnalyticsUtil.logPrint()
        }
    }

    override fun onItemOpened(item: ExplorerItem) {
        when(item){
            is ExplorerItem.Folder -> vBreadcrumbs.addItem(Krumb(item.title))
            is ExplorerItem.File -> vSearch.closeSearch()
        }
    }

    private fun getExplorerFragment() = supportFragmentManager.getFragment<ExplorerFragment>()

    private fun getSettingsFragment() = supportFragmentManager.getFragment<SettingsFragment>()

}