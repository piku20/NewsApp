package com.example.news.ui.main

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.databinding.ActivityMainBinding
import com.example.news.utils.MyResponse
import com.example.news.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.ViewModelLifecycle
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){
    private var _binding : ActivityMainBinding?=null
    private val binding get() = _binding

    private val viewModel : MainViewModel by viewModels()

    @Inject
    lateinit var newsAdapter : AdapterNews

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupViews()
        observeNewsData()
        viewModel.getAllNotes()
    }

    private fun setupViews(){
        binding?.recyclerview?.apply {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = newsAdapter
        }
    }

    private fun observeNewsData(){
        viewModel.newsData.observe(this@MainActivity){
            response -> when(response.status){
                MyResponse.Status.LOADING -> {
                    binding?.loading?.visibility = View.VISIBLE
                }

            MyResponse.Status.SUCCESS -> {
                    binding?.loading?.visibility = View.GONE

                    response?.data?.articles?.let{
                        newsAdapter.submitData(it)
                    }
                }

            MyResponse.Status.ERROR -> {
                    binding?.loading?.visibility = View.GONE
                    Toast.makeText(
                        this@MainActivity,
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}