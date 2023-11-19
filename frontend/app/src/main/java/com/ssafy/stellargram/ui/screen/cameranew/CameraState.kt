package com.ssafy.stellargram.ui.screen.cameranew

sealed class CameraState{
    object PermissionNotGranted : CameraState()

    object Success : CameraState()
}
