#include <jni.h>
#include <string>
#include <vector>
#include <thread>
#include <android/log.h>

std::vector<std::vector<double>> starList = {};
std::vector<std::vector<double>> constellationLineList = {};

std::vector<std::vector<double>> starTrans = {};
std::vector<std::vector<double>> constTrans = {};

std::vector<std::vector<double>> starResult = {};
std::vector<std::vector<double>> constResult = {};

std::vector<double> temp(3, 0.0);
//---------------------------------------------------------------
/*
 * starList, constellationLineList 최초 등록
*/

extern "C"{
    JNIEXPORT void JNICALL
    Java_com_ssafy_stellargram_ui_MainActivity_setStarList(
            JNIEnv* env,
            jobject,
            jobjectArray array
    ){
        jsize rowSize = env -> GetArrayLength(array);

        for(jsize i = 0; i < rowSize; i++) {
            jdoubleArray oneRow = (jdoubleArray)env ->GetObjectArrayElement(array, i);
            jsize colSize = env -> GetArrayLength(oneRow);
            std::vector<double> row(colSize);
            env ->GetDoubleArrayRegion(oneRow, 0, colSize, &row[0]);
            env ->DeleteLocalRef(oneRow);

            starList.push_back(row);
        }
        starTrans.resize(starList.size(), std::vector<double>(6, 0.0));
        starResult.resize(starList.size(), std::vector<double>(5, 0.0));

        std::vector<std::thread> threads;
        int threadCount = std::thread::hardware_concurrency();

        __android_log_print(ANDROID_LOG_DEBUG, "test", "thread count: %d", threadCount);
    };

    JNIEXPORT void JNICALL
    Java_com_ssafy_stellargram_ui_MainActivity_setLineList(
            JNIEnv* env,
            jobject,
            jobjectArray array
    ){
        jsize rowSize = env -> GetArrayLength(array);

        for(jsize i = 0; i < rowSize; i++) {
            jdoubleArray oneRow = (jdoubleArray)env ->GetObjectArrayElement(array, i);
            jsize colSize = env -> GetArrayLength(oneRow);
            std::vector<double> row(colSize);
            env ->GetDoubleArrayRegion(oneRow, 0, colSize, &row[0]);
            env ->DeleteLocalRef(oneRow);

            constellationLineList.push_back(row);
        }
        constTrans.resize(constellationLineList.size(), std::vector<double>(3, 0.0));
        constResult.resize(constellationLineList.size(), std::vector<double>(2, 0.0));

        __android_log_print(ANDROID_LOG_DEBUG, "test", "const Length: %d", constellationLineList.size());
    };

}

//---------------------------------------------------------------
/*
 * util 함수들 등록
*/

//현재 경도의 LST를 구하는 함수.
double getLocalSiderealTime(double longitude) {
    auto now = std::chrono::system_clock::now();
    auto duration = now.time_since_epoch();
    auto millis = std::chrono::duration_cast<std::chrono::milliseconds>(duration).count();
    double JD = (millis / 1000.0) / 86400.0 + 2440587.5;
    double GMST = 18.697374558 + 24.06570982441908 * (JD - 2451545);
    double theta = fmod(GMST * 15.0 + longitude, 360.0);
    return theta * M_PI / 180.0;
}

