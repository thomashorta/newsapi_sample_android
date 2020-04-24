package com.thomas.test.newsapisample.feature.sourcelist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.thomas.test.newsapisample.R
import com.thomas.test.newsapisample.feature.common.BaseFragment
import com.thomas.test.newsapisample.feature.common.NetworkState
import com.thomas.test.newsapisample.feature.sourcelist.adapter.SourceListAdapter
import kotlinx.android.synthetic.main.common_layout.*
import kotlinx.android.synthetic.main.source_list_fragment.*
import org.koin.android.ext.android.inject

class SourceListFragment : BaseFragment() {

    private val viewModel: SourceListViewModel by inject()
    private lateinit var adapter: SourceListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.source_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        viewModel.fetchSources()
    }

    override fun onResume() {
        super.onResume()
        activityCallback?.updateTitle("News Sources")
    }

    private fun setupRecyclerView() {
        adapter = SourceListAdapter { source ->
            Log.d("Sources", "Clicked on ${source.name}")
            activityCallback?.showArticles(source)
        }
        rvSources.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.apply {
            networkStateLiveData.observe(viewLifecycleOwner, Observer { state ->
                when (state) {
                    NetworkState.SUCCESS -> {
                        pbLoading.visibility = View.GONE
                        rvSources.visibility = View.VISIBLE
                        tvError.visibility = View.GONE
                    }
                    NetworkState.FAILURE -> {
                        pbLoading.visibility = View.GONE
                        rvSources.visibility = View.GONE
                        tvError.visibility = View.VISIBLE
                    }
                    NetworkState.LOADING -> {
                        pbLoading.visibility = View.VISIBLE
                        rvSources.visibility = View.GONE
                        tvError.visibility = View.GONE
                    }
                    else -> {
                        pbLoading.visibility = View.GONE
                    }
                }
            })

            sourcesLiveData.observe(viewLifecycleOwner, Observer { sources ->
                adapter.setSources(sources)
            })
        }
    }

    companion object {
        fun newInstance() = SourceListFragment()
    }

}
