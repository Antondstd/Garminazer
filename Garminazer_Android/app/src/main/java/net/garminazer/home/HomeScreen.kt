package net.garminazer.home

import android.R.id.message
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.provider.AlarmClock
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.ContextCompat.startForegroundService
import java.util.Calendar


@Composable
fun HomeScreen(
) {
    var showBranding by rememberSaveable { mutableStateOf(true) }
    Scaffold(modifier = Modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            AnimatedVisibility(
                showBranding, Modifier.fillMaxWidth()
            ) {
                Branding()
            }
        }
    }
}

@Composable
private fun Branding(modifier: Modifier = Modifier) {
    val localContext = LocalContext.current
    Column(
        modifier = modifier.wrapContentHeight(align = Alignment.CenterVertically)
    ) {
        Text("Test", fontSize = 30.sp)
        Button(onClick = { startAlarm(localContext) }) {
            Text("Start Alarm", fontSize = 30.sp)
        }

        Button(onClick = { startAlarm(localContext) }) {
            Text("Start Alarm", fontSize = 30.sp)
        }
        Spacer(modifier = Modifier.size(40.dp))

        Button(onClick = { stopAlarm(localContext) }) {
            Text("Stop Alarm", fontSize = 30.sp)
        }
    }
}


@SuppressLint("ScheduleExactAlarm")
fun startAlarm(context: Context){
    val alarmManager = context.getSystemService(AlarmManager::class.java)
//    alarmManager?.set(
//        AlarmManager.ELAPSED_REALTIME_WAKEUP,
//        SystemClock.elapsedRealtime() + 5 * 1000,
//        PendingIntent.getActivity(context, 0, Intent(), PendingIntent.FLAG_IMMUTABLE)
//    )
//    val time = SystemClock.elapsedRealtime() + 5 * 1000
//    val pendingIntent = PendingIntent.getActivity(context, 0, Intent(), PendingIntent.FLAG_IMMUTABLE)
//    alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(time,pendingIntent),pendingIntent)
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.MINUTE, 1)

    val intent: Intent = Intent(AlarmClock.ACTION_SET_ALARM)
        .putExtra(AlarmClock.EXTRA_MESSAGE, message) // show on it
        .putExtra(AlarmClock.EXTRA_HOUR, calendar.get(Calendar.HOUR_OF_DAY)) //24 hours
        .putExtra(AlarmClock.EXTRA_MINUTES, calendar.get(Calendar.MINUTE)) //not more than 60 :)
        .putExtra(AlarmClock.EXTRA_SKIP_UI,true)
    startActivity(context,intent,null)
}

fun stopAlarm(context: Context){
    val alarmManager = context.getSystemService(AlarmManager::class.java)
    val next = alarmManager.nextAlarmClock
    if (next != null) {
//        alarmManager.cancel(next.showIntent)
        next.showIntent.cancel()
    }
    val intent = Intent(AlarmClock.ACTION_DISMISS_ALARM)
        .putExtra(AlarmClock.EXTRA_SKIP_UI,true)
        .putExtra(AlarmClock.EXTRA_ALARM_SEARCH_MODE,AlarmClock.ALARM_SEARCH_MODE_NEXT)
//    val pIntent = PendingIntent.getBroadcast(context, 1001, intent, PendingIntent.FLAG_IMMUTABLE);
//    manager.cancel(pIntent)
    startActivity(context,intent,null)
}


@Preview(name = "Welcome light theme", uiMode = UI_MODE_NIGHT_YES)
@Preview(name = "Welcome dark theme", uiMode = UI_MODE_NIGHT_NO)
@Composable
fun HomecreenPreview() {
    HomeScreen(
    )
}