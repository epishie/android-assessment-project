package com.vp.favorites

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.vp.favorites.databinding.ActivityFavoriteBinding
import com.vp.favorites.viewmodel.FavoriteViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class FavoriteActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter:FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initList()

        viewModel = ViewModelProviders.of(this, factory).get(FavoriteViewModel::class.java)
        viewModel.favoriteMovies.observe(this, Observer {
            adapter.submitList(it)
        })
    }

    private fun initList() {
        adapter = FavoriteAdapter { id ->
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("app://movies/details")
                    .buildUpon()
                    .appendQueryParameter("imdbID", id)
                    .build()
            ).apply {
                setPackage(packageName)
            }
            startActivity(intent)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = GridLayoutManager(
            this,
            when (resources.configuration.orientation) {
                Configuration.ORIENTATION_PORTRAIT -> 2
                else -> 3
            }
        )
    }
}
