package com.ssafy.stellargram.ui.screen.chat

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ssafy.stellargram.StellargramApplication
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.ChatRoom
import com.ssafy.stellargram.model.CombinedChatRoom
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatRoomListViewModel @Inject constructor() : ViewModel() {
    // 내 아이디 가져오기
    private val myId = StellargramApplication.prefs.getString("myId", "").toLong()

    // 방 갯수
    var roomCount: Int by mutableIntStateOf(-2)

    // 방 목록
    private val _rooms: MutableList<CombinedChatRoom> = mutableStateListOf()
    val rooms: List<CombinedChatRoom> = _rooms

    // 통신 실패 여부
    private val _isFailed: MutableState<Boolean> = mutableStateOf(false)
    val isFailed = _isFailed.value

    // 내가 참여한 채팅방 정보 가져오기
    suspend fun getRoomInfo() {
        // 실패여부 저장변수 리셋
        _isFailed.value = false

        // 방 목록 리셋
        _rooms.clear()

        // 내가 참여한 채팅방 리스트 요청
        val response = NetworkModule.provideRetrofitInstanceChat().getRoomList(myId)

        // 응답 성공적으로 왔다면
        if (response?.code == 200) {
            // 방갯수 저장
            this.roomCount = response.data.roomCount

            // 방 정보 임시저장
            val tempRoomList: List<ChatRoom> = response.data.roomList

            // 응답 온 방 1개에 대해
            for (room in tempRoomList) {
                // 위도 경도 파싱
                val latilong = room.observeSiteId.split("-")
                val thisLatitude = latilong[0].toDouble() / 1000
                val thisLongitude = latilong[1].toDouble() / 1000
                // 새 방정보 dto에 담기
                val newRoomInfo = CombinedChatRoom(
                    roomId = room.roomId,
                    personnel = room.personnel,
                    observeSiteId = room.observeSiteId,
                    latitude = thisLatitude,
                    longitude = thisLongitude,
                    siteName = "알 수 없음"
                )

                // 방 1개에 연결된 관측소 정보 요청
                val responseSite =
                    NetworkModule.provideRetrofitInstanceSite()
                        .getSiteInfoById(
                            room.observeSiteId
                        )

                // 응답이 성공적이었다면
                if (responseSite?.code == 200) {
                    // 이름을 새 방정보에 담기
                    if (responseSite.data != null)
                        newRoomInfo.siteName = responseSite.data.name
                }

                // 새 방정보를 리스트에 추가
                _rooms.add(newRoomInfo)
            }
        } else _isFailed.value = true
    }
}