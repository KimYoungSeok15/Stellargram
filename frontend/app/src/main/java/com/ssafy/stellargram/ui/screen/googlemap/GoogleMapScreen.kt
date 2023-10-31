package com.ssafy.stellargram.ui.screen.googlemap

import android.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PinConfig
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
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

    // Create a permission launcher
    val context = LocalContext.current
    var location by remember { mutableStateOf("Camera location") }
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

    Box(Modifier.fillMaxSize()) {
        GoogleMap(
            properties = mapProperties,
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState,
            modifier = Modifier.fillMaxSize(),
//            onMapLongClick = {latLng -> conten },
            content = {
                val pinConfig = PinConfig.builder()
                    .setBackgroundColor(Color.MAGENTA)
                    .build()

                AdvancedMarker(
                    state = MarkerState(position = LatLng(37.0, 128.0)),
                    title = "Magenta marker in Sydney",
                    pinConfig = pinConfig
                )
                AdvancedMarker(
                    state = MarkerState(position = LatLng(37.5, 127.03)),
                    title = "draggable",
                    pinConfig = pinConfig,
                    draggable = true
                )
            }
        )
        DisappearingScaleBar(
            modifier = Modifier
                .padding(top = 5.dp, end = 15.dp)
                .align(Alignment.BottomStart)
                .offset(0.dp, (-50).dp),
            cameraPositionState = cameraPositionState
        )
        Text(text = "${lat}, ${lng}", modifier = Modifier.align(Alignment.BottomCenter),
            androidx.compose.ui.graphics.Color.Black)


    }
}




private fun setMapLongClick(map: GoogleMap) {
    map.setOnMapLongClickListener { latLng ->
        map.addMarker(
            MarkerOptions()
                .position(latLng)
        )
    }
}