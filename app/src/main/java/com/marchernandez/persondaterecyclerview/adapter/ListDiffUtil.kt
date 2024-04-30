package com.marchernandez.persondaterecyclerview.adapter

import androidx.recyclerview.widget.DiffUtil
import com.marchernandez.persondaterecyclerview.Person

class ListDiffUtil(private val oldList: List<Person>, private val newList: List<Person>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItemPosition == newItemPosition
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}