//모든 별들의 적경 적위를 방위각, 고도에 대한 좌표로 표현한 함수.
void transCoordinate(double longitude, double latitude, bool flag) {

    double sidereal = getLocalSiderealTime(longitude);

    double newLatitude = latitude * M_PI / 180.0;

    double sinPhi = sin(newLatitude);
    double cosPhi = cos(newLatitude);

    int colSize = flag ? 5 : 2;
    if(flag){
        for (size_t i = 0; i < starList.size(); ++i) {
            double hourAngle = sidereal - starList[i][0];
            double sinDec = sin(starList[i][1]);
            double cosDec = cos(starList[i][1]);
            double sina = sinDec * sinPhi + cosDec * cosPhi * cos(hourAngle);
            double cosa = sqrt(1.0 - (sina * sina));
            double sinA = -sin(hourAngle) * cosDec / cosa;
            double cosA = (sinDec - (sinPhi * sina)) / (cosPhi * cosa);

            starTrans[i][0] = cosa * cosA;
            starTrans[i][1] = cosa * sinA;
            starTrans[i][2] = sina;
            for(int j = 2; j < colSize; j++){
                starTrans[i][j + 1] = starList[i][j];
            }
        }
    }
    else{
        for (size_t i = 0; i < constellationLineList.size(); ++i) {
            double hourAngle = sidereal - constellationLineList[i][0];
            double sinDec = sin(constellationLineList[i][1]);
            double cosDec = cos(constellationLineList[i][1]);
            double sina = sinDec * sinPhi + cosDec * cosPhi * cos(hourAngle);
            double cosa = sqrt(1.0 - (sina * sina));
            double sinA = -sin(hourAngle) * cosDec / cosa;
            double cosA = (sinDec - (sinPhi * sina)) / (cosPhi * cosa);

            constTrans[i][0] = cosa * cosA;
            constTrans[i][1] = cosa * sinA;
            constTrans[i][2] = sina;
        }
    }
}

//시선 방향이 theta, phi일 때 방위각, 고도로 변환한 data, zoom에 대한 x, y 좌표 변환
void getSight(double _theta, double _phi, const std::vector<std::vector<double>>& data, std::vector<std::vector<double>>& resultMatrix, float _zoom) {
    double theta = _theta * M_PI / 180.0;
    double phi = _phi * M_PI / 180.0;
    double cosTheta = cos(theta);
    double sinTheta = sin(theta);
    double cosPhi = cos(phi);
    double sinPhi = sin(phi);

    std::vector<std::vector<double>> transMatrix = {
            {cosTheta * cosPhi, -cosTheta * sinPhi, sinTheta},
            {sinTheta * cosPhi, -sinTheta * sinPhi, -cosTheta},
            {sinPhi, cosPhi, 0.0}
    };

    int colSize = data[0].size();
    for(size_t i = 0; i < data.size(); ++i) {

        for(int j = 3; j < colSize; j++){
            resultMatrix[i][j - 1] = data[i][j];
        }

        for(size_t j = 0; j < 3; ++j) {
            for(size_t k = 0; k < 3; ++k) {
                temp[j] += data[i][k] * transMatrix[k][j];
            }
        }

        double a = asin(temp[2]);
        double cosa = cos(a);
        if(std::abs(cosa) < 1.0E-6) {
            resultMatrix[i][0] = 0.0;
            resultMatrix[i][1] = 1000000.0;
            continue;
        }
        double _sin = temp[1] / cosa;
        double _cos = temp[0] / cosa;

        double new_theta = _cos > 0 ? asin(_sin) : M_PI - asin(_sin);
        resultMatrix[i][0] = -1500.0 * new_theta * pow(10.0, _zoom);
        resultMatrix[i][1] = -1500.0 * log(std::abs((1 + sin(a)) / cosa)) * pow(10.0, _zoom);
        temp[0] = 0.0; temp[1] = 0.0; temp[2] = 0.0;
    }
}


//---------------------------------------------------------------
/*
 * 계산 로직 함수
*/

void getAllStars(double longitude, double latitude){
    transCoordinate(longitude, latitude, true);
}

void getAllConstellationLines(double longitude, double latitude){
    transCoordinate(longitude, latitude, false);
}

std::vector<std::vector<double>> getVisibleStars(double _limit, double screenHeight, double screenWidth, const std::vector<std::vector<double>>& starSight) {
    std::vector<std::vector<double>> visible;
    for (const auto& star : starSight) {
        if (star[3] > _limit ||
            std::abs(star[0]) > screenHeight / 2.0 ||
            std::abs(star[1]) > screenWidth / 2.0) {
            continue;
        }
        visible.push_back(star);
    }
    return visible;
}

