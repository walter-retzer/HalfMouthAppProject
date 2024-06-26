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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.profileToolbar
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.icon_flash_light
import halfmouthappproject.composeapp.generated.resources.icon_gallery
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import qrscanner.QrScanner
import theme.mainYellowColor
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
                        Button(
                            onClick = {
                                startBarCodeScan = true
                                qrCodeURL = ""
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = mainYellowColor),
                        ) {
                            Text(
                                text = "Scanear QR Code",
                                modifier = Modifier
                                    .background(Color.Transparent)
                                    .padding(horizontal = 12.dp, vertical = 12.dp),
                                fontSize = 16.sp,
                                color = Color.Black,
                            )
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
