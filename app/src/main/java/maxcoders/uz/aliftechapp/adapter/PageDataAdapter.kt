package maxcoders.uz.aliftechapp.adapter

import android.view.ViewGroup
import android.widget.AdapterView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import maxcoders.uz.aliftechapp.model.Data

class PageDataAdapter(val adapterClickListener: AdapterClickListener): PagingDataAdapter<Data,RecyclerView.ViewHolder>(COMPORATOR) {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null){
            (holder as DataViewHolder).bind(data)
        }
        holder.itemView.setOnClickListener{
            adapterClickListener.clickListeners(data!!)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DataViewHolder.create(parent)
    }


    companion object{
        private val COMPORATOR = object : DiffUtil.ItemCallback<Data>(){
            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem==newItem
            }

        }
    }
}