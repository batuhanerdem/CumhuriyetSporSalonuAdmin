package com.example.cumhuriyetsporsalonuadmin.utils

data class SelectableData<T>(val data: T, var isSelected: Boolean) {
    fun getReversed(): SelectableData<T> {
        return SelectableData(this.data, !this.isSelected)
    }

    companion object {
        fun <T> List<T>.toSelectable(): List<SelectableData<T>> {
            val newList = mutableListOf<SelectableData<T>>()
            this.map {
                newList.add(SelectableData(it, false))
            }
            return newList
        }
    }
}