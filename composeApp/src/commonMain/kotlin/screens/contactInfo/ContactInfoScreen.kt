package screens.contactInfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import components.AppToolbarLarge
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.agua
import halfmouthappproject.composeapp.generated.resources.leveduras
import halfmouthappproject.composeapp.generated.resources.lupulo
import halfmouthappproject.composeapp.generated.resources.malte
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactInfoScreen() {

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            AppToolbarLarge(
                title = "Nossas Cervejas",
                onNavigationLeftIconClick = { },
                onNavigationProfileIconClick = { },
                onNavigationSettingsIconClick = { },
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = innerPadding,
        ) {

            item { SubItemTitle() }
            item { SubListIngredients() }
        }
    }
}

@Composable
fun SubItemTitle() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "Nossos Ingredientes:",
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SubListIngredients() {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        val mock = listOf(
            Ingredients("Lúpulo", "", Res.drawable.lupulo),
            Ingredients("Malte", "", Res.drawable.malte),
            Ingredients("Leveduras", "", Res.drawable.leveduras),
            Ingredients("Agua", "", Res.drawable.agua),
        )
        items(mock.size) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                val (text, image) = createRefs()
                Image(
                    painter = painterResource(mock[it].image),
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
                    text = mock[it].name,
                )
            }
        }
    }
}

data class Ingredients @OptIn(ExperimentalResourceApi::class) constructor(
    val name: String,
    val description: String,
    val image: DrawableResource
)