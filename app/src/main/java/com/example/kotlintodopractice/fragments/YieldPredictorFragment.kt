package com.example.kotlindotopractice.utils

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.kotlintodopractice.R
import com.example.kotlintodopractice.databinding.FragmentYieldPredictorBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class YieldPredictorFragment : Fragment() {

    private var _binding: FragmentYieldPredictorBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentYieldPredictorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI components
        val baseYieldInput = binding.inputBaseYield
        val soilTypeGroup = binding.soilTypeGroup
        val rainfallGroup = binding.rainfallGroup
        val fertilizerUrea = binding.fertilizerUrea
        val fertilizerSuperphosphate = binding.fertilizerSuperphosphate
        val fertilizerPotassiumSulfate = binding.fertilizerPotassiumSulfate
        val calculateButton = binding.calculateButton
        bottomNavigation = requireActivity().findViewById(R.id.bottom_navigation34) // Initialize BottomNavigationView

        // Set click listener for calculate button
        calculateButton.setOnClickListener {
            calculateYield(baseYieldInput, soilTypeGroup, rainfallGroup, fertilizerUrea, fertilizerSuperphosphate, fertilizerPotassiumSulfate)
        }

        // Set up bottom navigation listener
        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            Log.d("BottomNav", "Menu item selected: ${menuItem.itemId}")
            when (menuItem.itemId) {
                R.id.nav_todo -> {
                    findNavController().navigate(R.id.action_yieldPredictorFragment_to_homeFragment3)
                    true
                }
                R.id.ic_calc -> {
                    true // Do nothing, already on this fragment
                }
                R.id.ic_logout -> {
                    findNavController().navigate(R.id.action_yieldPredictorFragment_to_signInFragment) // Adjust with your action ID
                    true
                }
                else -> false
            }
        }
    }

    private fun calculateYield(
        baseYieldInput: EditText,
        soilTypeGroup: RadioGroup,
        rainfallGroup: RadioGroup,
        fertilizerUrea: CheckBox,
        fertilizerSuperphosphate: CheckBox,
        fertilizerPotassiumSulfate: CheckBox
    ) {
        val baseYield = baseYieldInput.text.toString().toDoubleOrNull()
        if (baseYield == null) {
            Toast.makeText(requireContext(), "Please enter a valid base yield", Toast.LENGTH_SHORT).show()
            return
        }

        // Determine soil type factor
        val soilFactor = when (soilTypeGroup.checkedRadioButtonId) {
            R.id.soil_loamy -> 1.2
            R.id.soil_clay -> 0.8
            R.id.soil_sandy -> 0.6
            else -> 1.0
        }

        // Determine rainfall factor
        val rainfallFactor = when (rainfallGroup.checkedRadioButtonId) {
            R.id.rainfall_low -> 0.7
            R.id.rainfall_moderate -> 1.0
            R.id.rainfall_high -> 1.3
            else -> 1.0
        }

        // Fertilizer adjustments
        var fertilizerFactor = 1.0
        if (fertilizerUrea.isChecked) {
            fertilizerFactor += 0.1
        }
        if (fertilizerSuperphosphate.isChecked) {
            fertilizerFactor += 0.15
        }
        if (fertilizerPotassiumSulfate.isChecked) {
            fertilizerFactor += 0.2
        }

        // Calculate the final yield
        val finalYield = baseYield * soilFactor * rainfallFactor * fertilizerFactor

        // Inflate custom layout for result dialog
        val dialogView = layoutInflater.inflate(R.layout.dialog_yield_result, null)
        val titleTextView = dialogView.findViewById<TextView>(R.id.predicted_yield_title)
        val messageTextView = dialogView.findViewById<TextView>(R.id.predicted_yield_message)

        // Set the calculated result
        messageTextView.text = String.format("The predicted yield is: %.2f kg/ha", finalYield)

        // Create the AlertDialog
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(dialogView)
            .setPositiveButton("OK") { dialog, _ ->
                // Animate the dialog out before dismissing
                val scaleOut = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_out)
                dialogView.startAnimation(scaleOut)
                scaleOut.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {}
                    override fun onAnimationEnd(animation: Animation?) {
                        dialog.dismiss() // Dismiss after the animation ends
                    }
                    override fun onAnimationRepeat(animation: Animation?) {}
                })
            }

        // Show the dialog
        val dialog = dialogBuilder.create()
        dialog.show()

        // Start pulsating animation for the dialog content

        // Customize the "OK" button color to green after showing the dialog
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#4CAF50"))

        // Start color animation for the dialog background
        val dialogBackground = dialogView.findViewById<LinearLayout>(R.id.dialog_background) // Reference to the background layout
        val colorFrom = Color.parseColor("#4CAF50") // Original green color
        val colorTo = Color.parseColor("#A5D6A7") // Lighter green color

        val colorAnimation = ObjectAnimator.ofArgb(dialogBackground, "backgroundColor", colorFrom, colorTo)
        colorAnimation.duration = 1000 // 1 second for the color change
        colorAnimation.repeatCount = ObjectAnimator.INFINITE
        colorAnimation.repeatMode = ObjectAnimator.REVERSE
        colorAnimation.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}
