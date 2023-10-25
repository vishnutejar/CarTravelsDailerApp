package com.cartravelsdailerapp.Repositorys.DAO

import androidx.room.*
import com.cartravelsdailerapp.models.CallHistory

@Dao
interface CallHistoryDao {
    @Query("SELECT * FROM CallHistory group by number ORDER BY id DESC LIMIT 10 OFFSET :offset")
    fun getAll(offset: Int): List<CallHistory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(listofCallHistory: List<CallHistory>)

    @Insert
    fun insertCallHistory(callHistory: CallHistory)

    @Delete
    fun delete(callHistory: CallHistory)

    @Query("SELECT * FROM CallHistory WHERE number = :number")
    fun callDataByNumber(number: String): List<CallHistory>

    @Query("SELECT * FROM CallHistory WHERE number || name LIKE '%' || :searchQuery || '%'")
    fun searchCall(searchQuery: String): List<CallHistory>
}