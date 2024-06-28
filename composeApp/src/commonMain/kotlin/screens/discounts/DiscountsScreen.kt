package screens.discounts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import components.ButtonWithIcon
import components.DrawerMenuNavigation
import components.SimpleToolbar
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.beer_on_right
import halfmouthappproject.composeapp.generated.resources.icon_bolt_fill_off
import halfmouthappproject.composeapp.generated.resources.icon_bolt_fill_on
import halfmouthappproject.composeapp.generated.resources.icon_gallery_send
import halfmouthappproject.composeapp.generated.resources.icon_qr_code
import halfmouthappproject.composeapp.generated.resources.qr_code_big
import halfmouthappproject.composeapp.generated.resources.ticket_qr_code
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import qrscanner.QrScanner
import theme.mainYellowColor
import theme.onSurfaceVariantDark
import theme.surfaceBrightDark
import theme.surfaceVariantDark
import util.snackBarWithActionButton
import viewmodel.DiscountsViewModel
import viewmodel.DiscountsViewState


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
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val snackBarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var qrCodeURL by remember { mutableStateOf("") }
    var startBarCodeScan by remember { mutableStateOf(false) }
    var flashlightOn by remember { mutableStateOf(false) }
    var launchGallery by remember { mutableStateOf( false) }
    var isSnackBarDisplaying by remember { mutableStateOf( false) }


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
            when (val state = uiState) {
                is DiscountsViewState.Dashboard -> {
                    isSnackBarDisplaying = false

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Column(
                            modifier = Modifier
                                .windowInsetsPadding(WindowInsets.safeDrawing)
                                .verticalScroll(rememberScrollState())
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (qrCodeURL.isEmpty() && startBarCodeScan) {
                                ConstraintLayout {
                                    val (card, textTitle, qrCode) = createRefs()
                                    Box(
                                        modifier = Modifier
                                            .padding(
                                                start = 16.dp,
                                                end = 16.dp,
                                                top = 10.dp,
                                                bottom = 16.dp
                                            )
                                            .background(
                                                color = surfaceVariantDark,
                                                shape = RoundedCornerShape(25.dp)
                                            )
                                            .fillMaxSize()
                                            .constrainAs(card) {
                                                top.linkTo(parent.top, margin = 10.dp)
                                            }
                                    ) {
                                        Column(
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {

                                            Image(
                                                painter = painterResource(Res.drawable.ticket_qr_code),
                                                contentDescription = "qr-code",
                                                contentScale = ContentScale.Fit,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(
                                                        top = 30.dp,
                                                        start = 10.dp,
                                                        end = 10.dp
                                                    )
                                            )

                                            Box(
                                                modifier = Modifier
                                                    .padding(top = 16.dp, bottom = 16.dp)
                                                    .background(
                                                        color = surfaceVariantDark,
                                                        shape = RoundedCornerShape(25.dp)
                                                    )
                                                    .height(42.dp),
                                                contentAlignment = Alignment.Center
                                            ) {

                                                Row(
                                                    modifier = Modifier
                                                        .border(
                                                            2.dp,
                                                            onSurfaceVariantDark,
                                                            RoundedCornerShape(size = 25.dp)
                                                        ),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                                ) {

                                                    Spacer(modifier = Modifier.width(10.dp))

                                                    Icon(
                                                        modifier = Modifier
                                                            .size(24.dp)
                                                            .clickable {
                                                                flashlightOn = !flashlightOn
                                                            },
                                                        imageVector = if (flashlightOn) vectorResource(
                                                            Res.drawable.icon_bolt_fill_on
                                                        )
                                                        else vectorResource(Res.drawable.icon_bolt_fill_off),
                                                        contentDescription = "flash",
                                                        tint = mainYellowColor
                                                    )

                                                    Text(
                                                        modifier = Modifier.clickable {
                                                            flashlightOn = !flashlightOn
                                                        },
                                                        text = "Flash  ",
                                                        textAlign = TextAlign.Center,
                                                        style = MaterialTheme.typography.titleLarge,
                                                        fontSize = 15.sp,
                                                    )

                                                    VerticalDivider(
                                                        modifier = Modifier.padding(2.dp),
                                                        thickness = 2.dp,
                                                        color = onSurfaceVariantDark
                                                    )

                                                    Icon(
                                                        modifier = Modifier
                                                            .size(24.dp)
                                                            .clickable { launchGallery = true },
                                                        painter = painterResource(Res.drawable.icon_gallery_send),
                                                        contentDescription = "gallery",
                                                        tint = mainYellowColor
                                                    )

                                                    Text(
                                                        modifier = Modifier
                                                            .clickable { launchGallery = true },
                                                        text = "Galeria",
                                                        textAlign = TextAlign.Center,
                                                        style = MaterialTheme.typography.titleLarge,
                                                        fontSize = 15.sp,
                                                    )

                                                    Spacer(modifier = Modifier.width(10.dp))
                                                }
                                            }
                                        }
                                    }

                                    Text(
                                        text = "Aponte a câmera para o QR Code para conseguir o seu desconto",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            lineBreak = LineBreak.Paragraph
                                        ),
                                        fontSize = 17.sp,
                                        color = surfaceBrightDark,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 60.dp,
                                                end = 60.dp,
                                            )
                                            .constrainAs(textTitle) {
                                                top.linkTo(card.top, margin = 60.dp)
                                                start.linkTo(card.start, margin = 0.dp)
                                                end.linkTo(card.end, margin = 0.dp)
                                            },
                                    )

                                    Box(
                                        modifier = Modifier
                                            .size(240.dp)
                                            .clip(shape = RoundedCornerShape(size = 14.dp))
                                            .clipToBounds()
                                            .border(
                                                2.dp,
                                                onSurfaceVariantDark,
                                                RoundedCornerShape(size = 14.dp)
                                            )
                                            .constrainAs(qrCode) {
                                                bottom.linkTo(card.bottom, margin = 100.dp)
                                                start.linkTo(card.start, margin = 0.dp)
                                                end.linkTo(card.end, margin = 0.dp)
                                            },
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
                                                viewModel.getSuccess(it)
                                            },
                                            onGalleryCallBackHandler = {
                                                launchGallery = it
                                            },
                                            onFailure = { text ->
                                                viewModel.getError(text)
                                            }
                                        )
                                    }
                                }
                            } else {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    ConstraintLayout {
                                        val (card, textTitle, qrCode, dividerLine) = createRefs()
                                        Box(
                                            modifier = Modifier
                                                .padding(
                                                    start = 16.dp,
                                                    end = 16.dp,
                                                    top = 10.dp,
                                                    bottom = 16.dp
                                                )
                                                .background(
                                                    color = surfaceVariantDark,
                                                    shape = RoundedCornerShape(25.dp)
                                                )
                                                .fillMaxSize()
                                                .constrainAs(card) {
                                                    top.linkTo(parent.top, margin = 10.dp)
                                                }
                                        ) {

                                            Column(
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Image(
                                                    painter = painterResource(Res.drawable.ticket_qr_code),
                                                    contentDescription = "qr-code",
                                                    contentScale = ContentScale.Fit,
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .padding(
                                                            top = 30.dp,
                                                            start = 10.dp,
                                                            end = 10.dp
                                                        )
                                                )

                                                ButtonWithIcon(
                                                    modifier = Modifier
                                                        .padding(top = 16.dp, bottom = 16.dp),
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
                                            text = "Leia o QR Code e aproveite os cupons de descontos das nossas cervejas artesanais",
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                lineBreak = LineBreak.Paragraph
                                            ),
                                            fontSize = 17.sp,
                                            color = surfaceBrightDark,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    start = 60.dp,
                                                    end = 60.dp,
                                                )
                                                .constrainAs(textTitle) {
                                                    top.linkTo(card.top, margin = 60.dp)
                                                    start.linkTo(card.start, margin = 0.dp)
                                                    end.linkTo(card.end, margin = 0.dp)
                                                },
                                        )

                                        Image(
                                            painter = painterResource(Res.drawable.qr_code_big),
                                            contentDescription = "qr-code",
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier
                                                .size(240.dp)
                                                .constrainAs(qrCode) {
                                                    bottom.linkTo(card.bottom, margin = 130.dp)
                                                    start.linkTo(card.start)
                                                    end.linkTo(card.end)
                                                }
                                        )

                                        HorizontalDivider(
                                            modifier = Modifier
                                                .width(100.dp)
                                                .alpha(0.7f)
                                                .constrainAs(dividerLine){
                                                    bottom.linkTo(card.bottom, margin = 132.dp)
                                                    start.linkTo(card.start, margin = 0.dp)
                                                    end.linkTo(card.end, margin = 0.dp)
                                                },
                                            thickness = 4.dp,
                                            color = surfaceVariantDark
                                        )
                                    }
                               }
                            }
                        }
                    }
                }

                is DiscountsViewState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Column(
                            modifier = Modifier
                                .windowInsetsPadding(WindowInsets.safeDrawing)
                                .verticalScroll(rememberScrollState())
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .background(
                                        color = surfaceVariantDark,
                                        shape = RoundedCornerShape(25.dp)
                                    )
                                    .width(240.dp)
                                    .height(360.dp)
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(Res.drawable.icon_qr_code),
                                        contentDescription = "qr-code",
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .size(150.dp)
                                            .padding(top = 20.dp)
                                    )

                                    HorizontalDivider(
                                        modifier = Modifier
                                            .padding(
                                                start = 80.dp,
                                                end = 80.dp,
                                                top = 20.dp,
                                            )
                                            .alpha(0.7f),
                                        thickness = 1.dp,
                                        color = onSurfaceVariantDark
                                    )

                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 16.dp,
                                                end = 16.dp,
                                                top = 20.dp,
                                            ),
                                        text = state.message,
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontSize = 24.sp
                                    )
                                }
                            }
                        }
                    }
                    if(!isSnackBarDisplaying){
                        snackBarWithActionButton(
                            coroutineScope = scope,
                            snackBarHostState = snackBarHostState,
                            message = "Deseja tentar novamente?",
                            actionLabel = "Sim",
                            onAction = { viewModel.getRefreshScan() }
                        )
                        isSnackBarDisplaying = true
                    }
                }

                is DiscountsViewState.Success -> {
                    isSnackBarDisplaying = false
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Column(
                            modifier = Modifier
                                .windowInsetsPadding(WindowInsets.safeDrawing)
                                .verticalScroll(rememberScrollState())
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(start = 30.dp, end = 30.dp, top = 10.dp)
                                    .background(
                                        color = surfaceVariantDark,
                                        shape = RoundedCornerShape(25.dp)
                                    )
                                    .height(350.dp),
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.beer_on_right),
                                        contentDescription = "qr-code",
                                        modifier = Modifier
                                            .size(150.dp)
                                            .padding(top = 10.dp),
                                        tint = Color(0xFFCAC8A4)
                                    )

                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 16.dp,
                                                end = 16.dp,
                                                top = 16.dp,
                                            ),
                                        text = "Desconto de 10%",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontSize = 24.sp
                                    )

                                    HorizontalDivider(
                                        modifier = Modifier
                                            .padding(
                                                start = 70.dp,
                                                end = 70.dp,
                                                top = 24.dp,
                                                bottom = 24.dp
                                            )
                                            .alpha(0.7f),
                                        thickness = 1.dp,
                                        color = onSurfaceVariantDark
                                    )

                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                start = 16.dp,
                                                end = 16.dp,
                                            ),
                                        text = "Parabéns,\no seu cupom de desconto\né valido até o dia 31/12/2024",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
