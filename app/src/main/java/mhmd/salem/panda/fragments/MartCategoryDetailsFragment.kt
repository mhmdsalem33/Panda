package mhmd.salem.panda.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import mhmd.salem.panda.Models.MartCatgeoryDetailsModel
import mhmd.salem.panda.adapters.CategoryDetailsAdapter
import mhmd.salem.panda.databinding.FragmentMartCategoryDetailsBinding


class MartCategoryDetailsFragment : Fragment() {

    private lateinit var binding :FragmentMartCategoryDetailsBinding
    private lateinit var uid     :String
    private lateinit var id      :String
    private lateinit var name    :String
    private lateinit var type    :String
    private var firestore        :FirebaseFirestore ? = null
    private lateinit var categoryDetailsAdapter :CategoryDetailsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()
        categoryDetailsAdapter = CategoryDetailsAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMartCategoryDetailsBinding.inflate(inflater , container , false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataByBundle()
        getDataFromFirestore()




    }

    private fun getDataFromFirestore() {
        firestore!!.collection("Mart").document(uid).collection("category")
            .document(id).collection("categoryDetails").addSnapshotListener{value , error ->
                    val categoryDetailsList = arrayListOf<MartCatgeoryDetailsModel>()
                    val categoryListModel   = value?.toObjects(MartCatgeoryDetailsModel::class.java)
                        categoryDetailsList.addAll(categoryListModel!!)

                        categoryDetailsAdapter.differ.submitList(categoryDetailsList)

                       binding.recCateg.apply {
                           layoutManager = LinearLayoutManager(context , RecyclerView.VERTICAL , false)
                           adapter       = categoryDetailsAdapter

                       }




            }
    }

    private fun getDataByBundle() {
        val data = arguments
        uid   = data?.getString("uid").toString()
        id    = data?.getString("id").toString()
        name  = data?.getString("name").toString()
        type  = data?.getString("type").toString()

        binding.collpasing.title = name
        binding.shop.text = "Shop Now From ${name}"


    }

}