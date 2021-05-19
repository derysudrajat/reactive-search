package id.derysudrajat.myreactivesearch

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import id.derysudrajat.myreactivesearch.databinding.ActivityMainBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@FlowPreview
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edPlace.doOnTextChanged { text, _, _, _ ->
            lifecycleScope.launch { model.queryChannel.send(text.toString()) }
        }

        model.searchResult.observe(this, {
            val placeName = arrayListOf<String>()
            it.map { placesItem -> placeName.add(placesItem.placeName) }
            val adapter = ArrayAdapter(this, android.R.layout.select_dialog_item, placeName)
            adapter.notifyDataSetChanged()
            binding.edPlace.setAdapter(adapter)
        })
    }
}