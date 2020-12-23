package com.test.runar.CustomView

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Matrix
import android.graphics.RectF
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.StyleableRes
import androidx.appcompat.widget.AppCompatImageView
import com.test.runar.R


/**
 * [android.widget.ImageView] that supports directional cropping in both vertical and
 * horizontal directions instead of being restricted to center-crop. Automatically sets [ ] to MATRIX and defaults to center-crop.
 *
 * XML attributes (for offsets either a float or a fraction is allowed in values, e. g. 50% or 0.5):
 * - app:verticalCropOffset
 * - app:horizontalCropOffset
 * - app:verticalFitOffset
 * - app:horizontalFitOffset
 * - app:offsetScaleType
 *
 * The `app:offsetScaleType` accepts one of the enum values:
 * - crop: the same behavior as in the original answer, i. e. the image is scaled so that both dimensions of the image will be equal to or larger than the corresponding dimension of the view; `app:horizontalCropOffset` and `app:verticalCropOffset` are then applied
 * - fitInside: image is scaled so that both dimensions of the image will be equal to or less than the corresponding dimension of the view; `app:horizontalFitOffset` and `app:verticalFitOffset` are then applied
 * - fitX: image is scaled so that its X dimension is equal to the view's X dimension. Y dimension is scaled so that the ratio is preserved. If image's Y dimension is larger than view's dimension, `app:verticalCropOffset` is applied, otherwise `app:verticalFitOffset` is applied
 * - fitY: image is scaled so that its Y dimension is equal to the view's Y dimension. X dimension is scaled so that the ratio is preserved. If image's X dimension is larger than view's dimension, `app:horizontalCropOffset` is applied, otherwise `app:horizontalFitOffset` is applied
 */
