package maxcoders.uz.aliftechapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.recylcer_list_items.view.*
import maxcoders.uz.aliftechapp.R
import maxcoders.uz.aliftechapp.model.Data

class DataViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val data:Data? = null

    fun bind(data: Data?){
        if (data == null){

        }else{
            showData(data)
        }
    }

    private fun showData(data: Data) {
        itemView.textviewName.text = data.name
        itemView.coder.text = "End date: " + data.endDate
        Glide.with(itemView)
            .load(data.icon)
            .into(itemView.imageView)
    }

     companion object{
         fun create(parent:ViewGroup):DataViewHolder{
             val view = LayoutInflater.from(parent.context)
                 .inflate(R.layout.recylcer_list_items, parent, false)
             return DataViewHolder(view)
         }
     }
}