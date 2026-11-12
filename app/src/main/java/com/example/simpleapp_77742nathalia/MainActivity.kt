package com.example.simpleapp_77742nathalia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.simpleapp_77742nathalia.ui.components.*
import com.example.simpleapp_77742nathalia.ui.theme.SimpleApp77742NathaliaTheme
import com.example.simpleapp_77742nathalia.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleApp77742NathaliaTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("State Hoisting", "BaaS Auth", "Preconditions", "Custom Exception")

    // Hoisted remember & rememberSaveable state variables (moved from StateSection)
    var rememberCounter by remember { mutableIntStateOf(0) }
    var rememberSaveableCounter by rememberSaveable { mutableIntStateOf(0) }

    // CollectViewModel States
    val hoistedCounter1 by viewModel.hoistedCounter1.collectAsState()
    val hoistedCounter2 by viewModel.hoistedCounter2.collectAsState()

    val currentUser by viewModel.baasAuthManager.currentUser.collectAsState()
    val authError by viewModel.baasAuthManager.authError.collectAsState()

    val isLightOn by viewModel.isLightOn.collectAsState()
    val lightLog by viewModel.lightExceptionLog.collectAsState()
    val numberInputText by viewModel.numberInputText.collectAsState()
    val numberLog by viewModel.numberExceptionLog.collectAsState()

    val wordInputText by viewModel.wordInputText.collectAsState()
    val useSubclass by viewModel.useSubclassException.collectAsState()
    val wordLog by viewModel.wordExceptionLog.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Mobile Apps II — Assignment 2",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "77742 Nathalia Macedo",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Scrollable Tabs
            PrimaryScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 16.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = title, fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }

            // Tab Content Area
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                when (selectedTabIndex) {
                    0 -> StateSection(
                        rememberCounter = rememberCounter,
                        rememberSaveableCounter = rememberSaveableCounter,
                        onIncrement = {
                            rememberCounter += 1
                            rememberSaveableCounter += 1
                            viewModel.incrementHoistedCounters()
                        },
                        onReset = {
                            rememberCounter = 0
                            rememberSaveableCounter = 0
                            viewModel.resetHoistedCounters()
                        },
                        hoistedCounter1 = hoistedCounter1,
                        hoistedCounter2 = hoistedCounter2
                    )

                    1 -> BaasSection(
                        currentUser = currentUser,
                        authError = authError,
                        onSignInEmail = { email, pass -> viewModel.baasAuthManager.signInWithEmail(email, pass) },
                        onRegisterEmail = { email, pass -> viewModel.baasAuthManager.registerWithEmail(email, pass) },
                        onSignInAnonymous = { viewModel.baasAuthManager.signInAnonymously() },
                        onSignOut = { viewModel.baasAuthManager.signOut() }
                    )

                    2 -> PreconditionsSection(
                        isLightOn = isLightOn,
                        lightLog = lightLog,
                        onToggleLight = { viewModel.toggleLight(it) },
                        onExecuteLightAction = { viewModel.executeLightAction() },
                        numberInputText = numberInputText,
                        numberLog = numberLog,
                        onUpdateNumberInput = { viewModel.updateNumberInput(it) },
                        onValidateNumberInput = { viewModel.validateNumberInput() }
                    )

                    3 -> CustomExceptionSection(
                        wordInputText = wordInputText,
                        useSubclass = useSubclass,
                        wordLog = wordLog,
                        onUpdateWordInput = { viewModel.updateWordInput(it) },
                        onToggleUseSubclass = { viewModel.toggleUseSubclassException(it) },
                        onCheckWordLength = { viewModel.checkWordLength() }
                    )
                }
            }
        }
    }
}
