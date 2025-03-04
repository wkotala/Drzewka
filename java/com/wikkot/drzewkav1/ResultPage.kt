package com.wikkot.drzewkav1

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wikkot.drzewkav1.databinding.FragmentResultPageBinding
import java.util.Locale

class ResultPage : Fragment() {
    private var _binding: FragmentResultPageBinding? = null
    private val binding get() = _binding!!
    private val mainVM by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultPageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainVM.mainNavButton = binding.BackToTitlePageButton

        binding.BackToTitlePageButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.ConfirmExitQuestion))
                .setIcon(R.mipmap.ic_launcher_round)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.Yes)) { _, _ ->
                    findNavController().navigate(R.id.action_resultPage_to_titlePage)
                }
                .setNegativeButton(getString(R.string.No), null)
                .show()
        }

        assert(mainVM.names.size == mainVM.sharedCosts.size)
        val model = MainModel(mainVM.names.toList(), mainVM.sharedCosts.toList(), mainVM.additionalTransfers.toList())
        val transfers = model.getConciseListOfTransfers()
        val error = model.getError()

        binding.noTransfersTv.text = getString(R.string.NoTransfers, transfers.size)

        binding.singleTransfersRv.layoutManager = LinearLayoutManager(activity)
        binding.singleTransfersRv.adapter = SingleTransferAdapter(transfers)

        binding.errorTv.text = String.format(Locale.ENGLISH, "Błąd wynikający z szacowania kwot: %.3f zł", error)
    }
}