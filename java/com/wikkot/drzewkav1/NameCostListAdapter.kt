package com.wikkot.drzewkav1

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.wikkot.drzewkav1.databinding.NameCostBinding

class NameCostListAdapter(private val names: List<String>, private val costs: MutableList<Double>, private val context: Context)
    : RecyclerView.Adapter<NameCostListAdapter.NameCostVh>() {
    inner class NameCostVh(binding: NameCostBinding): RecyclerView.ViewHolder(binding.root){
        val name = binding.name
        val cost = binding.cost
        val addCost = binding.addCost
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameCostVh {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NameCostBinding.inflate(inflater, parent, false)
        return NameCostVh(binding)
    }

    override fun getItemCount(): Int {
        assert(names.size==costs.size)
        return names.size
    }

    override fun onBindViewHolder(holder: NameCostVh, position: Int) {
        holder.name.text = names[position]
        holder.cost.text = doubleToAmount(costs[position])
        holder.addCost.setText("")

        holder.addCost.setOnKeyListener { _, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if(holder.addCost.text.isNotEmpty()){
                    val amount = holder.addCost.text.toString().toDouble()
                    costs[position] += amount
                    holder.cost.text = doubleToAmount(costs[position])
                    holder.addCost.text.clear()
                    holder.addCost.clearFocus()
                    holder.addCost.hideKeyboard()

                    Toast.makeText(context, addedCostInfo(amount, position), Toast.LENGTH_SHORT)
                        .show()
                }
                true
            }
            else{
                false
            }
        }

    }

    private fun addedCostInfo(amount: Double, position: Int) : SpannableStringBuilder {
        return SpannableStringBuilder()
            .append("Dodano ")
            .bold { append("${doubleToAmount(amount)} zł") }
            .append(" dla ")
            .bold { append(names[position]) }
    }
}

