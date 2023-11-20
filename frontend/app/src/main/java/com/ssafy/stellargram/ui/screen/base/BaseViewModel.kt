package com.ssafy.stellargram.ui.screen.base

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.integration.compose.GlideImage
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.MemberMeResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor() : ViewModel() {
    private val _isDialogOpen = mutableStateOf(false)
    val isDialogOpen: Boolean
        get() = _isDialogOpen.value

    private val _memberInfo = MutableStateFlow<MemberMeResponse.Data?>(null)
    val memberInfo: StateFlow<MemberMeResponse.Data?>
        get() = _memberInfo

    fun openProfileModificationDialog() {
        viewModelScope.launch {
            try {
                val response = NetworkModule.provideRetrofitInstance().getMemberMe()
                val memberInfo = response.body()?.data
                _memberInfo.value = memberInfo
                Log.d("프로필수정", "openProfileModificationDialog: ${_memberInfo.value}")
                _isDialogOpen.value = true
                Log.d("프로필수정", "openProfileModificationDialog: ${isDialogOpen}")
            } catch (e:Exception) {
                Log.d("프로필수정", "openProfileModificationDialog: $e")
            }
            // Update state to open the dialog

        }
    }

    fun closeProfileModificationDialog() {
        _isDialogOpen.value = false
    }
}
