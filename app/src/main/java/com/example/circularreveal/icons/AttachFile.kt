package com.example.circularreveal.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AttachFile: ImageVector
	get() {
		if (_AttachFile != null) {
			return _AttachFile!!
		}
		_AttachFile = ImageVector.Builder(
            name = "Attach_file",
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
				moveTo(720f, 630f)
				quadToRelative(0f, 104f, -73f, 177f)
				reflectiveQuadTo(470f, 880f)
				reflectiveQuadToRelative(-177f, -73f)
				reflectiveQuadToRelative(-73f, -177f)
				verticalLineToRelative(-370f)
				quadToRelative(0f, -75f, 52.5f, -127.5f)
				reflectiveQuadTo(400f, 80f)
				reflectiveQuadToRelative(127.5f, 52.5f)
				reflectiveQuadTo(580f, 260f)
				verticalLineToRelative(350f)
				quadToRelative(0f, 46f, -32f, 78f)
				reflectiveQuadToRelative(-78f, 32f)
				reflectiveQuadToRelative(-78f, -32f)
				reflectiveQuadToRelative(-32f, -78f)
				verticalLineToRelative(-370f)
				horizontalLineToRelative(80f)
				verticalLineToRelative(370f)
				quadToRelative(0f, 13f, 8.5f, 21.5f)
				reflectiveQuadTo(470f, 640f)
				reflectiveQuadToRelative(21.5f, -8.5f)
				reflectiveQuadTo(500f, 610f)
				verticalLineToRelative(-350f)
				quadToRelative(-1f, -42f, -29.5f, -71f)
				reflectiveQuadTo(400f, 160f)
				reflectiveQuadToRelative(-71f, 29f)
				reflectiveQuadToRelative(-29f, 71f)
				verticalLineToRelative(370f)
				quadToRelative(-1f, 71f, 49f, 120.5f)
				reflectiveQuadTo(470f, 800f)
				quadToRelative(70f, 0f, 119f, -49.5f)
				reflectiveQuadTo(640f, 630f)
				verticalLineToRelative(-390f)
				horizontalLineToRelative(80f)
				close()
			}
		}.build()
		return _AttachFile!!
	}

private var _AttachFile: ImageVector? = null
