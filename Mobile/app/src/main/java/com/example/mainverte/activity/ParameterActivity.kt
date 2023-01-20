package com.example.mainverte.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import com.example.mainverte.MainVerteApplication
import com.example.mainverte.R
import com.example.mainverte.balisesFavRecyclerView.BaliseFavViewModel
import com.example.mainverte.balisesFavRecyclerView.BaliseFavViewModelFactory
import com.example.mainverte.models.Balise
import com.example.mainverte.parameterViewModel.ParameterViewModel
import com.example.mainverte.parameterViewModel.ParameterViewModelFactory
import com.example.mainverte.room.DAO.ParameterDao
import com.example.mainverte.room.MainVerteDataBase
import com.example.mainverte.room.models.Parameter
import com.example.mainverte.room.repository.BaliseFavRepository
import com.example.mainverte.room.repository.ParameterRepository
import kotlinx.android.synthetic.main.activity_list_balises_fav.*
import kotlinx.android.synthetic.main.activity_parameter.*
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ParameterActivity : AppCompatActivity() {

    private var balise : Balise? = null
    var userParameter : Parameter? = null
    private val parameterViewModel: ParameterViewModel by viewModels {
        ParameterViewModelFactory((this.application as MainVerteApplication).parameterRepository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parameter)
        val intent = intent
        this.balise = intent.getParcelableExtra("balise")

        if (this.balise != null) {
            textTitle.text = "Paramètres de " + this.balise!!.name

            parameterViewModel.getParameterByIdBalise(balise!!.idBalise!!)
                .observe(this) { parameter ->
                    if (parameter.isNullOrEmpty()){
                        defaultNumber()
                    }
                    else{
                        mapping(parameter[0])
                    }

                }
            buttonSave.setOnClickListener {
                mappingToParameter()
                if (userParameter != null){
                    parameterViewModel.insert(userParameter!!)
                    Toast.makeText(this, "Enregistré avec succès", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun mapping(parameter: Parameter){
        inputTempMin.setText(parameter.tempMin.toString())
        inputTempMax.setText(parameter.tempMax.toString())
        inputHumiditeMax.setText(parameter.humidMax.toString())
        inputHumiditeMin.setText(parameter.humidMin.toString())
        switchTemp.isChecked = parameter.notifTemp
        switchHumid.isChecked = parameter.notifHumid
    }

    fun mappingToParameter(){
        defaultNumber()
        if (this.balise != null){
            userParameter = Parameter(this.balise!!.idBalise,this.balise!!.name!!,Integer.parseInt(inputTempMin.getText().toString()), Integer.parseInt(inputTempMax.getText().toString()), Integer.parseInt(inputHumiditeMin.getText().toString()),
                Integer.parseInt(inputHumiditeMax.getText().toString()),switchTemp.isChecked, switchHumid.isChecked)

        }
    }

    fun defaultNumber(){
        if (inputTempMin.text.isNullOrEmpty()){
            inputTempMin.setText(Editable.Factory.getInstance().newEditable("0"))
        }
        if (inputTempMax.text.isNullOrEmpty()){
            inputTempMax.setText(Editable.Factory.getInstance().newEditable("0"))
        }
        if (inputHumiditeMax.text.isNullOrEmpty()){
            inputHumiditeMax.setText(Editable.Factory.getInstance().newEditable("0"))
        }
        if (inputHumiditeMin.text.isNullOrEmpty()){
            inputHumiditeMin.setText(Editable.Factory.getInstance().newEditable("0"))
        }
    }
}