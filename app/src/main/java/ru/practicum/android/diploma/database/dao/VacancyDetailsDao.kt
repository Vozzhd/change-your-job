package ru.practicum.android.diploma.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.database.entities.VacancyDetailsEntity

@Dao
interface VacancyDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVacancy(vacancy: VacancyDetailsEntity)

    @Query("DELETE FROM $TABLE_NAME where id = :id")
    suspend fun deleteVacancyById(id: String)

    @Query("SELECT * FROM $TABLE_NAME")
    fun getVacancies(): Flow<List<VacancyDetailsEntity>>

    @Query("SELECT * FROM $TABLE_NAME WHERE id = :id order by timeAdd DESC")
    fun getVacancyByID(id: String): Flow<VacancyDetailsEntity>

    @Update
    suspend fun updateVacancy(vacancy: VacancyDetailsEntity)

    companion object {
        const val TABLE_NAME = "vacancy_details"
    }
}
