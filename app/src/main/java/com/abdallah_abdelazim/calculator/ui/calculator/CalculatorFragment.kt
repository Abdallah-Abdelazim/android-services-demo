package com.abdallah_abdelazim.calculator.ui.calculator

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView.*
import com.abdallah_abdelazim.calculator.R
import com.abdallah_abdelazim.calculator.data.service.mathengine.MathEngineService
import com.abdallah_abdelazim.calculator.data.service.mathengine.MathOperator.*
import com.abdallah_abdelazim.calculator.databinding.FragmentCalculatorBinding
import com.abdallah_abdelazim.calculator.util.showSnackbar
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource


class CalculatorFragment : Fragment() {

    companion object {
        private val TAG = CalculatorFragment::class.simpleName
        private const val RC_LOCATION_PERMISSIONS = 1
    }

    private var _binding: FragmentCalculatorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: CalculatorViewModel by viewModels()

    private lateinit var mathEngineService: MathEngineService
    private var isBound: Boolean = false

    private lateinit var pendingQuestionsAdapter: MathOperationsAdapter
    private lateinit var completedQuestionsAdapter: MathOperationsAdapter

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

        setupPendingOperationsRecyclerView()
        setupCompletedOperationsRecyclerView()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.pendingMathQuestions.observe(viewLifecycleOwner, {
            Log.d(TAG, "pendingMathQuestions = $it")

            pendingQuestionsAdapter.updateMathQuestions(it)
            binding.tvNoPendingOperations.visibility = if (it.isEmpty()) {
                VISIBLE
            } else {
                GONE
            }
        })

        viewModel.completedMathQuestions.observe(viewLifecycleOwner, {
            Log.d(TAG, "completedMathQuestions = $it")

            completedQuestionsAdapter.updateMathQuestions(it)
            binding.tvNoCompletedOperations.visibility = if (it.isEmpty()) {
                VISIBLE
            } else {
                GONE
            }
        })

        viewModel.calculateMathQuestionEvent.observe(viewLifecycleOwner, {
            if (isBound) {
                mathEngineService.calculate(it)
            }
        })

        viewModel.showLocationInfoEvent.observe(viewLifecycleOwner, {
            showLocationInfo()
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

    private fun setupPendingOperationsRecyclerView() {
        binding.rvPendingOperations.setHasFixedSize(true)

        val itemDecoration: ItemDecoration =
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.rvPendingOperations.addItemDecoration(itemDecoration)

        pendingQuestionsAdapter = MathOperationsAdapter()
        binding.rvPendingOperations.adapter = pendingQuestionsAdapter
    }

    private fun setupCompletedOperationsRecyclerView() {
        binding.rvCompletedOperations.setHasFixedSize(true)

        val itemDecoration: ItemDecoration =
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.rvCompletedOperations.addItemDecoration(itemDecoration)

        completedQuestionsAdapter = MathOperationsAdapter()
        binding.rvCompletedOperations.adapter = completedQuestionsAdapter
    }

    private fun showLocationInfo() {
        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                RC_LOCATION_PERMISSIONS
            )
        } else {
            binding.btnShowLocationInfo.setText(R.string.loading)
            binding.btnShowLocationInfo.isEnabled = false

            val cancellationTokenSource = CancellationTokenSource()
            fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).addOnCompleteListener(requireActivity()) {

                binding.btnShowLocationInfo.setText(R.string.show_location_info)
                binding.btnShowLocationInfo.isEnabled = true

                val location = it.result

                binding.cardLocationInfo.visibility = VISIBLE
                binding.tvLocationLat.text = location.latitude.toString()
                binding.tvLocationLng.text = location.longitude.toString()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == RC_LOCATION_PERMISSIONS &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            showLocationInfo()
        } else super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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