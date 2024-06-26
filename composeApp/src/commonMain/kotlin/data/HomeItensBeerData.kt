package data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.agua
import halfmouthappproject.composeapp.generated.resources.beer_craft_glass
import halfmouthappproject.composeapp.generated.resources.beer_growler
import halfmouthappproject.composeapp.generated.resources.beer_mug_ipa
import halfmouthappproject.composeapp.generated.resources.brewingbeer
import halfmouthappproject.composeapp.generated.resources.leveduras
import halfmouthappproject.composeapp.generated.resources.lupulo
import halfmouthappproject.composeapp.generated.resources.malte
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi


data class Ingredients @OptIn(ExperimentalResourceApi::class) constructor(
    val name: String,
    val description: String,
    val image: DrawableResource
)

data class BeerType @OptIn(ExperimentalResourceApi::class) constructor(
    val title: String,
    val subtitle: String,
    val info: String,
    val imageId: DrawableResource,
)

data class NavigationMenuDrawerItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null
)

@OptIn(ExperimentalResourceApi::class)
val listOfIngredients = listOf(
    Ingredients("Lúpulo", "", Res.drawable.lupulo),
    Ingredients("Malte", "", Res.drawable.malte),
    Ingredients("Leveduras", "", Res.drawable.leveduras),
    Ingredients("Agua", "", Res.drawable.agua),
)

@OptIn(ExperimentalResourceApi::class)
val beerTypeList = listOf(
    BeerType(
        "HalfMouth\nIpa",
        "Nossa Ipa oferece uma intensidade de sabores dos maltes e lupulos.",
        "IBU = 9 | ABV = 4,5% | Temp. Ideal = ±3°C",
        Res.drawable.brewingbeer
    ),
    BeerType(
        "HalfMouth\nSession Ipa",
        "Nossa Session Ipa oferece uma intensidade de sabores dos maltes e lupulos.",
        "IBU = 8 | ABV = 4,5% | Temp. Ideal = ±6°C",
        Res.drawable.beer_mug_ipa
    ),
    BeerType(
        "HalfMouth\nPilsen",
        "Nossa Pilsen oferece uma intensidade de sabores dos maltes e lupulos.",
        "IBU = 7 | ABV = 3,5% | Temp. Ideal = ±5°C",
        Res.drawable.beer_craft_glass
    ),
    BeerType(
        "HalfMouth\nIpa",
        "Nossa Ipa oferece uma intensidade de sabores dos maltes e lupulos.",
        "IBU = 9 | ABV = 4,5% | Temp. Ideal = ±3°C",
        Res.drawable.beer_growler
    ),
    BeerType(
        "HalfMouth\nSession Ipa",
        "Nossa Session Ipa oferece uma intensidade de sabores dos maltes e lupulos.",
        "IBU = 8 | ABV = 4,5% | Temp. Ideal = ±6°C",
        Res.drawable.beer_growler
    ),
    BeerType(
        "HalfMouth\nPilsen",
        "Nossa Pilsen oferece uma intensidade de sabores dos maltes e lupulos.",
        "IBU = 7 | ABV = 3,5% | Temp. Ideal = ±5°C",
        Res.drawable.beer_growler
    ),
)

val items = listOf(
    NavigationMenuDrawerItem(
        title = "Menu",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    NavigationMenuDrawerItem(
        title = "Informação",
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info,
        badgeCount = 45
    ),
    NavigationMenuDrawerItem(
        title = "Configurações",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
    )
)