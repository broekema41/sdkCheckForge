package com.example.testingforgerock

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import net.openid.appauth.AuthorizationRequest
import org.forgerock.android.auth.FRAuth
import org.forgerock.android.auth.FRListener
import org.forgerock.android.auth.FRUser
import org.forgerock.android.auth.Logger
import org.forgerock.android.auth.Node
import org.forgerock.android.auth.NodeListener


class MainActivity : AppCompatActivity(), NodeListener<FRUser> {
    private val TAG = "MyActivity"
    var status: TextView? = null
    var loginButton: Button? = null
    var logoutButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        Logger.set(Logger.Level.DEBUG)
        Log.d(TAG, R.string.forgerock_url.toString())
        FRAuth.start(this)

        status = findViewById(R.id.textViewUserStatus)
        loginButton = findViewById(R.id.buttonLogin)
        logoutButton = findViewById(R.id.buttonLogout)
        updateStatus()

        // Attach `FRUser.login()` to `loginButton`
        loginButton?.setOnClickListener(View.OnClickListener { view: View? ->
            Log.d(TAG, "loginButton:clicked")
            Log.d(TAG, getString(R.string.forgerock_url))
            centralizedLogin();
//            FRUser.login(
//                applicationContext,
//                this
//            )
        })

         // Attach `FRUser.getCurrentUser().logout()` to `logoutButton`
        logoutButton?.setOnClickListener(View.OnClickListener { view: View? ->
            Log.d(TAG, "logoutButton:clicked")
            FRUser.getCurrentUser().logout()
            updateStatus()
        })

    }

    private fun centralizedLogin() {
        FRUser.browser().appAuthConfigurer()
            .authorizationRequest { r: AuthorizationRequest.Builder ->
               r.setPrompt("Login")
            }.customTabsIntent { t: CustomTabsIntent.Builder ->
            }.done().login(this, object : FRListener<FRUser?> {
                override fun onSuccess(result: FRUser?) {
                    Logger.debug("MAIN", result?.accessToken?.value)
                    updateStatus()
                }

                override fun onException(e: java.lang.Exception) {
                    Logger.error("MAIN", e.message)
                    updateStatus()
                }
            })
    }

    private fun updateStatus() {
        val that = this;
        Thread(
            Runnable {
                this.runOnUiThread {
                    if (FRUser.getCurrentUser() == null) {
                        status?.text = "User is not authenticated."
                        loginButton?.isEnabled = true
                        logoutButton?.isEnabled = false
                    } else {
                        status?.text = buildString {
                            append("User is authenticated.\n")
                            append(FRUser.getCurrentUser().accessToken.idToken)
                        }
                        loginButton?.isEnabled = false
                        logoutButton?.isEnabled = true
                    }
                }
            }
        ).start()
    }

    override fun onCallbackReceived(node: Node) {
        Log.e(TAG, "onCallbackReceived: $node");
        val fragment: UsernamePasswordFragment = UsernamePasswordFragment.newInstance(node)
        fragment.show(this.supportFragmentManager, UsernamePasswordFragment::class.java.getName())
    }

    override fun onException(e: Exception) {
        Log.e(TAG, e.message, e);
    }

    override fun onSuccess(result: FRUser) {
        updateStatus();
    }

}

