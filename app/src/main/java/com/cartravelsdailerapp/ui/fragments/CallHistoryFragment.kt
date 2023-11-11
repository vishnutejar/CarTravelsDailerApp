package com.cartravelsdailerapp.ui.fragments

import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cartravelsdailerapp.R
import com.cartravelsdailerapp.databinding.FragmentCallHistoryBinding
import com.cartravelsdailerapp.db.AppDatabase
import com.cartravelsdailerapp.db.DatabaseBuilder
import com.cartravelsdailerapp.models.CallHistory
import com.cartravelsdailerapp.models.Contact
import com.cartravelsdailerapp.viewmodels.MainActivityViewModel
import com.cartravelsdailerapp.viewmodels.MyViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class CallHistoryFragment : Fragment() {
    lateinit var binding: FragmentCallHistoryBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var viewModel: MainActivityViewModel
    lateinit var db: AppDatabase
    val listofPages = mutableListOf<Fragment>(CallLogsFrag(), ContactsFrag())
    val callHistorytabtitle = arrayOf(
        "Call Histroy",
        "Contacts"
    )
    val callHistorytabicon = context?.resources?.let {
        arrayOf(
            it.getDrawable(R.drawable.ic_history),
            it.getDrawable(R.drawable.ic_chat)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCallHistoryBinding.inflate(layoutInflater)
        binding.searchContacts.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
/*
                if (binding.recyclerViewCallHistory.isVisible) {
                    filter(msg)
                } else {
                    filterContacts(msg)
                }
*/
                return false
            }
        })
        val myViewModelFactory =
            MyViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(
            this,
            myViewModelFactory
        )[MainActivityViewModel::class.java]
        return binding.root
    }

    fun hideSoftKeyboard(view: View, context: Context) {
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                    InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun filter(text: String) {
        if (text.isEmpty()) {
            //  loadData()
        } else {
            // creating a new array list to filter our data.
            val filteredlist: ArrayList<CallHistory> =
                DatabaseBuilder.getInstance(requireContext()).CallHistoryDao()
                    .searchCall(text) as ArrayList<CallHistory>
            //adapter.filterList(filteredlist.distinctBy { u -> u.number } as ArrayList<CallHistory>)
        }
    }

    private fun filterContacts(text: String) {
        if (text.isEmpty()) {
            // loadContactsData()
        } else {
            // creating a new array list to filter our data.
            val filteredlist: ArrayList<Contact> =
                DatabaseBuilder.getInstance(requireContext()).CallHistoryDao()
                    .searchContactCall(text) as ArrayList<Contact>
            // contactsAdapter.filterList(filteredlist.distinctBy { u -> u.number } as ArrayList<Contact>)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = CallHistoryFraAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = callHistorytabtitle[position]
            when (position) {
                0 -> tab.icon = resources.getDrawable(R.drawable.ic_history)

                1 -> tab.icon = resources.getDrawable(R.drawable.ic_chat)

            }


        }.attach()

    }

    class CallHistoryFraAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        val listofPages = mutableListOf<Fragment>(CallLogsFrag(), ContactsFrag())

        override fun getItemCount(): Int = listofPages.size

        override fun createFragment(position: Int): Fragment {

            return when (position) {
                0 -> CallLogsFrag()
                1 -> ContactsFrag()
                else -> CallLogsFrag()
            }
        }

    }

/*
    class CustomPagerAdapter(private val mContext: Context) : PagerAdapter() {
        override fun getCount(): Int {
            return 0
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun getPageTitle(position: Int): CharSequence {

            return ""
        }
    }
*/
}

