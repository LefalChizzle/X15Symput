package com.amosgwa.lisukeyboard.view.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.amosgwa.lisukeyboard.R
import com.amosgwa.lisukeyboard.databinding.ActivityWelcomeBinding
import com.amosgwa.lisukeyboard.databinding.FragmentWelcomePageBinding

class WelcomeActivity : AppCompatActivity() {

    private var welcomePagerAdapter: WelcomePagerAdapter? = null
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)
        setUpWelcomePages()
    }

    private fun setUpWelcomePages() {
        welcomePagerAdapter = WelcomePagerAdapter(supportFragmentManager, generateWelcomePages())
        binding.container.adapter = welcomePagerAdapter
    }

    private fun generateWelcomePages(): ArrayList<WelcomeItem> {
        return ArrayList<WelcomeItem>().apply {
            for (i in 0..3) {
                add(WelcomeItem(i))
            }
        }
    }

    inner class WelcomePagerAdapter(
            fm: FragmentManager,
            private val pages: ArrayList<WelcomeItem>
    ) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return WelcomePage.newInstance(pages[position])
        }

        override fun getCount(): Int {
            return pages.size
        }
    }

    data class WelcomeItem(
            val currentPage: Int
    )

    class WelcomePage : Fragment() {
        private lateinit var binding: FragmentWelcomePageBinding
        override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View? {
            binding = FragmentWelcomePageBinding.inflate(inflater, container, false)
            binding.sectionLabel.text = getString(R.string.section_format, arguments?.getInt(ARG_PAGE_NUM))
            return binding.root
        }

        companion object {
            private val ARG_PAGE_NUM = "page_number"
            fun newInstance(item: WelcomeItem): WelcomePage {
                val fragment = WelcomePage()
                val args = Bundle()
                args.putInt(ARG_PAGE_NUM, item.currentPage)
                fragment.arguments = args
                return fragment
            }
        }
    }
}
