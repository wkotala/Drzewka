package com.wikkot.drzewkav1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.wikkot.drzewkav1.databinding.FragmentDisplayTransfersSubpageBinding

class DisplayTransfersSubpage : Fragment() {
    private var _binding: FragmentDisplayTransfersSubpageBinding? = null
    private val binding get() = _binding!!
    private val mainVM by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDisplayTransfersSubpageBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //DEBUG()

        mainVM.mainNavButton!!.visibility = View.VISIBLE

        binding.transfersRv.layoutManager = LinearLayoutManager(activity)
        binding.transfersRv.adapter = TransferListAdapter(mainVM.additionalTransfers)

        binding.addTransferButton.setOnClickListener {
            findNavController().navigate(R.id.action_displayTransfersSubpage_to_addTransferSubpage)
        }

        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if(position == mainVM.additionalTransfers.size) { // Footer
                    return
                }

                val item = mainVM.additionalTransfers[position]

                mainVM.additionalTransfers.removeAt(position)
                binding.transfersRv.adapter?.notifyItemRemoved(position)

                Snackbar.make(
                    binding.root,
                    "${getString(R.string.DeletedTransfer)} \"${item.name}\"",
                    Snackbar.LENGTH_SHORT
                ).apply {
                    setAction(getString(R.string.Restore)) {
                        mainVM.additionalTransfers.add(position, item)
                        binding.transfersRv.adapter?.notifyItemInserted(position)
                    }
                    show()
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.transfersRv)
    }

    private fun DEBUG() {
        for(i in 1..10)
            mainVM.additionalTransfers.add(Transfer(listOf(mainVM.names[0]), listOf(mainVM.names[0]), 1.0, "test"))
    }
}