package com.ssafy.stellargram.ui.screen.googlemap

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.location.Geocoder
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.widgets.DisappearingScaleBar
import com.ssafy.stellargram.R
import com.ssafy.stellargram.BuildConfig

@Composable
fun GoogleMapScreen(navController: NavController){
    val viewModel: GoogleMapViewModel = viewModel() // Create an instance of GoogleMapViewModel
    val context = LocalContext.current

    LaunchedEffect(Unit){//init
        viewModel.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        Places.initialize(context.applicationContext, BuildConfig.MAPS_API_KEY)
        viewModel.placesClient = Places.createClient(context)
        viewModel.geoCoder = Geocoder(context)
    }
    Column {
        Searchbar(viewModel = viewModel)
        Box(modifier = Modifier){
            GoogleMap(viewModel = viewModel ,navController = navController)
            Column {
                AnimatedVisibility(
                    viewModel.locationAutofill.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.Gray)
                        .alpha(0.3f)
                ) {
                    Spacer(Modifier.height(16.dp))
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(viewModel.locationAutofill) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable {
                                    viewModel.getCoordinates(it)
                                    viewModel.locationAutofill.clear()
                                    viewModel.textIpt = ""
                                }) {
                                Text(it.address)
                            }
                        }
                    }
                }
            }
            NewMarkerForm(viewModel = viewModel)
            PointDetail(viewModel = viewModel, navController)
        }
    }
}



@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GoogleMap(viewModel: GoogleMapViewModel ,navController: NavController) {
    val context = LocalContext.current
    val multiCampus = LatLng(37.501254, 127.039611) // temporary geocode for initializing map
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(multiCampus, 15f) // default geocode set on multi-campus
    }
    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)
    )

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
            MapUiSettings(mapToolbarEnabled = false, myLocationButtonEnabled = false, rotationGesturesEnabled = false)
        )
    }

    LaunchedEffect(locationPermissionState.allPermissionsGranted) {
        if (!locationPermissionState.allPermissionsGranted) {
            locationPermissionState.launchMultiplePermissionRequest()
        }
        else {
            viewModel.getCurrentLocation()
            mapProperties = MapProperties(maxZoomPreference = 20f, minZoomPreference = 5f, isMyLocationEnabled = true)
            mapUiSettings = MapUiSettings(mapToolbarEnabled = false, myLocationButtonEnabled = true)
        }
    }

    // 마지막 위치를 반환해 맵의 중앙으로 설정
//    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//    fusedLocationClient.lastLocation.addOnSuccessListener {location: Location? ->
//        if (location != null){
//            val update = CameraUpdateFactory.newLatLngZoom(
//                LatLng(location.latitude,location.longitude),
//                15f
//            )
//            cameraPositionState.move(update)
//        }
//    }.addOnFailureListener{
//        it.printStackTrace()
//    }

