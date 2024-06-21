package screens.contactInfo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import components.AppToolbarLarge
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.icon_email
import halfmouthappproject.composeapp.generated.resources.icon_location
import halfmouthappproject.composeapp.generated.resources.icon_phone
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import theme.onSurfaceDark
import util.OpenWhatsAppChat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactInfoScreen(onNavigateToProfile:() -> Unit) {

    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val snackBarHostState = remember { SnackbarHostState() }
    var showChat by remember { mutableStateOf(false) }

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
            subListHalfMouthContact{ showChat = true }
        }
        if (showChat) {
            OpenWhatsAppChat()
            showChat = false
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
fun LazyListScope.subListHalfMouthContact(onClick: () -> Unit) {
    items(1) {
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Nosso Contato:",
        )
        Text(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 4.dp, bottom = 4.dp),
            text = "Solicite um orçamento através dos canais abaixo.",
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .clickable { onClick() }
                    .padding(top = 5.dp),
                painter = painterResource(Res.drawable.icon_phone),
                contentDescription = null,
                tint = onSurfaceDark
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = "+55 15 99999-9999",
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .clickable { }
                    .padding(top = 5.dp),
                painter = painterResource(Res.drawable.icon_email),
                contentDescription = null,
                tint = onSurfaceDark
            )
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentHeight(),
                text = "halfmouth@halfmouth.com.br",
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "Nosso Endereço:",
        )
        Text(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 4.dp, bottom = 4.dp),
            text = "Venha encher os seus growlers.",
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .clickable { }
                    .padding(top = 5.dp),
                painter = painterResource(Res.drawable.icon_location),
                contentDescription = null,
                tint = onSurfaceDark
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .wrapContentHeight(),
                text = "Rua João Martini Filho, 426 - Jardim São Conrado, Sorocaba-SP.",
            )
        }
    }
}
