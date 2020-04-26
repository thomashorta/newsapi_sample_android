package com.thomas.test.newsapisample.feature.articlecontent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import coil.api.load
import coil.size.Scale

import com.thomas.test.newsapisample.R
import com.thomas.test.newsapisample.data.model.Article
import com.thomas.test.newsapisample.data.model.ArticlePO
import kotlinx.android.synthetic.main.article_content_fragment.*
import org.koin.android.ext.android.inject

class ArticleContentFragment : Fragment() {

    private val viewModel: ArticleContentViewModel by inject()

    private val article: Article by lazy {
        arguments?.getSerializable(ARG_ARTICLE) as Article
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.article_content_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        viewModel.loadContent(article)
    }

    private fun setupObservers() {
        viewModel.articleContentLiveData.observe(viewLifecycleOwner, Observer { content ->
            setContent(content)
        })
    }

    private fun setContent(content: ArticlePO) {
        // load image
        ivContentImage.load(content.imageUrl) {
            placeholder(R.color.colorAccent)
            error(R.color.colorAccent)
            crossfade(true)
            scale(Scale.FILL)
        }

        tvContentTitle.text = content.title
        tvContentAuthor.text = getString(R.string.content_author_format, content.author)
        tvContentDescription.text = content.description
        tvContentText.text = content.content
        tvContentDate.text = getString(R.string.content_publish_format, content.timeSincePublish)
    }

    companion object {
        private const val ARG_ARTICLE = "NewsContentFragment.ARG_ARTICLE"

        fun newInstance(article: Article) = ArticleContentFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_ARTICLE, article)
            }
        }
    }

}
