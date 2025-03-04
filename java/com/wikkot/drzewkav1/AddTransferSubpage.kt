package com.wikkot.drzewkav1

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.wikkot.drzewkav1.databinding.FragmentAddTransferSubpageBinding
import java.lang.Integer.min

class AddTransferSubpage : Fragment() {
    private var _binding: FragmentAddTransferSubpageBinding? = null
    private val binding get() = _binding!!
    private val mainVM by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTransferSubpageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainVM.mainNavButton!!.visibility = View.GONE

        var transferAmountWrapper = doubleArrayOf(0.0)
        var senders: MutableList<Boolean> = MutableList(mainVM.names.size) {false}
        var recipients: MutableList<Boolean> = MutableList(mainVM.names.size) {false}

        binding.sendersAndRecipientsRv.layoutManager = LinearLayoutManager(activity)
        binding.sendersAndRecipientsRv.adapter = SenderRecipientAdapter(
            mainVM.names.toList(),
            senders,
            recipients,
            binding.senderBox,
            binding.recipientBox,
            binding.sentAmount,
            binding.receivedAmount,
            transferAmountWrapper
        )

        fun updateAmounts() {
            binding.sentAmount.setText(doubleToAmount(transferAmountWrapper[0] * recipients.count{it}))
            binding.receivedAmount.setText(doubleToAmount(transferAmountWrapper[0] * senders.count{it}))
        }

        updateAmounts()

        fun resetAmounts() {
            transferAmountWrapper[0] = 0.0
            updateAmounts()
        }

        // Checkboxy "check all"
        binding.senderBox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                senders.fill(true)
                binding.sendersAndRecipientsRv.adapter?.notifyDataSetChanged()
                resetAmounts()
            } else if(senders.count{it} == senders.size) {
                senders.fill(false)
                binding.sendersAndRecipientsRv.adapter?.notifyDataSetChanged()
                resetAmounts()
            }
        }

        binding.recipientBox.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                recipients.fill(true)
                binding.sendersAndRecipientsRv.adapter?.notifyDataSetChanged()
                resetAmounts()
            } else if(recipients.count{it} == recipients.size) {
                recipients.fill(false)
                binding.sendersAndRecipientsRv.adapter?.notifyDataSetChanged()
                resetAmounts()
            }
        }

        // Zmiana kwot
        binding.sentAmount.setOnKeyListener { _, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                transferAmountWrapper[0] = if (binding.sentAmount.text.isEmpty() or
                    (senders.count{it} == 0) or (recipients.count{it} == 0)) {
                    0.0
                } else {
                    binding.sentAmount.text.toString().toDouble() / recipients.count{it}
                }

                updateAmounts()
                binding.sentAmount.clearFocus()
            }
            false
        }

        binding.receivedAmount.setOnKeyListener { _, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                transferAmountWrapper[0] = if (binding.receivedAmount.text.isEmpty() or
                    (senders.count{it} == 0) or (recipients.count{it} == 0)) {
                    0.0
                } else {
                    binding.receivedAmount.text.toString().toDouble() / senders.count{it}
                }

                updateAmounts()
                binding.receivedAmount.clearFocus()
            }
            false
        }

        // Zapis i dodanie przelewu
        binding.SaveTransferButton.setOnClickListener {
            val transferName = binding.transferName.text.toString()
            var errorText: String? = null

            if(transferName.isEmpty()) {
                errorText = getString(R.string.EmptyTransferNameError)
            } else if(senders.count{it} == 0) {
                errorText = getString(R.string.NoSendersError)
            } else if(recipients.count{it} == 0) {
                errorText = getString(R.string.NoRecipientsError)
            } else if(transferAmountWrapper[0] * min(senders.count{it}, recipients.count{it}) < Constants.grosz / 2) {
                errorText = getString(R.string.TransferAmountTooLittle)
            }

            if(errorText != null) {
                Snackbar.make(binding.root, errorText, Snackbar.LENGTH_SHORT).show()
            }
            else {
                saveTransfer(senders, recipients, transferAmountWrapper[0], transferName)
            }
        }
    }

    private fun saveTransfer(
        senders: MutableList<Boolean>,
        recipients: MutableList<Boolean>,
        transferAmount: Double,
        transferName: String
    ) {
        val senderList: List<String> = buildList {
            senders.forEachIndexed { index, isSender ->
                if (isSender) {
                    add(mainVM.names[index])
                }
            }
        }

        val recipientList: List<String> = buildList {
            recipients.forEachIndexed { index, isRecipient ->
                if(isRecipient){
                    add(mainVM.names[index])
                }
            }
        }

        mainVM.additionalTransfers.add(
            Transfer(senderList, recipientList, transferAmount, transferName))
        findNavController().popBackStack()
    }
}