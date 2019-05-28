package com.sample.test.activity

import android.os.Bundle
import com.sample.test.R
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

}