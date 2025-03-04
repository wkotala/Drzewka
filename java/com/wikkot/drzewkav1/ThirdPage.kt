package com.wikkot.drzewkav1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.wikkot.drzewkav1.databinding.FragmentThirdPageBinding

class ThirdPage : Fragment() {
    private var _binding: FragmentThirdPageBinding? = null
    private val binding get() = _binding!!
    private val mainVM by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirdPageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainVM.mainNavButton = binding.NextPageButton

        binding.NextPageButton.setOnClickListener {
            findNavController().navigate(R.id.action_thirdPage_to_resultPage)
        }
    }
}