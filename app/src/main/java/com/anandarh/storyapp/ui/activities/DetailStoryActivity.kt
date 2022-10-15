package com.anandarh.storyapp.ui.activities

import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anandarh.storyapp.R
import com.anandarh.storyapp.databinding.ActivityDetailStoryBinding
import com.anandarh.storyapp.models.StoryModel
import com.anandarh.storyapp.ui.components.BackButton
import com.squareup.picasso.Picasso
import org.ocpsoft.prettytime.PrettyTime
import java.time.ZonedDateTime

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var ivPhoto: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvDescription: TextVieW
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
        btnBack.setOnClickListener { finish() }

        with(binding) {
            ivPhoto = ivDetailPhoto
            tvName = tvDetailName
            tvDate = tvDetailDate
            tvDescription = tvDetailDescription
        }

        data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(EXTRA_STORY, StoryModel::class.java)
        else
            intent.getParcelableExtra(EXTRA_STORY)

        with(data) {
            if (data != null) {
                Picasso.get().load(this!!.photoUrl).into(ivPhoto)
                tvName.text = name
                tvDate.text = PrettyTime().format(ZonedDateTime.parse(createdAt))
                tvDescription.text = description
            } else {
                Toast.makeText(
                    this@DetailStoryActivity,
                    baseContext.getString(R.string.something_wrong),
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }


    }
}