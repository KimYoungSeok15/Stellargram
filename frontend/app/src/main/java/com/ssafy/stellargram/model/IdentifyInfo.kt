package com.ssafy.stellargram.model

// 인식 응답
data class IdentifyResponse(
    val code: Int,
    val message: String,
    val data: MessagesData
)

// 사진 1개에 대해 인식된 정보
data class IdentifyPhotoInfo (
    var Dec: Long?,// 사진 정가운데의 적위
    var FOV: Long?,
    var Matches: Int?, //매칭된 별 갯수
    var Prob: String?,
    var RA: Long?, // 사진 정가운데의 적경
    var RMSE: Long?,
    var Roll: Long?,
    var T_extract: Long, // 별 검출 소요시간
    var T_solve: Long, // 별 인식 소요시간
    var distortion: Long?,
    var matched: List<IdentifyStarInfo>?, // 인식된 별들의 정보
    var matched_catID: List<Int>? // 인식된 별들의 hip 아이디

)

// TODO: 백 응답 수정되면 수정할 것
// 인식된 별 1개 정보
data class IdentifyStarInfo(
    var absmag: Long, // 절대등급
    var con: String,
    var id: Int, // db내 
    var mag: Long, // 등급
    var pixelx: Int,
    var pixely: Int
)