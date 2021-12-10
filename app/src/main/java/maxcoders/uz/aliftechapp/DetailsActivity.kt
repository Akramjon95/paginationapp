package maxcoders.uz.aliftechapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val intent = getIntent()
        val name = intent.getStringExtra("name")
        val endDate = intent.getStringExtra("enddate")
        val icon = intent.getStringExtra("icon")

        if (intent !== null){
            details_name.text = name
            details_date.text = endDate

            Glide.with(this)
                .load(icon)
                .into(details_image)
        }
    }
}