package com.instrument.githubdownloader.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.instrument.githubdownloader.MyApp
import com.instrument.githubdownloader.R
import com.instrument.githubdownloader.dagger.MainActivitySubcomponent
import com.instrument.githubdownloader.dagger.module.MainActivityModule
import com.instrument.githubdownloader.databinding.ActivityMainBinding
import com.instrument.githubdownloader.repository.UserScreenRepository
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    val mainActivitySubcomponent: MainActivitySubcomponent by lazy {
        MyApp.getComponent()
            .mainActivitySubcomponent()
            .create(MainActivityModule(this))
    }

    @Inject
    lateinit var userScreenRepository: UserScreenRepository

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mainActivitySubcomponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        userScreenRepository.openSearchFragment()

        updateUI()
    }

    private fun updateUI(){
        binding.tvSearch.setOnClickListener {
            userScreenRepository.openSearchFragment()
        }

        binding.tvLoader.setOnClickListener {
            userScreenRepository.openDownloadFragment()
        }

        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.findFragmentById(binding.fContainerView.id)
            when(currentFragment){
                is SearchFragment -> {
                    binding.tvLoader.setTextColor(ContextCompat.getColor(this, R.color.down_menu_color))
                    binding.tvSearch.setTextColor(ContextCompat.getColor(this, R.color.active_menu_color))
                }
                is DownloadFragment ->{
                    binding.tvSearch.setTextColor(ContextCompat.getColor(this, R.color.down_menu_color))
                    binding.tvLoader.setTextColor(ContextCompat.getColor(this, R.color.active_menu_color))
                }
                else ->{
                    binding.tvSearch.setTextColor(ContextCompat.getColor(this, R.color.down_menu_color))
                    binding.tvLoader.setTextColor(ContextCompat.getColor(this, R.color.down_menu_color))
                }
            }
        }
    }

    override fun onBackPressed() {
        if(supportFragmentManager.findFragmentById(R.id.fContainerView) is SearchFragment){
            finish()
            return
        }

        super.onBackPressedDispatcher.onBackPressed()
    }
}