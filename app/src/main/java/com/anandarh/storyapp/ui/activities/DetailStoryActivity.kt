package com.anandarh.storyapp.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.anandarh.storyapp.R
import com.anandarh.storyapp.databinding.ActivityDetailStoryBinding
import com.anandarh.storyapp.models.StoryModel
import com.anandarh.storyapp.ui.components.BackButton
import com.anandarh.storyapp.utils.addressFromCoordinate
import com.squareup.picasso.Picasso
import org.ocpsoft.prettytime.PrettyTime
import java.time.ZonedDateTime

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var ivPhoto: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvAddress: TextView
    private lateinit var btnBack: BackButton

    private var data: StoryModel? = null

    companion object {
        const val EXTRA_STORY = "extra_story"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btnBack = binding.btnBack
        val param = btnBack.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(
            btnBack.marginLeft,
            btnBack.marginTop + 10,
            btnBack.marginRight,
            btnBack.marginRight
        )
        btnBack.layoutParams = param
        btnBack.setOnClickListener { supportFinishAfterTransition() }

        with(binding) {
            ivPhoto = ivDetailPhoto
            tvName = tvDetailName
            tvDate = tvDetailDate
            tvDescription = tvDetailDescription
            tvAddress = tvDetailAddress
        }

        data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(EXTRA_STORY, StoryModel::class.java)
        else
            intent.getParcelableExtra(EXTRA_STORY)

        with(data) {
            if (this != null) {
                Picasso.get().load(photoUrl).into(ivPhoto)
                tvName.text = name
                tvDate.text = PrettyTime().format(ZonedDateTime.parse(createdAt))
                tvDescription.text = description

                if (lat != null && this.lon != null) {
                    val address = addressFromCoordinate(this@DetailStoryActivity, lat, lon, true)
                    tvAddress.text =
                        this@DetailStoryActivity.getString(R.string.location_with_pin, address)
                }

            } else {
                Toast.makeText(
                    this@DetailStoryActivity,
                    this@DetailStoryActivity.getString(R.string.something_wrong),
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }


    }
}