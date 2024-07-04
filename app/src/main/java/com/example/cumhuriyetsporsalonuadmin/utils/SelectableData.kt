package com.example.cumhuriyetsporsalonuadmin.utils

data class SelectableData<T>(val data: T, var isSelected: Boolean) {
    fun getReversed(): SelectableData<T> {
        return SelectableData(this.data, !this.isSelected)
    }

    companion object {
        fun <T> MutableList<T>.toSelectable(): MutableList<SelectableData<T>> {
            return this.map { SelectableData(it, false) }.toMutableList()
        }

        fun <T> T.toSelectable(): SelectableData<T> {
            return SelectableData(this, false)
        }
    }
}