package com.thomas.test.newsapisample.feature.articlecontent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import coil.load
import coil.size.Scale
import com.thomas.test.newsapisample.R
import com.thomas.test.newsapisample.data.model.Article
import com.thomas.test.newsapisample.data.model.ArticlePO
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class ArticleContentFragment : Fragment() {
    private val viewModel: ArticleContentViewModel by stateViewModel()

    private lateinit var ivContentImage: ImageView
    private lateinit var tvContentTitle: TextView
    private lateinit var tvContentAuthor: TextView
    private lateinit var tvContentDescription: TextView
    private lateinit var tvContentText: TextView
    private lateinit var tvContentDate: TextView

    private val article: Article by lazy {
        arguments?.getSerializable(ARG_ARTICLE) as Article
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.article_content_fragment, container, false)
        with(view) {
            ivContentImage = findViewById(R.id.ivContentImage)
            tvContentTitle = findViewById(R.id.tvContentTitle)
            tvContentAuthor = findViewById(R.id.tvContentAuthor)
            tvContentDescription = findViewById(R.id.tvContentDescription)
            tvContentText = findViewById(R.id.tvContentText)
            tvContentDate = findViewById(R.id.tvContentDate)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        viewModel.loadContent(article)
    }

    private fun setupObservers() {
        viewModel.articleContentLiveData.observe(viewLifecycleOwner, { content ->
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
