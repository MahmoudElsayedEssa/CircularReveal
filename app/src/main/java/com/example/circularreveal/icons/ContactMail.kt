package com.example.circularreveal.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ContactMail: ImageVector
	get() {
		if (_contactMail != null) {
			return _contactMail!!
		}
		_contactMail = ImageVector.Builder(
            name = "Contact_mail",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
			path(
    			fill = SolidColor(Color.Black),
    			fillAlpha = 1.0f,
    			stroke = null,
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 1.0f,
    			strokeLineCap = StrokeCap.Butt,
    			strokeLineJoin = StrokeJoin.Miter,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(560f, 440f)
				horizontalLineToRelative(280f)
				verticalLineToRelative(-200f)
				horizontalLineTo(560f)
				close()
				moveToRelative(140f, -50f)
				lineToRelative(-100f, -70f)
				verticalLineToRelative(-40f)
				lineToRelative(100f, 70f)
				lineToRelative(100f, -70f)
				verticalLineToRelative(40f)
				close()
				moveTo(80f, 840f)
				quadToRelative(-33f, 0f, -56.5f, -23.5f)
				reflectiveQuadTo(0f, 760f)
				verticalLineToRelative(-560f)
				quadToRelative(0f, -33f, 23.5f, -56.5f)
				reflectiveQuadTo(80f, 120f)
				horizontalLineToRelative(800f)
				quadToRelative(33f, 0f, 56.5f, 23.5f)
				reflectiveQuadTo(960f, 200f)
				verticalLineToRelative(560f)
				quadToRelative(0f, 33f, -23.5f, 56.5f)
				reflectiveQuadTo(880f, 840f)
				close()
				moveToRelative(556f, -80f)
				horizontalLineToRelative(244f)
				verticalLineToRelative(-560f)
				horizontalLineTo(80f)
				verticalLineToRelative(560f)
				horizontalLineToRelative(4f)
				quadToRelative(42f, -75f, 116f, -117.5f)
				reflectiveQuadTo(360f, 600f)
				reflectiveQuadToRelative(160f, 42.5f)
				reflectiveQuadTo(636f, 760f)
				moveTo(360f, 560f)
				quadToRelative(50f, 0f, 85f, -35f)
				reflectiveQuadToRelative(35f, -85f)
				reflectiveQuadToRelative(-35f, -85f)
				reflectiveQuadToRelative(-85f, -35f)
				reflectiveQuadToRelative(-85f, 35f)
				reflectiveQuadToRelative(-35f, 85f)
				reflectiveQuadToRelative(35f, 85f)
				reflectiveQuadToRelative(85f, 35f)
				moveTo(182f, 760f)
				horizontalLineToRelative(356f)
				quadToRelative(-34f, -38f, -80.5f, -59f)
				reflectiveQuadTo(360f, 680f)
				reflectiveQuadToRelative(-97f, 21f)
				reflectiveQuadToRelative(-81f, 59f)
				moveToRelative(178f, -280f)
				quadToRelative(-17f, 0f, -28.5f, -11.5f)
				reflectiveQuadTo(320f, 440f)
				reflectiveQuadToRelative(11.5f, -28.5f)
				reflectiveQuadTo(360f, 400f)
				reflectiveQuadToRelative(28.5f, 11.5f)
				reflectiveQuadTo(400f, 440f)
				reflectiveQuadToRelative(-11.5f, 28.5f)
				reflectiveQuadTo(360f, 480f)
				moveToRelative(120f, 0f)
			}
		}.build()
		return _contactMail!!
	}

private var _contactMail: ImageVector? = null
