package com.shokker.mycian.Model

import android.location.Location

interface UserCase {
    fun loadSavedFilterState()        // :FilterState
    fun saveFilterState(state: FilterState)

    fun getFlatList(state:FilterState, locationBox: Pair<Location,Location>) //:List<ResultFlat>

    fun showResultOnMap(state:FilterState, locationBox: Pair<Location,Location>)
    fun showOnSelectedItem()

}