package com.azyabon.moviesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.azyabon.moviesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("View binding is only valid between onCreate and onDestroy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigationView.isVisible = destination.id in setOf(
                R.id.homeFragment,
                R.id.genresFragment,
                R.id.favoritesFragment
            )
            binding.ibSearch.isVisible = destination.id == R.id.homeFragment

            binding.topAppBar.isVisible = destination.id != R.id.movieFragment

            binding.topAppBar.setTitle(when (destination.id) {
                R.id.homeFragment -> getString(R.string.home)
                R.id.genresFragment -> getString(R.string.genres)
                R.id.favoritesFragment -> getString(R.string.favorites)
                R.id.searchFragment -> getString(R.string.search)
                else -> ""
            })
        }

        binding.ibSearch.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_searchFragment)
        }

        binding.bottomNavigationView.setupWithNavController(navController)
    }
}