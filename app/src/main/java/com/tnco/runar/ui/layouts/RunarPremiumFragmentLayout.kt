package com.tnco.runar.ui.layouts

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.android.billingclient.api.ProductDetails
import com.tnco.runar.R
import com.tnco.runar.repository.SharedDataRepository
import com.tnco.runar.ui.fragment.RunarPremiumFragmentDirections
import com.tnco.runar.ui.viewmodel.RunarPremiumViewModel

@Composable
fun RunarPremiumFragmentLayout(
    navController: NavController,
    fontSize: Float,
    listOfSkus: List<ProductDetails>,
    buyEnabled: Boolean = false,
    onClick: (ProductDetails) -> Unit
) {

    Scaffold(
        backgroundColor = colorResource(id = R.color.settings_top_app_bar),
    ) {

        val choseSku = remember {
            mutableStateOf(listOfSkus[0])
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MonetizationTopBar(fontSize, navController)
            Features(fontSize = fontSize)
            Text(
                text = stringResource(id = R.string.choose_payment_plan),
                color = colorResource(id = R.color.purchase_header_color),
                fontFamily = FontFamily(Font(R.font.amatic_sc_bold)),
                style = TextStyle(
                    fontSize = with(LocalDensity.current) {
                        (fontSize * 1.8f).toSp()
                    }
                ),
            )
            Row(
                modifier = Modifier.horizontalScroll(
                    state = rememberScrollState()
                ),
                horizontalArrangement = Arrangement.spacedBy(
                    8.dp,
                    Alignment.CenterHorizontally
                )
            ) {
                listOfSkus.forEach { sku ->
                    sku.subscriptionOfferDetails?.get(0)?.let {
                        SkuCard(
                            title = sku.name,
                            description = sku.description,
                            cost = it.pricingPhases.pricingPhaseList[0].formattedPrice,
                            fontSize = fontSize,
                            isSelected = (choseSku.value.productId == sku.productId),
                            discount = if (sku.productId == "runar_yearly") "-50%" else null
                        ) {
                            choseSku.value = sku
                        }
                    }
                }
            }
            PayButton(fontSize, choseSku.value, buyEnabled) {
                onClick(it)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExtraText(name = stringResource(id = R.string.terms_of_use), fontSize = fontSize, clickAction = {
                })
                ExtraText(name = stringResource(id = R.string.privacy_policy), fontSize = fontSize, clickAction = {
                    val direction =
                        RunarPremiumFragmentDirections.actionRunarPremiumFragmentToPrivacyPolicyFragment()
                    navController.navigate(direction)
                })
                ExtraText(name = stringResource(id = R.string.restore), fontSize = fontSize, weight = FontWeight.W700, clickAction = {
                })
            }
            Spacer(
                modifier = Modifier.height(2.dp)
            )
        }
    }
}

@Composable
fun MonetizationTopBar(fontSize: Float, navController: NavController) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(id = R.string.runar_premium),
            color = colorResource(id = R.color.purchase_header_color),
            fontFamily = FontFamily(Font(R.font.amatic_sc_bold)),
            style = TextStyle(
                fontSize = with(LocalDensity.current) {
                    (fontSize * 2.5f).toSp()
                }
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .padding(horizontal = 60.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.monetization_close_button),
            contentDescription = null,
            modifier = Modifier
                .padding(end = 12.dp)
                .align(Alignment.TopEnd)
                .clickable(onClick = { navController.popBackStack() }),
        )
    }
}

