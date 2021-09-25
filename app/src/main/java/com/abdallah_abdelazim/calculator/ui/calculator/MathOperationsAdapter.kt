package com.abdallah_abdelazim.calculator.ui.calculator

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.abdallah_abdelazim.calculator.R
import com.abdallah_abdelazim.calculator.data.model.MathQuestion
import com.abdallah_abdelazim.calculator.databinding.ItemMathOperationBinding

class MathOperationsAdapter(
    private var mathQuestionsList: List<MathQuestion>? = null
) : RecyclerView.Adapter<MathOperationsAdapter.ViewHolder>() {

    companion object {
        private val TAG = MathOperationsAdapter::class.simpleName
    }

    private lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (!::inflater.isInitialized) inflater = LayoutInflater.from(parent.context)

        val binding: ItemMathOperationBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_math_operation,
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.mathQuestion = mathQuestionsList?.get(position)
    }

    override fun getItemCount(): Int {
        return mathQuestionsList?.size ?: 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMathQuestions(mathQuestionsList: List<MathQuestion>?) {
        this.mathQuestionsList = mathQuestionsList
        notifyDataSetChanged()
    }

    class ViewHolder(internal val binding: ItemMathOperationBinding) :
        RecyclerView.ViewHolder(binding.root)
}