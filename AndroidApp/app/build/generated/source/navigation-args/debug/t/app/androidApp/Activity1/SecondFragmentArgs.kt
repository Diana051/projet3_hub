package t.app.androidApp.Activity1

import android.os.Bundle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.String
import kotlin.jvm.JvmStatic

data class SecondFragmentArgs(
  val serverAddress: String = "test"
) : NavArgs {
  fun toBundle(): Bundle {
    val result = Bundle()
    result.putString("serverAddress", this.serverAddress)
    return result
  }

  companion object {
    @JvmStatic
    fun fromBundle(bundle: Bundle): SecondFragmentArgs {
      bundle.setClassLoader(SecondFragmentArgs::class.java.classLoader)
      val __serverAddress : String?
      if (bundle.containsKey("serverAddress")) {
        __serverAddress = bundle.getString("serverAddress")
        if (__serverAddress == null) {
          throw IllegalArgumentException("Argument \"serverAddress\" is marked as non-null but was passed a null value.")
        }
      } else {
        __serverAddress = "test"
      }
      return SecondFragmentArgs(__serverAddress)
    }
  }
}
