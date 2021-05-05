package ipvc.estg.commov

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64.decode
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

import java.util.*


class InfoWindowAdapter : GoogleMap.InfoWindowAdapter{
    private var windowV : View
    private var contextV : Context

    constructor(context: Context) {
        this.contextV = context
        windowV= LayoutInflater.from(context).inflate(R.layout.infowindow, null)
    }


    fun passaTexto(marker: Marker, view: View){

        val campos = marker.snippet.split("_")
        //0-descriçao e data
        //1-report userid
        //2-id user sharedp
        //3-imagem

        var tit = marker.title
        var desc= campos[0]
        val title = view.findViewById<TextView>(R.id.title)
        val infoW = view.findViewById<TextView>(R.id.infoW)
        val imgV = view.findViewById<ImageView>(R.id.imageView)
        val edit = view.findViewById<Button>(R.id.edita)
        val delete = view.findViewById<Button>(R.id.elimina)

        val imageBytes = Base64.getDecoder().decode(campos[3])
        val decoded = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        imgV.setImageBitmap(decoded)
        title.text = tit +"\n"
        infoW.text = desc

        if(campos[1].equals(campos[2])){
            edit.visibility = View.VISIBLE
            delete.visibility = View.VISIBLE
        }else{
            edit.visibility = (View.INVISIBLE)
            delete.visibility = (View.INVISIBLE)
        }




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