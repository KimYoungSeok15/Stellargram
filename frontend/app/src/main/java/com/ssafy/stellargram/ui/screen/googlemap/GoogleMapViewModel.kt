package com.ssafy.stellargram.ui.screen.googlemap

import android.annotation.SuppressLint
import android.location.Geocoder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.ssafy.stellargram.data.remote.NetworkModule
import com.ssafy.stellargram.model.ObserveSiteRequest
import com.ssafy.stellargram.util.CalcZoom
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

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
    /** Address String, mutated by 'getAddress'  */
    var address by mutableStateOf("")

    var textIpt by mutableStateOf("")



    val locationAutofill = mutableStateListOf<AutocompleteResult>()
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
    fun getAddress(latLng: LatLng) {
        viewModelScope.launch {
            val temp = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
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
    /**
     * LatLng -> address String
     */
    fun getFullAddress(latLng: LatLng) : String {
        var ans : String = ""
        viewModelScope.launch {
            val temp = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            ans = temp?.get(0)?.getAddressLine(0).toString()
        }
        return ans
    }

    fun postObserveSite(latLng: LatLng){
        viewModelScope.launch {
            val request = ObserveSiteRequest(latLng.latitude, latLng.latitude, "")
            val response = NetworkModule.provideRetrofitInstanceObserveSite().postObserveSite(request)
        }
    }

    fun getObserveSiteLists(latLng: LatLng, zoomLevel: Float): MutableList<Pair<LatLng, String>>{
        var obsSiteList: MutableList<Pair<LatLng, String>> = mutableListOf()
        viewModelScope.launch {
            val lat = latLng.latitude
            val lng = latLng.longitude

            val radius = calcZoom.getScreenDiameter(zoomLevel)
            val response = NetworkModule.provideRetrofitInstanceObserveSearch().getObserveSearch(
                lat.toFloat() - 1.5f * radius,
                lat.toFloat() + 1.5f * radius,
                lng.toFloat() - 1.5f * radius,
                lng.toFloat() + 1.5f * radius)
            response.data.forEach{
                val new_latLng = LatLng(it.latitude.toDouble(), it.longitude.toDouble())
                obsSiteList.add(Pair(new_latLng, getFullAddress(new_latLng)))
            }
        }
        return obsSiteList
    }
}
