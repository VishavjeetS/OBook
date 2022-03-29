package com.example.obook.util

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.obook.MainActivity
import com.example.obook.R
import com.example.obook.Welcome
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.edpass
import kotlinx.android.synthetic.main.activity_login.sign

class Login : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    private lateinit var auth: FirebaseAuth
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var refUser:DatabaseReference
    private lateinit var personFetchedName   : String
    private lateinit var personFetchedEmail  : String
    private lateinit var googleSignInOptions : GoogleSignInOptions
    private lateinit var mGoogleSignInClient : GoogleSignInClient

    private companion object {
        private const val RC_SIGN_IN = 1011;
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val toolbar:Toolbar = findViewById(R.id.toolbar_register)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Login"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)



        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.web_server_client))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .build()

        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_server_client))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)


        mAuth = FirebaseAuth.getInstance()
        auth = Firebase.auth

        sign.setOnClickListener {
            loginUser()
        }
        gsign.setOnClickListener {
            Log.d(TAG, "OnCreate: begin Google SignIn")
            val intent = mGoogleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                val user = mAuth.currentUser
                Log.d("Gmail ID", "firebaseAuthWithGoogle: $account")
                firebaseAuthWithGoogle(account?.idToken)
//                if (user != null) {
//                    updateUI(user)
//                }
            }
            catch (e:ApiException){
                Log.d("Error", "Google Sign in failed ", e)
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String?) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account")
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                //Login Success
                Log.d(TAG, "firebaseAuthWithGoogleAccount: LoggedIn")

                //Get Logged User
                val firebaseUser = mAuth.currentUser

                //get user info
                val uid = firebaseUser!!.uid
                val email = firebaseUser.email

                Log.d(TAG, "firebaseAuthWithGoogleAccount: Email: $email")
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Uid: $uid")


                //Check if user is new or existing
                if(authResult.additionalUserInfo!!.isNewUser){
                    //User is new - Account Created
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Account Created....\n$email")
                    Toast.makeText(this, "Account Created....\n$email", Toast.LENGTH_SHORT).show()
//                    updateUI(firebaseUser)
                }
                else{
                    //Existing User
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Existing User\n$email")
                    Toast.makeText(this, "Existing User\n$email", Toast.LENGTH_SHORT).show()
//                    updateUI(firebaseUser)
                }

                //Start Profile Activity
                startActivity(Intent(this, MainActivity::class.java))

            }
            .addOnFailureListener { e ->
                //Login Failed
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Login Failed due to ${e.message}")
                Toast.makeText(this, "Login Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loginUser() {
        val email: String = edemail.text.toString()
        val password: String = edpass.text.toString()
        when {
            email == "" -> {
                Toast.makeText(this, "Please write email", Toast.LENGTH_LONG).show()
            }
            password == "" -> {
                Toast.makeText(this, "Please write password", Toast.LENGTH_LONG).show()
            }
            else -> {
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Welcome, $email", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        this.finish()
                    }
                    else{
                        Toast.makeText(applicationContext, "Error Message: "+task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun signUp(view: android.view.View) {
        val intent = Intent(this, SignUp::class.java)
        startActivity(intent)
        this.finish()
    }
    fun forpass(view: android.view.View) {

    }
    fun signIn(view: android.view.View) {}
}