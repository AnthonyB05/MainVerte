package com.example.mainverte.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import com.example.mainverte.MainVerteApplication
import com.example.mainverte.R
import com.example.mainverte.balisesFavRecyclerView.BaliseFavViewModel
import com.example.mainverte.balisesFavRecyclerView.BaliseFavViewModelFactory
import com.example.mainverte.parameterViewModel.ParameterViewModel
import com.example.mainverte.parameterViewModel.ParameterViewModelFactory
import com.example.mainverte.room.DAO.ParameterDao
import com.example.mainverte.room.MainVerteDataBase
import com.example.mainverte.room.models.Parameter
import com.example.mainverte.room.repository.BaliseFavRepository
import com.example.mainverte.room.repository.ParameterRepository
import kotlinx.android.synthetic.main.activity_parameter.*
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ParameterActivity : AppCompatActivity() {

    val database by lazy { MainVerteDataBase.getDatabase(this) }
    val repository by lazy { ParameterRepository(database.parameterDao()) }
    var userParameter : Parameter? = null
    private val parameterViewModel: ParameterViewModel by viewModels {
        ParameterViewModelFactory((this.application as MainVerteApplication).parameterRepository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parameter)
        val temp = parameterViewModel.allParameters
        temp.value
        buttonSave.setOnClickListener {
            val temp : Parameter = Parameter(5,6,1,2,true, true)
            //parameterViewModel.insert(temp)
        }
    }
}