package mhmd.salem.panda.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import mhmd.salem.panda.R
import mhmd.salem.panda.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding :ActivityLoginBinding
    private var firebaseAuth :FirebaseAuth ? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginBtn.setOnClickListener {
                login()
        }

    }

    private fun login() {
        val userEmail    = binding.emailLog.text.toString()
        val userPassword = binding.passwordLog.text.toString()

        firebaseAuth!!.signInWithEmailAndPassword(userEmail , userPassword)
            .addOnCompleteListener{
                it.addOnSuccessListener {
                    if (firebaseAuth!!.currentUser!!.isEmailVerified)
                    {
                        val intent = Intent(this , MainActivity::class.java)
                            startActivity(intent)
                    }
                    else
                    {
                        Toast.makeText(applicationContext, "Please Open Your Email And Verify Your Email Check Spam Message", Toast.LENGTH_LONG).show()
                    }
                }
                it.addOnFailureListener{
                    Toast.makeText(this, ""+it.message, Toast.LENGTH_SHORT).show()
                }
            }
    }
}