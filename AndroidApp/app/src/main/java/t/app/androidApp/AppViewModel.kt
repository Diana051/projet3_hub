package t.app.androidApp

import SearchData
import StationGraphData
import StationId
import StationIdList
import StationLocation
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.fuel.core.isClientError
import com.github.kittinunf.fuel.core.isSuccessful
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import t.app.androidApp.Data.SurveyData

class AppViewModel(val fuelService: FuelService): ViewModel() {
    lateinit var surveyJson: String
    lateinit var searchJson: JsonElement
    val searchStationResult = MutableLiveData<List<StationId>>()
    val stationLocation = MutableLiveData<StationLocation>()
    val dataVertices = MutableLiveData<List<List<Int>>>()
    val dataGraph = MutableLiveData<Bitmap>()

// Serialization methods
    fun saveSurveyData(courriel: String, prenom: String, nom: String, age: Int, interet: Boolean ): Unit {
        val surveyData = SurveyData(courriel, prenom, nom, age, interet)
        surveyJson = Json.encodeToString(surveyData)
    }

// SearchFragment Requests
    fun requestSearchStation(chain: String): Unit {
        GlobalScope.launch(Dispatchers.Main) {
            val result = fuelService.searchStation(Json.encodeToString(SearchData(chain)))
            if (result.isSuccessful)
                searchStationResult.value =
                    Json.decodeFromString<StationIdList>(result.responseMessage).stations
            else
                searchStationResult.value =
                    listOf<StationId>(StationId(400, "ERROR 400 - Invalid Request"))
        }
    }

    private fun generateBitmap(graph: String): Bitmap {
        val decodedString = Base64.decode(graph, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }
    
    fun requestStationLocation(code: Int): Unit {
        GlobalScope.launch(Dispatchers.Main) {
            val result = fuelService.getStationLocation(code.toString())
            when {
                result.second.isSuccessful -> stationLocation.value =
                    Json.decodeFromString<StationLocation>(result.third.get())

                result.second.statusCode == 404 -> stationLocation.value =
                    StationLocation("ERROR 404 -No Stations have this code", 0F, 0F)

                result.second.isClientError -> stationLocation.value =
                    StationLocation("ERROR 400 - Invalid request", 0F, 0F)
            }
        }
    }

// DataFragment Requests
    fun requestData(view: View, annee: Int, temps: String, station: String): Unit {
        GlobalScope.launch(Dispatchers.Main) {

            val stationCode: String =
             if (station.isEmpty()) "toutes" else station

            val result = fuelService.getUsageData(annee, temps, stationCode)
            when {
                result.second.isSuccessful -> {
                    val data = Json.decodeFromString<StationGraphData>(result.third.get())
                    dataVertices.value = data.donnees
                    dataGraph.value = generateBitmap(data.graphique)
                }
                result.second.statusCode == 404 -> {
                    view.findViewById<TextView>(R.id.text_points).text =
                        view.context.getString(R.string.error_404_NoData)
                }
                result.second.isClientError ->{
                    view.findViewById<TextView>(R.id.text_points).text =
                        view.context.getString(R.string.error_400_InvalidRequest)
                }
            }
        }
    }

    fun requestPredictionData(view: View, station: String): Unit {
        GlobalScope.launch(Dispatchers.Main) {

            val stationCode: String =
                if (station.isEmpty()) "toutes" else station

            val result = fuelService.getPredictionData(stationCode)
            when {
                result.isSuccessful -> {
                    //NOTE: make new observables...?
                    val data = Json.decodeFromString<StationGraphData>(result.responseMessage)
                    dataVertices.value = data.donnees
                    dataGraph.value = generateBitmap(data.graphique)
                }
                result.statusCode == 404 -> {
                    view.findViewById<TextView>(R.id.text_points).text = "ERROR 404 - No Data for request"
                }
                result.isClientError ->{
                    view.findViewById<TextView>(R.id.text_points).text = "ERROR 400 - Invalid request"
                }
            }
        }

    }

}