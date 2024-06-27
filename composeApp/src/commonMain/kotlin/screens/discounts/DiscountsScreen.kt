package screens.discounts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.ButtonWithIcon
import components.ProgressButton
import components.profileToolbar
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.icon_flash_light
import halfmouthappproject.composeapp.generated.resources.icon_gallery
import halfmouthappproject.composeapp.generated.resources.icon_qr_code
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import qrscanner.QrScanner
import theme.darkScheme
import theme.mainYellowColor
import theme.onSurfaceVariantDark
import theme.primaryContainerDark
import theme.secondaryContainerDarkMediumContrast
import theme.surfaceVariantDark
import viewmodel.DiscountsViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun DiscountsScreen() {
    val viewModel = getViewModel(
        key = "discounts-screen",
        factory = viewModelFactory { DiscountsViewModel() }
    )
    val urlDiscounts by viewModel.urlDiscount.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    var qrCodeURL by remember { mutableStateOf("") }
    var startBarCodeScan by remember { mutableStateOf(false) }
    var flashlightOn by remember { mutableStateOf(false) }
    var launchGallery by remember { mutableStateOf(value = false) }
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            profileToolbar(
                title = "Descontos",
                onNavigationIconBack = {  },
                onNavigationIconClose = { if (startBarCodeScan) startBarCodeScan = false },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (qrCodeURL.isEmpty() && startBarCodeScan) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Row(
                            modifier = Modifier.padding(vertical = 5.dp, horizontal = 18.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Aponte a câmera para o QR Code para conseguir o seu desconto",
                                modifier = Modifier
                                    .background(Color.Transparent)
                                    .padding(horizontal = 12.dp, vertical = 12.dp),
                                style = MaterialTheme.typography.titleLarge,
                                fontSize = 18.sp,
                                textAlign = TextAlign.Center,
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(250.dp)
                                .clip(shape = RoundedCornerShape(size = 14.dp))
                                .clipToBounds()
                                .border(2.dp, Color.Gray, RoundedCornerShape(size = 14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            QrScanner(
                                modifier = Modifier
                                    .clipToBounds()
                                    .clip(shape = RoundedCornerShape(size = 14.dp)),
                                flashlightOn = flashlightOn,
                                launchGallery = launchGallery,
                                onCompletion = {
                                    qrCodeURL = it
                                    startBarCodeScan = false
                                },
                                onGalleryCallBackHandler = {
                                    launchGallery = it
                                },
                                onFailure = {
                                    coroutineScope.launch {
                                        if (it.isEmpty()) {
                                            snackBarHostState.showSnackbar("Invalid qr code")
                                        } else {
                                            snackBarHostState.showSnackbar(it)
                                        }
                                    }
                                }
                            )
                        }

                        Box(
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, top = 30.dp)
                                .background(
                                    color = if (flashlightOn) mainYellowColor else Color.White,
                                    shape = RoundedCornerShape(25.dp)
                                )
                                .height(35.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 5.dp, horizontal = 18.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(11.dp)
                            ) {
                                Icon(
                                    imageVector = vectorResource(Res.drawable.icon_flash_light),
                                    "flash",
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable { flashlightOn = !flashlightOn },
                                    tint = Color.Black
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, top = 30.dp)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(25.dp)
                                )
                                .height(35.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 5.dp, horizontal = 18.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(11.dp)
                            ) {
                                Image(
                                    painter = painterResource(Res.drawable.icon_gallery),
                                    contentDescription = "gallery",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable { launchGallery = true }
                                )
                            }
                        }
                    }
                } else {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Descubra o seu desconto",
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent)
                                .padding(start = 16.dp, end = 16.dp, top = 20.dp),
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Start
                        )

                        Box(
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                                .background(
                                    color = surfaceVariantDark,
                                    shape = RoundedCornerShape(25.dp)
                                )
                                .height(330.dp),
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(Res.drawable.icon_qr_code),
                                    contentDescription = "qr-code",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.size(150.dp).padding(top = 20.dp)
                                )

                                Text(
                                    text = "Leia o QR Code e aproveite os cupons de descontos das nossas cervejas",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 16.dp,
                                            end = 16.dp,
                                            top = 20.dp,
                                        ),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontSize = 15.sp
                                )

                                HorizontalDivider(
                                    modifier = Modifier
                                        .padding(
                                            start = 60.dp,
                                            end = 60.dp,
                                            top = 16.dp,
                                            bottom = 20.dp
                                        )
                                        .alpha(0.7f),
                                    thickness = 1.dp,
                                    color = onSurfaceVariantDark
                                )

                                ButtonWithIcon(
                                    text = "Ler QR Code",
                                    textSize = 14.sp,
                                    drawableResource = Res.drawable.icon_qr_code,
                                    onClick = {
                                        startBarCodeScan = true
                                        qrCodeURL = ""
                                    }
                                )

