package mhmd.salem.panda.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import mhmd.salem.panda.Models.UserModel
import mhmd.salem.panda.R
import mhmd.salem.panda.activites.SplashScreenActivity
import mhmd.salem.panda.databinding.FragmentProfileBinding
import java.lang.StringBuilder


class ProfileFragment : Fragment() {

    private lateinit var binding :FragmentProfileBinding
    private var imageView :ImageView ?= null
    private var imgUri    :Uri  ?= null
    private lateinit var waitingDialog :AlertDialog
    private var          firebaseAuth  :FirebaseAuth ? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater , container , false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onFieldsProfileClick()
        getProfileInformation()
        updateImageProfile()
        logOut()



    }

    private fun logOut() {
        binding.logOut.setOnClickListener {
            firebaseAuth!!.signOut()
            val intent       = Intent(context , SplashScreenActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    }

    private fun updateImageProfile() {
        waitingDialog = AlertDialog.Builder(context)
            .setMessage("Waiting...")
            .setCancelable(false)
            .create()


        imageView = binding.imgProfile

        val getImage =registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                imageView?.setImageURI(uri)
                imgUri = uri
            }
        )

        binding.imgProfile.setOnClickListener{
            getImage.launch("image/")

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Change Avatar")
            builder.setMessage("Do you want really change avatar ?")
            builder.setNegativeButton("Cancel"){dialogInterface , _-> dialogInterface.dismiss()}
            builder.setPositiveButton("Change"){dialogInterface , _->

                if (imgUri != null)
                {
                    waitingDialog.show()
                    val pathFolder = FirebaseStorage.getInstance().getReference()
                        .child("avatar/"+firebaseAuth!!.currentUser!!.uid)
                    pathFolder.putFile(imgUri!!)
                        .addOnFailureListener{ error ->
                            Toast.makeText(context, ""+error.message, Toast.LENGTH_SHORT).show()
                            waitingDialog.dismiss()
                        }
                        .addOnCompleteListener{
                            pathFolder.downloadUrl.addOnSuccessListener { uri ->
                                val updateData = HashMap<String , Any>()
                                updateData["avatar"] =  uri.toString()


                                FirebaseDatabase.getInstance().getReference("Users")
                                    .child(firebaseAuth!!.currentUser!!.uid)
                                    .updateChildren(updateData)
                                    .addOnSuccessListener {
                                        Snackbar.make(view!! , "Profile Avatar Updated successfully" ,Snackbar.LENGTH_LONG).show()
                                    }.addOnFailureListener{error ->
                                        Snackbar.make(view!! , ""+error.message ,Snackbar.LENGTH_LONG).show()
                                    }
                                waitingDialog.dismiss()
                            }
                        }.addOnProgressListener { taskSnashot ->
                            val progress = (100*taskSnashot.bytesTransferred /  taskSnashot.totalByteCount)
                            waitingDialog.setMessage(StringBuilder("Uploading ").append(progress).append("%"))
                        }
                }
            }.setCancelable(false)
            val dialog = builder.create()
            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(Color.BLACK)
            }
            dialog.show()
        }
    }

    private fun getProfileInformation() {

        FirebaseDatabase.getInstance().getReference("Users")
            .child(firebaseAuth!!.currentUser!!.uid)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userModel = snapshot.getValue(UserModel::class.java)
                    val userName         = binding.root.findViewById<TextView>(R.id.name_profile)
                    val userEmail        = binding.root.findViewById<TextView>(R.id.email_profile)
                    val userPhone        = binding.root.findViewById<TextView>(R.id.number_profile)
                    val userAddress      = binding.root.findViewById<TextView>(R.id.address_profile)

                        userName.text    = userModel!!.name
                        userEmail.text   = userModel.email
                        userPhone.text   = userModel.phoneNumber
                        userAddress.text = userModel.address

                    Glide.with(context!!)
                        .load(userModel.avatar)
                        .into(binding.imgProfile)
                }
                override fun onCancelled(error: DatabaseError) {
                   Log.d("error" , error.message)
                }
            })
    }

    private fun onFieldsProfileClick() {
        binding.btnProfileUpdate.setOnClickListener {


          val edtName     = binding.nameProfile.text.toString()
          val edtEmail    = binding.emailProfile.text.toString()
          val edtNumber   = binding.numberProfile.text.toString()
          var edtAddress  =  binding.addressProfile.text.toString()


            if (edtName.isEmpty())
            {
                Toast.makeText(context, "Name is empty", Toast.LENGTH_SHORT).show()
            }
            else if(edtEmail.isEmpty())
            {
                Toast.makeText(context, "Email is empty", Toast.LENGTH_SHORT).show()
            }
            else if(edtNumber.isEmpty())
            {
                Toast.makeText(context, "number is empty", Toast.LENGTH_SHORT).show()
            }
            else if(edtAddress.isEmpty())
            {
                Toast.makeText(context, "address is empty", Toast.LENGTH_SHORT).show()
            }
            else
            {

                val userMap = HashMap<String , Any>()
                    userMap["name"]        = edtName
                    userMap["address"]     = edtAddress
                    userMap["email"]       = edtEmail
                    userMap["phoneNumber"] = edtNumber

                FirebaseDatabase.getInstance().getReference("Users")
                    .child(firebaseAuth!!.currentUser!!.uid)
                    .updateChildren(userMap)
                    .addOnCompleteListener{ it->
                        it.addOnSuccessListener {
                            Toast.makeText(context, "Profile Information Updated Success", Toast.LENGTH_SHORT).show()
                        }
                        it.addOnFailureListener{
                            Toast.makeText(context, ""+it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }
    }

}