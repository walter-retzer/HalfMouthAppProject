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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import components.ButtonWithIcon
import components.DrawerMenuNavigation
import components.MyAppCircularProgressIndicator
import components.SimpleToolbar
import database.TicketDao
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.icon_bolt_fill_off
import halfmouthappproject.composeapp.generated.resources.icon_bolt_fill_on
import halfmouthappproject.composeapp.generated.resources.icon_gallery_send
import halfmouthappproject.composeapp.generated.resources.icon_qr_code
import halfmouthappproject.composeapp.generated.resources.icon_thumbs_down
import halfmouthappproject.composeapp.generated.resources.logohalfmouth
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
import util.ConstantsApp.Companion.TICKET_SUCCESS_INSERT
import util.snackBarOnlyMessage
import util.snackBarWithActionButton
import viewmodel.DiscountsViewModel
import viewmodel.DiscountsViewState


@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun DiscountsScreen(
    ticketDao: TicketDao,
    onNavigateToDrawerMenu: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val viewModel = getViewModel(
        key = "discounts-screen",
        factory = viewModelFactory { DiscountsViewModel(ticketDao) }
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
    var isSnackBarSuccessDisplaying by remember { mutableStateOf( false) }


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
                    }
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
                                                    .height(45.dp),
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

                                    val topTitleGuideLine = createGuidelineFromTop(0.14f)
                                    val startTitleGuideLine = createGuidelineFromStart(0.15f)
                                    val endTitleGuideLine = createGuidelineFromEnd(0.15f)

                                    Text(
                                        text = "Aponte a câmera no QR Code ou abra seu cupom e garanta o seu desconto",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            lineBreak = LineBreak.Paragraph
                                        ),
                                        fontSize = 18.sp,
                                        color = surfaceBrightDark,
                                        modifier = Modifier
                                            .constrainAs(textTitle) {
                                                top.linkTo(topTitleGuideLine)
                                                start.linkTo(startTitleGuideLine)
                                                end.linkTo(endTitleGuideLine)
                                                width = Dimension.fillToConstraints
                                            }
                                    )

                                    val topScanQrCodeGuideLine = createGuidelineFromTop(0.40f)
                                    val startScanQrCodeGuideLine = createGuidelineFromStart(0.17f)
                                    val endScanQrCodeGuideLine = createGuidelineFromEnd(0.17f)
                                    val bottomScanQrCodeGuideLine = createGuidelineFromBottom(0.19f)

                                    Box(
                                        modifier = Modifier
                                            .clip(shape = RoundedCornerShape(size = 14.dp))
                                            .clipToBounds()
                                            .border(
                                                2.dp,
                                                onSurfaceVariantDark,
                                                RoundedCornerShape(size = 14.dp)
                                            )
                                            .constrainAs(qrCode) {
                                                top.linkTo(topScanQrCodeGuideLine)
                                                start.linkTo(startScanQrCodeGuideLine)
                                                end.linkTo(endScanQrCodeGuideLine)
                                                bottom.linkTo(bottomScanQrCodeGuideLine)
                                                width = Dimension.fillToConstraints
                                                height = Dimension.fillToConstraints
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

                                        val topTitleGuideLine = createGuidelineFromTop(0.14f)
                                        val startTitleGuideLine = createGuidelineFromStart(0.15f)
                                        val endTitleGuideLine = createGuidelineFromEnd(0.15f)

                                        Text(
                                            text = "Leia o QR Code e aproveite os cupons de descontos das nossas cervejas",
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                lineBreak = LineBreak.Paragraph
                                            ),
                                            fontSize = 18.sp,
                                            color = surfaceBrightDark,
                                            modifier = Modifier
                                                .constrainAs(textTitle) {
                                                    top.linkTo(topTitleGuideLine)
                                                    start.linkTo(startTitleGuideLine)
                                                    end.linkTo(endTitleGuideLine)
                                                    width = Dimension.fillToConstraints
                                                }
                                        )

                                        val topQrCodeGuideLine = createGuidelineFromTop(0.40f)
                                        val startQrCodeGuideLine = createGuidelineFromStart(0.20f)
                                        val endQrCodeGuideLine = createGuidelineFromEnd(0.20f)
                                        val bottomQrCodeGuideLine = createGuidelineFromBottom(0.25f)

                                        Image(
                                            painter = painterResource(Res.drawable.qr_code_big),
                                            contentDescription = "qr-code",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .constrainAs(qrCode) {
                                                    top.linkTo(topQrCodeGuideLine)
                                                    start.linkTo(startQrCodeGuideLine)
                                                    end.linkTo(endQrCodeGuideLine)
                                                    bottom.linkTo(bottomQrCodeGuideLine)
                                                    width = Dimension.fillToConstraints
                                                    height = Dimension.fillToConstraints
                                                },
                                        )
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
                            ConstraintLayout {
                                val (card, textTitle, icon, textDiscount) = createRefs()

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
                                                    end = 10.dp,
                                                    bottom = 30.dp
                                                )
                                        )
                                    }
                                }

                                val topTitleGuideLine = createGuidelineFromTop(0.14f)
                                val startTitleGuideLine = createGuidelineFromStart(0.15f)
                                val endTitleGuideLine = createGuidelineFromEnd(0.15f)

                                Text(
                                    text = state.message,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        lineBreak = LineBreak.Paragraph
                                    ),
                                    fontSize = 18.sp,
                                    color = surfaceBrightDark,
                                    modifier = Modifier
                                        .constrainAs(textTitle) {
                                            top.linkTo(topTitleGuideLine)
                                            start.linkTo(startTitleGuideLine)
                                            end.linkTo(endTitleGuideLine)
                                            width = Dimension.fillToConstraints
                                        }
                                )

                                val topIconGuideLine = createGuidelineFromTop(0.43f)
                                val bottomIconGuideLine = createGuidelineFromBottom(0.21f)
                                val startIconGuideLine = createGuidelineFromStart(0.15f)
                                val endIconGuideLine = createGuidelineFromEnd(0.15f)

                                Image(
                                    painter = painterResource(Res.drawable.icon_thumbs_down),
                                    contentDescription = "qr-code",
                                    modifier = Modifier
                                        .constrainAs(icon) {
                                            top.linkTo(topIconGuideLine)
                                            start.linkTo(startIconGuideLine)
                                            end.linkTo(endIconGuideLine)
                                            bottom.linkTo(bottomIconGuideLine)
                                            width = Dimension.fillToConstraints
                                            height = Dimension.fillToConstraints
                                        }
                                )

                                val bottomDiscountGuideLine = createGuidelineFromBottom(0.12f)
                                val startDiscountGuideLine = createGuidelineFromStart(0.15f)
                                val endDiscountGuideLine = createGuidelineFromEnd(0.15f)

                                Text(
                                    text = "Verifique o QR Code",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontSize = 24.sp,
                                    color = surfaceBrightDark,
                                    modifier = Modifier
                                        .constrainAs(textDiscount) {
                                            bottom.linkTo(bottomDiscountGuideLine)
                                            start.linkTo(startDiscountGuideLine)
                                            end.linkTo(endDiscountGuideLine)
                                            width = Dimension.fillToConstraints
                                        }
                                )
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
                            ConstraintLayout {
                                val (card, textTitle, icon, textDiscount) = createRefs()

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
                                                    end = 10.dp,
                                                    bottom = 30.dp
                                                )
                                        )
                                    }
                                }

                                val topTitleGuideLine = createGuidelineFromTop(0.14f)
                                val startTitleGuideLine = createGuidelineFromStart(0.15f)
                                val endTitleGuideLine = createGuidelineFromEnd(0.15f)

                                Text(
                                    text = state.expirationMessage,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        lineBreak = LineBreak.Paragraph
                                    ),
                                    fontSize = 18.sp,
                                    color = surfaceBrightDark,
                                    modifier = Modifier
                                        .constrainAs(textTitle) {
                                            top.linkTo(topTitleGuideLine)
                                            start.linkTo(startTitleGuideLine)
                                            end.linkTo(endTitleGuideLine)
                                            width = Dimension.fillToConstraints
                                        }
                                )

                                val topIconGuideLine = createGuidelineFromTop(0.42f)
                                val startIconGuideLine = createGuidelineFromStart(0.15f)
                                val endIconGuideLine = createGuidelineFromEnd(0.15f)

                                Image(
                                    painter = painterResource(Res.drawable.logohalfmouth),
                                    contentDescription = "qr-code",
                                    modifier = Modifier
                                        .constrainAs(icon) {
                                            top.linkTo(topIconGuideLine)
                                            start.linkTo(startIconGuideLine)
                                            end.linkTo(endIconGuideLine)
                                            width = Dimension.fillToConstraints
                                        }
                                )


                                val bottomDiscountGuideLine = createGuidelineFromBottom(0.12f)
                                val startDiscountGuideLine = createGuidelineFromStart(0.15f)
                                val endDiscountGuideLine = createGuidelineFromEnd(0.15f)

                                Text(
                                    text = state.message,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontSize = 24.sp,
                                    color = surfaceBrightDark,
                                    modifier = Modifier
                                        .constrainAs(textDiscount) {
                                            bottom.linkTo(bottomDiscountGuideLine)
                                            start.linkTo(startDiscountGuideLine)
                                            end.linkTo(endDiscountGuideLine)
                                            width = Dimension.fillToConstraints
                                        }
                                )
                            }
                        }
                    }

                    if(!isSnackBarSuccessDisplaying){
                        snackBarOnlyMessage(
                            snackBarHostState = snackBarHostState,
                            coroutineScope = scope,
                            message = TICKET_SUCCESS_INSERT
                        )
                        isSnackBarSuccessDisplaying = true
                    }
                }

                is DiscountsViewState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        MyAppCircularProgressIndicator()
                    }
                }
            }
        }
    }
}
