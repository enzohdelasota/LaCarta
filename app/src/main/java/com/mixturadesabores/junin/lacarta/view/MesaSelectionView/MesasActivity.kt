package com.mixturadesabores.junin.lacarta.view.MesaSelectionView

import android.app.Activity
import android.app.ActionBar
import android.app.FragmentTransaction
import android.databinding.DataBindingUtil

import android.support.v4.view.ViewPager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.mixturadesabores.junin.domain.entities.Nivel

import com.mixturadesabores.junin.lacarta.R
import com.mixturadesabores.junin.lacarta.databinding.ActivityMesasBinding
import com.mixturadesabores.junin.lacarta.viewmodel.LevelViewModel

class MesasActivity : Activity(), LevelViewModel.MainActivityViewModel, ActionBar.TabListener {

    private lateinit var activityMesasBinding: ActivityMesasBinding
    private lateinit var levelViewModel: LevelViewModel

    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMesasBinding = DataBindingUtil.setContentView(this, R.layout.activity_mesas)
        levelViewModel = LevelViewModel(this)
        activityMesasBinding.mainViewModel = levelViewModel

        val actionBar = actionBar
        actionBar!!.navigationMode = ActionBar.NAVIGATION_MODE_TABS

        levelViewModel.fetchLevelList(this)

        viewPager = activityMesasBinding.container
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_mesas, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onTabSelected(tab: ActionBar.Tab, fragmentTransaction: FragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        viewPager!!.currentItem = tab.position
    }

    override fun onTabUnselected(tab: ActionBar.Tab, fragmentTransaction: FragmentTransaction) {}

    override fun onTabReselected(tab: ActionBar.Tab, fragmentTransaction: FragmentTransaction) {}

    override fun endCallProgress(any: Any?) {
        if (any is Throwable) {
            println("Error: " + any.message)
        } else {
            val adapter = NivelesPagerAdapter(fragmentManager, any as MutableList<Nivel>)
            viewPager.adapter = adapter

            for (i in 0..adapter.count - 1) {
                actionBar.addTab(
                        actionBar.newTab()
                                .setText(getString(R.string.tab_level_title) + adapter.getPageTitle(i))
                                .setTabListener(this)
                )
            }

            viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener(){
                override fun onPageSelected(position: Int) {
                    actionBar.setSelectedNavigationItem(position)
                }
            })
        }
    }

    override fun onDestroy() {
        levelViewModel.dispose()
        super.onDestroy()
    }
}
