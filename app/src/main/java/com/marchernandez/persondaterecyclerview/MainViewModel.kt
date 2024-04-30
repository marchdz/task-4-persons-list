package com.marchernandez.persondaterecyclerview

import android.widget.EditText
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private var _persons = MutableStateFlow<List<Person>>(emptyList())
    val persons: StateFlow<List<Person>> = _persons

    fun validateNameAndDate(vararg editTexts: EditText): Boolean {
        return editTexts.all { it.text.toString().trim().isNotEmpty() }
    }

    fun addPerson(name: String, date: String) {
        _persons.update { it.plus(Person(_persons.value.size, name, date)) }
    }

    fun sortBy(criteria: Int) {
        _persons.value = when (criteria) {
            R.string.menu_sort_name_ascending -> _persons.value.sortedBy { it.name }
            R.string.menu_sort_name_descending -> _persons.value.sortedBy { it.name }.reversed()
            R.string.menu_sort_date_ascending -> _persons.value.sortedBy { it.date }
            R.string.menu_sort_date_descending -> _persons.value.sortedBy { it.date }.reversed()
            R.string.menu_sort_insert_ascending -> _persons.value.sortedBy { it.position }
            else -> _persons.value.sortedBy { it.position }.reversed()
        }
    }
}