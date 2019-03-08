package com.example.animationdemo

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {
    private var isToggled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val root: LinearLayout = findViewById(R.id.personCard)

        this.resetCardBody(root.personEntryBody)

        root.infoIcon.setOnClickListener { toggleCardBody(root.personEntryBody) }
    }

    private fun resetCardBody(body: View) {
        if (isToggled) {
            body.layoutParams.height = 0
            body.requestLayout()
            isToggled = false
        }
    }

    private fun toggleCardBody(body: View) {
        body.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val a11yFontScale = body.context.resources.configuration.fontScale
        val maxHeight = ((body.measuredHeight + body.paddingTop + body.paddingBottom) * a11yFontScale).toInt()

        val startHeight = if (isToggled) maxHeight else 0
        val targetHeight = if (isToggled) 0 else maxHeight

        val expandAnimator = ValueAnimator
            .ofInt(startHeight, targetHeight)
            .setDuration(200)

        expandAnimator.addUpdateListener {
            val value = it.animatedValue as Int
            body.layoutParams.height = value
            body.requestLayout()
        }

        expandAnimator.interpolator = FastOutSlowInInterpolator()

        expandAnimator.doOnEnd {
            if (!isToggled) body.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED)
            isToggled = !isToggled
        }

        expandAnimator.start()
    }
}
