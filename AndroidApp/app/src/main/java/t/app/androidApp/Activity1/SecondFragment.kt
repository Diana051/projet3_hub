package t.app.androidApp.Activity1

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import t.app.androidApp.AppViewModel
import t.app.androidApp.R

class SecondFragment : Fragment(), KoinComponent {
    // private val args: SecondFragmentArgs by navArgs()
    private val model: AppViewModel by inject()
    private val destination = SecondFragmentDirections.actionSecondFragmentToSecondActivity()
    private lateinit var sendBtn: Button
    private lateinit var skipBtn: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sendBtn = view.findViewById(R.id.sendSurvey)
        sendBtn.setOnClickListener { this.onSend(view) }
        skipBtn = view.findViewById(R.id.skipButton)
        skipBtn.setOnClickListener { findNavController().navigate(destination) }
    }

    private fun loadSurvey(view: View): Boolean {
        val email = view.findViewById<TextInputEditText>(R.id.emailField).text.toString()
        val name = view.findViewById<TextInputEditText>(R.id.nameField).text.toString()
        val surname = view.findViewById<TextInputEditText>(R.id.surnameField).text.toString()
        val age = view.findViewById<TextInputEditText>(R.id.ageField).text.toString()
        val interest = view.findViewById<CheckBox>(R.id.interestCheckbox).isChecked
        val valid = verifyEntry(listOf(email, name, surname, age))
        if (valid)
            model.saveSurveyData(email, surname, name, age.toInt(), interest)
        else showError(view, true)
        return valid

    }
    
    private fun verifyEntry(entries: List<String>): Boolean {
        var isCorrect: Boolean = true
        entries.forEach {
            isCorrect = isCorrect && it.isNotBlank()
        }
        return isCorrect
    }

    private fun onSend(view: View): Unit {
        if (loadSurvey(view))
        GlobalScope.launch(Dispatchers.Main) {

            if (model.fuelService.sendSurvey(model.surveyJson))
                findNavController().navigate(destination)
            else
                showError(view, false)
        }
    }

    private fun showError(view: View, isEntryError: Boolean): Unit {
        //generalize this in viewmodel

        val msg = if (isEntryError)
            "Please fill the entire survey before sending it"
        else
            "HTTP_ERROR 400 - error in the JSON"

        MaterialAlertDialogBuilder(activity as Context)
         .setTitle("Oups!")
         .setMessage(msg)
         .setPositiveButton("OK"){ dialog, _, ->
             //NOTE: spread these elsewhere? what is which parameter???
             dialog.dismiss()

         }
         .show()

    }
}