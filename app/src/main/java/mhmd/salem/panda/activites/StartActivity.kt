package mhmd.salem.panda.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import mhmd.salem.panda.R
import mhmd.salem.panda.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private lateinit var binding :ActivityStartBinding
    private var firebaseAuth :FirebaseAuth ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()


        if (firebaseAuth!!.currentUser?.isEmailVerified == true)
        {
            val intent  =Intent(this , MainActivity::class.java)
                startActivity(intent)
                finish()
        }
        else
        {
            val intent  =Intent(this , SplashScreenActivity::class.java)
                startActivity(intent)
                finish()
        }


    }
}