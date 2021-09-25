package com.abdallah_abdelazim.calculator.ui.calculator

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.abdallah_abdelazim.calculator.R
import com.abdallah_abdelazim.calculator.data.service.mathengine.MathEngineService
import com.abdallah_abdelazim.calculator.data.service.mathengine.MathOperator.*
import com.abdallah_abdelazim.calculator.databinding.FragmentCalculatorBinding
import com.abdallah_abdelazim.calculator.util.showSnackbar


class CalculatorFragment : Fragment() {

    companion object {
        private val TAG = CalculatorFragment::class.simpleName
    }

    private var _binding: FragmentCalculatorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: CalculatorViewModel by viewModels()

    private lateinit var mathEngineService: MathEngineService
    private var isBound: Boolean = false

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MathEngineService.MathEngineBinder
            mathEngineService = binder.service
            isBound = true

            mathEngineService.resultMathQuestion.observe(viewLifecycleOwner, {
                Log.d(TAG, "MathEngineService result: $it")
                viewModel.notifyMathQuestionResult(it)
            })
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            isBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bind to MathEngineService
        Intent(context, MathEngineService::class.java).also { intent ->
            context?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_calculator, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.pendingMathQuestions.observe(viewLifecycleOwner, {
            Log.d(TAG, "pendingMathQuestions = $it")
            // TODO
        })

        viewModel.completedMathQuestions.observe(viewLifecycleOwner, {
            Log.d(TAG, "completedMathQuestions = $it")
            // TODO
        })

        viewModel.calculateMathQuestionEvent.observe(viewLifecycleOwner, {
            if (isBound) {
                mathEngineService.calculate(it)
            }
        })

        viewModel.messageEvent.observe(viewLifecycleOwner, { msgStrResId ->
            showSnackbar(msgStrResId)
        })

        binding.btnCalculate.setOnClickListener {
            val mathOperator = when (binding.spinnerOperator.selectedItemPosition) {
                1 -> ADD
                2 -> SUBTRACT
                3 -> MULTIPLY
                4 -> DIVIDE
                else -> null
            }

            val operands = if (binding.etOperands.text.isNotEmpty()) {
                binding.etOperands.text.toString()
                    .split(",")
                    .map {
                        it.toDouble()
                    }
            } else null

            val delaySecs = if (binding.etDelay.text.isNotEmpty()) {
                binding.etDelay.text.toString().toLong()
            } else null

            viewModel.calculate(mathOperator, operands, delaySecs)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()

        context?.unbindService(connection)
        isBound = false
    }
}