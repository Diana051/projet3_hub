package t.app.androidApp

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import org.koin.core.KoinComponent
import org.koin.core.inject
import t.app.androidApp.Data.TimeOptions
import t.app.androidApp.Data.YearOptions

class DataFragment : Fragment(), AdapterView.OnItemSelectedListener, KoinComponent {

    private var yearOption: Int = 0
    private var timeOption: String = ""
    private val model: AppViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_data, container, false)
        observe(view)
        setSpinners(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchBtn = view.findViewById<Button>(R.id.button_search)
        searchBtn.setOnClickListener { onSearch(view) }

        // PLACEHOLDER
        /*val predictBtn = view.findViewById<Button>(R.id.button_prediction_test)
        predictBtn.setOnClickListener { testPrediction(view) }
    }

    // NOTE: placeholder.
    private fun testPrediction(view: View) {
        val stationCode = view.findViewById<TextInputEditText>(R.id.textInput_code).text.toString()
        model.requestPredictionData(view, stationCode)*/
    }

    private fun observe(view: View): Unit {
        model.dataVertices.observe(viewLifecycleOwner, androidx.lifecycle.Observer { list ->
            showVertices(view, list)
        })
        model.dataGraph.observe(viewLifecycleOwner, androidx.lifecycle.Observer { image ->
            showGraph(view, image)
        })
    }

    private fun showGraph(view: View, image: Bitmap): Unit {
        view.findViewById<ImageView>(R.id.graphView).setImageBitmap(image)

    }

    private fun showVertices(view: View, list: List<List<Int>>) {
        var text : String = "(X , Y)\n"

        if(list.isNotEmpty())
            list.forEach {
                text += ("(" + it[0].toString() + ", " + it[1].toString() + ") \n")
            }

        view.findViewById<TextView>(R.id.text_points).text = text
    }

        //NOTE : replace spinners by exposed dropdowns (material design): https://material.io/develop/android/components/text-fields
    private fun setSpinners(view: View): Unit {
        val timeSpinner = view.findViewById<Spinner>(R.id.spinner_timeoptions)
        val yearSpinner = view.findViewById<Spinner>(R.id.spinner_yearoptions)
        timeSpinner.adapter =
            ArrayAdapter<TimeOptions>(activity as Context,  R.layout.spinner_item,
                TimeOptions.values())
        timeSpinner.onItemSelectedListener = this
        yearSpinner.adapter =
            ArrayAdapter<YearOptions>(activity as Context, R.layout.spinner_item,
                YearOptions.values())
        yearSpinner.onItemSelectedListener = this
            
    }

    private fun onSearch(view: View): Unit {
        val stationCode = view.findViewById<TextInputEditText>(R.id.textInput_code).text.toString()
        model.requestData(view, yearOption, timeOption, stationCode)

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        when (parent.id) {
            R.id.spinner_timeoptions -> {
                val option = parent.selectedItem as TimeOptions
                timeOption = option.option
            }

            R.id.spinner_yearoptions -> {
                val option = parent.selectedItem as YearOptions
                yearOption = option.year
            }
        }
        
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }





}