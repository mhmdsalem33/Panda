package mhmd.salem.panda.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import at.favre.lib.crypto.bcrypt.BCrypt
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import mhmd.salem.panda.Models.UserModel
import mhmd.salem.panda.R
import mhmd.salem.panda.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding :ActivityRegisterBinding
    private var firebaseAuth :FirebaseAuth ? = null
    private var db : FirebaseDatabase ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        db           = FirebaseDatabase.getInstance()

        binding.regBtn.setOnClickListener {
            register()
        }


    }

    private fun register() {
        val userName       = binding.nameReg.text.toString()
        val userEmail      = binding.emailReg.text.toString()
        val userPhone      = binding.phoneReg.text.toString()
        val userPassword   = binding.passwordReg.text.toString()


        val passwordHash = BCrypt.withDefaults().hashToString(12 , userPassword.toCharArray())


        if (userEmail.isEmpty())
        {
            Toast.makeText(this, "Name is Empty", Toast.LENGTH_SHORT).show()
        }
        else if (userEmail.isEmpty())
        {
            Toast.makeText(this, "Email is empty", Toast.LENGTH_SHORT).show()
        }
        else if (userPhone.isEmpty())
        {
            Toast.makeText(this, "User Phone is empty", Toast.LENGTH_SHORT).show()
        }
        else if (userPassword.isEmpty())
        {
            Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show()
        }
        else
        {
            firebaseAuth!!.createUserWithEmailAndPassword(userEmail , userPassword)
                .addOnCompleteListener{
                    it.addOnSuccessListener {


                        val userModel = UserModel()
                            userModel.name        = userName
                            userModel.email       = userEmail
                            userModel.password    = passwordHash
                            userModel.phoneNumber = userPhone
                            userModel.id          = firebaseAuth!!.currentUser!!.uid

                        db!!.getReference("Users")
                            .child(firebaseAuth!!.currentUser!!.uid)
                            .setValue(userModel)
                            .addOnCompleteListener{
                                it.addOnFailureListener{
                                    Toast.makeText(applicationContext, ""+it.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        verifyUser()
                    }
                    it.addOnFailureListener{
                        Toast.makeText(applicationContext, ""+it.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    private fun verifyUser() {
     firebaseAuth!!.currentUser!!.sendEmailVerification()
         .addOnCompleteListener{
                it.addOnSuccessListener {
                  val intent = Intent(applicationContext , LoginActivity::class.java)
                      startActivity(intent)
                }
             it.addOnFailureListener{
                 Toast.makeText(applicationContext, ""+it.message, Toast.LENGTH_SHORT).show()
             }
         }

    }
}