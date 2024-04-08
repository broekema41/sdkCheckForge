package com.example.testingforgerock

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.testingforgerock.ui.theme.TestingForgerockTheme
import org.forgerock.android.auth.FRAuth
import org.forgerock.android.auth.Logger

class MainActivity : ComponentActivity() {
    private val TAG = "MyActivity"
    override fun onCreate(savedInstanceState: Bundle?) {




        super.onCreate(savedInstanceState)
        setContent {
            TestingForgerockTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Testing Forgerock SDK")
                }
            }
        }
        Logger.set(Logger.Level.DEBUG)

        Log.e(TAG, R.string.forgerock_url.toString())
        FRAuth.start(this)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestingForgerockTheme {
        Greeting("Android")
    }
}