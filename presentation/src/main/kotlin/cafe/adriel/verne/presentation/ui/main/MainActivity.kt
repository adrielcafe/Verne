package cafe.adriel.verne.presentation.ui.main

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
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.presentation.R
import cafe.adriel.verne.presentation.extension.getFragment
import cafe.adriel.verne.presentation.helper.AnalyticsHelper
import cafe.adriel.verne.presentation.ui.BaseActivity
import cafe.adriel.verne.presentation.ui.main.explorer.ExplorerFragment
import cafe.adriel.verne.presentation.ui.main.explorer.listener.ExplorerFragmentListener
import cafe.adriel.verne.presentation.ui.main.preferences.PreferencesFragment
import cafe.adriel.verne.presentation.util.StateAware
import cafe.adriel.verne.shared.extension.tagOf
import com.ferfalk.simplesearchview.SimpleSearchView
import com.ferfalk.simplesearchview.SimpleSearchViewListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(), StateAware<MainViewState>,
    ExplorerFragmentListener {

    override val viewModel by viewModel<MainViewModel>()
    private val analyticsHelper by inject<AnalyticsHelper>()

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
        with(vSearch) {
            setTextColor(Color.WHITE)
            setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    getExplorerFragment()?.search(newText?.trim() ?: "")
                    analyticsHelper.logTextSearch()
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
            replace(R.id.vPreferences, getPreferencesFragment() ?: PreferencesFragment(),
                tagOf<PreferencesFragment>()
            )
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        viewModel.observeState(this, ::onStateUpdated)
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
            analyticsHelper.logVoiceSearch()
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onAttachFragment(fragment: Fragment) {
        when (fragment) {
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

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.action_preferences -> {
            vDrawer.openDrawer(GravityCompat.END)
            true
        }
        else -> false
    }

    override fun onStateUpdated(state: MainViewState) {
        // TODO
    }

    override fun onPrintHtml(fileName: String, html: String) {
        viewModel.printHtml(this, fileName, html)
        analyticsHelper.logPrint()
    }

    override fun onItemOpened(item: ExplorerItem) {
        when (item) {
            is ExplorerItem.Folder -> vBreadcrumbs.addItem(Krumb(item.title))
            is ExplorerItem.File -> vSearch.closeSearch()
        }
    }

    private fun getExplorerFragment() = supportFragmentManager.getFragment<ExplorerFragment>()

    private fun getPreferencesFragment() = supportFragmentManager.getFragment<PreferencesFragment>()
}
