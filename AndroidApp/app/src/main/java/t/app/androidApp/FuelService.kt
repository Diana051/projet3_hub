package t.app.androidApp

import StationGraphData
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

class FuelService() {
    private var serverIPAddress = ""

    fun loadServerAddress(address: String) {
        serverIPAddress = "http://$address"
        //FuelManager.instance.basePath =  //TODO: CHANGE
    }

    suspend fun sendTestRequest(): Boolean {// Could be done as Async-style function
         return withContext(Dispatchers.IO) {
            val response =
                Fuel.post("$serverIPAddress/server/envoi")
                    .body("Test sent to server...")
                    .response().second
            Log.d("RESP_TEST", "$response")
            return@withContext response.isSuccessful
        }
    }

    /* PUT /sondage sends survey information as Json*/
    suspend fun sendSurvey(surveyJson: String): Boolean {
        return withContext(Dispatchers.IO) {
            val (request, response) =
            Fuel.put("$serverIPAddress/server/sondage")
                .body(surveyJson).also { Log.d("REQ_SURVEY", "$it")}
                .response()
            Log.d("RESP_SURVEY", "$response")
            return@withContext response.isSuccessful
        }
    }

    /* POST /station/recherche sends text as Json and returns StationData of all stations with matching name*/
    suspend fun searchStation(nameJson: String): Response {
        return withContext(Dispatchers.IO) {
            val (request, response) =
            Fuel.post("$serverIPAddress/station/recherche")
                .jsonBody(nameJson).also{ Log.d("REQ_E1_SEARCH", "$it") }
                .responseString()
            Log.d("RESP_E1_SEARCH", "$response")
            return@withContext response
        }
    }

    /*GET /station/<code> retrieves Station's location data*/
    suspend fun getStationLocation(stationCode: String): ResponseResultOf<String> {
        return withContext(Dispatchers.IO) {
            val triplet =
            Fuel.get("$serverIPAddress/station/$stationCode")
                .responseString()
            Log.d("RES_E1_LOCATE", "${triplet.third}.")
            return@withContext triplet
        }
    }

    // GET /donnees/usage/<temps>/<station> qui retourne une liste de points et un graphique PNG
    suspend fun getUsageData(annee: Int, temps: String, station: String): ResponseResultOf<String> {
        return withContext(Dispatchers.IO) {
            val triplet =
            Fuel.get("$serverIPAddress/donnees/usage/$annee/$temps/$station")
                .responseString()
            Log.d("RES_E2_XYGRAPH", "${triplet.third}.")
            return@withContext triplet
        }
    }

    // test GET/PREDICTION/USAGE/STATION
    suspend fun getPredictionData(station: String): Response {
        return withContext(Dispatchers.IO) {
            val (request, response) =
                Fuel.get("$serverIPAddress/donnees/usage/$station")
                    .responseString()

            return@withContext response
        }
    }


}

