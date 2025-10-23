package com.example.alabaster

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import kotlin.concurrent.thread

class DropOffMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityDropOffMapBinding
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val client = OkHttpClient()
    private val dropOffAddress = "2 Garden Crescent, New Germany, Pinetown, KwaZulu-Natal, 3601"
    private val locationPermissionCode = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDropOffMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

       // binding.btnBack.setOnClickListener { finish() }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            enableMyLocationSafely()
            showDropOffAndRoute()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
    }

    private fun enableMyLocationSafely() {
        try {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                map.isMyLocationEnabled = true
            }
        } catch (e: SecurityException) {
            Log.e("MapSecurity", "Location permission issue: ${e.message}")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            enableMyLocationSafely()
            showDropOffAndRoute()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDropOffAndRoute() {
        thread {
            try {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = geocoder.getFromLocationName(dropOffAddress, 1)
                if (!addresses.isNullOrEmpty()) {
                    val dropOff = LatLng(addresses[0].latitude, addresses[0].longitude)
                    runOnUiThread { getUserLocation(dropOff) }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Could not find drop-off address", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } catch (e: Exception) {
                Log.e("Geocode", "Error: ${e.message}")
            }
        }
    }

    private fun getUserLocation(dropOff: LatLng) {
        try {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) return

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val userLatLng = LatLng(location.latitude, location.longitude)
                    map.addMarker(
                        MarkerOptions()
                            .position(userLatLng)
                            .title("Your Location")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    )
                    map.addMarker(MarkerOptions().position(dropOff).title("Drop-off Location"))
                    drawRoute(userLatLng, dropOff)

                    // Fit both markers in view
                    val bounds = LatLngBounds.Builder()
                        .include(userLatLng)
                        .include(dropOff)
                        .build()
                    map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200))
                } else {
                    Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: SecurityException) {
            Log.e("LocationError", "Permission missing: ${e.message}")
        }
    }

    private fun drawRoute(origin: LatLng, destination: LatLng) {
        val apiKey = "YOUR_API_KEY_HERE" // replace with your real API key
        val url =
            "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                    "&destination=${destination.latitude},${destination.longitude}&key=$apiKey"

        thread {
            try {
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val json = response.body?.string()
                val jsonObj = JSONObject(json ?: "")
                val routes = jsonObj.getJSONArray("routes")

                if (routes.length() > 0) {
                    val overviewPolyline =
                        routes.getJSONObject(0).getJSONObject("overview_polyline")
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
                } else {
                    Log.e("Directions", "No routes found.")
                }
            } catch (e: Exception) {
                Log.e("Directions", "Error: ${e.message}")
            }
        }
    }
}
