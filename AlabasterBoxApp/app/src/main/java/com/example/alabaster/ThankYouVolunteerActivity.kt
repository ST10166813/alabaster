package com.example.alabaster

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import kotlin.math.ceil

class ThankYouVolunteerActivity : AppCompatActivity() {

    private lateinit var lottie: LottieAnimationView
    private val handler = Handler(Looper.getMainLooper())
    private var redirected = false

    private fun goHome() {
        if (redirected) return
        redirected = true
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thank_you_volunteer)

        lottie = findViewById(R.id.thankYouLottie)

        // Defensive defaults
        lottie.repeatCount = 0
        lottie.repeatMode = LottieDrawable.RESTART
        lottie.progress = 0f

        // 1) When composition loads, set view aspect ratio to match animation
        lottie.addLottieOnCompositionLoadedListener { comp ->
            val w = comp.bounds.width().coerceAtLeast(1)
            val h = comp.bounds.height().coerceAtLeast(1)

            val params = lottie.layoutParams as ConstraintLayout.LayoutParams
            // Width is fixed by percent; set height via ratio so it never crops
            params.dimensionRatio = "W,$w:$h"
            lottie.layoutParams = params
            lottie.requestLayout()

            // Start animation once sizing is correct
            lottie.playAnimation()

            // Fallback: redirect after actual duration (+ small buffer)
            val durationMs = ceil(comp.duration).toLong()
            handler.postDelayed({ goHome() }, durationMs + 300L)
        }

        // 2) Primary redirect path: when animation ends
        lottie.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) { goHome() }
        })

        // 3) Backup if some files never dispatch onAnimationEnd
        lottie.addAnimatorUpdateListener {
            if (lottie.progress >= 0.995f) goHome()
        }
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
