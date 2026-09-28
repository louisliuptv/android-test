package com.example.emptyapp.navigation

object Destinations {
    const val LOGIN = "login"
    const val GPS_LIST = "gps_list"
    const val GPS_DETAIL = "gps_detail/{gpsId}"

    fun gpsDetail(id: Int) = "gps_detail/$id"
}
