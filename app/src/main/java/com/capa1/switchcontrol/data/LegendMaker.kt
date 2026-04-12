package com.capa1.switchcontrol.data

import android.content.Context
import com.capa1.switchcontrol.R
import com.capa1.switchcontrol.data.model.Mode
import com.capa1.switchcontrol.data.model.State
import com.capa1.switchcontrol.data.model.SwData
import java.util.Calendar
import javax.inject.Inject

class LegendMaker @Inject constructor(
    private val context: Context,
) {

    private fun isSet( days: Int, position: Int): Boolean {
        return days shr position and 1 == 1
    }

    fun legend(swData: SwData?): String {
        if (swData == null) {
            return  context.getString(R.string.noInfo)
        }
        when (swData.mode) {
            Mode.PULSE_NA,
            Mode.PULSE_NC -> return context.getString(R.string.pulse, swData.secs)
            Mode.TEMP -> {
                return if (swData.tempX10 == -700) {
                    context.getString(R.string.no_sensor)
                } else {
                    context.getString(R.string.turnIf, swData.secs/10)
                }
            }
            else -> {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val min = calendar.get(Calendar.MINUTE)
                val today = calendar.get(Calendar.DAY_OF_WEEK) - 1
                val tomorrow = (today + 1) % 7
                val rightNow = (hour * 60) + min
                var delta = 24 * 60
                if (swData.state != State.ON) {
                    for (prg in swData.prgs) {
                        if (isSet(prg.days, today)) {
                            val deltaTrans = prg.start - rightNow
                            if (deltaTrans in 1..<delta) {
                                delta = deltaTrans
                            }
                        }
                        if (isSet(prg.days, tomorrow)) {
                            val deltaTrans = prg.start + 24 * 60 - rightNow
                            if (deltaTrans < delta) {
                                delta = deltaTrans
                            }
                        }
                    }
                } else {
                    for (prg in swData.prgs) {
                        if (isSet(prg.days, today)) {
                            val deltaTrans = prg.stop - rightNow
                            if (deltaTrans in 1..<delta) {
                                delta = deltaTrans
                            }
                        }
                        if (isSet(prg.days, tomorrow)) {
                            val deltaTrans = prg.stop + 24 * 60 - rightNow
                            if (deltaTrans < delta) {
                                delta = deltaTrans
                            }
                        }
                    }
                }
                val deltaHours = delta / 60
                val deltaMin = delta % 60
                val tempText = if (swData.mode == Mode.TIMERS_TEMP) {
                    context.getString(R.string.ifTemp, swData.secs / 10, swData.tempX10 / 10)
                } else {
                    ""
                }
                return if (deltaHours < 24) {
                    context.getString(R.string.changeIn, deltaHours, deltaMin, tempText)
                } else {
                    context.getString(R.string.changeMore24, tempText)
                }
            }
        }
    }
}