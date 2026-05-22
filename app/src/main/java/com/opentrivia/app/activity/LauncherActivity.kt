package com.opentrivia.app.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.opentrivia.advance.R
import com.opentrivia.app.ui.screen.LauncherViewModel
import com.opentrivia.app.ui.theme.AppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class LauncherActivity : BaseActivity() {

    private val viewModel: LauncherViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                LaunchedEffect(uiState.isReady) {
                    if (uiState.isReady) {
                        startActivity(Intent(this@LauncherActivity, MainActivity::class.java))
                        finish()
                    }
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.mipmap.ic_launcher),
                            contentDescription = null,
                            modifier = Modifier.size(96.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(24.dp))
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
