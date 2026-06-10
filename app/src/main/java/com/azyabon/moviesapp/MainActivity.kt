package com.azyabon.moviesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.azyabon.moviesapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("View binding is only valid between onCreate and onDestroy")

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

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
                R.id.moviesFragment -> "Movies"
                else -> ""
            })
        }

        setSupportActionBar(binding.topAppBar);
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.genresFragment,
                R.id.favoritesFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.ibSearch.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_searchFragment)
        }

        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}