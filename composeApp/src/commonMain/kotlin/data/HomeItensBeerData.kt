package data

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
