package t.app.androidApp
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
/** Documentation: https://start.insert-koin.io/#/getting-started/modules-definitions*/

val appModule = module {
    // single instance of FuelService
    single{ FuelService() }

    viewModel { AppViewModel(get())}
}
