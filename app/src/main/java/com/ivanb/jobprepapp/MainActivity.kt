package com.ivanb.jobprepapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivanb.jobprepapp.R
import com.ivanb.jobprepapp.Week2.RotationDemoScreen
import dagger.hilt.android.AndroidEntryPoint

// Stanja ekrana radi lakše navigacije
enum class Screen {
    HOME,
    APP_NAVIGATION,
    ROTATION_DEMO
}



@AndroidEntryPoint
class MainActivity : androidx.activity.ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Pamti koji je trenutno aktivan ekran
            var currentScreen by rememberSaveable { mutableStateOf(Screen.HOME) }

            when (currentScreen) {
                Screen.HOME -> {
                    MojEkran(
                        name = "Ivan",
                        onOpenAppClick = { currentScreen = Screen.APP_NAVIGATION },
                        onOpenRotationDemoClick = { currentScreen = Screen.ROTATION_DEMO }
                    )
                }
                Screen.APP_NAVIGATION -> {
                    // Pretpostavka je da AppNavigation već postoji u tvom projektu
                    _root_ide_package_.com.ivanb.jobprepapp.Week1.AppNavigation(onCloseApp = {
                        currentScreen = Screen.HOME
                    })
                }
                Screen.ROTATION_DEMO -> {
                    // Ekran s prikazom razlike ViewModel vs remember pri rotaciji
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
                            // Gumb za povratak nazad
                            Button(onClick = { currentScreen = Screen.HOME }) {
                                Text("Natrag na glavni ekran")
                            }
                            Spacer(modifier = Modifier.height(Spacing.medium))

                            // Pozivamo tvoj RotationDemoScreen iz Week2
                            RotationDemoScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MojEkran(
    name: String,
    onOpenAppClick: () -> Unit = {},
    onOpenRotationDemoClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Pozdrav, $name!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Ovo je ekran za vježbanje osnovnih Compose komponenata.",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(Spacing.medium))

            Text(text = "1. Navigacija na glavnu aplikaciju", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "Klikom na gumb ispod otvara se AppNavigation s listom knjiga.", fontSize = 12.sp)
            Button(
                onClick = onOpenAppClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text("POKRENI APLIKACIJU (AppNavigation) ➔", fontWeight = FontWeight.Bold)
            }


            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            // Zadaci 1-4 dan - Osnovni layoutovi
            Text(text = "2. Osnovni rasporedi (Column, Row, Box)", fontWeight = FontWeight.Bold, fontSize = 16.sp)

            Text(text = "• Column (elementi jedan pod drugi):", fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
            ExampleColumn()

            Text(text = "• Row (elementi jedan pored drugog):", fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
            ExampleRow()

            Text(text = "• Box (elementi jedan preko drugog):", fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
            ExampleBox()

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            // MODIFIER I REDOSLIJED
            Text(text = "3. Primjer utjecaja redoslijeda Modifier-a", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "Prvo padding pa background:", fontSize = 12.sp)
            Text(
                text = "Pozdrav",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
            )
            Text(text = "Prvo background pa padding:", fontSize = 12.sp)
            Text(
                text = "Pozdrav",
                modifier = Modifier
                    .background(Color.LightGray)
                    .padding(8.dp)
                    .fillMaxWidth()
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            // SLIKE I KARTICE
            Text(text = "4. Prikaz slika, profila i vlastitih kartica", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Profilna slika"
            )
            Text(
                text = "Junior Android Developer",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )

            Text(text = "• Vlastita komponenta PersonCard:", fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
            PersonCard("Ivan", 23)

            Text(text = "• Row sa SpaceBetween i obojanim Box-om:", fontSize = 12.sp)
            RowArrangment()

            Text(text = "• Box s centriranim tekstom:", fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
            BoxElement()

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            // Stanja
            Text(text = "5. Rad sa stanjem (State & remember)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "Primjer brojača s mogućnosti uvećavanja i resetiranja:", fontSize = 12.sp)
            Spacer(modifier = Modifier.height(Spacing.small))
            SimpleCounter()


            Spacer(modifier = Modifier.height(Spacing.small))

            Text(text = "6. Demo očuvanja stanja pri rotaciji (ViewModel vs remember):", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "Prikazuje kako se obično stanje gubi pri rotaciji mobitela, dok ViewModel čuva podatke.", fontSize = 11.sp, color = Color.Gray)

            Button(
                onClick = onOpenRotationDemoClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Zeleni gumb za razliku
            ) {
                Text("OTVORI ROTATION DEMO ➔", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(Spacing.large))
        }
    }
}

@Composable
fun ExampleColumn() {
    Column {
        Text(text = "Prvi red")
        Text(text = "Drugi red")
        Text(text = "Treći red")
    }
}

@Composable
fun ExampleRow() {
    Row {
        Text("Lijevo ")
        Text("Sredina ")
        Text("Desno")
    }
}

@Composable
fun ExampleBox() {
    Box {
        Text("Ispod")
        Text("Iznad")
    }
}

@Composable
fun PersonCard(name: String, age: Int) {
    Column(
        modifier = Modifier
            .padding(all = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray)
    ) {
        Text("Ime: $name")
        Text("Godine: $age")
    }
}

@Composable
fun RowArrangment() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(Color.Magenta)
        )
        Text(text = "Neki tekst na desnoj strani")
    }
}

@Composable
fun BoxElement() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.Red)
    ) {
        Text(text = "Preko", modifier = Modifier.align(Alignment.Center), color = Color.White)
    }
}

@Composable
fun SimpleCounter() {
    var count by remember { mutableStateOf(0) }
    Row {
        Button(onClick = { count++ }) {
            Text("Kliknuto $count puta")
        }
        Spacer(modifier = Modifier.size(Spacing.small))
        Button(onClick = { count = 0 }) {
            Text("Reset")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MojEkranPreview() {
    MojEkran("Ivan")
}