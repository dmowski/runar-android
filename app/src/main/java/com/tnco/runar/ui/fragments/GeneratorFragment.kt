package com.tnco.runar.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tnco.runar.R
import com.tnco.runar.presentation.viewmodel.GeneratorViewModel
import com.tnco.runar.repository.SharedDataRepository.fontSize

class GeneratorFragment : Fragment(){
    val viewModel: GeneratorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = ComposeView(requireContext()).apply {
            setContent {
                Bars()
            }
        }
        return view
    }

}

@Composable
private fun Bars() {
    val header = stringResource(id = R.string.pattern_generator)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = header,
                        color = colorResource(id = R.color.library_top_bar_header),
                        fontFamily = FontFamily(Font(R.font.roboto_medium)),
                        style = TextStyle(fontSize = with(LocalDensity.current) { ((fontSize * 1.35).toFloat()).toSp() })
                    )
                },
                backgroundColor = colorResource(id = R.color.library_top_bar)
            )
        },
        backgroundColor = Color(0x73000000)
    ){}
}