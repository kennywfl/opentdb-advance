package com.opentrivia.app.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.core.util.valueIterator
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.opentrivia.app.lib.Constants
import com.opentrivia.app.lib.datasource.DataManager
import com.opentrivia.app.lib.datasource.local.sharedpreference.AppSharedPreference
import com.opentrivia.app.lib.datasource.model.Questions
import com.opentrivia.app.model.QuizViewModel
import com.opentrivia.app.ui.screen.CatalogScreen
import com.opentrivia.app.ui.screen.CatalogViewModel
import com.opentrivia.app.ui.screen.MainScreen
import com.opentrivia.app.ui.screen.MainViewModel
import com.opentrivia.app.ui.screen.QuizContentView
import com.opentrivia.app.ui.screen.QuizScreen
import com.opentrivia.app.ui.screen.ResultContentView
import com.opentrivia.app.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject

@Serializable
private sealed interface NavRoute : NavKey {
    @Serializable data object Main : NavRoute
    @Serializable data object Catalog : NavRoute
    @Serializable data object Quiz : NavRoute
    @Serializable data object QuickQuiz : NavRoute
    @Serializable data object Result : NavRoute
}

private data class BottomTab(
    val route: NavRoute,
    val label: String,
    val icon: ImageVector
)

private val bottomTabs = listOf(
    BottomTab(NavRoute.Main, "Browse", Icons.Filled.Home),
    BottomTab(NavRoute.Catalog, "Catalog", Icons.Filled.List),
    BottomTab(NavRoute.Quiz, "Quiz", Icons.Filled.Star)
)

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MainNavHost()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainNavHost() {
    val backStack = rememberNavBackStack(NavRoute.Main)
    val quizViewModel: QuizViewModel = viewModel()
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Open Trivia") },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = {
                                showMenu = false
                                context.startActivity(
                                    Intent(context, SettingActivity::class.java)
                                )
                            }
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                bottomTabs.forEach { tab ->
                    NavigationBarItem(
                        selected = tab.route == backStack.firstOrNull(),
                        onClick = {
                            if (backStack.size > 1) {
                                backStack.clear()
                            }
                            backStack.add(tab.route)
                        },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider = entryProvider {
                    entry<NavRoute.Main> { MainTabContent() }
                    entry<NavRoute.Catalog> { CatalogTabContent() }
                    entry<NavRoute.Quiz> {
                        QuizTabContent(
                            quizViewModel = quizViewModel,
                            onStartQuiz = { backStack.add(NavRoute.QuickQuiz) },
                            onShowResults = { backStack.add(NavRoute.Result) }
                        )
                    }
                    entry<NavRoute.QuickQuiz> {
                        QuizContentView(
                            quizViewModel = quizViewModel,
                            onDismiss = { backStack.removeLastOrNull() }
                        )
                    }
                    entry<NavRoute.Result> {
                        ResultContentView(
                            questions = quizViewModel.questionList,
                            onClose = { backStack.removeLastOrNull() }
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun MainTabContent() {
    val mainViewModel = koinInject<MainViewModel>()
    val uiState by mainViewModel.uiState.collectAsState()
    val prefs = koinInject<AppSharedPreference>()
    val categories = remember(uiState) {
        prefs.retrieveCategories().toMutableList().apply {
            add(0, "Select a category" to "-1")
        }
    }
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    MainScreen(
        uiState = uiState,
        categories = categories,
        selectedCategoryIndex = selectedCategoryIndex,
        onCategorySelected = { index ->
            selectedCategoryIndex = index
            mainViewModel.loadQuestions(categories[index].second.toInt())
        }
    )
}

@Composable
private fun CatalogTabContent() {
    val catalogViewModel = koinInject<CatalogViewModel>()
    val uiState by catalogViewModel.uiState.collectAsState()
    CatalogScreen(uiState = uiState)
}

@Composable
private fun QuizTabContent(
    quizViewModel: QuizViewModel,
    onStartQuiz: () -> Unit,
    onShowResults: () -> Unit
) {
    val prefs = koinInject<AppSharedPreference>()
    val dataManager = koinInject<DataManager>()
    var isInputEnabled by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    var isResultVisible by remember { mutableStateOf(false) }
    var resultTime by remember { mutableStateOf("") }
    var resultCorrectCount by remember { mutableStateOf("") }
    var categories by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }

    val answerMap by quizViewModel.answerMap.collectAsState()
    LaunchedEffect(answerMap) {
        if (answerMap.size() > 0) {
            val correct = answerMap.valueIterator().asSequence().count { it }
            isResultVisible = true
            isLoading = false
            resultTime = quizViewModel.remainingTime
            resultCorrectCount = "Correct: $correct / ${quizViewModel.questionList.size}"
        }
    }

    LaunchedEffect(Unit) {
        categories = prefs.retrieveCategories().toMutableList().apply {
            add(0, "Default" to "noop")
        }
    }

    QuizScreen(
        categories = categories,
        isInputEnabled = isInputEnabled,
        isLoading = isLoading,
        isResultVisible = isResultVisible,
        resultTime = resultTime,
        resultCorrectCount = resultCorrectCount,
        onCategorySelected = { },
        onNextClick = {
            isLoading = true
            isInputEnabled = false
            quizViewModel.clear()
            isResultVisible = false
            val categoryId = prefs.retrieveCategories().firstOrNull()?.second ?: ""
            fetchQuestions(dataManager, quizViewModel, categoryId) {
                isLoading = false
                isInputEnabled = true
                onStartQuiz()
            }
        },
        onResultClick = onShowResults
    )
}

private fun fetchQuestions(
    dataManager: DataManager,
    quizViewModel: QuizViewModel,
    categoryId: String,
    onComplete: () -> Unit
) {
    val realCategory = if (categoryId == "noop") null else categoryId.toIntOrNull()
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = dataManager.getTriviaWithToken(
                amount = Constants.QUIZ_SIZE,
                category = realCategory
            )
            val questions = mutableListOf<Questions>()
            if (response.results.isNotEmpty()) {
                response.results.forEach { result ->
                    val listOfAnswer = mutableListOf(result.correctAnswer to true)
                    result.incorrectAnswers.forEach {
                        listOfAnswer.add(it to false)
                    }
                    listOfAnswer.shuffle()
                    questions.add(
                        Questions(
                            result.category,
                            result.question,
                            result.difficulty,
                            Constants.Api.PARAM_MULTIPLE == result.type,
                            listOfAnswer
                        )
                    )
                }
            }
            quizViewModel.questionList = questions
        } catch (_: Exception) { }
        onComplete()
    }
}
