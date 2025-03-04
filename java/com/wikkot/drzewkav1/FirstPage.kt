package com.wikkot.drzewkav1

import android.graphics.Rect
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wikkot.drzewkav1.databinding.FragmentFirstPageBinding

class FirstPage : Fragment() {
    private var _binding: FragmentFirstPageBinding? = null
    private val binding get() = _binding!!
    private val mainVM by activityViewModels<MainViewModel>()
    private var isKeyboardVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstPageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainVM.mainNavButton = binding.NextPageButton
        //DEBUG()

        binding.root.hideKeyboard()
        isKeyboardVisible = false
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)

        binding.NextPageButton.setOnClickListener {
            //checkAndAddName(binding.NameInput.text.toString(), true)
            findNavController().navigate(R.id.action_firstPage_to_secondPage)
        }

        binding.namesRv.layoutManager = LinearLayoutManager(activity)
        binding.namesRv.adapter = NameListAdapter(mainVM.names)

        binding.NameInput.setOnKeyListener { _, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP){
                checkAndAddName(binding.NameInput.text.toString())
                true
            } else{
                false
            }
        }

        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val removedName = mainVM.names[position]

                mainVM.removeAt(position)
                binding.namesRv.adapter?.notifyItemRemoved(position)

                Toast.makeText(activity, "${getString(R.string.Deleted)} \"${removedName}\"", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.namesRv)
    }

    private fun DEBUG() {
        mainVM.names.addAll(('A'..'Z').map { it.toString() })
        mainVM.sharedCosts.addAll(List(('A'..'Z').map{it}.size) { 0.0 })
    }

    private fun checkAndAddName(nameFromUser: String, ignoreErrors: Boolean = false) {
        var name = nameFromUser.trim()
        if(name.isEmpty()){
            if(!ignoreErrors) {
                Toast.makeText(activity, getString(R.string.BlankNameError), Toast.LENGTH_SHORT).show()
            }
        }
        else if(mainVM.names.contains(name)){
            if(!ignoreErrors) {
                Toast.makeText(activity, getString(R.string.RepeatNameError), Toast.LENGTH_SHORT).show()
            }
        }
        else{
            mainVM.addName(name)
            val newItemPosition = mainVM.names.size - 1
            binding.namesRv.adapter?.notifyItemInserted(newItemPosition)
            binding.namesRv.scrollToPosition(newItemPosition)
        }
        binding.NameInput.text.clear()
    }

    private val globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val rect = Rect()
        if(_binding != null){
            binding.root.getWindowVisibleDisplayFrame(rect)
            val screenHeight = requireActivity().resources.displayMetrics.heightPixels
            val keypadHeight = screenHeight - rect.bottom
            val shouldKeyboardBeVisible = (keypadHeight > screenHeight * 0.15)

            if(isKeyboardVisible != shouldKeyboardBeVisible) {
                isKeyboardVisible = shouldKeyboardBeVisible

                if(isKeyboardVisible) {
                    binding.NextPageButton.visibility = View.GONE
                } else {
                    binding.NextPageButton.postDelayed({
                        binding.NextPageButton.visibility = View.VISIBLE
                    }, 10)
                }
            }
        }
    }
}