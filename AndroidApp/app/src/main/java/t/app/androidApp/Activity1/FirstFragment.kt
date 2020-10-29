package t.app.androidApp.Activity1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import t.app.androidApp.AppViewModel
import t.app.androidApp.R

class FirstFragment : Fragment() {
    /*METHODE DE COMMUNICAITON FRAGMENT-ACTIVITE PAR INTERFACE

    interface ConnectionRequestListener {
        fun loadAddress(address: String)
        fun connect(): Boolean
    }
    private var requestListener: ConnectionRequestListener? = null //TODO: lazy inject..?

    override fun onAttach(context: Context) { //TODO: What is context?
        super.onAttach(context)
        requestListener = context as FirstFragment.ConnectionRequestListener?
    }

    override fun onDetach() {
        super.onDetach()
        requestListener = null
    }*/

    private val model: AppViewModel by inject()
    private val nextFrag = FirstFragmentDirections.actionFirstFragmentToSecondFragment()
    private lateinit var connectBtn: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectBtn = view.findViewById(R.id.button_connection)
        connectBtn.setOnClickListener { this.onClick(view) }
    }

    private fun onClick(view: View) : Unit {
        val textLayout = view.findViewById<TextInputLayout>(R.id.layout_ip)
        val textInput = view.findViewById<TextInputEditText>(R.id.ip_address).text.toString()

        if (textInput.isNotBlank()) {
            GlobalScope.launch(Dispatchers.Main) {
                textLayout.error = null
                model.fuelService.loadServerAddress(textInput)
                if (model.fuelService.sendTestRequest())
                    findNavController().navigate(nextFrag)
                else
                    textLayout.error = "Connection failure: please verify the entered IP Address"
            }
        }
        else textLayout.error = "Please enter the server's IP Address"
    }

}