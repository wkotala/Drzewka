package com.wikkot.drzewkav1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.wikkot.drzewkav1.databinding.FragmentTitlePageBinding

class TitlePage : Fragment() {
    private var _binding: FragmentTitlePageBinding? = null
    private val binding get() = _binding!!
    private val mainVM by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTitlePageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.NextPageButton.setOnClickListener {
            mainVM.clear()
            findNavController().navigate(R.id.action_titlePage_to_firstPage)
        }
    }
}