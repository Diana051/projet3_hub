package t.app.androidApp.Activity1
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import org.koin.android.viewmodel.ext.android.viewModel
import t.app.androidApp.AppViewModel
import t.app.androidApp.R

class MainActivity : AppCompatActivity() {
    private val model: AppViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**ConnectionRequestListener interface methods
    override fun loadAddress(address: String) {
        model.fuelService.loadServerAddress(address)
    }

    override fun connect(): Boolean {

        return model.fuelService.sendTestRequest()

    }*/
}