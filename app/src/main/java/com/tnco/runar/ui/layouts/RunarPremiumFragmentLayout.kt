package com.tnco.runar.ui.layouts

import android.widget.Button
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tnco.runar.R
import com.tnco.runar.model.SkuModel
import com.tnco.runar.ui.screenCompose.componets.AppBar
import com.tnco.runar.ui.viewmodel.RunarPremiumViewModel
import com.tnco.runar.util.PurchaseHelper

@Composable
fun RunarPremiumFragmentLayout(
    navController: NavController,
    fontSize: Float,
    listOfSkus: List<SkuModel>,
    onClick: (SkuModel) -> Unit
) {

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(id = R.string.runar_premium),
                navController = navController,
                showIcon = true
            )
        },
        backgroundColor = colorResource(id = R.color.settings_top_app_bar),
    ) {

        val choseSku = remember {
            mutableStateOf(listOfSkus[0])
        }
        val purchaseHelper = PurchaseHelper(LocalContext.current)
        purchaseHelper.billingSetup()

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                    SkuCard(
                        title = sku.title,
                        description = sku.description,
                        cost = sku.cost,
                        currencySign = sku.currencySign,
                        fontSize = fontSize,
                        isSelected = (choseSku.value.id == sku.id),
                        discount = sku.discount
                    ) {
                        choseSku.value = sku
                    }
                }
            }
            PayButton(fontSize, onClick, purchaseHelper)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExtraText(name = stringResource(id = R.string.terms_of_use), fontSize = fontSize)
                ExtraText(name = stringResource(id = R.string.privacy_policy), fontSize = fontSize)
                ExtraText(name = stringResource(id = R.string.restore), fontSize = fontSize)
            }
            Spacer(
                modifier = Modifier.height(2.dp)
            )
        }
    }
}

@Composable
fun PayButton(
    fontSize: Float,
    onClick: (SkuModel) -> Unit,
    purchaseHelper: PurchaseHelper
) {
    val buyEnabled by purchaseHelper.buyEnabled.collectAsState(false)
    val consumeEnabled by purchaseHelper.consumeEnabled.collectAsState(false)
    val productName by purchaseHelper.productName.collectAsState("")
    val statusText by purchaseHelper.statusText.collectAsState("")

    Button(
        onClick = {
            purchaseHelper.makePurchase()
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.purchase_header_color)
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.pay),
            color = colorResource(id = R.color.purchase_card_color),
            fontFamily = FontFamily(Font(resId = R.font.amatic_sc_bold)),
            style = TextStyle(
                fontSize = with(LocalDensity.current) {
                    (fontSize * 1.4f).toSp()
                }
            )
        )
    }
}

@Composable
fun SkuCard(
    title: String,
    cost: String,
    currencySign: String,
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
        colorResource(id = R.color.purchase_header_color).copy(alpha = 0.5f)

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
                width = 1.5.dp,
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
                    .width(1.dp),
                color = colorResource(id = R.color.purchase_header_color)
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
            color = colorResource(id = R.color.purchase_card_color),
            fontFamily = FontFamily(Font(resId = R.font.sf_pro_display)),
            style = TextStyle(
                fontSize = with(LocalDensity.current) {
                    (fontSize * 0.7f).toSp()
                }
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

    Column {
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
        Spacer(
            modifier = Modifier.height(16.dp)
        )
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
fun ExtraText(name: String, fontSize: Float) {
    Text(
        text = name,
        color = colorResource(id = R.color.purchase_header_secondary_color),
        fontFamily = FontFamily(Font(resId = R.font.sf_pro_display)),
        style = TextStyle(
            fontSize = with(LocalDensity.current) {
                (fontSize * 0.8f).toSp()
            }
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
    RunarPremiumFragmentLayout(
        navController = rememberNavController(),
        fontSize = 55f,
        listOfSkus = viewModel(modelClass = RunarPremiumViewModel::class.java).listOfSkus
    ) {}
}
