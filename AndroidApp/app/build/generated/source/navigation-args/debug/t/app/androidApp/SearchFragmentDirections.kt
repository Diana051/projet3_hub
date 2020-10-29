package t.app.androidApp

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections

class SearchFragmentDirections private constructor() {
  companion object {
    fun actionSearchFragmentToDataFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_searchFragment_to_dataFragment)
  }
}
