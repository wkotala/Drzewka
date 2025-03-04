package com.wikkot.drzewkav1

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.wikkot.drzewkav1.databinding.SingleTransferBinding

class SingleTransferAdapter(private val transfers: List<SingleTransfer>)
    : RecyclerView.Adapter<SingleTransferAdapter.SingleTransferVh>() {

    inner class SingleTransferVh(binding: SingleTransferBinding) : ViewHolder(binding.root) {
        val transferInfo = binding.transferInfo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleTransferVh {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SingleTransferBinding.inflate(inflater, parent, false)
        return SingleTransferVh(binding)
    }

    override fun getItemCount(): Int {
        return transfers.size
    }

    override fun onBindViewHolder(holder: SingleTransferVh, position: Int) {
        val transfer = transfers[position]

        holder.transferInfo.text = SpannableStringBuilder()
            .append("Przelew od ")
            .bold { append(transfer.sender) }
            .append(" do ")
            .bold { append(transfer.recipient) }
            .append(" na kwotę ")
            .bold { append(transfer.amount + " zł") }
    }
}