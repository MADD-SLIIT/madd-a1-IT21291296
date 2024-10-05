package com.example.kotlintodopractice.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.kotlintodopractice.R
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.bottomnavigation.BottomNavigationView

class SplashFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        mAuth.signOut()

        val imageView = view.findViewById<ImageView>(R.id.imageView3)
        val logoImage = view.findViewById<ImageView>(R.id.logo_image)

        startRotationAnimation(imageView)
        startFadeInAnimation(logoImage)

        Handler(Looper.getMainLooper()).postDelayed({
            navController.navigate(R.id.action_splashFragment_to_signInFragment)
        }, 3000) // 3-second delay to simulate splash screen
    }

    private fun init(view: View) {
        mAuth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)
    }

    private fun startRotationAnimation(imageView: ImageView) {
        val rotateAnimation = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 10000
            repeatCount = Animation.INFINITE
            interpolator = android.view.animation.LinearInterpolator()
        }

        imageView.startAnimation(rotateAnimation)
    }

    private fun startFadeInAnimation(imageView: ImageView) {
        val fadeIn = AlphaAnimation(0.0f, 1.0f).apply {
            duration = 2500 // 1 second fade-in duration
            fillAfter = true // Keeps the image visible after animation
        }

        imageView.startAnimation(fadeIn)
    }
}
