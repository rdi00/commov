package ipvc.estg.commov

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class InfoWindowAdapter : GoogleMap.InfoWindowAdapter{
    private lateinit var windowV : View
    private lateinit var contextV : Context

    constructor(context: Context) {
        this.contextV = context
        windowV= LayoutInflater.from(context).inflate(R.layout.infowindow,null)
    }


    fun passaTexto (marker: Marker, view: View){

        var tit = marker.title
        var desc= marker.snippet
        val title = view.findViewById<TextView>(R.id.title)
        val infoW = view.findViewById<TextView>(R.id.infoW)

        title.text = tit +"\n"
        infoW.text = desc
    }


    override fun getInfoWindow(marker: Marker): View? {
        passaTexto(marker, windowV)
        return windowV
    }

    override fun getInfoContents(marker: Marker): View? {
        passaTexto(marker, windowV)
        return windowV
    }
}