//    val permissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
//    LaunchedEffect(key1 = permissionState, key2 = Unit){
//        if(!permissionState.status.isGranted){
//            permissionState.launchPermissionRequest()
//        } else{
//            mapProperties = MapProperties(maxZoomPreference = 20f, minZoomPreference = 5f, isMyLocationEnabled = true)
//            mapUiSettings = MapUiSettings(mapToolbarEnabled = false, myLocationButtonEnabled = true)
//        }
//    }
//    var lat by remember { mutableStateOf("")}
//    var lng by remember { mutableStateOf("")}


    LaunchedEffect(key1 = cameraPositionState.isMoving){
        if (!cameraPositionState.isMoving) {
            // it will be done only when the map stops moving.
            val cameraPosition = cameraPositionState.position.target
            viewModel.currentCameraLatLng = cameraPosition
            viewModel.getAddress()
            try{
                Log.d("content", "get inside")
                viewModel.getObserveSiteLists()
            } catch(e: Exception){
                Log.d("error", "Cannot get observe site lists.")
            }
        }
    }

    LaunchedEffect(key1 = viewModel.currentLatLong ){ // 디바이스의 위치가 변할 때 따라온다
        viewModel.zoomLevel = cameraPositionState.position.zoom
        val update = CameraUpdateFactory.newLatLngZoom(viewModel.currentLatLong, viewModel.zoomLevel)
        cameraPositionState.move(update)
    }

    LaunchedEffect(key1 = viewModel.newMarkerLatLng ){// 새 마커를 생성하고 클릭할 때 따라온다
        if(viewModel.formShowing){
            viewModel.zoomLevel = cameraPositionState.position.zoom
            val update = CameraUpdateFactory.newLatLngZoom(viewModel.newMarkerLatLng, viewModel.zoomLevel)
            cameraPositionState.move(update)
        }
    }

    val bitmap = AppCompatResources.getDrawable(context,R.drawable.telescope_svgrepo_com)!!.toBitmap(100,100)
    Box(Modifier.fillMaxWidth()) {
        GoogleMap(
            googleMapOptionsFactory = {GoogleMapOptions().mapId("ac73ce0af3eedd78")},
            properties = mapProperties,
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState,
            modifier = Modifier.fillMaxSize(),
            onMapClick = { _ -> viewModel.formShowing = false },
            onMapLongClick = { latLng ->
                viewModel.newMarkerShowing = true
                viewModel.newMarkerLatLng = latLng
            },
            content = {
                viewModel.markerList.forEach {
                    CustomMarker(latlng = LatLng(it.latitude.toDouble(),it.longitude.toDouble()), title = it.name, bitmap = bitmap, viewModel)
                }
                if(viewModel.newMarkerShowing){
                    NewMarker(latLng = viewModel.newMarkerLatLng , viewModel = viewModel , bitmap = bitmap )
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
        Text(text = viewModel.address,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .sizeIn(maxWidth = 200.dp),
            overflow = TextOverflow.Ellipsis
        )

    }
}


@Composable
fun Searchbar(viewModel: GoogleMapViewModel){
    val focusRequester = remember { FocusRequester() }
    // When the page loaded, Searchbar get focus immediately
    LaunchedEffect(key1 = Unit){
//        focusRequester.requestFocus()
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .padding(8.dp)
    ) {
        OutlinedTextField(
            value = viewModel.textIpt,
            onValueChange = {
                viewModel.textIpt = it
                viewModel.searchPlaces(it)
            },
            singleLine = true,
            keyboardActions = KeyboardActions(onDone = {Log.d("ENTER_KEY_ACTION",viewModel.address)}),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
        )
    }
}

@Composable
fun CustomMarker(latlng: LatLng, title: String, bitmap: Bitmap, viewModel: GoogleMapViewModel){
    val roundLat = String.format("%.5f",latlng.latitude)
    val roundLng =  String.format("%.5f",latlng.longitude)
    val chatRoomID = String.format("%.3f",latlng.latitude).replace(".","")+"-" + String.format("%.3f",latlng.longitude).replace(".","")
    MarkerInfoWindow(
        title= title,
        state = MarkerState(latlng),
        icon = BitmapDescriptorFactory.fromBitmap(bitmap),
        tag = "$roundLat-$roundLng",
        onClick = { marker ->
            Log.d("MARKER",marker.tag.toString())
            viewModel.getObserveSiteDetail(LatLng(roundLat.toDouble(),roundLng.toDouble()),chatRoomID)
            false
        }
    ) {
        Box(
            modifier = Modifier
                .sizeIn(maxWidth = 200.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(20.dp)
                )
        ) {
            Column(
                modifier = Modifier.padding(16.dp,0.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = if(title!="") title else "이름 없음")
                Text(
                    text = "lat: ${latlng.latitude} \n lng: ${latlng.longitude}",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
fun NewMarker(latLng: LatLng, viewModel: GoogleMapViewModel, bitmap: Bitmap){
    Marker(
        state = MarkerState(position = latLng),
//        icon = BitmapDescriptorFactory.fromBitmap(bitmap),
        draggable = true,
        onClick = { marker ->
            viewModel.newMarkerLatLng = marker.position
            viewModel.formShowing = true
            true
        },
        )
}
@Composable
fun NewMarkerForm(viewModel: GoogleMapViewModel){
    if(viewModel.formShowing){
        Column(verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                    )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.back),
                            contentDescription = "delete",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .height(40.dp)
                                .clickable {
                                    viewModel.formShowing = false
                                    viewModel.newMarkerShowing = false
                                }
                        )
                        Text(text = "관측지 등록")
                        Image(
                            painter = painterResource(id = R.drawable.add),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .height(40.dp)
                                .clickable {
                                    try {
                                        viewModel.postObserveSite(viewModel.newMarkerLatLng)
                                    } catch (e: Exception) {
                                        Log.d("error", e.message ?: "")
                                    }
                                }
                        )

                    }

                    OutlinedTextField(
                        value = viewModel.newMarkerTitle,
                        onValueChange = { if (it.length < 20) viewModel.newMarkerTitle = it },
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        textStyle = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.primary),
                        label = { Text("Title") }
                    )
                    Text(
                        text = "lat: ${viewModel.newMarkerLatLng.latitude} \n lng: ${viewModel.newMarkerLatLng.longitude}",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(10.dp, 20.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Composable
fun PointDetail(viewModel: GoogleMapViewModel, navController: NavController){
    if (viewModel.detailShowingID != ""){
        Column(verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                    )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.back),
                            contentDescription = "delete",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .height(40.dp)
                                .clickable {
                                    viewModel.detailShowingID = ""
                                }
                        )
                        Text(text = if (viewModel.detailMarker.name != "") viewModel.detailMarker.name else "이름 없음" )
                        Image(
                            painter = painterResource(id = R.drawable.chat),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .height(40.dp)
                                .clickable {
                                    try {
                                        viewModel.enterChatRoom(navController =navController)
                                        Log.d(
                                            "MARKER",
                                            "id : ${viewModel.detailShowingID} chatroom selected"
                                        )
                                    } catch (e: Exception) {
                                        Log.d("error", e.message ?: "")
                                    }
                                }
                        )
                    }
                    Text(
                        text = "리뷰 수: ${viewModel.detailMarker.reviewCount}",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(10.dp, 20.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = "관측지 평점: ${if(viewModel.detailMarker.reviewCount != 0) viewModel.detailMarker.ratingSum/viewModel.detailMarker.reviewCount else 0}",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(10.dp, 20.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}