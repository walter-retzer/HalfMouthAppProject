package screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import components.MyAppCircularProgressIndicator
import components.profileToolbar
import data.UserPreferences
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.icon_account
import halfmouthappproject.composeapp.generated.resources.icon_email
import halfmouthappproject.composeapp.generated.resources.icon_equipaments
import halfmouthappproject.composeapp.generated.resources.icon_logout
import halfmouthappproject.composeapp.generated.resources.icon_phone
import halfmouthappproject.composeapp.generated.resources.splashscreenlogo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import theme.onSurfaceVariantDark
import util.RestartApp
import util.formattedAsPhone
import util.snackBarOnlyMessage

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onNavigateSignOut: () -> Unit,
) {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val auth = remember { Firebase.auth }
    val settingsPref = Settings()
    val uid: String? = settingsPref[UserPreferences.UID]
    val name: String? = settingsPref[UserPreferences.NAME]
    val email: String? = settingsPref[UserPreferences.EMAIL]
    val phone: String? = settingsPref[UserPreferences.PHONE]
    var progressButtonIsActivated by remember { mutableStateOf(false) }
    var signOut by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost =  { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            profileToolbar(
                title = "Perfil",
                onNavigationIconBack = { onNavigateBack() },
                onNavigationIconClose = { onNavigateBack() },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.splashscreenlogo),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                        .background(Color.White, CircleShape)
                        .padding(16.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name.toString(),
                    style = TextStyle(
                        color = Color.Yellow,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    ),
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = Modifier
                        .padding(8.dp),
                    painter = painterResource(Res.drawable.icon_email),
                    contentDescription = null,
                    tint = Color.White
                )
                Text(
                    text = email.toString(),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    ),
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = Modifier
                        .padding(8.dp),
                    painter = painterResource(Res.drawable.icon_phone),
                    contentDescription = null,
                    tint = Color.White
                )
                Text(
                    text = phone.toString().formattedAsPhone(),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = Modifier
                        .padding(8.dp),
                    painter = painterResource(Res.drawable.icon_equipaments),
                    contentDescription = null,
                    tint = Color.White
                )
                Text(
                    text = "ID: $uid",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    ),
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(16.dp),
                thickness = 1.dp,
                color = onSurfaceVariantDark
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            scope.launch {
                                try {
                                    progressButtonIsActivated = true
                                    auth.currentUser?.delete()
                                    auth.signOut()

                                    settingsPref.clear()
                                    delay(2000L)
                                    signOut = true

                                    //onNavigateSignOut()
                                } catch (e: Exception) {
                                    progressButtonIsActivated = false
                                    println(e)
                                    snackBarOnlyMessage(
                                        snackBarHostState = snackBarHostState,
                                        coroutineScope = scope,
                                        message = "Não foi possível excluir a sua conta, por favor, tente mais tarde."
                                    )
                                }
                            }

                        },
                    painter = painterResource(Res.drawable.icon_account),
                    contentDescription = null,
                    tint = Color.White
                )
                Text(
                    modifier = Modifier.clickable {  },
                    text = "Excluir Conta",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    ),
                )

                Icon(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            scope.launch {
                                try {
                                    progressButtonIsActivated = true
                                    auth.signOut()

                                    settingsPref.clear()


                                    delay(2000L)
                                    signOut = true

                                } catch (e: Exception) {
                                    progressButtonIsActivated = false
                                    println(e)
                                    snackBarOnlyMessage(
                                        snackBarHostState = snackBarHostState,
                                        coroutineScope = scope,
                                        message = "Não foi possível deletar a sua conta, por favor, tente mais tarde."
                                    )
                                }
                            }

                        },
                    painter = painterResource(Res.drawable.icon_logout),
                    contentDescription = null,
                    tint = Color.White
                )
                Text(
                    modifier = Modifier.clickable {  },
                    text = "Sair",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    ),
                )

                if(progressButtonIsActivated){
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        MyAppCircularProgressIndicator()
                    }
                }

                if(signOut) RestartApp()
            }
        }
    }
}
