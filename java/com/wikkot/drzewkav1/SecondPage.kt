package com.wikkot.drzewkav1

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wikkot.drzewkav1.databinding.FragmentSecondPageBinding

class SecondPage : Fragment() {
    private var _binding: FragmentSecondPageBinding? = null
    private val binding get() = _binding!!
    private val mainVM by activityViewModels<MainViewModel>()
    private var isKeyboardVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondPageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.hideKeyboard()
        isKeyboardVisible = false
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)

        mainVM.mainNavButton = binding.NextPageButton

        binding.NextPageButton.setOnClickListener {
            binding.nameCostRv.hideKeyboard()
            findNavController().navigate(R.id.action_secondPage_to_thirdPage)
        }

        binding.nameCostRv.layoutManager = LinearLayoutManager(activity)
        binding.nameCostRv.adapter = NameCostListAdapter(mainVM.names.toList(), mainVM.sharedCosts, requireContext())
    }

    private val globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val rect = Rect()
        if(_binding != null){
            binding.root.getWindowVisibleDisplayFrame(rect)
            val screenHeight = requireActivity().resources.displayMetrics.heightPixels
            val keypadHeight = screenHeight - rect.bottom
            val shouldKeyboardBeVisible = (keypadHeight > screenHeight * 0.15)

            if(isKeyboardVisible != shouldKeyboardBeVisible) {
                isKeyboardVisible = shouldKeyboardBeVisible

                if(isKeyboardVisible) {
                    binding.NextPageButton.visibility = View.GONE
                } else {
                    binding.NextPageButton.postDelayed({
                        binding.NextPageButton.visibility = View.VISIBLE
                    }, 10)
                }
            }
        }
    }
}