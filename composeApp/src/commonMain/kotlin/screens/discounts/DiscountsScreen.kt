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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.ButtonWithIcon
import components.DrawerMenuNavigation
import components.SimpleToolbar
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.icon_bolt_fill_off
import halfmouthappproject.composeapp.generated.resources.icon_bolt_fill_on
import halfmouthappproject.composeapp.generated.resources.icon_gallery_send
import halfmouthappproject.composeapp.generated.resources.icon_qr_code
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import qrscanner.QrScanner
import theme.darkScheme
import theme.mainYellowColor
import theme.onSurfaceVariantDark
import theme.surfaceVariantDark
import util.snackBarOnlyMessage
import viewmodel.DiscountsViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun DiscountsScreen(
    onNavigateToDrawerMenu: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val viewModel = getViewModel(
        key = "discounts-screen",
        factory = viewModelFactory { DiscountsViewModel() }
    )
    val urlDiscounts by viewModel.urlDiscount.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val snackBarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var qrCodeURL by remember { mutableStateOf("") }
    var startBarCodeScan by remember { mutableStateOf(false) }
    var flashlightOn by remember { mutableStateOf(false) }
    var launchGallery by remember { mutableStateOf(value = false) }


    ModalNavigationDrawer(
        drawerContent = {
            DrawerMenuNavigation(
                scope = scope,
                drawerState = drawerState,
                onNavigateToDrawerMenu = onNavigateToDrawerMenu,
            )
        },
        drawerState = drawerState
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
            topBar = {
                SimpleToolbar(
                    title = "Descontos",
                    onNavigationToMenu = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onNavigationClose = {
                        if (startBarCodeScan) startBarCodeScan = false
                        else onNavigateBack()
                    },
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
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 1.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = "Aponte a câmera para o QR Code para conseguir o seu desconto",
                                    modifier = Modifier.fillMaxWidth(),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Start,
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
                                    onFailure = { text ->
                                        if (text.isEmpty()) {
                                            snackBarOnlyMessage(
                                                snackBarHostState = snackBarHostState,
                                                coroutineScope = scope,
                                                message = "Qr Code Inválido",
                                                duration = SnackbarDuration.Long
                                            )
                                        } else {
                                            snackBarOnlyMessage(
                                                snackBarHostState = snackBarHostState,
                                                coroutineScope = scope,
                                                message = text,
                                                duration = SnackbarDuration.Long
                                            )
                                        }
                                    }
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                                    .background(
                                        color = mainYellowColor,
                                        shape = RoundedCornerShape(25.dp)
                                    )
                                    .height(35.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    modifier = Modifier.padding(
                                        vertical = 2.dp,
                                        horizontal = 16.dp
                                    ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clickable { flashlightOn = !flashlightOn },
                                        imageVector = if (flashlightOn) vectorResource(Res.drawable.icon_bolt_fill_on)
                                        else vectorResource(Res.drawable.icon_bolt_fill_off),
                                        contentDescription = "flash",
                                        tint = Color.Black
                                    )

                                    Text(
                                        modifier = Modifier.clickable {
                                            flashlightOn = !flashlightOn
                                        },
                                        text = "Flash  ",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontSize = 15.sp,
                                        color = darkScheme.onPrimary,
                                    )

                                    VerticalDivider(
                                        modifier = Modifier.padding(2.dp),
                                        thickness = 1.dp,
                                        color = onSurfaceVariantDark
                                    )

                                    Icon(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clickable { launchGallery = true },
                                        painter = painterResource(Res.drawable.icon_gallery_send),
                                        contentDescription = "gallery",
                                        tint = Color.Black
                                    )

                                    Text(
                                        modifier = Modifier.clickable { launchGallery = true },
                                        text = "Galeria",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontSize = 15.sp,
                                        color = darkScheme.onPrimary,
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
                                text = "Acesse seu cupom descubra o seu desconto",
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
                                    .padding(start = 30.dp, end = 30.dp, top = 10.dp)
                                    .background(
                                        color = surfaceVariantDark,
                                        shape = RoundedCornerShape(25.dp)
                                    )
                                    .height(360.dp),
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
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 16.dp,
                                                end = 16.dp,
                                                top = 20.dp,
                                            ),
                                        text = "Leia o QR Code e aproveite\nos cupons de descontos\ndas nossas cervejas",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontSize = 15.sp
                                    )

                                    HorizontalDivider(
                                        modifier = Modifier
                                            .padding(
                                                start = 80.dp,
                                                end = 80.dp,
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
}