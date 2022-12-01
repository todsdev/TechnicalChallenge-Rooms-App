package com.tods.rooms.ui.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tods.rooms.data.model.api_model.ResponseModel
import com.tods.rooms.repository.RoomsRepository
import com.tods.rooms.state.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val repository: RoomsRepository
): ViewModel() {
    private val _search = MutableStateFlow<ResourceState<ResponseModel>>(ResourceState.Loading())
    var search: StateFlow<ResourceState<ResponseModel>> = _search

    fun fetch(to: String, amount: Float) = viewModelScope.launch {
        safeFetch(to, amount)
    }

    private suspend fun safeFetch(to: String, amount: Float) {
        _search.value = ResourceState.Loading()
        try {
            val response = repository.recoverCurrencyInformation(to, amount)
            _search.value = handleResponse(response)
        } catch(t: Throwable) {
            when(t) {
                is IOException -> _search.value = ResourceState.Error("An error with internet connection happened")
                else -> _search.value = ResourceState.Error("An error converting data occurred")
            }
        }
    }

    private fun handleResponse(response: Response<ResponseModel>): ResourceState<ResponseModel> {
        if (response.isSuccessful) {
            response.body().let { values ->
                return ResourceState.Success(values)
            }
        }
        return ResourceState.Error(response.message())
    }
}