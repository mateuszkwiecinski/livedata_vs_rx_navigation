package pls.help.livedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_change_counter.*
import pls.help.livedata.domain.DomainState

class ChangeCounter : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_counter)

        btnIncrement.setOnClickListener {
            Log.i("MyApp", "incrementing")
            DomainState.increment()
        }
    }
}
