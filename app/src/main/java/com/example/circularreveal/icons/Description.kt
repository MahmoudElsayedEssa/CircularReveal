package com.example.circularreveal.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Description: ImageVector
	get() {
		if (_description != null) {
			return _description!!
		}
		_description = ImageVector.Builder(
            name = "Description",
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
				moveTo(320f, 720f)
				horizontalLineToRelative(320f)
				verticalLineToRelative(-80f)
				horizontalLineTo(320f)
				close()
				moveToRelative(0f, -160f)
				horizontalLineToRelative(320f)
				verticalLineToRelative(-80f)
				horizontalLineTo(320f)
				close()
				moveTo(240f, 880f)
				quadToRelative(-33f, 0f, -56.5f, -23.5f)
				reflectiveQuadTo(160f, 800f)
				verticalLineToRelative(-640f)
				quadToRelative(0f, -33f, 23.5f, -56.5f)
				reflectiveQuadTo(240f, 80f)
				horizontalLineToRelative(320f)
				lineToRelative(240f, 240f)
				verticalLineToRelative(480f)
				quadToRelative(0f, 33f, -23.5f, 56.5f)
				reflectiveQuadTo(720f, 880f)
				close()
				moveToRelative(280f, -520f)
				verticalLineToRelative(-200f)
				horizontalLineTo(240f)
				verticalLineToRelative(640f)
				horizontalLineToRelative(480f)
				verticalLineToRelative(-440f)
				close()
				moveTo(240f, 160f)
				verticalLineToRelative(200f)
				close()
				verticalLineToRelative(640f)
				close()
			}
		}.build()
		return _description!!
	}

private var _description: ImageVector? = null
