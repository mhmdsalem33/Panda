package mhmd.salem.panda.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.firestore.FirebaseFirestore
import mhmd.salem.panda.Models.MartCategoriesModel
import mhmd.salem.panda.Models.ViewPagerModel
import mhmd.salem.panda.R
import mhmd.salem.panda.adapters.MartCategoriesAdapter
import mhmd.salem.panda.adapters.ViewPagerAdapter
import mhmd.salem.panda.databinding.FragmentMartBinding
import kotlin.math.abs



class MartFragment : Fragment() {

    private lateinit var binding :FragmentMartBinding
    private lateinit var uid        :String
    private lateinit var name       :String
    private lateinit var time       :String
    private lateinit var deliverFee :String
    private lateinit var minAmount  :String
    private lateinit var viewPager2: ViewPager2
    private lateinit var handler: Handler
    private var firestore :FirebaseFirestore ? = null
    private lateinit var martCategoriesAdapter: MartCategoriesAdapter
    private lateinit var viewPagerAdapter : ViewPagerAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        martCategoriesAdapter = MartCategoriesAdapter()
        firestore             = FirebaseFirestore.getInstance()



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMartBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getViewByBundle()
        getCategoriesDataFromFirestore()
        onCategoryClick()
        loadDataViewPager()
        setUpTransFormer()
        hanlderViewPager()
        onSearchClick()
        onCustomToolbarClick()



    }

    private fun onCustomToolbarClick() {
        binding.inlced.arrowBackMart.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }


        binding.inlced.favMart.setOnClickListener {
            findNavController().navigate(R.id.favoriteFragment)
        }
        binding.inlced.shopMart.setOnClickListener {
            findNavController().navigate(R.id.cartFragment)
        }


    }

    private fun onSearchClick() {
            binding.searchMart.setOnClickListener {

                val fragment = SearchMartCategoryFragment()
                val bundle   = Bundle()
                    bundle.putString("uid" , uid)
                fragment.arguments = bundle

                findNavController().navigate(R.id.searchMartCategoryFragment , bundle)
            }
    }

    // START VIEW PAGER
    private fun hanlderViewPager() {
        viewPager2.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable , 3000)
            }
        })
    }
    private fun loadDataViewPager() {
        viewPager2 = binding.root.findViewById(R.id.viewpagger)
        handler    = Handler(Looper.myLooper()!!)
        viewPagerAdapter = ViewPagerAdapter(viewPager2)

        firestore!!.collection("Mart").document(uid)
            .collection("viewpagger")
            .addSnapshotListener{ values , error ->
                val viewPagerList   = arrayListOf<ViewPagerModel>()
                val viewPagerModel  = values?.toObjects(ViewPagerModel::class.java)
                    viewPagerList.addAll(viewPagerModel!!)


                viewPagerAdapter.setViewPager(viewPagerList =  viewPagerList as ArrayList<ViewPagerModel>)

                viewPager2.adapter = viewPagerAdapter
                viewPager2.offscreenPageLimit = 3
                viewPager2.clipToPadding = false
                viewPager2.clipChildren  = false
                viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER


            }
    }
    private fun setUpTransFormer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer{page , position ->

            val r = 1  - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }
        viewPager2.setPageTransformer(transformer)




    }
    private val runnable = Runnable {
        viewPager2.currentItem = viewPager2.currentItem + 1
    }
    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)

    }
    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable , 2000)
    }
    // END VIEW PAGER


    private fun onCategoryClick() {
        martCategoriesAdapter.onCategoryClick = { data ->
            val fragment = MartCategoryDetailsFragment()
            val bundle = Bundle()
                bundle.putString("uid"   ,  uid)
                bundle.putString("id"    ,  data.id)
                bundle.putString("name"  ,  name)
                bundle.putString("type"  ,  data.type)
              fragment.arguments = bundle
            findNavController().navigate(R.id.martCategoryDetailsFragment , bundle)

        }
    }

    private fun getCategoriesDataFromFirestore() {


        firestore!!.collection("Mart").document(uid).collection("category")
            .addSnapshotListener{value , error ->
                val martCategoryList   = arrayListOf<MartCategoriesModel>()
                val martCategoryModel  = value?.toObjects(MartCategoriesModel::class.java)
                    martCategoryList.addAll(martCategoryModel!!)

                martCategoriesAdapter.differ.submitList(martCategoryList)

                binding.recCategories.apply {
                    layoutManager = GridLayoutManager(context , 4 , RecyclerView.VERTICAL , false)
                    adapter       = martCategoriesAdapter
                }



            }


    }

    private fun getViewByBundle() {
        val data = arguments
        uid        = data?.getString("uid").toString()
        name       = data?.getString("name").toString()
        time       = data?.getString("time").toString()
        deliverFee = data?.getString("delivery_fee").toString()
        minAmount  = data?.getString("min_amount").toString()

        binding.inlced.marName.text = name
        binding.deleiveryFee.text   = deliverFee
        binding.minAmount.text      = minAmount
        binding.timeMart.text       = time




    }


}