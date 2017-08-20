package com.bwidlarz.speechcalculator.utils

import android.app.Activity
import android.content.Context
import android.view.View
import com.bwidlarz.speechcalculator.R
import com.bwidlarz.speechcalculator.common.gone
import com.bwidlarz.speechcalculator.common.visible
import com.bwidlarz.speechcalculator.data.Settings
import com.bwidlarz.speechcalculator.databinding.ActivityMainBinding
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig

class AnimationFactory(private val context: Context, private val sharedPrefSettings: Settings) {

    private val DELAY_TUTORIAL_MS = 0L

    fun setMainShowcase(activityContext: Activity, viewBinding: ActivityMainBinding): MaterialShowcaseSequence {
        val config = ShowcaseConfig()
        config.delay = DELAY_TUTORIAL_MS
        val sequence = MaterialShowcaseSequence(activityContext, sharedPrefSettings.tutorialShownId.toString())
        sequence.apply {

            setConfig(config)
            addSequenceItem(
                    MaterialShowcaseView.Builder(activityContext)
                            .setDismissText(getString(R.string.got_it))
                            .withoutShape()
                            .setContentText(getString(R.string.welcome_content))
                            .build()
            )
            addSequenceItem(viewBinding.buttonBar.newExpressionButton, getString(R.string.expression_content), getString(R.string.got_it))
            addSequenceItem(viewBinding.buttonBar.continueExpressionButton, getString(R.string.continue_content), getString(R.string.got_it))
            addSequenceItem(viewBinding.buttonBar.continuesEvaluationButton, getString(R.string.loop_content), getString(R.string.got_it))
            addSequenceItem(
                    MaterialShowcaseView.Builder(activityContext)
                            .setTarget(viewBinding.expression)
                            .setTargetTouchable(true)
                            .setDismissText(getString(R.string.got_it))
                            .setContentText(getString(R.string.expression_edittext_content))//todo immediately
                            .withRectangleShape(true)
                            .build()
            )
            addSequenceItem(
                    MaterialShowcaseView.Builder(activityContext)
                            .setTarget(viewBinding.evaluation)
                            .setDismissText(getString(R.string.got_it))
                            .setContentText(getString(R.string.evaluation_text_content))
                            .withRectangleShape()
                            .build()
            )
            addSequenceItem(viewBinding.buttonBar.resetButton, getString(R.string.reset_content), getString(R.string.got_it))
            addSequenceItem(viewBinding.buttonBar.stopButton, getString(R.string.stop_content_finish), getString(R.string.got_it))
        }
        return sequence
    }

    fun animateVisibility(view: View, shouldBeVisible: Boolean) {
        view.clearAnimation()

        val finalScale = if (shouldBeVisible) 1f else 0f

        view.animate().scaleX(finalScale).scaleY(finalScale)
                .setDuration(200)
                .withEndAction { if (!shouldBeVisible) view.gone() }
                .withStartAction { if (shouldBeVisible) view.visible() }
                .start()
    }

    private fun getString(resId: Int): String = context.getString(resId)
}