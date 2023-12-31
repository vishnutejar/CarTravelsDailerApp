package com.cartravelsdailerapp.viewmodels

import android.app.Application
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.cartravelsdailerapp.PrefUtils
import com.cartravelsdailerapp.Repositorys.CallLogsRepository
import com.cartravelsdailerapp.db.DatabaseBuilder
import com.cartravelsdailerapp.models.CallHistory
import com.cartravelsdailerapp.models.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivityViewModel(
    var context: Application,
    private val callLogsRepository: CallLogsRepository
) : AndroidViewModel(context) {
    private val _isCallLogsdb = MutableLiveData<Boolean>()
    val IsCallLogsdb: LiveData<Boolean>
        get() = _isCallLogsdb
    private val _callLogsdb = MutableLiveData<List<CallHistory>>()
    val callLogsdb: LiveData<List<CallHistory>>
        get() = _callLogsdb

    private val _callLogs = MutableLiveData<List<CallHistory>>()
    val callLogs: LiveData<List<CallHistory>>
        get() = _callLogs
    private val _newCallLogs = MutableLiveData<CallHistory>()
    val newCallLogs: LiveData<CallHistory>
        get() = _newCallLogs

    private val _contact = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> get() = _contact

    var db = DatabaseBuilder.getInstance(context).CallHistoryDao()
    suspend fun getCallLogsHistory() {
        viewModelScope.launch {
            _callLogs.value = callLogsRepository.fetchCallLogs()
            withContext(Dispatchers.Main) {
                db.insertAll(_callLogs.value!!)
                Log.d("insert data-->", _callLogs.value!!.count().toString())
                _isCallLogsdb.value=true
            }
        }
    }

    fun getAllCallLogsHistory(offset: Int): List<CallHistory> {
        return DatabaseBuilder.getInstance(context).CallHistoryDao().getAll(offset)
    }

    fun getAllContacts(offset: Int): List<Contact> {
        return DatabaseBuilder.getInstance(context).CallHistoryDao().getAllContacts(offset)
    }

    suspend fun getNewCallLogsHistory() {
        viewModelScope.launch {
            _newCallLogs.value = callLogsRepository.fetchCallLogSignle()
            _newCallLogs.value?.let { db.insertCallHistory(it) }
        }
    }

    suspend fun getAllContacts() {
        viewModelScope.launch {
            _contact.value = callLogsRepository.fetchContacts()
            withContext(Dispatchers.IO) {
                db.insertAllContacts(_contact.value!!)

            }

        }
    }

}

