package com.thomas.test.newsapisample.robot

import androidx.test.core.app.ActivityScenario
import com.github.kittinunf.result.coroutines.SuspendableResult
import com.thomas.test.newsapisample.R
import com.thomas.test.newsapisample.TestActivity
import com.thomas.test.newsapisample.data.model.Source
import com.thomas.test.newsapisample.data.model.SourceResponse
import com.thomas.test.newsapisample.data.repository.NewsRepository
import com.thomas.test.newsapisample.feature.common.BaseFragment
import com.thomas.test.newsapisample.feature.sourcelist.SourceListFragment
import io.mockk.coEvery
import io.mockk.mockk
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

class SourceListFragmentRobot : BaseEspressoRobot() {
    private lateinit var scenario: ActivityScenario<TestActivity>

    fun tearDown() {
        scenario.close()
    }

    fun arrange(block: Arrange.() -> Unit) = Arrange().block()

    fun act(block: Act.() -> Unit) = Act().block()

    fun assert(block: Assert.() -> Unit) = Assert().block()

    inner class Arrange {
        private val mockModules: MutableList<Module> = mutableListOf()

        fun launch(callback: BaseFragment.ActivityCallback) {
            loadKoinModules(mockModules)
            scenario = ActivityScenario.launch(TestActivity::class.java)
            scenario.onActivity {
                it.callback = callback
                it.startFragment(SourceListFragment.newInstance())
            }
        }

        fun mockSuccessRepository(sourceList: List<Source>) {
            val mockRepository: NewsRepository = mockk {
                coEvery {
                    getSources(any(), any())
                } coAnswers {
                    SuspendableResult.of {
                        SourceResponse(
                            status = "",
                            sources = sourceList
                        )
                    }
                }
            }

            mockModules.add(
                module {
                    single { mockRepository }
                }
            )
        }
    }

    inner class Act {
        fun clickSourceListItem(position: Int) {
            clickRecyclerViewItem(R.id.rvSources, position)
        }
    }

    inner class Assert {
        fun sourceListIsVisible(): Unit = viewIsVisible(R.id.rvSources)
        fun sourceListIsNotEmpty(): Unit = recyclerViewIsNotEmpty(R.id.rvSources)
        fun sourceListHasCount(count: Int): Unit = recyclerViewHasCount(R.id.rvSources, count)
    }
}
