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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import mhmd.salem.panda.Models.SearchMartModel
import mhmd.salem.panda.R
import mhmd.salem.panda.adapters.SearchMartAdapter
import mhmd.salem.panda.databinding.FragmentSearchBinding
import java.util.*
import kotlin.collections.ArrayList


class SearchFragment : Fragment() {

    private lateinit var binding :FragmentSearchBinding
    private  var searchList      :List<SearchMartModel> = ArrayList()
    private  val searchAdapter   = SearchMartAdapter(searchList)

    private var firestore    :FirebaseFirestore ? = null
    private var firebaseAuth :FirebaseAuth      ? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore    = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater , container , false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

          prepareRecView()
          onSearchBox()
          onItemClick()




    }

    private fun onItemClick() {
        searchAdapter.onItemClick = {data ->
          val fragment = MartFragment()
          val bundle   = Bundle()
              bundle.putString("uid"           , data.id)
              bundle.putString("name"          , data.name)
              bundle.putString("time"          , data.time)
              bundle.putString("min_amount"    , data.minAmount)
              bundle.putString("delivery_fee"  , data.deliveryFee)
            fragment.arguments = bundle

            findNavController().navigate(R.id.martFragment , bundle)


        }
    }


    // Start Search
    private fun onSearchBox() {

        binding.searchBox.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                val searchText  :String =  binding.searchBox.text.toString()
                searchInFirestore(searchText.lowercase(Locale.getDefault()))
            }
            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }
    private fun prepareRecView() {
        binding.searchRecHome.hasFixedSize()
        binding.searchRecHome.layoutManager = GridLayoutManager(context , 2 ,RecyclerView.VERTICAL , false)
        binding.searchRecHome.adapter       = searchAdapter

    }
    private fun searchInFirestore(searchText: String) {

        firestore!!.collection("Mart")
            .orderBy("searchName")
            .startAt(searchText)
            .endAt("$searchText\uf8ff")
            .limit(10)
            .get()
            .addOnCompleteListener {

                        searchList  = it.result.toObjects(SearchMartModel::class.java)
                        searchAdapter.searchList = searchList
                        searchAdapter.notifyDataSetChanged()

            }
            .addOnFailureListener{
                Toast.makeText(context, ""+it.message, Toast.LENGTH_SHORT).show()
            }

    }
    // End Search




}