package t.app.androidApp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.viewmodel.ext.android.viewModel

class SecondActivity: AppCompatActivity() {
    private val model: AppViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        /* see Navigation UI documentation for further details and options for UI guided navigation:
        developer.android.com/guide/navigation/navigation-ui?hl=TR
         */
        setNavigation()
    }

    private fun setNavigation(): Unit {
        val navHostFrag : NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            .setupWithNavController(navHostFrag.navController)

    }
}