package com.danielecampogiani.demo.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.danielecampogiani.demo.R
import com.danielecampogiani.demo.StargazersApplication
import com.danielecampogiani.demo.hideKeyBoard
import com.danielecampogiani.demo.ui.adapter.StargazerAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject

class StargazerFragment : androidx.fragment.app.Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {

        fun newInstance(): StargazerFragment {
            val fragment = StargazerFragment()
            val args = Bundle(0)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        StargazersApplication.getAppComponent(context).inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(this, viewModelFactory)[StargazersViewModel::class.java]

        recycler_view.setHasFixedSize(true)
        val stargazerAdapter = StargazerAdapter()
        recycler_view.adapter = stargazerAdapter

        val linearLayoutManager = recycler_view.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
        val endlessScrollListener = EndlessRecyclerViewScrollListener(linearLayoutManager, viewModel::fetchNextPage)

        search_button.setOnClickListener {
            hideKeyBoard()
            viewModel.fetchFirstPage(owner_edit_text.text.toString(), repo_name_edit_text.text.toString())
        }

        viewModel.viewState.observe(this, Observer {
            when (it) {
                ViewState.MissingOwner -> showMissingOwner()
                ViewState.MissingRepoName -> showMissingRepoName()
                ViewState.Loading -> showLoading()
                ViewState.Empty -> showErrorMessage(getString(R.string.no_stargazers))
                is ViewState.Result -> showResult(it.items, stargazerAdapter)
                is ViewState.Error -> showErrorMessage(it.message)
            }
        })

        viewModel.scrollState.observe(this, Observer {
            when (it) {
                ViewState.InfiniteScrollState.Enabled -> recycler_view.addOnScrollListener(endlessScrollListener)
                ViewState.InfiniteScrollState.Disabled -> recycler_view.removeOnScrollListener(endlessScrollListener)
            }
        })
    }

    private fun showMissingOwner() {
        clearValidationErrors()
        hideLoading()
        hideMessage()
        owner_text_input_layout.error = getString(R.string.mandatory_field)
    }

    private fun showMissingRepoName() {
        clearValidationErrors()
        hideLoading()
        hideMessage()
        repo_name_text_input_layout.error = getString(R.string.mandatory_field)
    }

    private fun showLoading() {
        clearValidationErrors()
        hideMessage()
        loading.visibility = View.VISIBLE
    }

    private fun showResult(items: List<Stargazer>, stargazerAdapter: StargazerAdapter) {
        hideLoading()
        clearValidationErrors()
        hideMessage()
        recycler_view.visibility = View.VISIBLE
        stargazerAdapter.addItems(items)
    }

    private fun showErrorMessage(message: String) {
        hideLoading()
        clearValidationErrors()
        hideResults()
        message_view.visibility = View.VISIBLE
        message_view.text = message
    }

    private fun clearValidationErrors() {
        owner_text_input_layout.error = null
        repo_name_text_input_layout.error = null
    }

    private fun hideLoading() {
        loading.visibility = View.INVISIBLE
    }

    private fun hideResults() {
        recycler_view.visibility = View.INVISIBLE
    }

    private fun hideMessage() {
        message_view.visibility = View.INVISIBLE
    }
}
