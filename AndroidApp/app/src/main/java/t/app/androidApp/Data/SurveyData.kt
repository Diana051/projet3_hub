package t.app.androidApp.Data
import kotlinx.serialization.Serializable

@Serializable
data class SurveyData (val courriel: String, val prenom: String, val nom: String, val age: Int, val interet: Boolean )