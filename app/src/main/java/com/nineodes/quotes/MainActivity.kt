package com.nineodes.quotes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.nineodes.quotes.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<MainActivityBinding>(
            this, R.layout.main_activity
        ).apply {
            button.setOnClickListener {
                val intent = Intent(this@MainActivity, QuotesService::class.java)
                    .putExtra("quotes", editText.text.toString())
                    .putExtra("quotes_cn", editTextCn.text.toString())
                startService(intent)
            }
        }
    }
}