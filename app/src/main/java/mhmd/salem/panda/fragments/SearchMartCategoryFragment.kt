package mhmd.salem.panda.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import mhmd.salem.panda.Models.MartCategoriesModel
import mhmd.salem.panda.R
import mhmd.salem.panda.ViewModels.MealViewModel
import mhmd.salem.panda.activites.MainActivity
import mhmd.salem.panda.adapters.SearchMartCategoriesAdapter
import mhmd.salem.panda.databinding.FragmentSearchMartCategoryBinding
import java.util.*
import kotlin.collections.ArrayList


class SearchMartCategoryFragment : Fragment() {

    private lateinit var binding :FragmentSearchMartCategoryBinding

    private var searchList  :List<MartCategoriesModel> = ArrayList()
    private val searchMartCategoriesAdapter = SearchMartCategoriesAdapter(searchList)

    private var firestore :FirebaseFirestore ? = null
    private lateinit var uid :String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = FirebaseFirestore.getInstance()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchMartCategoryBinding.inflate(inflater, container , false)
        return   binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            getDataByBundle()
            prepareRecView()
            onSearchBox()
            onItemClick()





    }



    private fun getDataByBundle() {
        val data  = arguments
            uid   = data?.getString("uid")!!
    }
    // Start search
    private fun prepareRecView() {
        binding.searchRecMartCategory.hasFixedSize()
        binding.searchRecMartCategory.layoutManager = GridLayoutManager(context , 4 , RecyclerView.VERTICAL , false)
        binding.searchRecMartCategory.adapter       = searchMartCategoriesAdapter

    }
    private fun onSearchBox() {
        binding.searchBox.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searchText :String = binding.searchBox.text.toString()
                searchInFirestore(searchText.lowercase(Locale.getDefault()))
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }
    private fun searchInFirestore(searchText: String) {
    firestore!!.collection("Mart").document(uid)
        .collection("category")
        .orderBy("type")
        .startAt(searchText)
        .endAt("$searchText\uf8ff")
        .limit(10)
        .get()
        .addOnCompleteListener{
                searchList  =  it.result.toObjects(MartCategoriesModel::class.java)
                searchMartCategoriesAdapter.searchList = searchList
            searchMartCategoriesAdapter.notifyDataSetChanged()
        }


    }
    // end  search

    private fun onItemClick() {
       searchMartCategoriesAdapter.onItemClick ={data ->
         val fragment = MartCategoryDetailsFragment()
         val bundle   = Bundle()
             bundle.putString("uid"  , uid)
             bundle.putString("id"   , data.id)
             bundle.putString("name" ,data.name)
             bundle.putString("type" , data.type)
           fragment.arguments = bundle
           findNavController().navigate(R.id.martCategoryDetailsFragment , bundle)
       }
    }


}