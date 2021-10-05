package com.thomas.test.newsapisample.robot

import androidx.test.core.app.ActivityScenario
import com.thomas.test.newsapisample.R
import com.thomas.test.newsapisample.TestActivity
import com.thomas.test.newsapisample.data.model.Article
import com.thomas.test.newsapisample.data.model.Source
import com.thomas.test.newsapisample.feature.articlelist.ArticleListFragment
import com.thomas.test.newsapisample.feature.articlelist.ArticleListViewModel
import com.thomas.test.newsapisample.feature.common.BaseFragment
import com.thomas.test.newsapisample.feature.common.NetworkState
import com.thomas.test.newsapisample.util.SourceFactory
import com.thomas.test.newsapisample.util.mockkLiveData
import io.mockk.every
import io.mockk.mockk
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

class ArticleListFragmentRobot : BaseEspressoRobot() {
    private lateinit var scenario: ActivityScenario<TestActivity>

    fun arrange(block: Arrange.() -> Unit) = Arrange().block()

    fun act(block: Act.() -> Unit) = Act().block()

    fun assert(block: Assert.() -> Unit) = Assert().block()

    inner class Arrange {
        private val mockModules: MutableList<Module> = mutableListOf()

        fun launch(
            callback: BaseFragment.ActivityCallback,
            source: Source = SourceFactory.createSource("test")
        ) {
            loadKoinModules(mockModules)
            scenario = ActivityScenario.launch(TestActivity::class.java)
            scenario.onActivity {
                it.callback = callback
                it.startFragment(ArticleListFragment.newInstance(source))
            }
        }

        fun mockViewModelSuccess(articleList: List<Article>) {
            val mockViewModel: ArticleListViewModel = mockk(relaxed = true) {
                val mockArticleListLiveData = mockkLiveData { articlesLiveData }
                val mockNetworkStateLiveData = mockkLiveData { networkStateLiveData }

                every {
                    fetchArticles(any())
                } answers {
                    mockArticleListLiveData.value = articleList
                    mockNetworkStateLiveData.value = NetworkState.SUCCESS
                }
            }

            mockModules.add(
                module {
                    viewModel { mockViewModel }
                }
            )
        }

        fun mockViewModelLoading() {
            val mockViewModel: ArticleListViewModel = mockk(relaxed = true) {
                mockkLiveData { articlesLiveData }
                val mockNetworkStateLiveData = mockkLiveData { networkStateLiveData }

                every {
                    fetchArticles(any())
                } answers {
                    mockNetworkStateLiveData.value = NetworkState.LOADING
                }
            }

            mockModules.add(
                module {
                    viewModel { mockViewModel }
                }
            )
        }

        fun mockViewModelFailure() {
            val mockViewModel: ArticleListViewModel = mockk(relaxed = true) {
                mockkLiveData { articlesLiveData }
                val mockNetworkStateLiveData = mockkLiveData { networkStateLiveData }

                every {
                    fetchArticles(any())
                } answers {
                    mockNetworkStateLiveData.value = NetworkState.FAILURE
                }
            }

            mockModules.add(
                module {
                    viewModel { mockViewModel }
                }
            )
        }
    }

    inner class Act {
        fun clickArticleListItem(position: Int) {
            clickRecyclerViewItem(R.id.rvArticles, position)
        }
    }

    inner class Assert {
        fun articleListIsVisible(): Unit = viewIsVisible(R.id.rvArticles)
        fun articleListIsNotVisible(): Unit = viewIsGone(R.id.rvArticles)
        fun errorIsVisible(): Unit = viewIsVisible(R.id.tvError)
        fun loadingIsVisible(): Unit = viewIsVisible(R.id.pbLoading)
        fun articleListIsNotEmpty(): Unit = recyclerViewIsNotEmpty(R.id.rvArticles)
        fun articleListHasCount(count: Int): Unit = recyclerViewHasCount(R.id.rvArticles, count)
    }
}
