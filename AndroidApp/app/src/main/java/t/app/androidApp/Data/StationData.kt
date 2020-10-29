import kotlinx.serialization.Serializable

//Sent to server
@Serializable
data class SearchData (val chaine: String )

@Serializable
data class StationId ( val code: Int, val nom: String)

//Received from server
@Serializable
data class StationIdList( val stations: List<StationId>)
@Serializable
data class StationLocation( val nom: String, val latitude: Float, val longitude: Float)
@Serializable
data class StationGraphData( val donnees: List<List<Int>>, val graphique: String)