package com.example.kookplayer.views.Fragments

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kookplayer.adapter.FavAdapter
import com.example.kookplayer.databinding.FragmentFavoriteBinding
import com.example.kookplayer.db.entities.SongModel
import com.example.kookplayer.viewModel.FavViewModel

class FavoriteFragment : Fragment() {

    lateinit var binding: FragmentFavoriteBinding

    companion object {
        lateinit var viewModel: FavViewModel
        lateinit var favSongsAdapter: FavAdapter
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)


        viewModel = ViewModelProvider(this).get(FavViewModel::class.java)
        viewModel!!.dataset.observe(viewLifecycleOwner, favSongsObserver)

        favSongsAdapter = context?.let {
            FavAdapter(
                it as Activity,
                viewModel.getDataset()
            )
        }!!

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.favRv.layoutManager = LinearLayoutManager(context)

        viewModel.updateDataset()

        val mHandler = Handler()
        activity?.runOnUiThread(object : Runnable {
            override fun run() {
                viewModel?.updateDataset()
                mHandler.postDelayed(this, 1000)
            }
        })

    }

    override fun onResume() {
        super.onResume()

        viewModel.updateDataset()

    }

    private val favSongsObserver = Observer<ArrayList<Any>> { dataset ->
        favSongsAdapter?.dataset = dataset as ArrayList<SongModel>
        binding.favRv.adapter = favSongsAdapter
    }
}