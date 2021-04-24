package ipvc.estg.commov

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ipvc.estg.commov.api.Endpoints
import ipvc.estg.commov.api.Report
import ipvc.estg.commov.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class MapReports : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var reports: List<Report>

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_reports)
        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )

        val iduser= sharedPref.getInt(getString(R.string.shareduser), 0)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val request = ServiceBuilder.buildService(Endpoints::class.java)
        val call = request.getReports()
        var pos: LatLng

        call.enqueue(object : Callback<List<Report>> {
            override fun onResponse(call: Call<List<Report>>, response: Response<List<Report>>) {
                if (response.isSuccessful) {
                    reports = response.body()!!
                    for (report in reports) {
                        var anomesdia = toSimpleString(report.data_criacao)
                        if (report.user_id == iduser) {
                            pos = LatLng(report.latitude.toString().toDouble(), report.longitude.toString().toDouble())
                            mMap.addMarker(MarkerOptions().position(pos).title(report.titulo).snippet(report.descricao + "\n" + "Data: " + anomesdia).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)))
                            mMap.setInfoWindowAdapter( InfoWindowAdapter(this@MapReports))
                        } else{

                            pos = LatLng(report.latitude.toString().toDouble(), report.longitude.toString().toDouble())
                            mMap.addMarker(MarkerOptions().position(pos).title(report.titulo).snippet(report.descricao + "\n" + "Data: " + anomesdia).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                            mMap.setInfoWindowAdapter(InfoWindowAdapter(this@MapReports))
                        }
                    }

                }
            }

            override fun onFailure(call: Call<List<Report>>, t: Throwable) {
                Toast.makeText(this@MapReports, "${t.message}", Toast.LENGTH_SHORT).show()
            }


        })

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                var loc = LatLng(lastLocation.latitude, lastLocation.longitude)
                mMap.addMarker(MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromResource(R.drawable.bluemarker)).title(getString(R.string.loc)))

                val center = findViewById<FloatingActionButton>(R.id.centeractionbtn)
                center.setOnClickListener{
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15f))
                }

                // preenche as coordenadas
                val caixaCoords= findViewById<TextView>(R.id.coordsText)
                caixaCoords.setText("Lat: " + loc.latitude + " - Long: " + loc.longitude)
    
            }
        }

            createLocationRequest()

    }


    fun toSimpleString(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("yyyy-MM-dd").format(this)
    }

    fun alertLogout(view: View) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this@MapReports)
        alertDialog.setTitle(getString(R.string.logout_alert_title))
        alertDialog.setMessage(getString(R.string.pergunta_fim_Sessao))
        alertDialog.setPositiveButton(getString(R.string.resposta_alert_Sim)) { dialog: DialogInterface?, which: Int ->
            val sharedPref: SharedPreferences = getSharedPreferences(
                    getString(R.string.preference_file_key), Context.MODE_PRIVATE
            )
            with(sharedPref.edit()){
                putBoolean(getString(R.string.sharedlogin), false)
                putString(getString(R.string.sharedemail), "")
                putInt(getString(R.string.shareduser), 0)
                commit()
            }

        val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            }
        alertDialog.setNegativeButton(getString(R.string.resposta_alert_nao)) { dialog: DialogInterface?, which: Int ->


        }
        alertDialog.show()
    }

    override fun onBackPressed() {
        alertLogout(window.decorView.rootView)
    }

    companion object {

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        private const val REQUEST_CHECK_SETTINGS = 2
    }


    //pedido de localizaçao
    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

//parar pedido localizaçao
    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)

    }

    //voltar a continuar pedido localização
    public override fun onResume() {
        super.onResume()
        startLocationUpdates()

    }

    //buscar coordenadas
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null )
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val center = LatLng(41.700161, -8.826422)

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 10F))
    }
}
