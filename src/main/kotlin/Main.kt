import java.io.*
import kotlin.math.*
import kotlin.system.measureNanoTime

fun readData(): MutableList<DoubleArray>{
    val file = File("E:\\Kotlin\\kotlin-example\\src\\main\\kotlin\\hyg_v37.csv")
    val br = BufferedReader(FileReader(file))

//    val br = BufferedReader(InputStreamReader(System.`in`))
    val bw = BufferedWriter(OutputStreamWriter(System.`out`))

    val header: List<String> = br.readLine().split(",")
    var starData = mutableListOf<DoubleArray>()

    br.lines().forEach{
        val attrList = it.split(',')

        val id = if(attrList[0] == "") -1.0 else attrList[0].toDouble()
        val mag = if(attrList[13] == "") 999.0 else attrList[13].toDouble()
        val ci = if(attrList[16] == "") 999.0 else attrList[16].toDouble()
        val ra = if(attrList[16] == "") 999.0 else attrList[23].toDouble()
        val dec = if(attrList[16] == "") 999.0 else attrList[24].toDouble()

        starData.add(doubleArrayOf(ra, dec, ci, mag, id))
    }

    val rowLeng: Int = starData.size
    val colLeng: Int = 5
    return starData
}

fun getMeanSiderealTime(longitude: Double): Double{
    val JD: Double = (System.currentTimeMillis() * 0.001) / 86400.0 +  2440587.5
    val GMST = 18.697374558 + 24.06570982441908*(JD - 2451545)
    val theta = (GMST * 15.0 + (longitude)) % 360.0
    return theta * PI / 180.0
}

fun getAllRisedStars(longitude: Double, latitude: Double, sidereal: Double, starList: Array<DoubleArray>): Array<DoubleArray>{

    val new_latitude = latitude * PI / 180.0

    val sinPhi = sin(new_latitude)
    val cosPhi = cos(new_latitude)

    var starArray = Array(starList.size) {DoubleArray(6)}

    for (i in 0 until starList.size){
        val hourAngle = sidereal - starList[i][0]
        val sinDec = sin(starList[i][1])
        val cosDec = cos(starList[i][1])
        val sina = sinDec * sinPhi + cosDec * cosPhi * cos(hourAngle)
        val cosa = sqrt(1.0 - (sina * sina))
        val sinA = -sin(hourAngle) * cosDec / cosa
        val cosA = (sinDec -(sinPhi * sina)) / (cosPhi * cosa)

        starArray[i][0] = cosa * cosA
        starArray[i][1] = cosa * sinA
        starArray[i][2] = sina
        starArray[i][3] = starList[i][2]
        starArray[i][4] = starList[i][3]
        starArray[i][5] = starList[i][4]
    }
    return starArray
}

fun getSight(_theta: Double, _phi: Double, starData: Array<DoubleArray>): Array<DoubleArray> {
    val theta = _theta * PI / 180.0
    val phi = _phi * PI / 180.0
    val cosTheta = cos(theta)
    val sinTheta = sin(theta)
    val cosPhi = cos(phi)
    val sinPhi = sin(phi)

    val transMatrix = arrayOf(
        doubleArrayOf(cosTheta * cosPhi, -cosTheta * sinPhi, sinTheta),
        doubleArrayOf(sinTheta * cosPhi, -sinTheta * sinPhi, -cosTheta),
        doubleArrayOf(sinPhi, cosPhi, 0.0)
    )
    val resultMatrix = Array(starData.size) {DoubleArray(5)}

    for(i in 0 until starData.size){
        resultMatrix[i][2] = starData[i][3]
        resultMatrix[i][3] = starData[i][4]
        resultMatrix[i][4] = starData[i][5]

        val temp = DoubleArray(3)
        for(j in 0 until 3){
            for(k in 0 until 3){
                temp[j] += (starData[i][k] * transMatrix[k][j])
            }
        }
        val a = asin(temp[2])
        val cosa = cos(a)
        if(abs(cosa) <1.0E-6){
            resultMatrix[i][0] = 0.0
            resultMatrix[i][1] = 10000.0
            continue
        }
        val _sin = starData[i][1] / cosa
        val _cos = starData[i][0] / cosa

        val new_theta = if(_cos > 0) asin(_sin) else PI - asin(_sin)
        resultMatrix[i][0] = new_theta
        resultMatrix[i][1] = ln(abs((1 + sin(a)) / cosa))
    }
    return resultMatrix
}

fun main(){
//    먼저 입출력을 받는다.
    val readTime = measureNanoTime {
        val _readData = readData()
        var starData = _readData.toTypedArray()
    }
    println("Read time: ${readTime * 0.000001} ms")
    val _readData = readData()
    var starData = _readData.toTypedArray()

    val longitude: Double = 127.039611
    val latitude: Double = 37.501254

    val elapsedTime1 = measureNanoTime {
        val LST = getMeanSiderealTime(longitude)
    }
    val LST = getMeanSiderealTime(longitude)
    println("***** Get LST *****")
    println("Elapsed time: ${elapsedTime1 * 0.000001} ms")

    val elapsedTime2 = measureNanoTime {
        val starArray = getAllRisedStars(longitude, latitude, LST, starData)
    }

    println("***** Conversion between horizontal and equatorial systems *****")
    println("Elapsed time: ${elapsedTime2 * 0.000001} ms")
    val starArray = getAllRisedStars(longitude, latitude, LST, starData)

    val elapsedTime3 = measureNanoTime {
        val result = getSight(30.0, 60.0, starArray)
    }

    println("***** Conversion between x, y, z vectors to Mercator System *****")
    println("Elapsed time: ${elapsedTime3 * 0.000001} ms")
    val result = getSight(30.0, 60.0, starArray)
}