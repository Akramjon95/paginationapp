package maxcoders.uz.aliftechapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import maxcoders.uz.aliftechapp.adapter.AdapterClickListener
import maxcoders.uz.aliftechapp.adapter.DataAdapter
import maxcoders.uz.aliftechapp.adapter.PageDataAdapter
import maxcoders.uz.aliftechapp.data.DataViewModel
import maxcoders.uz.aliftechapp.model.Data


@ExperimentalPagingApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AdapterClickListener {
    private val TAG = "MainActivity"

   private lateinit var viewModel: DataViewModel
    private lateinit var adpt: PageDataAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        initAdapter()
        loadData()

    }

    private fun loadData(){
        lifecycleScope.launchWhenCreated {
            viewModel.getPagingDataFromDb().collectLatest {
                adpt.submitData(it)
            }
        }

        lifecycleScope.launch {
            adpt.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading}
                .collect { recyclerView.scrollToPosition(0) }
        }
    }

    private fun initAdapter(){
        adpt = PageDataAdapter(this)
        recyclerView.adapter = adpt.withLoadStateHeaderAndFooter(
            header = DataAdapter{adpt.retry()},
            footer = DataAdapter{adpt.retry()}
        )

        adpt.addLoadStateListener { loadState->
            recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
            progressbar_main.isVisible = loadState.source.refresh is LoadState.Loading

        }
    }


    override fun clickListeners(data: Data) {
        val intent = Intent(this, DetailsActivity::class.java )
        intent.putExtra("name", data.name)
        intent.putExtra("enddate", data.endDate)
        intent.putExtra("icon", data.icon)
        startActivity(intent)
    }

}
