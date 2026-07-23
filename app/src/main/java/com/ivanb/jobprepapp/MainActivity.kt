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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MojEkran("Ivan")
        }
    }
}

@Composable
fun MojEkran(name: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Text(text = "Hello $name!")

        ExampleColumn()
        ExampleRow()
        ExampleBox()


        Text(
            text = "Pozdrav",
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .background(Color.LightGray)
        )
        Text(
            text = "Pozdrav",
            modifier = Modifier
                .background(Color.LightGray)
                .padding(16.dp)
                .fillMaxWidth()
        )
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Profilna slika"
        )
        Text(
            text = "Junior Android Developer",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
        Button(
            onClick = { /* Klik logika */ },
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text("Klikni me")
        }

        // AKO IMAS BookCard I sampleBooks OTKOMENTIRAJ OVO:
        // BookCard(book = sampleBooks[0])

        Text(
            text = "Zadaci za 3. dan!!!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
        PersonCard("Ivan", 23)
        RowArrangment()
        BoxElement()
        SimpleCounter()
        Counter()
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
        Text("Lijevo")
        Text("Sredina")
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
        Text(text = "Neki tekst")
    }
}

@Composable
fun BoxElement() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.Red)
    ) {
        Text(text = "Preko", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun Counter() {
    // Korištenje mutableIntStateOf + 'by' delegacije
    var count by remember { mutableStateOf(0) }

    Button(onClick = { count++ }) {
        Text("Kliknuto $count puta")
    }
}

@Composable
fun SimpleCounter(){
    var count by remember {mutableStateOf(0)  }
    Row{
        Button(onClick = {count++ }){
            Text("Kliknuto $count puta")

        }
        Button(onClick = {count=0}){
            Text("Reset")
        }
    }


}

@Preview(showBackground = true)
@Composable
fun MojEkranPreview() {
    MojEkran("Ivan")
}