//                                Row(
//                                    modifier = Modifier.padding(
//                                        vertical = 5.dp,
//                                        horizontal = 18.dp
//                                    ),
//                                    verticalAlignment = Alignment.CenterVertically,
//                                    horizontalArrangement = Arrangement.spacedBy(11.dp)
//                                ) {
////                                    ProgressButton(
//                                        text = " Ler QR Code ",
//                                        isLoading = false,
//                                        textSize = 14.sp,
//                                        onClick = {
//                                            startBarCodeScan = true
//                                            qrCodeURL = ""
//                                        }
//                                    )
//
//                                    ProgressButton(
//                                        text = "Abrir QR Code",
//                                        isLoading = false,
//                                        textSize = 14.sp,
//                                        onClick = {
//                                            startBarCodeScan = true
//                                            qrCodeURL = ""
//                                        }
//                                    )
//
//                                    ButtonWithIcon(
//                                        text = "Câmera",
//                                        textSize = 14.sp,
//                                        drawableResource = Res.drawable.icon_qr_code,
//                                        onClick = {
//                                            startBarCodeScan = true
//                                            qrCodeURL = ""
//                                        }
//                                    )
//
//                                    ButtonWithIcon(
//                                        text = "Galeria",
//                                        textSize = 14.sp,
//                                        drawableResource = Res.drawable.icon_gallery,
//                                        onClick = {
//                                            launchGallery = true
//                                        }
//                                    )
//
//                                    val shape = RoundedCornerShape(20.dp)
//
//                                    Button(
//                                        modifier = Modifier
//                                            .height(54.dp)
//                                            .clip(shape)
//                                            .background(
//                                                brush = Brush.linearGradient(
//                                                    0f to mainYellowColor,
//                                                    1f to mainYellowColor
//                                                )
//                                            ),
//                                        shape = shape,
//                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
//                                        onClick = {
//                                            startBarCodeScan = true
//                                            qrCodeURL = ""
//                                        },
//                                    ) {
//                                        Image(
//                                            painter = painterResource(Res.drawable.icon_qr_code),
//                                            contentDescription = "qr-code",
//                                            contentScale = ContentScale.Fit,
//                                            modifier = Modifier.size(24.dp)
//                                        )
//
//                                        Text(
//                                            modifier = Modifier.wrapContentHeight(),
//                                            text = "Camera",
//                                            color = darkScheme.onPrimary,
//                                            style = TextStyle(
//                                                fontFamily = FontFamily.SansSerif,
//                                                fontWeight = FontWeight.Bold,
//                                                fontSize = 14.sp,
//                                                lineHeight = 16.sp,
//                                                letterSpacing = 0.5.sp
//                                            )
//                                        )
//                                    }
//
//                                    Button(
//                                        modifier = Modifier
//                                            .height(54.dp)
//                                            .clip(shape)
//                                            .background(
//                                                brush = Brush.linearGradient(
//                                                    0f to mainYellowColor,
//                                                    1f to mainYellowColor
//                                                )
//                                            ),
//                                        shape = shape,
//                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
//                                        onClick = {
//                                            qrCodeURL = ""
//                                            launchGallery = true
//                                                  },
//                                    ) {
//                                        Image(
//                                            painter = painterResource(Res.drawable.icon_gallery),
//                                            contentDescription = "qr-code",
//                                            contentScale = ContentScale.Fit,
//                                            modifier = Modifier.size(24.dp)
//                                        )
//
//                                        Text(
//                                            modifier = Modifier.wrapContentHeight(),
//                                            text = "Galeria",
//                                            color = darkScheme.onPrimary,
//                                            style = TextStyle(
//                                                fontFamily = FontFamily.SansSerif,
//                                                fontWeight = FontWeight.Bold,
//                                                fontSize = 14.sp,
//                                                lineHeight = 16.sp,
//                                                letterSpacing = 0.5.sp
//                                            )
//                                        )
//                                    }
//                                }
                           }

                        }

                        Text(
                            text = qrCodeURL,
                            color = Color.White,
                            modifier = Modifier.padding(top = 12.dp)
                        )

                        if (qrCodeURL == urlDiscounts) {
                            Text(
                                text = "Parabéns pelo Desconto!",
                                color = Color.White,
                                modifier = Modifier.padding(top = 12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
