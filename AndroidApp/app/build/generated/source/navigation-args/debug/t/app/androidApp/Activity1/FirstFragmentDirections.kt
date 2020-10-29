package t.app.androidApp.Activity1

import android.os.Bundle
import androidx.navigation.NavDirections
import kotlin.Int
import kotlin.String
import t.app.androidApp.R

class FirstFragmentDirections private constructor() {
  private data class ActionFirstFragmentToSecondFragment(
    val serverAddress: String = "test"
  ) : NavDirections {
    override fun getActionId(): Int = R.id.action_FirstFragment_to_SecondFragment

    override fun getArguments(): Bundle {
      val result = Bundle()
      result.putString("serverAddress", this.serverAddress)
      return result
    }
  }

  companion object {
    fun actionFirstFragmentToSecondFragment(serverAddress: String = "test"): NavDirections =
        ActionFirstFragmentToSecondFragment(serverAddress)
  }
}
