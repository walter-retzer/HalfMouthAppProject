package screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import components.AppToolbarLarge
import data.BeerType
import data.Ingredients
import data.beerTypeList
import data.listOfIngredients
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.splashscreenlogo
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import theme.onBackgroundDark
import theme.onSecondaryContainerDark
import theme.secondaryContainerDark


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun HomeScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToProfile: () -> Unit,
) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            AppToolbarLarge(
                title = "Menu",
                onNavigationLeftIconClick = { },
                onNavigationProfileIconClick = { onNavigateToProfile() },
                onNavigationSettingsIconClick = { onNavigateToSettings() },
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = innerPadding,
        ) {
            item { SubItemTitle("Nossos Ingredientes:", 8.dp) }
            item { SubListIngredients(listOfIngredients) }
            item { SubItemTitle("Nossas Cervejas:") }
            items(beerTypeList) { list -> BeerCard(list) }
        }
    }
}


@OptIn(ExperimentalResourceApi::class)
@Composable
fun BeerCard(itemList: BeerType) {
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
            ) {}
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
                .constrainAs(logo) {
                    top.linkTo(parent.top, margin = 20.dp)
                    start.linkTo(parent.start, margin = 40.dp)
                }
        )
        val shape = RoundedCornerShape(10.dp)
        Image(
            painter = painterResource(itemList.imageId),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(160.dp)
                .height(150.dp)
                .border(1.dp, onBackgroundDark, shape)
                .clip(RoundedCornerShape(10.dp))
                .constrainAs(imageBeer) {
                    top.linkTo(parent.top, margin = 20.dp)
                    end.linkTo(parent.end, margin = 40.dp)
                }
        )
        Text(
            text = itemList.title,
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
            text = itemList.subtitle,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 15.sp,
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .padding(start = 26.dp, end = 26.dp, top = 16.dp)
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
            text = itemList.info,
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

@Composable
fun SubItemTitle(title: String, paddingBottom: Dp = 0.dp) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, bottom = paddingBottom),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 18.sp,
            text = title
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SubListIngredients(ingredients: List<Ingredients>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(ingredients.size) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                val (text, image) = createRefs()
                Image(
                    painter = painterResource(ingredients[it].image),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(16))
                        .constrainAs(image) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
                Text(
                    modifier = Modifier
                        .constrainAs(text) {
                            top.linkTo(image.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    text = ingredients[it].name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 16.sp,
                )
            }
        }
    }
}
