package com.example.eventmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BrandPurple = Color(0xFF5B3CC4)
private val BrandPurpleDark = Color(0xFF3E2A8C)
private val ScreenBg = Color(0xFFF3F1FA)
private val RegisteredGreen = Color(0xFF2E7D32)
private val NotRegisteredRed = Color(0xFFC62828)

val data = listOf(
    listOf("Tech Fest 2026", "A 3 day technology festival with coding contests, hackathons and robotics demos.", "12 Sep 2026", "Not Registered"),
    listOf("Cultural Night", "Music, dance and drama performances by university students on the main stage.", "20 Sep 2026", "Registered"),
    listOf("Sports Meet", "Annual athletics meet with track and field events across all departments.", "28 Sep 2026", "Not Registered"),
    listOf("AI Workshop", "Hands on workshop covering machine learning basics, tools and mini projects.", "05 Oct 2026", "Not Registered"),
    listOf("Book Fair", "Campus book fair with author talks, readings and student discounts.", "15 Oct 2026", "Registered"),
    listOf("Alumni Meet", "Reunion evening for the passed out students of the university.", "25 Oct 2026", "Not Registered"),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = ScreenBg) {
                EventManagement()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventManagement() {
    val registeredList = remember {
        mutableStateListOf(*data.map { it[3] == "Registered" }.toTypedArray())
    }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val openIndex = selectedIndex

    val topBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = BrandPurple,
        titleContentColor = Color.White,
        navigationIconContentColor = Color.White
    )

    Scaffold(
        containerColor = ScreenBg,
        topBar = {
            if (openIndex == null) {
                TopAppBar(
                    title = { Text("Event Management App", fontWeight = FontWeight.Bold) },
                    colors = topBarColors
                )
            } else {
                TopAppBar(
                    title = { Text("Event Details", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { selectedIndex = null }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = topBarColors
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (openIndex == null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Text(
                        text = "Upcoming events",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandPurpleDark
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    for (i in data.indices) {
                        val event = data[i]
                        EventListItem(
                            name = event[0],
                            date = event[2],
                            registered = registeredList[i],
                            onClick = { selectedIndex = i }
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }
            } else {
                val event = data[openIndex]
                EventDetails(
                    name = event[0],
                    description = event[1],
                    date = event[2],
                    registered = registeredList[openIndex],
                    onToggleRegister = { registeredList[openIndex] = !registeredList[openIndex] }
                )
            }
        }
    }
}

@Composable
fun EventListItem(
    name: String,
    date: String,
    registered: Boolean,
    onClick: () -> Unit
) {
    val dotColor = if (registered) RegisteredGreen else NotRegisteredRed

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1D1B20))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = date, fontSize = 14.sp, color = Color(0xFF6B6B6B))
            }
            Spacer(modifier = Modifier.size(12.dp))
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
        }
    }
}

@Composable
fun EventDetails(
    name: String,
    description: String,
    date: String,
    registered: Boolean,
    onToggleRegister: () -> Unit
) {
    val buttonColor by animateColorAsState(
        targetValue = if (registered) RegisteredGreen else NotRegisteredRed,
        animationSpec = tween(durationMillis = 600),
        label = "buttonColor"
    )

    val registerButton = @Composable { fraction: Float ->
        Button(
            onClick = onToggleRegister,
            modifier = Modifier
                .fillMaxWidth(fraction)
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
        ) {
            Text(
                text = if (registered) "Cancel Registration" else "Register",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }

    val statusText = @Composable {
        Text(
            text = if (registered) "Status: Registered" else "Status: Not Registered",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = if (registered) RegisteredGreen else NotRegisteredRed
        )
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isTablet = maxWidth >= 600.dp

        if (isTablet) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = name, fontSize = 40.sp, fontWeight = FontWeight.Bold, color = BrandPurpleDark)
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = description, fontSize = 17.sp, color = Color(0xFF444444))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Date: $date", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color(0xFF6B6B6B))
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    registerButton(0.9f)
                    Spacer(modifier = Modifier.height(14.dp))
                    statusText()
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 28.dp, vertical = 32.dp)
            ) {
                Text(text = name, fontSize = 40.sp, fontWeight = FontWeight.Bold, color = BrandPurpleDark)
                Spacer(modifier = Modifier.height(24.dp))
                Text(text = description, fontSize = 17.sp, color = Color(0xFF444444))
                Spacer(modifier = Modifier.height(18.dp))
                Text(text = "Date: $date", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color(0xFF6B6B6B))

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    registerButton(1f)
                    Spacer(modifier = Modifier.height(16.dp))
                    statusText()
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
