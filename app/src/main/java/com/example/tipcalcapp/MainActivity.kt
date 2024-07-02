package com.example.tipcalcapp


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tipcalcapp.Components.InputField
import com.example.tipcalcapp.Util.calculateTotalPerPerson
import com.example.tipcalcapp.Util.calculateTotalTip
import com.example.tipcalcapp.Widgets.RoundIconButton
import com.example.tipcalcapp.Components.InputField
import com.example.tipcalcapp.ui.theme.TipCalcAppTheme
import com.example.tipcalcapp.Util.calculateTotalPerPerson
import com.example.tipcalcapp.Util.calculateTotalTip
import com.example.tipcalcapp.Widgets.RoundIconButton
import com.example.tipcalcapp.Components.InputField
import com.example.tipcalcapp.ui.theme.TipCalcAppTheme
import com.example.tipcalcapp.Util.calculateTotalPerPerson
import com.example.tipcalcapp.Util.calculateTotalTip
import com.example.tipcalcapp.Widgets.RoundIconButton
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalcAppTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(navController = navController)
        }
        composable("main") {
            MainContent()
        }
    }
}

@Composable
fun WelcomeScreen(navController: NavHostController) {
    LaunchedEffect(key1 = true) {
        delay(1000L)  // Delay for 1 seconds
        navController.navigate("main")
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFf3c4fb)),
        color = Color(0xFFf3c4fb)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.tip_calc_app),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Welcome to JetTipApp",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Calculate tips easily and quickly!",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp
                ),
                color = Color.Black
            )
        }
    }
}

@Composable
fun TopHeader(totalPerPerson: Double) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(20.dp)
            .clip(RectangleShape),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFf3c4fb)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val total = "%.2f".format(totalPerPerson)
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "$$total",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun MainContent() {
    val splitByState = remember { mutableStateOf(1) }
    val tipAmountState = remember { mutableStateOf(0.0) }
    val totalPerPersonState = remember { mutableStateOf(0.0) }
    val totalBillState = remember { mutableStateOf("") }
    val sliderPositionState = remember { mutableStateOf(0f) }

    val tipPercentage by remember { derivedStateOf { (sliderPositionState.value * 100).toInt() } }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopHeader(totalPerPerson = totalPerPersonState.value)
        BillForm(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            splitByState = splitByState,
            tipAmountState = tipAmountState,
            totalPerPersonState = totalPerPersonState,
            totalBillState = totalBillState,
            sliderPositionState = sliderPositionState  // Pass sliderPositionState
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    splitByState: MutableState<Int>,
    tipAmountState: MutableState<Double>,
    totalPerPersonState: MutableState<Double>,
    totalBillState: MutableState<String>,
    sliderPositionState: MutableState<Float>  // Added sliderPositionState
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val tipPercentage by remember { derivedStateOf { (sliderPositionState.value * 100).toInt() } }

    Surface(
        modifier = modifier
            .padding(20.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(CornerSize(8.dp)),
        border = BorderStroke(1.dp, Color.LightGray),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            InputField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                isSingleLine = true,
                onValueChange = {
                    totalBillState.value = it
                    val totalBill = totalBillState.value.toDoubleOrNull() ?: 0.0
                    tipAmountState.value = calculateTotalTip(totalBill, tipPercentage)
                    totalPerPersonState.value = calculateTotalPerPerson(totalBill, splitByState.value, tipPercentage)
                },
                onAction = KeyboardActions {
                    keyboardController?.hide()
                }
            )

            Row(
                modifier = Modifier.padding(3.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text("Split", modifier = Modifier.align(Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(120.dp))
                Row(
                    modifier = Modifier.padding(horizontal = 3.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    RoundIconButton(
                        imageVector = Icons.Default.Remove,
                        onClick = {
                            if (splitByState.value > 1) {
                                splitByState.value--
                                val totalBill = totalBillState.value.toDoubleOrNull() ?: 0.0
                                tipAmountState.value = calculateTotalTip(totalBill, tipPercentage)
                                totalPerPersonState.value = calculateTotalPerPerson(totalBill, splitByState.value, tipPercentage)
                            }
                        }
                    )
                    Text(
                        text = "${splitByState.value}",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 9.dp, end = 9.dp)
                    )
                    RoundIconButton(
                        imageVector = Icons.Default.Add,
                        onClick = {
                            splitByState.value++
                            val totalBill = totalBillState.value.toDoubleOrNull() ?: 0.0
                            tipAmountState.value = calculateTotalTip(totalBill, tipPercentage)
                            totalPerPersonState.value = calculateTotalPerPerson(totalBill, splitByState.value, tipPercentage)
                        }
                    )
                }
            }

            Row(
                modifier = Modifier.padding(horizontal = 3.dp, vertical = 12.dp)
            ) {
                Text("Tip", modifier = Modifier.align(alignment = Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(180.dp))
                Text("$${tipAmountState.value}", modifier = Modifier
                    .align(alignment = Alignment.CenterVertically))
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("${tipPercentage} %")

                Spacer(modifier = Modifier.height(14.dp))

                Slider(
                    value = sliderPositionState.value,
                    onValueChange = { newVal ->
                        sliderPositionState.value = newVal
                        val totalBill = totalBillState.value.toDoubleOrNull() ?: 0.0
                        tipAmountState.value = calculateTotalTip(totalBill, tipPercentage)
                        totalPerPersonState.value = calculateTotalPerPerson(totalBill, splitByState.value, tipPercentage)
                    },
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TipCalcAppTheme {
        MainContent()
    }
}
