package com.ssafy.stellargram.ui.screen.googlemap

import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PinConfig
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.widgets.DisappearingScaleBar

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GoogleMapScreen(navController: NavController) {
    val viewModel: GoogleMapViewModel = viewModel() // Create an instance of AuthViewModel
    val multicampus = LatLng(37.501254, 127.039611)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(multicampus, 15f)
    }
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(
                maxZoomPreference = 20f,
                minZoomPreference = 5f,
                isMyLocationEnabled = false
            )
        )
    }
    var mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(mapToolbarEnabled = false, myLocationButtonEnabled = false)
        )
    }
    var lat by remember { mutableStateOf("")}
    var lng by remember { mutableStateOf("")}
    val permissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    LaunchedEffect(key1 = permissionState, key2 = Unit){
        if(!permissionState.status.isGranted){
            permissionState.launchPermissionRequest()
        } else{
            mapProperties = MapProperties(maxZoomPreference = 20f, minZoomPreference = 5f, isMyLocationEnabled = true)
            mapUiSettings = MapUiSettings(mapToolbarEnabled = false, myLocationButtonEnabled = true)
        }
    }
    LaunchedEffect(key1 = cameraPositionState.isMoving){
        if (cameraPositionState.isMoving && cameraPositionState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) {
            // Do your work here, it will be done only when the map starts moving from a drag gesture.
            val nowPosition = cameraPositionState.position.target
            lat = String.format("%.5f",nowPosition.latitude)
            lng = String.format("%.5f",nowPosition.longitude)
        }
    }
    val items = remember { mutableStateListOf<Pair<LatLng, String>>() }

    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            properties = mapProperties,
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState,
            modifier = Modifier.fillMaxSize(),
            onMapLongClick = { latLng ->
                Log.d("IMHERE", "$latLng")
                Log.d("IMHERE","${items.toList()}")
                items.add(Pair(latLng,"${latLng.latitude}${latLng.longitude}")) },
            content = {
                val pinConfig = PinConfig.builder()
                    .setBackgroundColor(Color.MAGENTA)
                    .build()
                MarkerInfoWindow(state = rememberMarkerState(), onClick = { Log.d("IMHERE", "$it"); true }) { marker ->
                    Column {
                        Text(text = marker.title.toString() ?: "Default Marker Title", color = androidx.compose.ui.graphics.Color.Green)
                        Text(marker.snippet ?: "Default Marker Snippet", color = androidx.compose.ui.graphics.Color.Green)
                    }
                }
                items.forEach {
//                    AdvancedMarker(
                    Marker(
                        state = MarkerState(position = it.first),
                        title = it.second,
//                        pinConfig = pinConfig,

                    )
                }
            }
        )
        DisappearingScaleBar(
            modifier = Modifier
                .padding(top = 5.dp, end = 15.dp)
                .align(Alignment.BottomStart)
                .offset(0.dp, (-50).dp),
            cameraPositionState = cameraPositionState
        )
        Text(text = "$lat, $lng", modifier = Modifier.align(Alignment.BottomCenter),
            androidx.compose.ui.graphics.Color.Black)


    }
}