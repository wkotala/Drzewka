package com.wikkot.drzewkav1

import android.text.SpannableStringBuilder
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.wikkot.drzewkav1.databinding.TransferBinding


class TransferListAdapter(private val transfers: List<Transfer>)
    : RecyclerView.Adapter<TransferListAdapter.TransferVh>() {

    inner class TransferVh(binding: TransferBinding): ViewHolder(binding.root){
        val transferInfo = binding.transferInfo
        val horizontalDivider = binding.horizontalDivider
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransferVh {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TransferBinding.inflate(inflater, parent, false)
        return TransferVh(binding)
    }

    override fun getItemCount(): Int {
        return transfers.size + 1
    }

    override fun onBindViewHolder(holder: TransferVh, position: Int) {
        if(isFooter(position)) {
            holder.horizontalDivider.visibility = View.GONE
            holder.transferInfo.visibility = View.INVISIBLE
            holder.transferInfo.text = "\n\n\n"
            return
        }

        assert(transfers[position].senders.isNotEmpty())
        assert(transfers[position].recipients.isNotEmpty())

        val sentAmount = doubleToAmount(transfers[position].recipients.size * transfers[position].amount)
        val receivedAmount = doubleToAmount(transfers[position].senders.size * transfers[position].amount)
        val transferName = transfers[position].name

        holder.horizontalDivider.visibility = View.VISIBLE
        holder.transferInfo.visibility = View.VISIBLE
        holder.transferInfo.text = prepareTransferInfo(sentAmount, receivedAmount, position, transferName)
    }

    private fun isFooter(position: Int): Boolean {
        return position == transfers.size
    }

    private fun prepareTransferInfo(
        sentAmount: String,
        receivedAmount: String,
        position: Int,
        transferName: String
    ): SpannableStringBuilder {
        return SpannableStringBuilder()
            .bold { append("Nazwa: ") }
            .append("$transferName\n")
            .bold { append("Nadawcy: ") }
            .append(transfers[position].senders.joinToString(", ", "", "\n"))
            .bold { append("Odbiorcy: ") }
            .append(transfers[position].recipients.joinToString(", ", "", "\n"))
            .bold { append("Kwota: ") }
            .append("$sentAmount zł/nadawca | $receivedAmount zł/odbiorca")
    }
}