package org.craftsilicon.project

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController { App() }




//fun MainViewController(): UIViewController = ComposeUIViewController(configure = { enforceStrictPlistSanityCheck = false }) { App() }