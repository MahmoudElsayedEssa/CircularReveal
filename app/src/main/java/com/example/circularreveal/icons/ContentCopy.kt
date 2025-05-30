package com.example.circularreveal.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ContentCopy: ImageVector
	get() {
		if (_contentCopy != null) {
			return _contentCopy!!
		}
		_contentCopy = ImageVector.Builder(
            name = "Content_copy",
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
				moveTo(360f, 720f)
				quadToRelative(-33f, 0f, -56.5f, -23.5f)
				reflectiveQuadTo(280f, 640f)
				verticalLineToRelative(-480f)
				quadToRelative(0f, -33f, 23.5f, -56.5f)
				reflectiveQuadTo(360f, 80f)
				horizontalLineToRelative(360f)
				quadToRelative(33f, 0f, 56.5f, 23.5f)
				reflectiveQuadTo(800f, 160f)
				verticalLineToRelative(480f)
				quadToRelative(0f, 33f, -23.5f, 56.5f)
				reflectiveQuadTo(720f, 720f)
				close()
				moveToRelative(0f, -80f)
				horizontalLineToRelative(360f)
				verticalLineToRelative(-480f)
				horizontalLineTo(360f)
				close()
				moveTo(200f, 880f)
				quadToRelative(-33f, 0f, -56.5f, -23.5f)
				reflectiveQuadTo(120f, 800f)
				verticalLineToRelative(-560f)
				horizontalLineToRelative(80f)
				verticalLineToRelative(560f)
				horizontalLineToRelative(440f)
				verticalLineToRelative(80f)
				close()
				moveToRelative(160f, -240f)
				verticalLineToRelative(-480f)
				close()
			}
		}.build()
		return _contentCopy!!
	}

private var _contentCopy: ImageVector? = null
