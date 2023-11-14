package com.ssafy.stellargram.model
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "response")
data class AstronomicalEventResponse(
    @Element(name = "header")
    val header: AstroHeader?,

    @Element(name = "body")
    val body: AstroBody?
)

@Xml(name = "header")
data class AstroHeader(
    @PropertyElement(name = "resultCode")
    val resultCode: String,

    @PropertyElement(name = "resultMsg")
    val resultMsg: String
)

@Xml(name = "body")
data class AstroBody(
    @Element(name = "items")
    val items: AstroItems,

    @PropertyElement(name = "numOfRows")
    val numOfRows: Int,

    @PropertyElement(name = "pageNo")
    val pageNo: Int,

    @PropertyElement(name = "totalCount")
    val totalCount: Int
)

@Xml(name = "items")
data class AstroItems(
    @Element(name = "item")
    val item: List<AstroItem>
)

@Xml
data class AstroItem(
    @PropertyElement(name = "astroEvent")
    var astroEvent: String?,

    @PropertyElement(name = "astroTime")
    var astroTime: String?,

    @PropertyElement(name = "astroTitle")
    var astroTitle: String?,

    @PropertyElement(name = "locdate")
    var locdate: String?,

    @PropertyElement(name = "seq")
    var seq: Int?
){
    constructor() : this(null,null,null,null,null)
}