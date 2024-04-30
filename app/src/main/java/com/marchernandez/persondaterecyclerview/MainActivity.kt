package com.marchernandez.persondaterecyclerview

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.marchernandez.persondaterecyclerview.adapter.ListAdapter
import com.marchernandez.persondaterecyclerview.databinding.ActivityMainBinding
import com.marchernandez.persondaterecyclerview.databinding.ItemAddPersonBinding
import com.marchernandez.persondaterecyclerview.databinding.ItemDetailPersonBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    private lateinit var listAdapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initRecyclerView()
        initUIState()

        binding.fabAdd.setOnClickListener { showAddPersonAlertDialog() }

        binding.fabAdd.setOnLongClickListener { view ->
            initSortMenu(view)
            true
        }
    }

    private fun initRecyclerView() {
        listAdapter = ListAdapter { position ->
            showPersonDetailAlertDialog(position)
        }

        binding.rvPersons.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            mainViewModel.persons.collect { list -> listAdapter.updateList(list) }
        }
    }

    private fun initSortMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.menu_sort, popupMenu.menu)
        popupMenu.gravity = Gravity.END

        popupMenu.setOnMenuItemClickListener { option ->
            when (option.itemId) {
                R.id.sort_name_ascending -> mainViewModel.sortBy(R.string.menu_sort_name_ascending)
                R.id.sort_name_descending -> mainViewModel.sortBy(R.string.menu_sort_name_descending)
                R.id.sort_date_ascending -> mainViewModel.sortBy(R.string.menu_sort_date_ascending)
                R.id.sort_date_descending -> mainViewModel.sortBy(R.string.menu_sort_date_descending)
                R.id.sort_insert_ascending -> mainViewModel.sortBy(R.string.menu_sort_insert_ascending)
                R.id.sort_insert_descending -> mainViewModel.sortBy(R.string.menu_sort_insert_descending)
            }
            return@setOnMenuItemClickListener false
        }
        popupMenu.show()
    }

    private fun showAddPersonAlertDialog() {
        val builder = MaterialAlertDialogBuilder(this)
        val addPersonBinding = ItemAddPersonBinding.inflate(LayoutInflater.from(applicationContext))
        val view = addPersonBinding.root
        val calendar = Calendar.getInstance()
        val dateFormat = getString(R.string.date_format)
        val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        val date = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)

            addPersonBinding.etDate.setText(simpleDateFormat.format(calendar.time))
        }

        builder.setView(view)
        addPersonBinding.etDate.setOnClickListener {
            DatePickerDialog(
                this,
                date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
            ).show()
        }

        builder.setPositiveButton(R.string.dialog_add_add) { _, _ ->
            if (mainViewModel.validateNameAndDate(
                    addPersonBinding.etName, addPersonBinding.etDate
                )
            ) {
                mainViewModel.addPerson(
                    addPersonBinding.etName.text.toString().trim(),
                    addPersonBinding.etDate.text.toString()
                )
            } else {
                showFillBothFieldsSnackbar()
                showAddPersonAlertDialog()
            }
        }
        builder.setNegativeButton(R.string.dialog_add_cancel, null)
        builder.setCancelable(false)
        builder.show()
    }

    private fun showPersonDetailAlertDialog(position: Int) {
        val builder = MaterialAlertDialogBuilder(this)
        val detailBinding = ItemDetailPersonBinding.inflate(LayoutInflater.from(applicationContext))
        val view = detailBinding.root

        builder.setView(view)
        detailBinding.tvPersonName.text =
            getString(R.string.dialog_detail_name, mainViewModel.persons.value[position].name)
        detailBinding.tvPersonDate.text =
            getString(R.string.dialog_detail_date, mainViewModel.persons.value[position].date)
        builder.setPositiveButton(R.string.dialog_detail_close, null)
        builder.setCancelable(false)
        builder.show()
    }

    private fun showFillBothFieldsSnackbar() {
        Snackbar.make(binding.root, R.string.dialog_add_wrong_input, Snackbar.LENGTH_SHORT)
            .setAnchorView(binding.fabAdd).show()
    }
}