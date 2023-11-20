package com.ssafy.stellargram.model

// 인식 응답
data class IdentifyResponse(
    val code: Int,
    val message: String,
    val data: IdentifyPhotoData
)

// 사진 1개에 대해 인식된 정보
data class IdentifyPhotoData (
    var Dec: Double?,// 사진 정가운데의 적위
    var FOV: Double?,
    var Matches: Int?, //매칭된 별 갯수
    var Prob: String?,
    var RA: Double?, // 사진 정가운데의 적경
    var RMSE: Double?,
    var Roll: Double?,
    var T_extract: Double?, // 별 검출 소요시간
    var T_solve: Double?, // 별 인식 소요시간
    var distortion: Double?, // 왜곡 정보
    var matched: List<IdentifyStarInfo>?, // 인식된 별들의 정보
    var matched_catID: List<Int>? // 인식된 별들의 hip 아이디

)

// 인식된 사진 메타정보 분리
data class IdentifyPhotoInfo (
    var Dec: Double?,// 사진 정가운데의 적위
    var FOV: Double?,
    var Matches: Int?, //매칭된 별 갯수
    var Prob: String?,
    var RA: Double?, // 사진 정가운데의 적경
    var RMSE: Double?,
    var Roll: Double?,
    var T_extract: Double?, // 별 검출 소요시간
    var T_solve: Double?, // 별 인식 소요시간
    var distortion: Double? // 왜곡 정보
)

// 인식된 별 1개 정보
data class IdentifyStarInfo(
    var absmag: Double, // 절대등급
    var con: String,
    var id: Int, // db 내 id
    var hipid: Int, //hip 리스트의 아이디값
    var mag: Double, // 등급
    var pixelx: Int,
    var pixely: Int,
    var name: String?
)