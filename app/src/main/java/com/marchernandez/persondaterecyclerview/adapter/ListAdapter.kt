package com.marchernandez.persondaterecyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.marchernandez.persondaterecyclerview.Person
import com.marchernandez.persondaterecyclerview.R

class ListAdapter(
    private var personsList: List<Person> = emptyList(),
    private val onRowClickListener: (Int) -> Unit,
) : RecyclerView.Adapter<ListViewHolder>() {

    fun updateList(newPersonsList: List<Person>) {
        val personsListDiff = ListDiffUtil(personsList, newPersonsList)
        val result = DiffUtil.calculateDiff(personsListDiff)
        personsList = newPersonsList
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ListViewHolder(layoutInflater.inflate(R.layout.item_person, parent, false))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.render(personsList[position])
        holder.itemView.setOnClickListener { onRowClickListener(position) }
    }

    override fun getItemCount(): Int = personsList.size
}