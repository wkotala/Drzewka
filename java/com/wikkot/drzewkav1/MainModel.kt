// Main Logic
package com.wikkot.drzewkav1

import kotlin.math.abs

class MainModel(
    private val names: List<String>,
    private val sharedCosts: List<Double>,
    private val additionalTransfers: List<Transfer>
    ) {

    private val noPeople = names.size
    private val nameToIndex = names.mapIndexed { index, name -> name to index }.toMap()
    private var cashflow: MutableList<Double> = MutableList(noPeople) {0.0} // ile osoba ma dostac (lub oddac, jesli ujemne)
    private var cashflowCopy: MutableList<Double> = mutableListOf()
    private var transfersByIds: MutableList<SingleTransferByIds> = mutableListOf()

    fun getConciseListOfTransfers(): List<SingleTransfer> {
        computeCashflowLists()
        computeListOfTransfersByIds()
        return (transfersByIds.map { SingleTransfer(names[it.senderId], names[it.recipientId], it.amount) })
            .sortedWith(
                compareBy<SingleTransfer> { it.recipient }
                    .thenBy { it.amount.length }
                    .thenBy { it.amount })
    }


    fun getError(): Double {
        for (transfer in transfersByIds) {
            cashflowCopy[transfer.senderId] += transfer.amount.toDouble()
            cashflowCopy[transfer.recipientId] -= transfer.amount.toDouble()
        }
        return cashflowCopy.maxOfOrNull { abs(it) } ?: 0.0
    }

    private fun computeCashflowLists() {
        for(i in 0 until noPeople) {
            cashflow[i] += sharedCosts[i]

            val amount = sharedCosts[i] / noPeople
            for(j in 0 until noPeople) {
                cashflow[j] -= amount
            }
        }

        for(transfer in additionalTransfers) {
            for(sender in transfer.senders) {
                cashflow[nameToIndex[sender]!!] -= transfer.amount * transfer.recipients.size
            }

            for(recipient in transfer.recipients) {
                cashflow[nameToIndex[recipient]!!] += transfer.amount * transfer.senders.size
            }
        }

        cashflowCopy.addAll(cashflow)
    }

    private fun computeListOfTransfersByIds(){
        // generowanie przelewów
        for(i in 0 until noPeople-1) {
            var amount = -cashflow.minBy { it }
            val senderId = cashflow.indexOf(-amount)
            val recipientId = cashflow.indexOf(cashflow.maxBy { it })

            transfersByIds.add(SingleTransferByIds(senderId, recipientId, doubleToAmount(amount)))
            cashflow[recipientId] -= amount
            cashflow[senderId] += amount
        }


        // usuwanie przelewów na kwotę < grosz (= 0)
        for(i in transfersByIds.indices.reversed()) {
            if(transfersByIds[i].amount.toDouble() < Constants.grosz) {
                transfersByIds.removeAt(i)
            }
        }
    }
}