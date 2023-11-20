package com.ssafy.stellargram.ui.screen.googlemap

import android.annotation.SuppressLint
import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.ssafy.stellargram.StellargramApplication
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.ObserveSite
import com.ssafy.stellargram.model.ObserveSiteRequest
import com.ssafy.stellargram.model.SiteInfo
import com.ssafy.stellargram.ui.Screen
import com.ssafy.stellargram.util.CalcZoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

sealed class LocationState {
    object NoPermission : LocationState()
    object LocationDisabled : LocationState()
    object LocationLoading : LocationState()
    data class LocationAvailable(val cameraLatLang: LatLng) : LocationState()
    object Error : LocationState()
}

data class AutocompleteResult(
    val address: String,
    val placeId: String
)

@Suppress("DEPRECATION")
class GoogleMapViewModel @Inject constructor() : ViewModel() {

    private var job: Job? = null
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var placesClient: PlacesClient
    lateinit var geoCoder: Geocoder
    val calcZoom = CalcZoom()
    var locationState by mutableStateOf<LocationState>(LocationState.NoPermission)

    /** Current geoLocation via LatLng, mutated by 'getCurrentLocation' */
    var currentLatLong by mutableStateOf(LatLng(0.0, 0.0))

    /** Current geoLocation of screen(camera) via LatLng, mutated by 'camera.isMoving'*/
    var currentCameraLatLng by mutableStateOf(LatLng(0.0, 0.0))

    /** Address String, mutated by 'getAddress'  */
    var address by mutableStateOf("")
    var textIpt by mutableStateOf("")
    var zoomLevel by mutableFloatStateOf(0f)
    var markerList: MutableList<ObserveSite> = mutableListOf(ObserveSite(0f, 0f, "", 0, 0))
    val locationAutofill = mutableStateListOf<AutocompleteResult>()

    var formShowing by mutableStateOf(false)
    var newMarkerShowing by mutableStateOf(false)
    var newMarkerTitle by mutableStateOf("")
    var newMarkerLatLng by mutableStateOf(LatLng(0.0, 0.0))

    var detailShowingID by mutableStateOf("")
    var detailMarker = SiteInfo(0.0, 0.0, "", 0, 0)
    fun searchPlaces(query: String) {
        job?.cancel()
        locationAutofill.clear()
        job = viewModelScope.launch {
            val request = FindAutocompletePredictionsRequest
                .builder()
                .setQuery(query)
                .build()
            placesClient
                .findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    locationAutofill += response.autocompletePredictions.map {
                        AutocompleteResult(
                            it.getFullText(null).toString(),
                            it.placeId
                        )
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    println(it.cause)
                    println(it.message)
                }
        }
    }

    fun getCoordinates(result: AutocompleteResult) {
        val placeFields = listOf(Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.newInstance(result.placeId, placeFields)
        placesClient.fetchPlace(request).addOnSuccessListener {
            if (it != null) {
                currentLatLong = it.place.latLng!!
            }
        }.addOnFailureListener {
            it.printStackTrace()
        }
    }

    /** Mutate the 'currentLatLong' */
    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        locationState = LocationState.LocationLoading
        fusedLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                locationState =
                    if (location == null && locationState !is LocationState.LocationAvailable) {
                        LocationState.Error
                    } else {
                        currentLatLong = LatLng(location.latitude, location.longitude)
                        LocationState.LocationAvailable(
                            LatLng(
                                location.latitude,
                                location.longitude
                            )
                        )
                    }
            }
    }


    /**
     *  Input 'LatLng' -> mutate the value of 'address'
     */
    fun getAddress() {
        viewModelScope.launch {
            val temp = geoCoder.getFromLocation(
                currentCameraLatLng.latitude,
                currentCameraLatLng.longitude,
                1
            )
            if (temp != null && temp.isNotEmpty()) {
                val addressLines = temp[0].getAddressLine(0).split(" ")
                if (addressLines.size >= 4) {
                    // Skip the first element (country) and join the rest of the address
                    address = addressLines.subList(2, addressLines.size).joinToString(" ")
                } else {
                    // Handle the case when the address doesn't have enough elements
                    address = addressLines.joinToString(" ")
                }
            } else {
                // Handle the case when temp is null or empty
                address = ""
            }
        }
    }


    var cameraAddress by mutableStateOf("")

    /**
     * LatLng -> address String
     */
    fun getFullAddress(latLng: LatLng): String {
        viewModelScope.launch {
            val temp = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            cameraAddress = temp?.get(0)?.getAddressLine(0).toString()
        }
        return cameraAddress
    }

    fun postObserveSite(latLng: LatLng) {
        viewModelScope.launch {
//            val address = getFullAddress(latLng)
            val address = newMarkerTitle
            val request = ObserveSiteRequest(
                round(latLng.latitude * 10000) / 10000.0,
                round(latLng.longitude * 10000) / 10000.0,
                address
            )
            val response =
                NetworkModule.provideRetrofitInstanceObserveSite().postObserveSite(request)
            if (response.data != null) {
                newMarkerShowing = false
                getObserveSiteLists()
            }
        }
    }

    fun getObserveSiteLists() {
        viewModelScope.launch {
            val lat = currentCameraLatLng.latitude
            val lng = currentCameraLatLng.longitude

            val radius = calcZoom.getScreenDiameter(zoomLevel)
            try {
                val response =
                    NetworkModule.provideRetrofitInstanceObserveSearch().getObserveSearch(
                        lat.toFloat() - 1.5f * radius,
                        lat.toFloat() + 1.5f * radius,
                        lng.toFloat() - 1.5f * radius,
                        lng.toFloat() + 1.5f * radius
                    )
                Log.d("content", response.toString())
                if (response.data != null) markerList = response.data
                Log.d("content", "close2: ${markerList.size}")
            } catch (e: Exception) {
                Log.e("content", e.toString())
            }

        }
    }

    fun getObserveSiteDetail(latLng: LatLng, chatroomId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = NetworkModule.provideRetrofitInstanceSite()
                    .getSiteInfo(latitude = latLng.latitude, longitude = latLng.longitude)
                if (response.data != null) {
                    detailMarker = response.data
                    detailShowingID = chatroomId
                }
            } catch (e: Exception) {
                Log.e("DETATILMARKER", e.toString())
            }
        }
    }

    fun enterChatRoom(navController: NavController) {
        viewModelScope.launch {
            var myId = StellargramApplication.prefs.getString("myId", "")

            try {
                val response =
                    NetworkModule.provideRetrofitInstanceChat().joinChatRoom(detailShowingID)
                // 참여 성공했거나 이미 들어간 방이면
                if (response.code == 200) {
                    val roomId = response.data.roomNumber
//                    val roomId = response.data.roomPerson
                    navController.navigate(route = Screen.ChatRoom.route + "/${roomId}/2/${detailShowingID}")
                }
                if (response.code == 400) {
                    val responseMyrooms =
                        NetworkModule.provideRetrofitInstanceChat().getRoomList(myId.toLong())
                    if (responseMyrooms.code == 200) {
                        for (roomInfo in responseMyrooms.data.roomList) {
                            if (roomInfo.observeSiteId == detailShowingID)
                                navController.navigate(route = Screen.ChatRoom.route + "/${roomInfo.roomId}/2/${detailShowingID}")
                        }
                    }

                }
            } catch (e: Exception) {
                Log.d("참여 실패", e.toString())
            }
        }
    }
}

