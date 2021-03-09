package com.shokker.mycian.DI

import android.content.Context
import androidx.room.*
import com.shokker.mycian.Model.FilterState
import com.shokker.mycian.Model.LocalStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Module
@InstallIn(SingletonComponent::class)
class LocalStorageModule {
    @Provides
    fun provideLocalStorage(@ApplicationContext context:Context):LocalStorage{
       // FilterStateDB(@ApplicationContext context)

        return Room.databaseBuilder(
                context,
                FilterStateDB::class.java,
                "FilterStateDB"
        ).build()
    }
}

@Dao
interface FilterStateDao
{
    @Query("SELECT * from FILTERSTATE where id = 1")
    fun getCurrentFilterState():FilterState?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCurrentFilterState(filterState: FilterState)

    @Query("DELETE FROM FILTERSTATE")
    fun deleteAll()
}

@Database(entities = arrayOf(FilterState::class), version = 1, exportSchema = false)
@TypeConverters(FilterState.Converter::class)
public abstract class FilterStateDB: RoomDatabase(), LocalStorage {

    abstract fun filterStateDao():FilterStateDao
    override fun loadStoredFilter(): FilterState? {
        return filterStateDao().getCurrentFilterState()
    }

    override fun saveFilter(filterState: FilterState) {
        GlobalScope.launch { filterStateDao().saveCurrentFilterState(filterState) }
    }
}