@Composable
fun ColumnScope.PayButton(
    fontSize: Float,
    sku: ProductDetails,
    buttonEnabled: Boolean,
    onClick: (ProductDetails) -> Unit
) {
    Button(
        onClick = {
            onClick(sku)
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.purchase_header_color)
        ),
        shape = RoundedCornerShape(8.dp),
        enabled = buttonEnabled
    ) {
        Text(
            text = stringResource(id = R.string.pay),
            color = colorResource(id = R.color.purchase_card_color),
            fontFamily = FontFamily(Font(resId = R.font.amatic_sc_bold)),
            style = TextStyle(
                fontSize = with(LocalDensity.current) {
                    (fontSize * 1.4f).toSp()
                }
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun SkuCard(
    title: String,
    cost: String,
    currencySign: String = "",
    description: String = "pay once",
    fontSize: Float,
    isSelected: Boolean = false,
    discount: String?,
    onClick: () -> (Unit)
) {
    val textDescriptionDecoration =
        if (discount != null) TextDecoration.LineThrough else TextDecoration.None

    val backgroundColor =
        colorResource(id = if (isSelected) R.color.purchase_header_color else R.color.purchase_card_color)

    val titleColor =
        colorResource(id = if (isSelected) R.color.purchase_card_color else R.color.white)

    val borderColor = if (isSelected)
        colorResource(id = R.color.purchase_header_color)
    else
        colorResource(id = R.color.purchase_header_color).copy(alpha = 0.3f)

    // for device with height = 800dp and width = 400dp
    // use height = 200 and width = 120
    val configuration = LocalConfiguration.current
    val height = ((configuration.screenHeightDp * 180) / 800f).dp
    val width = ((configuration.screenWidthDp * 120) / 400f).dp

    Column(
        modifier = Modifier
            .height(height)
            .width(width)
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = colorResource(id = R.color.purchase_card_color))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(color = backgroundColor)
        ) {
            Text(
                text = title,
                color = titleColor,
                fontFamily = FontFamily(Font(R.font.amatic_sc_bold)),
                style = TextStyle(
                    fontSize = with(LocalDensity.current) {
                        (fontSize * 1.4f).toSp()
                    }
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = borderColor
            )
        }
        Box(
            contentAlignment = Alignment.BottomStart
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = currencySign + cost,
                    color = colorResource(id = R.color.white),
                    fontFamily = FontFamily(Font(resId = R.font.sf_pro_display)),
                    style = TextStyle(
                        fontSize = with(LocalDensity.current) {
                            (fontSize * 1.2f).toSp()
                        }
                    )
                )
                Text(
                    text = description,
                    color = colorResource(id = R.color.purchase_gray700),
                    fontFamily = FontFamily(Font(resId = R.font.sf_pro_display)),
                    style = TextStyle(
                        fontSize = with(LocalDensity.current) {
                            (fontSize * 0.8f).toSp()
                        },
                        textDecoration = textDescriptionDecoration
                    )
                )
            }
            if (discount != null)
                DisCount(percent = discount, fontSize = fontSize)
        }
    }
}

@Composable
fun DisCount(percent: String, fontSize: Float) {
    Box(
        modifier = Modifier
            .height(40.dp)
            .width(40.dp)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        colorResource(id = R.color.transparent),
                        colorResource(id = R.color.purchase_discount_card_color)
                    ),
                    start = Offset(x = 1f, y = 0f), end = Offset(x = 0f, y = 1f)
                ),
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = percent,
            color = colorResource(id = R.color.black),
            fontFamily = FontFamily(Font(resId = R.font.sf_pro_display)),
            style = TextStyle(
                fontSize = with(LocalDensity.current) {
                    (fontSize * 0.7f).toSp()
                },
                fontWeight = FontWeight.W600
            ),
            modifier = Modifier
                .padding(top = 8.dp)
                .rotate(45f)
        )
    }
}

@Composable
fun Features(fontSize: Float) {
    val listOfFeatures = listOf(
        R.string.all_runic_draws,
        R.string.all_runes_description,
        R.string.audio_library,
        R.string.runic_patterns_generator
    )

    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(
            text = stringResource(id = R.string.get_a_full_access),
            color = colorResource(id = R.color.purchase_header_color),
            fontFamily = FontFamily(Font(R.font.amatic_sc_bold)),
            style = TextStyle(
                fontSize = with(LocalDensity.current) {
                    (fontSize * 1.8f).toSp()
                }
            )
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        listOfFeatures.forEach { stringId ->
            Feature(title = stringResource(id = stringId), fontSize = fontSize)
        }
    }
}

@Composable
fun Feature(title: String, fontSize: Float) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.ios_true),
            contentDescription = title
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            color = colorResource(id = R.color.white),
            fontFamily = FontFamily(Font(resId = R.font.sf_pro_display)),
            style = TextStyle(
                fontSize = with(LocalDensity.current) {
                    (fontSize * 0.8f).toSp()
                }
            )
        )
    }
}

@Composable
fun ExtraText(name: String, fontSize: Float, weight: FontWeight = FontWeight.W400, clickAction: () -> Unit) {
    Text(
        modifier = Modifier.clickable(onClick = clickAction),
        text = name,
        color = colorResource(id = R.color.purchase_header_secondary_color),
        fontFamily = FontFamily(Font(resId = R.font.sf_pro_display)),
        style = TextStyle(
            fontSize = with(LocalDensity.current) {
                (fontSize * 0.8f).toSp()
            },
            fontWeight = weight
        ),
        textAlign = TextAlign.Center
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    locale = "ru"
)
@Composable
fun RunarPremiumFragmentLayoutPreview() {
    val viewModel = RunarPremiumViewModel(SharedDataRepository(LocalContext.current))

    RunarPremiumFragmentLayout(
        navController = rememberNavController(),
        fontSize = 55f,
        listOfSkus = listOf()
    ) {}
}
