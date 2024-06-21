package screens.contactInfo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.AppToolbarLarge
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.icon_email
import halfmouthappproject.composeapp.generated.resources.icon_location
import halfmouthappproject.composeapp.generated.resources.icon_phone
import halfmouthappproject.composeapp.generated.resources.icon_whats_app
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import theme.mainYellowColor
import theme.onSecondaryContainerDark
import theme.onSurfaceVariantDark
import util.OpenWhatsAppChat


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactInfoScreen(onNavigateToProfile:() -> Unit) {

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val snackBarHostState = remember { SnackbarHostState() }
    var isOpenWhatsAppChat by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            AppToolbarLarge(
                title = "Contato",
                onNavigationLeftIconClick = { },
                onNavigationProfileIconClick = {onNavigateToProfile() },
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
            subListHalfMouthContact{ isOpenWhatsAppChat = true }
        }
        if (isOpenWhatsAppChat) {
            OpenWhatsAppChat()
            isOpenWhatsAppChat = false
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
fun LazyListScope.subListHalfMouthContact(onOpenWhatsAppChat: () -> Unit) {
    items(1) {
        Text(
            modifier = Modifier.padding(top = 8.dp, start = 16.dp),
            text = "Fale Conosco",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 20.sp,
        )
        Text(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 4.dp, bottom = 2.dp, start = 24.dp, end = 16.dp),
            text = "Solicite um orçamento através dos canais:",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 16.sp,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier.padding(top = 5.dp, start = 24.dp),
                painter = painterResource(Res.drawable.icon_phone),
                contentDescription = null,
                tint = onSecondaryContainerDark
            )
            Text(
                modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                text = "+55 15 99999-9999",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp,
                color = onSecondaryContainerDark,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier.padding(start = 24.dp, top=6.dp),
                painter = painterResource(Res.drawable.icon_email),
                contentDescription = null,
                tint = onSecondaryContainerDark
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp, top = 6.dp)
                    .wrapContentHeight(),
                text = "halfmouth@halfmouth.com.br",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp,
                color = onSecondaryContainerDark,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ){
            Icon(
                modifier = Modifier
                    .size(120.dp)
                    .padding(start = 12.dp)
                    .clickable { onOpenWhatsAppChat() },
                painter = painterResource(Res.drawable.icon_whats_app),
                contentDescription = null,
                tint = mainYellowColor
            )
            Text(
                modifier = Modifier
                    .padding(top = 20.dp, start = 0.dp, end = 16.dp)
                    .wrapContentHeight()
                    .clickable { onOpenWhatsAppChat() },
                text = "Entre em contato pelo WhatsApp e faça o seu pedido agora!",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 17.sp,
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(16.dp)
                .alpha(0.7f),
            thickness = 1.dp,
            color = onSurfaceVariantDark
        )

        Text(
            modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
            text = "Nosso Endereço",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 20.sp,
        )
        Text(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 4.dp, bottom = 4.dp, start = 24.dp, end = 16.dp),
            text = "Venha encher os seus growlers.",
            style = MaterialTheme.typography.titleMedium,
            fontSize = 16.sp,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .clickable { }
                    .padding(top = 8.dp, start = 24.dp),
                painter = painterResource(Res.drawable.icon_location),
                contentDescription = null,
                tint = onSecondaryContainerDark
            )
            Text(
                modifier = Modifier
                    .padding(start = 4.dp, end = 16.dp)
                    .wrapContentHeight(),
                text = "Rua João Martini Filho, n°426, Jd. São Conrado, Sorocaba-SP.",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp,
                color = onSecondaryContainerDark,
            )
        }
    }
}
