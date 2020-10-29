package t.app.androidApp

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections

class DataFragmentDirections private constructor() {
  companion object {
    fun actionDataFragmentToPredictionsFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_dataFragment_to_predictionsFragment)
  }
}
