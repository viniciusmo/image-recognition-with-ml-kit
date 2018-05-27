package com.github.viniciusmo.labelingimages

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_list_image_labels.*

class ListImageLabelsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_image_labels)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Detected items"
        val linearVertical = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearVertical
        recyclerView.adapter = ImageLabelAdapter(intent.getParcelableArrayListExtra<ImageLabel>(KEY_LABELS),this)
    }

    companion object {

        private const val KEY_LABELS = "LABELS"

        fun callingIntent(context: Context, labels:ArrayList<ImageLabel>) : Intent {
            val intent = Intent(context, ListImageLabelsActivity::class.java)
            intent.putParcelableArrayListExtra(KEY_LABELS, ArrayList(labels))
            return intent
        }
    }

}
