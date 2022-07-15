package mhmd.salem.panda.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import mhmd.salem.panda.R
import mhmd.salem.panda.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }

    fun login(view :View){
        val intent = Intent(this , LoginActivity::class.java)
            startActivity(intent)

    }

    fun register(view :View){
        val intent = Intent(this , RegisterActivity::class.java)
        startActivity(intent)


    }
}