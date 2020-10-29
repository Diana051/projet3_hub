package t.app.androidApp.Activity1

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import t.app.androidApp.R

class SecondFragmentDirections private constructor() {
  companion object {
    fun actionSecondFragmentToSecondActivity(): NavDirections =
        ActionOnlyNavDirections(R.id.action_SecondFragment_to_secondActivity)
  }
}
