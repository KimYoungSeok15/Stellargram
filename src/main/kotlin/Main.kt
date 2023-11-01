
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberProperties


fun <T> changePrivateValField(instance: T, fieldName: String, changeValue: Any?) {
    val declaredFields = instance!!::class.java.declaredFields
    val findField = declaredFields.toList().first { it.name == fieldName }
    findField.set(instance, changeValue)
}

fun typeCast(origin: String, typeName: String): Any?{
    when(typeName){
        "Int?" -> return origin.toIntOrNull()
        "Double?" -> return origin.toDoubleOrNull()
        "String?" -> return origin
        else -> throw IllegalArgumentException("Type mismatch")
    }
}

fun checkEntity(filename: String, typeList: List<String>, sourceNames: List<String>, targetNames: List<String>): MutableList<Star>{

    val replaceMap = HashMap<String, String>();
    sourceNames.forEachIndexed { index, source -> replaceMap[source] = targetNames[index] }

    val file = File(filename)
    val inputStream = file.inputStream()
    val reader = inputStream.bufferedReader()
    val header = reader.readLine()
    val attrList = header.split(',').toMutableList()
    for((i, item) in attrList.withIndex()){
        for((j, source) in sourceNames.withIndex()){
            if(item == source){
                attrList[i] = targetNames[j]
            }
        }
    }

    var res = mutableListOf<Star>()

    var line :String?
    var i = 0
    while(reader.readLine().also { line = it } != null){
        i++
        if(i > 100) break

        val clazz = Class.forName("Star").kotlin

        var newInstance = clazz.createInstance()
        val values = line?.split(",")
        for(i in 0..<values?.size as Int){
            val newValue: Any? = typeCast(values[i], typeList[i])
            val propertyName = attrList[i]
            changePrivateValField(newInstance, propertyName, newValue)
            println(attrList[i])
            println(newValue)
        }
        println(newInstance.toString())
        if(newInstance is Star)
            res.add(newInstance)
    }
    return res
}

fun insertStar(stars: MutableList<Star>) {
    val properties = Star::class.declaredMemberProperties
    var propertyNames: MutableList<String> = mutableListOf()
    for (property in properties) {
        propertyNames.add(property.name)
    }
    transaction {
        SchemaUtils.create(Stars)
    }
    var i = 0
    for (star in stars) {
        i++
        if((i%1000) == 0) println(i)
        val clazz = star.javaClass.kotlin
        println(star.id)
        transaction {
            val insertStatement = Stars.insert {
                it[absmag] = star.absmag
                it[base] = star.base
                it[bayer] = star.bayer
                it[bf] = star.bf
                it[ci] = star.ci
                it[comp] = star.comp
                it[comp_primary] = star.comp_primary
                it[con] = star.con
                it[dec] = star.dec
                it[decrad] = star.decrad
                it[dist] = star.dist
                it[flam] = star.flam
                it[gl] = star.gl
                it[hd] = star.hd
                it[hip] = star.hip
                it[hr] = star.hr
                it[id] = star.id?: 0
                it[lum] = star.lum
                it[mag] = star.mag
                it[pmdec] = star.pmdec
                it[pmdecrad] = star.pmdecrad
                it[pmra] = star.pmra
                it[pmrarad] = star.pmrarad
                it[proper] = star.proper
                it[ra] = star.ra
                it[rarad] = star.rarad
                it[rv] = star.rv
                it[spect] = star.spect
                it[var_max] = star.var_max
                it[var_min] = star.var_min
                it[variable] = star.variable
                it[vx] = star.vx
                it[vy] = star.vy
                it[vz] = star.vz
                it[x] = star.x
                it[y] = star.y
                it[z] = star.z
            }
        }
    }
}

fun main(args: Array<String>) {
    val typeList = listOf("Int?", "Int?", "Int?", "Int?", "String?", "String?", "String?", "Double?", "Double?", "Double?", "Double?", "Double?", "Double?", "Double?", "Double?", "String?", "Double?", "Double?", "Double?", "Double?", "Double?", "Double?", "Double?", "Double?", "Double?", "Double?", "Double?", "String?", "Int?", "String?", "Int?", "Int?", "String?", "Double?", "String?", "Double?", "Double?")

    val currentPath = System.getProperty("user.dir")
    println(currentPath)

    var starList: MutableList<Star> = checkEntity("E:/Kotlin/kotlin-example/src/notebooks/hyg_v37.csv", typeList, listOf("var"), listOf("variable"))

    val dat = Database.connect("jdbc:sqlite:E:/Kotlin/kotlin-example/src/notebooks/stellargram.db", driver = "org.sqlite.JDBC")
    insertStar(starList)
}

object Stars: Table("stars") {
	val id = integer("id")
	val hip = integer("hip").nullable()
	val hd = integer("hd").nullable()
	val hr = integer("hr").nullable()
	val gl = varchar("gl", 15).nullable()
	val bf = varchar("bf", 15).nullable()
	val proper = varchar("proper", 15).nullable()
	val ra = double("ra").nullable()
	val dec = double("dec").nullable()
	val dist = double("dist").nullable()
	val pmra = double("pmra").nullable()
	val pmdec = double("pmdec").nullable()
	val rv = double("rv").nullable()
	val mag = double("mag").nullable()
	val absmag = double("absmag").nullable()
	val spect = varchar("spect", 15).nullable()
	val ci = double("ci").nullable()
	val x = double("x").nullable()
	val y = double("y").nullable()
	val z = double("z").nullable()
	val vx = double("vx").nullable()
	val vy = double("vy").nullable()
	val vz = double("vz").nullable()
	val rarad = double("rarad").nullable()
	val decrad = double("decrad").nullable()
	val pmrarad = double("pmrarad").nullable()
	val pmdecrad = double("pmdecrad").nullable()
	val bayer = varchar("bayer", 15).nullable()
	val flam = integer("flam").nullable()
	val con = varchar("con", 15).nullable()
	val comp = integer("comp").nullable()
	val comp_primary = integer("comp_primary").nullable()
	val base = varchar("base", 15).nullable()
	val lum = double("lum").nullable()
	val variable = varchar("variable", 15).nullable()
	val var_min = double("var_min").nullable()
	val var_max = double("var_max").nullable()

    override val primaryKey = PrimaryKey(id)
}
