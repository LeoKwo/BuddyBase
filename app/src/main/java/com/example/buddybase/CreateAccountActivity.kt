package com.example.buddybase

import android.content.Intent
import android.os.Bundle
import android.os.UserManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.AccessToken
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth


class CreateAccountActivity : AppCompatActivity() {
    var callbackManager: CallbackManager? = null
    lateinit var facebookSignInButton: LoginButton
    var firebaseAuth: FirebaseAuth? = null
    lateinit var userApp: UserApplication


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        //manager
        this.userApp = this.applicationContext as UserApplication


        FacebookSdk.sdkInitialize(getApplicationContext())
        AppEventsLogger.activateApp(this@CreateAccountActivity)

        firebaseAuth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create();
        facebookSignInButton = findViewById<View>(R.id.login_button) as LoginButton
        facebookSignInButton.setReadPermissions("email")



        // Callback registration
        facebookSignInButton.registerCallback(callbackManager, object :
                                              FacebookCallback<LoginResult> {

            override fun onSuccess(loginResult: LoginResult) {
                // App code
                handleFacebookAccessToken(loginResult.accessToken);
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                // App code
            }
        })
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {

        //APPROCH3 firebase
        //console result:
        //name: Amanda Jisoo Park
        ///email: apark8988@gmail.com
        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("CreateAccountActivity", "signInWithCredential:success")
                    Log.i("token", token.token.toString())
                    val user = firebaseAuth!!.currentUser
                    user?.let {
                        val manager: com.example.buddybase.manager.UserManager = this.userApp.userManager
                        manager.setEmail(user.email.toString())
                        manager.setFullName(user.displayName.toString())

//                        for (profile in it.providerData) {
//                            // Id of the provider (ex: google.com)
//                            val providerId = profile.providerId
//
//                            // UID specific to the provider
//                            val uid = profile.uid
//                            val justID = profile.providerId
//                            Log.i("justID", justID)
//
//                            // Name, email address
//                            val name = profile.displayName
//                            val email = profile.email
//                            if (name != null) {
//                                Log.i("name", name)
//                            }
//                            Log.i("uid", uid)
//                            if (email != null) {
//                                Log.i("email", email)
//                            }
//                        }
                    }

                    startActivity(Intent(this@CreateAccountActivity, SurveyActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("CreateAccountActivity", "signInWithCredential:failure", task.getException())
                    Toast.makeText(this@CreateAccountActivity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }




}