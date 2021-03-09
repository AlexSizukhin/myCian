package com.shokker.mycian.Model

interface LocalStorage {
    fun loadStoredFilter():FilterState?
    fun saveFilter(filterState: FilterState)
}