package org.wikipedia.diff

import android.content.Context
import android.content.Intent
import org.wikipedia.activity.SingleFragmentActivity

class ArticleEditDetailsActivity : SingleFragmentActivity<ArticleEditDetailsFragment>() {

    override fun createFragment(): ArticleEditDetailsFragment {
        return ArticleEditDetailsFragment.newInstance(intent.getStringExtra(EXTRA_ARTICLE_TITLE)!!,
                intent.getLongExtra(EXTRA_EDIT_REVISION_ID, 0),
                intent.getStringExtra(EXTRA_EDIT_LANGUAGE_CODE)!!,
                intent.getIntExtra(EXTRA_EDIT_SIZE, 0))
    }

    companion object {
        const val EXTRA_ARTICLE_TITLE = "articleTitle"
        const val EXTRA_EDIT_REVISION_ID = "revisionId"
        const val EXTRA_EDIT_LANGUAGE_CODE = "languageCode"
        const val EXTRA_EDIT_SIZE = "diffSize"

        fun newIntent(context: Context, articleTitle: String, revisionId: Long, languageCode: String, diffSize: Int): Intent {
            return Intent(context, ArticleEditDetailsActivity::class.java)
                    .putExtra(EXTRA_ARTICLE_TITLE, articleTitle)
                    .putExtra(EXTRA_EDIT_REVISION_ID, revisionId)
                    .putExtra(EXTRA_EDIT_LANGUAGE_CODE, languageCode)
                    .putExtra(EXTRA_EDIT_SIZE, diffSize)
        }
    }
}
