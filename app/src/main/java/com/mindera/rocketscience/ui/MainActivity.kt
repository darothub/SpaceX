package com.mindera.rocketscience.ui


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.RadioButton
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mindera.rocketscience.MainApplication
import com.mindera.rocketscience.R
import com.mindera.rocketscience.databinding.ActivityMainBinding
import com.mindera.rocketscience.databinding.FilterDialogBinding
import com.mindera.rocketscience.domain.toInt
import com.mindera.rocketscience.model.Launch
import com.mindera.rocketscience.model.Order
import com.mindera.rocketscience.ui.viewmodel.MainViewModel
import com.mindera.rocketscience.ui.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dialogBinding: FilterDialogBinding
    lateinit var filterDialog: AlertDialog
    lateinit var adapter: ViewAdapter
    private val mainViewModel by viewModels<MainViewModel>{MainViewModelFactory(
        MainApplication.getInfoRepository(), MainApplication.getLaunchRepository()
    )}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupAlertDialog()

        mainViewModel.getLaunches()
        setupRecyclerViewAdapter()
        lifecycleScope.launchWhenStarted {
            showCompanyInfo()
        }
        lifecycleScope.launchWhenStarted {
            collectAndSubmitLaunches(adapter)
        }
        dialogBinding.filterBtn.setOnClickListener {
            filterLaunches()
        }
    }

    private fun setupRecyclerViewAdapter() {
        adapter = ViewAdapter {launch, view->
            onEachLaunchClick(launch, view)
        }
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcv.layoutManager = layoutManager
        binding.rcv.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun onEachLaunchClick(launch: Launch, view: View) {
        val popup = PopupMenu(this, view)
        popup.setOnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.youtube -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(launch.links?.videoLink))
                    startActivity(intent)
                    true
                }
                R.id.wikipedia -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(launch.links?.wikipedia))
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        popup.inflate(R.menu.option_menu)
        try {
            val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldMPopup.isAccessible = true
            val mPopup = fieldMPopup.get(popup)
            mPopup.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(mPopup, true)
        }
        catch (e: Exception){
            Log.e("Main", "Error displaying menu icon")
        }
        finally {
            popup.show()
        }

    }

    private suspend fun showCompanyInfo() {
        val info = mainViewModel.getCompanyInfo()
        binding.companyDetails.text = getString(
            R.string.company_info,
            info.name,
            info.founder,
            info.founded,
            info.employees,
            info.launchSites,
            info.valuation
        )
    }

    private suspend fun collectAndSubmitLaunches(adapter: ViewAdapter) {
        mainViewModel.launchesFlow.collect {
            binding.rcv.adapter = adapter
            adapter.addHeaderAndSubmitList(it)
        }
    }

    private fun filterLaunches() {
        val year = dialogBinding.dropDownAc.text.toString()
        val resultRadioId = dialogBinding.resultRadiogrp.checkedRadioButtonId
        val resultRadio = dialogBinding.root.findViewById<RadioButton>(resultRadioId)
        val result = when (resultRadio.text) {
            getString(R.string.successful) -> true
            else -> false
        }
        val orderRadioId = dialogBinding.orderRadiogrp.checkedRadioButtonId
        val orderRadio = dialogBinding.root.findViewById<RadioButton>(orderRadioId)
        Log.d("FilterMain", orderRadio.text.toString())
        val order = when (orderRadio.text) {
            getString(R.string.asc) -> Order.ASC
            else -> Order.DESC
        }
        Log.d("FilterMain", "Year $year, result: $result, order:$order")
        mainViewModel.filterLaunches(year, result.toInt(), order.toInt())
        filterDialog.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.isChecked = item.isChecked
        when (item.itemId) {
            R.id.filter -> {
                lifecycleScope.launchWhenResumed {
                    getYearsForDialogDropDown()
                }
                filterDialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    suspend fun getYearsForDialogDropDown() {
        mainViewModel.getAllYears().collect {
            val adapter = ArrayAdapter(this, R.layout.drop_down_text_item, it)
            dialogBinding.dropDownAc.setAdapter(adapter)
        }
    }

    private fun setupAlertDialog() {
        val builder = MaterialAlertDialogBuilder(this)
        dialogBinding = FilterDialogBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)
        filterDialog = builder.create()
    }
}