class OffsetImageView(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : AppCompatImageView(context, attrs, defStyleAttr) {
    companion object {
        private const val DEFAULT_HORIZONTAL_OFFSET = 0.5f
        private const val DEFAULT_VERTICAL_OFFSET = 0.5f
    }

    enum class OffsetScaleType(val code: Int) {
        CROP(0), FIT_INSIDE(1), FIT_X(2), FIT_Y(3)
    }

    private var mHorizontalCropOffsetPercent = DEFAULT_HORIZONTAL_OFFSET
    private var mHorizontalFitOffsetPercent = DEFAULT_HORIZONTAL_OFFSET
    private var mVerticalCropOffsetPercent = DEFAULT_VERTICAL_OFFSET
    private var mVerticalFitOffsetPercent = DEFAULT_VERTICAL_OFFSET
    private var mOffsetScaleType = OffsetScaleType.CROP

    init {
        scaleType = ScaleType.MATRIX
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs,
                R.styleable.OffsetImageView, defStyleAttr, 0)

            readAttrFloatValueIfSet(a, R.styleable.OffsetImageView_verticalCropOffset)?.let {
                mVerticalCropOffsetPercent = it
            }
            readAttrFloatValueIfSet(a, R.styleable.OffsetImageView_horizontalCropOffset)?.let {
                mHorizontalCropOffsetPercent = it
            }
            readAttrFloatValueIfSet(a, R.styleable.OffsetImageView_verticalFitOffset)?.let {
                mVerticalFitOffsetPercent = it
            }
            readAttrFloatValueIfSet(a, R.styleable.OffsetImageView_horizontalFitOffset)?.let {
                mHorizontalFitOffsetPercent = it
            }
            with (a) {
                if (hasValue(R.styleable.OffsetImageView_offsetScaleType)) {
                    val code = getInt(R.styleable.OffsetImageView_offsetScaleType, -1)
                    if (code != -1) {
                        OffsetScaleType.values().find {
                            it.code == code
                        }?.let {
                            mOffsetScaleType = it
                        }
                    }
                }
            }

            a.recycle()
        }
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        applyOffset()
    }

    private fun readAttrFloatValueIfSet(typedArray: TypedArray, @StyleableRes index: Int): Float? {
        try {
            with(typedArray) {
                if (!hasValue(index)) return null
                var value = getFloat(index, -1f)
                if (value >= 0) return value

                value = getFraction(index, 1, 1, -1f)
                if (value >= 0) return value

                return null
            }
        } catch (e: RuntimeException) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Sets the crop box offset by the specified percentage values. For example, a center-crop would
     * be (0.5, 0.5), a top-left crop would be (0, 0), and a bottom-center crop would be (0.5, 1)
     */
    fun setOffsets(horizontalCropOffsetPercent: Float,
                   verticalCropOffsetPercent: Float,
                   horizontalFitOffsetPercent: Float,
                   verticalFitOffsetPercent: Float,
                   scaleType: OffsetScaleType
    ) {
        require(!(mHorizontalCropOffsetPercent < 0
                || mVerticalCropOffsetPercent < 0
                || mHorizontalFitOffsetPercent < 0
                || mVerticalFitOffsetPercent < 0
                || mHorizontalCropOffsetPercent > 1
                || mVerticalCropOffsetPercent > 1
                || mHorizontalFitOffsetPercent > 1
                || mVerticalFitOffsetPercent > 1)) { "Offset values must be a float between 0.0 and 1.0" }
        mHorizontalCropOffsetPercent = horizontalCropOffsetPercent
        mVerticalCropOffsetPercent = verticalCropOffsetPercent
        mHorizontalFitOffsetPercent = horizontalFitOffsetPercent
        mVerticalFitOffsetPercent = verticalFitOffsetPercent
        mOffsetScaleType = scaleType
        applyOffset()
    }

    private fun applyOffset() {
        val matrix: Matrix = imageMatrix
        val scale: Float
        val viewWidth: Int = width - paddingLeft - paddingRight
        val viewHeight: Int = height - paddingTop - paddingBottom
        val drawable = drawable
        val drawableWidth: Int
        val drawableHeight: Int

        if (drawable == null) {
            drawableWidth = 0
            drawableHeight = 0
        } else {
            // Allow for setting the drawable later in code by guarding ourselves here.
            drawableWidth = drawable.intrinsicWidth
            drawableHeight = drawable.intrinsicHeight
        }

        val scaleHeight = when (mOffsetScaleType) {
            OffsetScaleType.CROP -> drawableWidth * viewHeight > drawableHeight * viewWidth // If drawable is flatter than view, scale it to fill the view height.
            OffsetScaleType.FIT_INSIDE -> drawableWidth * viewHeight < drawableHeight * viewWidth // If drawable is is taller than view, scale according to height to fit inside.
            OffsetScaleType.FIT_X -> false // User wants to fit X axis -> scale according to width
            OffsetScaleType.FIT_Y -> true // User wants to fit Y axis -> scale according to height
        }
        // Get the scale.
        scale = if (scaleHeight) {
            viewHeight.toFloat() / drawableHeight.toFloat()
        } else {
            viewWidth.toFloat() / drawableWidth.toFloat()
        }
        val viewToDrawableWidth = viewWidth / scale
        val viewToDrawableHeight = viewHeight / scale

        if (drawableWidth >= viewToDrawableWidth && drawableHeight >= viewToDrawableHeight) {
            val xOffset = mHorizontalCropOffsetPercent * (drawableWidth - viewToDrawableWidth)
            val yOffset = mVerticalCropOffsetPercent * (drawableHeight - viewToDrawableHeight)

            // Define the rect from which to take the image portion.
            val drawableRect = RectF(
                xOffset,
                yOffset,
                xOffset + viewToDrawableWidth,
                yOffset + viewToDrawableHeight)
            val viewRect = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
            matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.FILL)
        } else {
            val xOffset = mHorizontalFitOffsetPercent * (viewToDrawableWidth - drawableWidth) * scale
            val yOffset = mVerticalFitOffsetPercent * (viewToDrawableHeight - drawableHeight) * scale

            val drawableRect = RectF(
                0f,
                0f,
                drawableWidth.toFloat(),
                drawableHeight.toFloat())
            val viewRect = RectF(xOffset, yOffset, xOffset + drawableWidth * scale, yOffset + drawableHeight * scale)
            matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.FILL)
        }
        imageMatrix = matrix
    }
}