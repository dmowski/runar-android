package com.test.runar.ui.fragments


import androidx.activity.compose.setContent
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.*
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.test.runar.R
import com.test.runar.databinding.FragmentLayoutDescriptionBinding
import com.test.runar.databinding.FragmentLibraryBinding
import com.test.runar.presentation.viewmodel.DescriptionViewModel
import com.test.runar.presentation.viewmodel.LibraryViewModel

class LibraryFragment : Fragment() {
/*
    private val viewModel: LibraryViewModel by viewModels()

    private var _binding: FragmentLibraryBinding? = null
    private val binding
        get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentLibraryBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        viewModel.fontSize.observe(viewLifecycleOwner){ textSize->
            binding.itemHeader1.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            binding.itemHeader2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            binding.itemHeader3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            binding.itemHeader4.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            binding.itemHeader5.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            val secondTextSize = (textSize*0.8).toFloat()
            binding.itemText1.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondTextSize)
            binding.itemText2.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondTextSize)
            binding.itemText3.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondTextSize)
            binding.itemText4.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondTextSize)
            binding.itemText5.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondTextSize)
        }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                Bars()
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FirstMenuItem()
}

@Composable
fun Bars() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Библиотека",
                        color = colorResource(id = R.color.library_top_bar_header)
                    )
                },
                backgroundColor = colorResource(id = R.color.library_top_bar),
                modifier = Modifier.aspectRatio(5.8f, false),
                actions = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bookmark_2),
                        tint = colorResource(id = R.color.library_top_bar_fav),
                        contentDescription = "Избранное",
                        modifier = Modifier
                            .padding(end = 10.dp)
                    )
                }
            )
        },
        backgroundColor = Color(0x00000000)
    ) {
        Column {
            FirstMenuItem()
            FirstMenuItem()
            FirstMenuItem()
        }
    }
}

@Composable
fun FirstMenuItem() {
    Row(
        Modifier
            .aspectRatio(3.8f, true)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .weight(16f)
        ) {

        }
        Column(
            Modifier
                .fillMaxSize()
                .weight(398f)
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(20f)
            )
            Row(
                Modifier
                    .fillMaxSize()
                    .weight(62f), verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.library_item1_pic),
                    contentDescription = null,
                    modifier = Modifier
                        .background(Color(0x00000000))
                        .padding(top = 5.dp, bottom = 5.dp)
                )
                Column(
                    Modifier
                        .fillMaxSize()
                        .weight(277f)
                        .padding(start = 15.dp)
                ) {
                    Text(
                        text = "Описание рун",
                        color = colorResource(id = R.color.library_item_header),
                        fontFamily = FontFamily(Font(R.font.roboto_medium))
                    )
                    Text(
                        text = "Текст описания",
                        color = colorResource(id = R.color.library_item_text),
                        fontFamily = FontFamily(Font(R.font.roboto_regular))
                    )
                }
                Box(
                    Modifier
                        .fillMaxSize()
                        .weight(17f)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_right),
                    contentDescription = null,
                    modifier = Modifier
                        .background(Color(0x00000000))
                        .weight(10f)
                )
                Box(
                    Modifier
                        .fillMaxSize()
                        .weight(16f)
                )
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(20f)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_divider),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
        }
    }
}