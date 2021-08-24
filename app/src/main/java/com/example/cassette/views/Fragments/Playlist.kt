package com.example.cassette.views.Fragments


//class Playlist : Fragment() {
//
//    var playlistAdapter: PlaylistAdapter? = null
//
//    lateinit var binding: CreatePlaylistDialogBinding
//
//    companion object {
//        var viewModel: PlaylistViewModel? = null
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        val view = inflater.inflate(R.layout.fragment_playlist, container, false)
//
//
////        binding = CreatePlaylistDialogBinding.bind(view)
//
//
//        viewModel = ViewModelProvider(this).get(PlaylistViewModel::class.java)
//        viewModel!!.getMutableLiveData().observe(this, playlistUpdateObserver)
//
//
//        context?.let { viewModel!!.setViewModelContext(it) }
//
//        playlistAdapter = activity?.let {
//            PlaylistAdapter(
//                it,
//                viewModel?.dataset as ArrayList<PlaylistModel>
//            )
//        }
//        return view
//    }
//
//    fun notifyDatasetChanged() {
//        playlistAdapter?.notifyDataSetChanged()
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//        fab.setOnClickListener {
//
//            val createPlaylist = CreatePlaylistDialog()
//
//            this.fragmentManager?.beginTransaction()
//                ?.let { it -> createPlaylist.show(it, "playlist") }
//
//            context?.let { it ->
//                PlaylistUtils.createPlaylist(it, "test playlist3")
//
//                viewModel?.updateDataset()
//
//
//                val b = viewModel?.dataset
//
//                val i = 0
//            }
//
//        }
//        viewModel?.updateDataset()
//
//
//        val b = viewModel?.dataset
//
//        val i = 0
//    }
//
//
////    val playlistUpdateObserver =
////        Observer<ArrayList<Any>> {
////            val recyclerViewAdapter = playlistAdapter
////            playlist_rv.layoutManager = GridLayoutManager(context, 2)
////            playlist_rv.adapter = recyclerViewAdapter
////        }
//
//    val playlistUpdateObserver: Observer<ArrayList<Any>> = Observer<ArrayList<Any>>() {
//        viewModel?.liveData?.value
//        val recyclerViewAdapter = playlistAdapter
//        playlist_rv.layoutManager = GridLayoutManager(context, 2)
//        playlist_rv.adapter = recyclerViewAdapter
//
//    }
//}