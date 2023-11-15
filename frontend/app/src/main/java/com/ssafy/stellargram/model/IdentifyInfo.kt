package com.ssafy.stellargram.model


// 사진 1개에 대해 인식된 정보
data class IdentifyResponse(
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
    var matched: List<IdentifyStarInfo>?,
    var matched_catID: List<Int>?
)

//data class IdentifyPhotoInfo (
//
//)

// 인식된 별 1개 정보
data class IdentifyStarInfo(
    var absmag: Long,
    var con: String,
    var id: Int,
    var mag: Long,
    var pixelx: Int,
    var pixely: Int
)