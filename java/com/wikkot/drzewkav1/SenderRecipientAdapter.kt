package com.wikkot.drzewkav1

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.wikkot.drzewkav1.databinding.SenderRecipientBinding

class SenderRecipientAdapter(private val names: List<String>,
                             private var senders: MutableList<Boolean>,
                             private var recipients: MutableList<Boolean>,
                             private var senderBox: CheckBox,
                             private var recipientBox: CheckBox,
                             private var sentAmount: EditText,
                             private var receivedAmount: EditText,
                             private var transferAmountWrapper: DoubleArray
)
    : RecyclerView.Adapter<SenderRecipientAdapter.SenderRecipientVh>() {

    inner class SenderRecipientVh(binding: SenderRecipientBinding) : ViewHolder(binding.root) {
        val senderBox = binding.senderBox
        val recipientBox = binding.recipientBox
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SenderRecipientVh {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SenderRecipientBinding.inflate(inflater, parent, false)
        return SenderRecipientVh(binding)
    }

    override fun getItemCount(): Int {
        return names.size
    }

    override fun onBindViewHolder(holder: SenderRecipientVh, position: Int) {
        holder.senderBox.setOnCheckedChangeListener(null) // IMPORTANT
        holder.recipientBox.setOnCheckedChangeListener(null) // IMPORTANT

        holder.senderBox.text = names[position]
        holder.recipientBox.text = names[position]
        holder.senderBox.isChecked = senders[position]
        holder.recipientBox.isChecked = recipients[position]

        holder.senderBox.setOnCheckedChangeListener { _, isChecked ->
            senders[position] = isChecked
            senderBox.isChecked = (senders.count{it} == senders.size)
            resetAmounts()
        }

        holder.recipientBox.setOnCheckedChangeListener {_, isChecked ->
            recipients[position] = isChecked
            recipientBox.isChecked = (recipients.count{it} == recipients.size)
            resetAmounts()
        }
    }

    private fun resetAmounts() {
        transferAmountWrapper[0] = 0.0
        sentAmount.setText(doubleToAmount(0.0))
        receivedAmount.setText(doubleToAmount(0.0))
    }
}