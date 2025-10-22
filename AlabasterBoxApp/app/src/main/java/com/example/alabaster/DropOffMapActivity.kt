package com.example.alabaster

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.alabaster.databinding.ActivityDropOffMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.*

class DropOffMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityDropOffMapBinding
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val dropOffAddress = "2 Garden Crescent, New Germany, Pinetown, KwaZulu-Natal, 3601"
    private val locationPermissionCode = 1001
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDropOffMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish() // or navigate to a specific home activity if you prefer
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        showDropOffAndRoute()
    }

    private fun showDropOffAndRoute() {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocationName(dropOffAddress, 1)

        if (!addresses.isNullOrEmpty()) {
            val dropOff = LatLng(addresses[0].latitude, addresses[0].longitude)
            map.addMarker(MarkerOptions().position(dropOff).title("Drop-off Location"))
            getUserLocation(dropOff)
        }
    }

    private fun getUserLocation(dropOff: LatLng) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val userLatLng = LatLng(location.latitude, location.longitude)
                    map.addMarker(
                        MarkerOptions()
                            .position(userLatLng)
                            .title("Your Location")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    )
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 12f))
                    drawRoute(userLatLng, dropOff)
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
    }

    private fun drawRoute(origin: LatLng, destination: LatLng) {
        val apiKey = "YOUR_API_KEY_HERE"
        val url =
            "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                    "&destination=${destination.latitude},${destination.longitude}&key=$apiKey"

        Thread {
            try {
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val json = response.body?.string()

                val jsonObj = JSONObject(json)
                val routes = jsonObj.getJSONArray("routes")
                if (routes.length() > 0) {
                    val overviewPolyline = routes.getJSONObject(0).getJSONObject("overview_polyline")
                    val points = overviewPolyline.getString("points")
                    val decodedPath = PolyUtil.decode(points)

                    runOnUiThread {
                        map.addPolyline(
                            PolylineOptions()
                                .addAll(decodedPath)
                                .color(Color.BLUE)
                                .width(10f)
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("MapError", "Error drawing route: ${e.message}")
            }
        }.start()
    }
}
