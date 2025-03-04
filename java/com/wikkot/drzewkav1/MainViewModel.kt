package com.wikkot.drzewkav1

import android.widget.Button
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var mainNavButton: Button? = null

    val names = mutableListOf<String>()
    val sharedCosts = mutableListOf<Double>()
    val additionalTransfers = mutableListOf<Transfer>() // Transfer = {senders, recipients, transferAmount}

    fun clear(){
        names.clear()
        sharedCosts.clear()
        additionalTransfers.clear()
        mainNavButton = null
    }

    fun removeAt(position: Int) {
        val removedName = names[position]
        names.removeAt(position)
        sharedCosts.removeAt(position)

        for(i in additionalTransfers.indices.reversed()) {
            if(additionalTransfers[i].senders.contains(removedName) or additionalTransfers[i].recipients.contains(removedName)){
                additionalTransfers.removeAt(i)
            }
        }
    }

    fun addName(name: String) {
        names.add(name)
        sharedCosts.add(0.0)
    }
}