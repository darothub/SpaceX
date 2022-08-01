package com.mindera.rocketscience.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.activity.viewModels
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.core.widget.PopupMenuCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mindera.rocketscience.MainApplication
import com.mindera.rocketscience.R
import com.mindera.rocketscience.databinding.ActivityMainBinding
import com.mindera.rocketscience.databinding.FilterDialogBinding
import com.mindera.rocketscience.domain.toInt
import com.mindera.rocketscience.model.Order
import com.mindera.rocketscience.ui.viewmodel.MainViewModel
import com.mindera.rocketscience.ui.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dialogBinding: FilterDialogBinding
    lateinit var dialog: Dialog
    private val filterDialog by lazy {
        Dialog(this, R.style.DialogTheme).apply {
            dialogBinding = FilterDialogBinding.inflate(layoutInflater)
            setContentView(dialogBinding.root)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(this.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            val window = this.window
            window?.attributes = lp
            window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            setCanceledOnTouchOutside(true)
        }
    }
    private val mainViewModel by viewModels<MainViewModel>{MainViewModelFactory(
        MainApplication.getInfoRepository(), MainApplication.getLaunchRepository()
    )}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        dialog = setDialogDropDown()

        mainViewModel.getLaunches()
        val adapter = ViewAdapter{}
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcv.layoutManager = layoutManager
        binding.rcv.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        lifecycleScope.launchWhenStarted {
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
        lifecycleScope.launchWhenStarted {
            mainViewModel.launchesFlow.collect {
                Log.d("MainBefore", "$it")
                binding.rcv.adapter = adapter
                adapter.addHeaderAndSubmitList(it)
                Log.d("MainAfter", "$it")
            }
        }
        dialogBinding.filterBtn.setOnClickListener {
            val year = dialogBinding.dropDownAc.text.toString()
            val resultRadioId = dialogBinding.resultRadiogrp.checkedRadioButtonId
            val resultRadio = dialogBinding.root.findViewById<RadioButton>(resultRadioId)
            val result = when(resultRadio.text){
                getString(R.string.successful) -> true
                else -> false
            }
            val orderRadioId = dialogBinding.resultRadiogrp.checkedRadioButtonId
            val orderRadio = dialogBinding.root.findViewById<RadioButton>(orderRadioId)
            val order = when(orderRadio.text){
                getString(R.string.asc) -> Order.ASC
                else -> Order.DESC
            }
            Log.d("FilterMain", "Year $year, result: $result, order:$order")
            mainViewModel.filterLaunches(year, result.toInt(), order)
            dialog.dismiss()
        }

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
                dialog.show()
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
    private fun setDialogDropDown() =
        Dialog(this, R.style.DialogTheme).apply {
            dialogBinding = FilterDialogBinding.inflate(layoutInflater)
            setContentView(dialogBinding.root)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(this.window!!.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            val window = this.window
            window?.attributes = lp
            window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            setCanceledOnTouchOutside(true)
        }

}
