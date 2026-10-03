package plat.rickandmorty.acastrom.ui.state

sealed interface UiAction {
    data object LoadingClick : UiAction
    data object RetryClick : UiAction
}