package com.marchernandez.persondaterecyclerview.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.marchernandez.persondaterecyclerview.Person
import com.marchernandez.persondaterecyclerview.databinding.ItemPersonBinding

class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemPersonBinding.bind(view)

    fun render(person: Person) {
        binding.tvPersonName.text = person.name
    }
}