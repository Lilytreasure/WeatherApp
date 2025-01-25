package org.craftsilicon.project.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import craftsilicon.composeapp.generated.resources.Res
import craftsilicon.composeapp.generated.resources.poppins_bold
import craftsilicon.composeapp.generated.resources.poppins_light
import craftsilicon.composeapp.generated.resources.poppins_medium
import craftsilicon.composeapp.generated.resources.poppins_regular
import org.jetbrains.compose.resources.Font


@Composable
fun poppins(): FontFamily {
    val poppinsRegular =
        Font(
            resource = Res.font.poppins_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal,
        )

    val poppinsBold =
        Font(
            resource = Res.font.poppins_bold,
            FontWeight.Bold,
            FontStyle.Normal,
        )

    val poppinsLight =
        Font(
            resource = Res.font.poppins_light,
            FontWeight.Light,
            FontStyle.Normal,
        )

    val poppinsMedium =
        Font(
            resource = Res.font.poppins_medium,
            FontWeight.Medium,
            FontStyle.Normal,
        )

    return FontFamily(
        poppinsLight,
        poppinsRegular,
        poppinsMedium,
        poppinsBold
    )
}
@Composable
internal fun getTypography(): Typography {
    val poppins = poppins()
    return Typography(
        displayLarge = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.W400,
            fontSize = 50.sp,
        ),
        displayMedium = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.W400,
            fontSize = 40.sp,
        ),
        displaySmall = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.W400,
            fontSize = 30.sp,
        ),
        headlineLarge = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
        ),
        headlineMedium = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.W400,
            fontSize = 24.sp,
        ),
        headlineSmall = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.W400,
            fontSize = 20.sp,
        ),
        titleLarge = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.W700,
            fontSize = 18.sp,
        ),
        titleMedium = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.W700,
            fontSize = 14.sp,
        ),
        titleSmall = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.W400,
            fontSize = 14.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.W400,
            fontSize = 12.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.W400,
            fontSize = 11.sp,
        ),
        labelLarge = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.W400,
            fontSize = 13.sp,
        ),
        labelMedium = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.W400,
            fontSize = 11.sp,
        ),
        labelSmall = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.W500,
            fontSize = 9.sp,
        ),
    )
}