std::vector<std::vector<double>> getVisibleConstellationLines(double screenHeight, double screenWidth, const std::vector<std::vector<double>>& lines){
    std::vector<std::vector<double>> visible;
    int totSize = lines.size();
    for (int i = 0; i < (totSize >> 1); i++){
        auto st = lines[i << 1];
        auto fi = lines[i << 1 | 1];
        if(std::abs(st[0]) > screenHeight / 2.0 ||
            std::abs(st[1]) > screenWidth / 2.0 ||
            std::abs(fi[0]) > screenHeight / 2.0 ||
            std::abs(fi[1]) > screenWidth / 2.0
        ){
            continue;
        }
        visible.push_back(std::vector<double>{st[0], st[1], fi[0], fi[1]});
    }
    return visible;
}

//---------------------------------------------------------------
/*
 * JNI 통신 함수
*/

extern "C"{

    JNIEXPORT jobjectArray JNICALL
    Java_com_ssafy_stellargram_ui_screen_skymap_SkyMapViewModel_getAllStars(
            JNIEnv* env,
            jobject,
            jdouble longitude,
            jdouble latitude,
            jdouble zoom,
            jdouble theta,
            jdouble phi,
            jdouble limit,
            jdouble screenHeight,
            jdouble screenWidth
    )
    {
        auto now = std::chrono::system_clock::now();
        auto duration = now.time_since_epoch();
        auto millis = std::chrono::duration_cast<std::chrono::milliseconds>(duration).count();

        jclass doubleArrayClass = env->FindClass("[D");

        getAllStars(longitude, latitude);
        getSight(theta, phi, starTrans, starResult, zoom);
        auto visibleStars = getVisibleStars(limit, screenHeight, screenWidth, starResult);

        now = std::chrono::system_clock::now();
        duration = now.time_since_epoch();
        auto millis1 = std::chrono::duration_cast<std::chrono::milliseconds>(duration).count();
        __android_log_print(ANDROID_LOG_DEBUG, "calc", "elapsed Time: %d ms", millis1 - millis);

        int numRows = visibleStars.size();

        // 결과 배열 생성 및 반환
        jobjectArray resultArray = env->NewObjectArray(numRows, doubleArrayClass, NULL);
        for (int i = 0; i < numRows; i++) {
            jdoubleArray rowArray = env->NewDoubleArray(5);
            env->SetDoubleArrayRegion(rowArray, 0, 5, visibleStars[i].data());
            env->SetObjectArrayElement(resultArray, i, rowArray);
            env->DeleteLocalRef(rowArray);
        }
        return resultArray;
    }

    JNIEXPORT jobjectArray JNICALL
    Java_com_ssafy_stellargram_ui_screen_skymap_SkyMapViewModel_getAllConstellationLines(
            JNIEnv* env,
            jobject,
            jdouble longitude,
            jdouble latitude,
            jdouble zoom,
            jdouble theta,
            jdouble phi,
            jdouble screenHeight,
            jdouble screenWidth
    ){
        jclass doubleArrayClass = env->FindClass("[D");
        getAllConstellationLines(longitude, latitude);
        getSight(theta, phi, constTrans, constResult, zoom);
        auto visibleLines = getVisibleConstellationLines(screenHeight, screenWidth, constResult);

        __android_log_print(ANDROID_LOG_DEBUG, "calc", "check");

        int numRows = visibleLines.size();

        // 결과 배열 생성 및 반환
        jobjectArray resultArray = env->NewObjectArray(numRows, doubleArrayClass, NULL);
        for (int i = 0; i < numRows; i++) {
            jdoubleArray rowArray = env->NewDoubleArray(4);
            env->SetDoubleArrayRegion(rowArray, 0, 4, visibleLines[i].data());
            env->SetObjectArrayElement(resultArray, i, rowArray);
            env->DeleteLocalRef(rowArray);
        }
        return resultArray;
    }
}