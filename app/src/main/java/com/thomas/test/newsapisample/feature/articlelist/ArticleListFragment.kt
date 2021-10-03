package com.thomas.test.newsapisample.feature.articlelist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thomas.test.newsapisample.R
import com.thomas.test.newsapisample.data.model.Source
import com.thomas.test.newsapisample.feature.articlelist.adapter.ArticleListAdapter
import com.thomas.test.newsapisample.feature.common.BaseFragment
import com.thomas.test.newsapisample.feature.common.NetworkState
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class ArticleListFragment : BaseFragment() {
    private val viewModel: ArticleListViewModel by stateViewModel()
    private lateinit var adapter: ArticleListAdapter

    private lateinit var pbLoading: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var rvArticles: RecyclerView

    private val source: Source? by lazy {
        arguments?.getSerializable(ARG_SOURCE) as? Source?
    }

    private val sourceId: String
        get() = source?.id ?: ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.article_list_fragment, container, false)
        with(view) {
            pbLoading = findViewById(R.id.pbLoading)
            tvError = findViewById(R.id.tvError)
            rvArticles = findViewById(R.id.rvArticles)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        viewModel.fetchArticles(sourceId)
    }

    override fun onResume() {
        super.onResume()
        activityCallback?.updateTitle(source?.name ?: "Articles")
    }

    private fun setupRecyclerView() {
        adapter = ArticleListAdapter { article ->
            Log.d("Articles", "Clicked on ${article.title}")
            activityCallback?.showContent(article)
        }
        rvArticles.adapter = adapter
        rvArticles.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItem = linearManager.findLastVisibleItemPosition()

                if (lastVisibleItem >= adapter.itemCount - 5) {
                    viewModel.fetchArticles(sourceId)
                }
            }
        })
    }

    private fun setupObservers() {
        viewModel.apply {
            networkStateLiveData.observe(viewLifecycleOwner, { state ->
                when (state) {
                    NetworkState.SUCCESS -> {
                        pbLoading.visibility = View.GONE
                        rvArticles.visibility = View.VISIBLE
                        tvError.visibility = View.GONE
                    }
                    NetworkState.FAILURE -> {
                        pbLoading.visibility = View.GONE
                        rvArticles.visibility = View.GONE
                        tvError.visibility = View.VISIBLE
                    }
                    NetworkState.LOADING -> {
                        pbLoading.visibility = View.VISIBLE
                        rvArticles.visibility = View.GONE
                        tvError.visibility = View.GONE
                    }
                    else -> {
                        pbLoading.visibility = View.GONE
                    }
                }
            })

            articlesLiveData.observe(viewLifecycleOwner, { articles ->
                adapter.setArticles(articles)
            })
        }
    }

    companion object {
        const val ARG_SOURCE = "NewsListFragment.ARG_SOURCE"

        fun newInstance(source: Source) = ArticleListFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_SOURCE, source)
            }
        }
    }
}
