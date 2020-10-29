package t.app.androidApp

import StationId
import StationLocation
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent

class SearchFragment : Fragment(), KoinComponent {

    private val model: AppViewModel by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_search, container, false)
        observe(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchBtn = view.findViewById<Button>(R.id.searchButton)
        searchBtn.setOnClickListener { onSearch(view) }
        val locateBtn = view.findViewById<Button>(R.id.locate_button)
        locateBtn.setOnClickListener { onLocate(view) }
    }

    private fun observe(view: View): Unit {
        model.searchStationResult.observe(viewLifecycleOwner, androidx.lifecycle.Observer { list ->
            showSearchResults(view, list)
        })
        model.stationLocation.observe(viewLifecycleOwner, androidx.lifecycle.Observer { location ->
            showCoordinates(view, location)
        })
    }

    private fun onSearch(view: View): Unit {
        val chain = view.findViewById<TextInputEditText>(R.id.chaine).text.toString()
        model.requestSearchStation(chain)
    }

    private fun onLocate(view: View): Unit {
        view.findViewById<TextInputLayout>(R.id.textInputLayout3).error =""
        val code = view.findViewById<TextInputEditText>(R.id.station_code).text.toString()
        if (code.isNotBlank() && code.length == 4)
            model.requestStationLocation(code.toInt())
        else
            view.findViewById<TextInputLayout>(R.id.textInputLayout3).error =
                "Please enter a 4-digit station code before launching localization"
    }

    private fun showSearchResults(view: View, list: List<StationId>): Unit {
        // NOTE :put in viewmodel methods that change the view but pass them the element to change?
        var text : String = ""

        if(list.isNotEmpty())
            list.forEach {
                text += ("Station Name: " + it.nom + " - Code: " + it.code.toString() + "\n")
            }
        else text = "No Stations Found"

        view.findViewById<TextView>(R.id.textView3).text = text
    }

    private fun showCoordinates(view: View, location: StationLocation): Unit {
        val text = "${location.nom} \n Latitude: ${location.latitude} Longitude: ${location.longitude}"
        view.findViewById<TextView>(R.id.textView5).text = text
    }
}