package com.mindera.rocketscience.ui


import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.mindera.rocketscience.databinding.StateLayoutBinding
import com.mindera.rocketscience.domain.UIState
import com.mindera.rocketscience.domain.UIStateListener
import com.mindera.rocketscience.domain.toInt
import com.mindera.rocketscience.model.Launch
import com.mindera.rocketscience.model.Order
import com.mindera.rocketscience.ui.adapter.ViewAdapter
import com.mindera.rocketscience.ui.viewmodel.MainViewModel
import com.mindera.rocketscience.ui.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity(), UIStateListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dialogBinding: FilterDialogBinding
    lateinit var filterDialog: AlertDialog
    lateinit var loaderDialog: AlertDialog
    lateinit var adapter: ViewAdapter
    lateinit var stateLayoutBinding: StateLayoutBinding
    private val mainViewModel by viewModels<MainViewModel>{MainViewModelFactory(
        MainApplication.getInfoRepository(), MainApplication.getLaunchRepository()
    )}
    private lateinit var uiStateListener: UIStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupAlertDialog()
        uiStateListener = this

        mainViewModel.getCompanyInfoAndLaunches()
        setupRecyclerViewAdapter()
        collectAndSubmitLaunches()
        collectAndShowCompanyInfo()

        dialogBinding.filterBtn.setOnClickListener {
            filterLaunches()
        }
    }

    private fun collectAndShowCompanyInfo() {
        lifecycleScope.launchWhenResumed {
            mainViewModel.infoFlow.collect { info ->
                binding.companyDetails.text = getString(
                    R.string.company_info,
                    info?.name,
                    info?.founder,
                    info?.founded,
                    info?.employees,
                    info?.launchSites,
                    info?.valuation
                )
            }
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
    private fun collectAndSubmitLaunches() {
        lifecycleScope.launchWhenResumed {
            mainViewModel.launchesFlowState.collect{ state ->
                when(state) {
                    is UIState.Loading -> {
                        uiStateListener.loading()
                    }
                    is UIState.Success<*> -> {
                        uiStateListener.onSuccess(state.data)
                    }
                }
            }
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
            R.id.showall -> {
                mainViewModel.getCompanyInfoAndLaunches()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private suspend fun getYearsForDialogDropDown() {
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

    private fun setupLoaderDialog() {
        val builder = MaterialAlertDialogBuilder(this)
        stateLayoutBinding = StateLayoutBinding.inflate(layoutInflater)
        builder.setView(stateLayoutBinding.root)
        loaderDialog = builder.create()
        loaderDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun <T> onSuccess(data: T) {
        loaderDialog.dismiss()
        binding.rcv.adapter = adapter
        adapter.addHeaderAndSubmitList(data as? List<Launch>)
    }

    override fun onError(error: String?) {
        TODO("Not yet implemented")
    }

    override fun loading() {
        setupLoaderDialog()
        loaderDialog.show()
    }
}

