package mhmd.salem.panda.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import mhmd.salem.panda.Models.CartModel
import mhmd.salem.panda.R
import mhmd.salem.panda.activites.PlaceOrderActivity
import mhmd.salem.panda.adapters.CartAdapter
import mhmd.salem.panda.databinding.FragmentCartBinding
import java.io.Serializable


class CartFragment : Fragment() {

    private lateinit var binding : FragmentCartBinding

    private  var firestore    : FirebaseFirestore ? = null
    private  var firebaseAuth : FirebaseAuth      ? = null
    private lateinit var myCartAdapter : CartAdapter





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firestore      = FirebaseFirestore.getInstance()
        firebaseAuth   = FirebaseAuth.getInstance()
        myCartAdapter  = CartAdapter()



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        visibleContent()
        getUserCartFromFirestore()
        onItemDelete()
        iconBack()
        onButtonShopNow()


    }

    private fun onButtonShopNow() {
    binding.shopNow.setOnClickListener {
        findNavController().navigate(R.id.homeFragment)
    }

    }

    private fun visibleContent() {
        binding.consEmpty.visibility  = View.GONE
        binding.consAppear.visibility = View.GONE
    }

    private fun iconBack() {
        binding.imgBack.setOnClickListener {
                findNavController().navigate(R.id.homeFragment)
        }
    }

    private fun onItemDelete() {
        myCartAdapter.onDeleteClick = {data ->
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Attention")
            builder.setMessage("Are you want to delete ${data.name}")
                .setNegativeButton("Cancel"){dialog , _-> dialog.dismiss()}
                .setPositiveButton("Delete"){dialog ,_->
                    firestore!!.collection("CurrentUser")
                        .document(firebaseAuth!!.currentUser!!.uid)
                        .collection("AddToCart")
                        .document(data.id)
                        .delete()
                        .addOnCompleteListener{  it->
                            it.addOnSuccessListener {
                                getUserCartFromFirestore()
                                Toast.makeText(context, "${data.name} removed success", Toast.LENGTH_SHORT).show()
                            }
                            it.addOnFailureListener{
                                Toast.makeText(context, ""+it.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    dialog.dismiss()
                }.setCancelable(false)
            val dialog = builder.create()
            dialog.show()
        }
    }
    private fun getUserCartFromFirestore() {
        firestore!!.collection("CurrentUser")
            .document(firebaseAuth!!.currentUser!!.uid)
            .collection("AddToCart")
            .addSnapshotListener{ values , error ->
                val cartList  = arrayListOf<CartModel>()
                val cartModel = values?.toObjects(CartModel::class.java)
                    cartList.addAll(cartModel!!)

                myCartAdapter.differ.submitList(cartList)
                calculateTotalAmount(cartList)
                binding.recMyCart.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter       = myCartAdapter
                }

                if (values.isEmpty)
                {
                    binding.consAppear.visibility = View.GONE
                    binding.consEmpty.visibility  = View.VISIBLE
                }
                else
                {
                    binding.consAppear.visibility = View.VISIBLE
                    binding.consEmpty.visibility  = View.GONE
                }

                binding.btnBuyNow.setOnClickListener {
                    val intent = Intent(context , PlaceOrderActivity::class.java)
                        intent.putExtra("itemList" , cartList as Serializable?)
                        startActivity(intent)
                }
            }
    }
    private fun calculateTotalAmount(cartList: ArrayList<CartModel>) {
        var totalAmount = 0.0
        for ( data in cartList)
        {
            totalAmount += data.totalPrice
        }
        binding.cartTotal.text = "$${totalAmount}"
        binding.subTotal.text  = "$${totalAmount}"
    }


}