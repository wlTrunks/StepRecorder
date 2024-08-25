package com.lingdtkhe.sgooglefitsaver.ui.screen.mappers

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateMapper {
    private val simpleDateFormat = SimpleDateFormat("MM/dd/yy", Locale.getDefault())
    fun map(time: Long): String = simpleDateFormat.format(Date(time))
}