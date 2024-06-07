package screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.compose.rememberNavController
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import components.AppToolbarLarge
import components.BottomNavigationBar
import data.UserPreferences
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.ipa_beer
import halfmouthappproject.composeapp.generated.resources.splashscreenlogo
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import theme.onSecondaryContainerDark
import theme.secondaryContainerDark
import util.snackBarOnlyMessage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun HomeScreen() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val settingsPref = Settings()
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost =  { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            AppToolbarLarge(
                title = "Nossas Cervejas",
                onNavigationLeftIconClick = {
                    scope.launch {
                        val test: String? = settingsPref[UserPreferences.UID]
                        println(test)
                        snackBarOnlyMessage(
                            snackBarHostState = snackBarHostState,
                            coroutineScope = scope,
                            message = test.toString()
                        )
                    }

                },
                onNavigationProfileIconClick = { },
                onNavigationSettingsIconClick = { },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomAppBar(
                contentColor = Color.Black,
                containerColor = Color.Black
            )
            {
                BottomNavigationBar(navController = rememberNavController())
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 50.dp),
            contentPadding = innerPadding,
        ) {
            items(itemList) { BeerCard() }
        }
    }
}


@OptIn(ExperimentalResourceApi::class)
@Composable
fun BeerCard() {
    ConstraintLayout {
        val (card, logo, imageBeer, text1, text2, icon1, text3) = createRefs()
        Row(modifier = Modifier.constrainAs(card) {
            top.linkTo(parent.top, margin = 40.dp)
        }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(205.dp)
                    .shadow(16.dp, shape = RectangleShape)
            ) {

            }
        }
        Image(
            painter = painterResource(Res.drawable.splashscreenlogo),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .border(4.dp, secondaryContainerDark, CircleShape)
                .background(Color.White, CircleShape)
                .padding(16.dp)
                .constrainAs(logo){
                    top.linkTo(parent.top, margin = 20.dp)
                    start.linkTo(parent.start, margin = 40.dp)
                }
        )
        Image(
            painter = painterResource(Res.drawable.ipa_beer),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(160.dp)
                .height(150.dp)
                .clip(RoundedCornerShape(8.dp))
                .constrainAs(imageBeer) {
                    top.linkTo(parent.top, margin = 20.dp)
                    end.linkTo(parent.end, margin = 40.dp)
                }
        )
        Text(
            text = "HalfMouth\nIpa",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 22.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(text1) {
                    top.linkTo(logo.bottom, margin = 10.dp)
                    start.linkTo(logo.start)
                    end.linkTo(logo.end)
                }
        )
        Text(
            text = "Nossa Ipa oferece uma intensidade de sabores dos maltes e lupulos.",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 15.sp,
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .padding(start = 26.dp, end =26.dp, top= 16.dp)
                .constrainAs(text2) {
                    top.linkTo(text1.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
        IconButton(
            modifier = Modifier
                .padding(start = 16.dp)
                .constrainAs(icon1) {
                    top.linkTo(text2.bottom)
                    start.linkTo(parent.start)
                },
            onClick = { }
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Localized description",
                tint = onSecondaryContainerDark
            )
        }
        Text(
            text = "IBU = 9 | ABV = 4,5% | Temp. Ideal = ±3°C",
            style = MaterialTheme.typography.bodySmall,
            color = onSecondaryContainerDark,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(text3) {
                    top.linkTo(icon1.top)
                    bottom.linkTo(icon1.bottom)
                    start.linkTo(icon1.end)
                }
        )
    }
}


data class ItemBeerMainMenu @OptIn(ExperimentalResourceApi::class) constructor(
    val id: Int,
    val title: String,
    val subtitle: String,
    val imageId: DrawableResource,
)

@OptIn(ExperimentalResourceApi::class)
val itemList = listOf(
    ItemBeerMainMenu(
        1,
        "Fresh Vegges and Greens",
        "Very awesome list item has very awesome subtitle. This is bit long",
        Res.drawable.splashscreenlogo
    ),
    ItemBeerMainMenu(
        2,
        "Best blue berries",
        "Very awesome list item has very awesome subtitle. This is bit long",
        Res.drawable.splashscreenlogo
    ),
    ItemBeerMainMenu(
        3,
        "Cherries La Bloom",
        "Very awesome list item has very awesome subtitle. This is bit long",
        Res.drawable.splashscreenlogo
    ),
    ItemBeerMainMenu(
        4,
        "Fruits everyday",
        "Very awesome list item has very awesome subtitle. This is bit long",
        Res.drawable.splashscreenlogo
    ),
    ItemBeerMainMenu(
        5,
        "Sweet and sour",
        "Very awesome list item has very awesome subtitle. This is bit long",
        Res.drawable.splashscreenlogo
    ),
    ItemBeerMainMenu(
        6,
        "Pancakes for everyone",
        "Very awesome list item has very awesome subtitle. This is bit long",
        Res.drawable.splashscreenlogo
    ),
    ItemBeerMainMenu(
        5,
        "Sweet and sour",
        "Very awesome list item has very awesome subtitle. This is bit long",
        Res.drawable.splashscreenlogo
    ),
    ItemBeerMainMenu(
        6,
        "Pancakes for everyone",
        "Very awesome list item has very awesome subtitle. This is bit long",
        Res.drawable.splashscreenlogo
    ),
    ItemBeerMainMenu(
        5,
        "Sweet and sour",
        "Very awesome list item has very awesome subtitle. This is bit long",
        Res.drawable.splashscreenlogo
    ),
    ItemBeerMainMenu(
        6,
        "Pancakes for everyone",
        "Very awesome list item has very awesome subtitle. This is bit long",
        Res.drawable.splashscreenlogo
    ),
    ItemBeerMainMenu(
        5,
        "Sweet and sour",
        "Very awesome list item has very awesome subtitle. This is bit long",
        Res.drawable.splashscreenlogo
    ),
    ItemBeerMainMenu(
        6,
        "Pancakes for everyone",
        "Very awesome list item has very awesome subtitle. This is bit long",
        Res.drawable.splashscreenlogo
    )
)