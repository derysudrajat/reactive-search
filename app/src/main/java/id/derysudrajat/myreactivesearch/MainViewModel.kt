package id.derysudrajat.myreactivesearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.derysudrajat.myreactivesearch.network.ApiConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class MainViewModel: ViewModel() {
    private val accessToken =
        "pk.eyJ1IjoiZGVyeXN1ZHJhamF0IiwiYSI6ImNra3BuNmNhaDBhNXEyb3Bhczd2aTY2ZGsifQ.v5VPB0cAhDooAfpYyYp5Ww"
    val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    val searchResult = queryChannel.asFlow()
        .debounce(300)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }
        .mapLatest {
            ApiConfig.provideApiService().getCountry(it, accessToken).features
        }.asLiveData()
}