package com.example.circularreveal.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Reply: ImageVector
	get() {
		if (_replay != null) {
			return _replay!!
		}
		_replay = ImageVector.Builder(
            name = "Reply",
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
				moveTo(760f, 760f)
				verticalLineToRelative(-160f)
				quadToRelative(0f, -50f, -35f, -85f)
				reflectiveQuadToRelative(-85f, -35f)
				horizontalLineTo(273f)
				lineToRelative(144f, 144f)
				lineToRelative(-57f, 56f)
				lineToRelative(-240f, -240f)
				lineToRelative(240f, -240f)
				lineToRelative(57f, 56f)
				lineToRelative(-144f, 144f)
				horizontalLineToRelative(367f)
				quadToRelative(83f, 0f, 141.5f, 58.5f)
				reflectiveQuadTo(840f, 600f)
				verticalLineToRelative(160f)
				close()
			}
		}.build()
		return _replay!!
	}

private var _replay: ImageVector? = null
