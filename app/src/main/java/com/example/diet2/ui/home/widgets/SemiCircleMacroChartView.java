package com.example.diet2.ui.home.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.example.diet2.R;

public class SemiCircleMacroChartView extends View {

    private final Paint basePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint segmentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF arcBounds = new RectF();

    private float strokeWidthPx;

    private float proteinFraction;
    private float carbsFraction;
    private float fatFraction;

    @ColorInt
    private int baseColor;
    @ColorInt
    private int proteinColor;
    @ColorInt
    private int carbsColor;
    @ColorInt
    private int fatColor;

    public SemiCircleMacroChartView(Context context) {
        super(context);
        init(context, null);
    }

    public SemiCircleMacroChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SemiCircleMacroChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        float defaultStroke = dpToPx(18f);

        // Default placeholder values (UI only)
        proteinFraction = 0.35f;
        carbsFraction = 0.45f;
        fatFraction = 0.20f;

        baseColor = 0x26FFFFFF; // translucent white
        proteinColor = 0xFF22C55E; // green
        carbsColor = 0xFF3B82F6; // blue
        fatColor = 0xFFF97316; // orange

        strokeWidthPx = defaultStroke;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SemiCircleMacroChartView);
            try {
                strokeWidthPx = a.getDimension(R.styleable.SemiCircleMacroChartView_strokeWidth, defaultStroke);
                proteinFraction = a.getFloat(R.styleable.SemiCircleMacroChartView_proteinFraction, proteinFraction);
                carbsFraction = a.getFloat(R.styleable.SemiCircleMacroChartView_carbsFraction, carbsFraction);
                fatFraction = a.getFloat(R.styleable.SemiCircleMacroChartView_fatFraction, fatFraction);

                baseColor = a.getColor(R.styleable.SemiCircleMacroChartView_baseColor, baseColor);
                proteinColor = a.getColor(R.styleable.SemiCircleMacroChartView_proteinColor, proteinColor);
                carbsColor = a.getColor(R.styleable.SemiCircleMacroChartView_carbsColor, carbsColor);
                fatColor = a.getColor(R.styleable.SemiCircleMacroChartView_fatColor, fatColor);
            } finally {
                a.recycle();
            }
        }

        basePaint.setStyle(Paint.Style.STROKE);
        basePaint.setStrokeCap(Paint.Cap.ROUND);
        basePaint.setStrokeWidth(strokeWidthPx);
        basePaint.setColor(baseColor);

        segmentPaint.setStyle(Paint.Style.STROKE);
        segmentPaint.setStrokeCap(Paint.Cap.ROUND);
        segmentPaint.setStrokeWidth(strokeWidthPx);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float halfStroke = strokeWidthPx / 2f;
        float left = getPaddingLeft() + halfStroke;
        float top = getPaddingTop() + halfStroke;
        float right = w - getPaddingRight() - halfStroke;
        float bottom = h - getPaddingBottom() - halfStroke;

        // Use a circle that fits within the view; we draw only the top half.
        arcBounds.set(left, top, right, bottom * 2f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Base arc (top half): start at 180° and sweep -180° (counterclockwise) to draw the upper semicircle.
        canvas.drawArc(arcBounds, 180f, -180f, false, basePaint);

        float p = clamp01(proteinFraction);
        float c = clamp01(carbsFraction);
        float f = clamp01(fatFraction);

        float total = p + c + f;
        if (total <= 0f) return;

        // Normalize so the segments fill the semicircle.
        p /= total;
        c /= total;
        f /= total;

        float startAngle = 180f;

        segmentPaint.setColor(proteinColor);
        float pSweep = -180f * p;
        canvas.drawArc(arcBounds, startAngle, pSweep, false, segmentPaint);
        startAngle += pSweep;

        segmentPaint.setColor(carbsColor);
        float cSweep = -180f * c;
        canvas.drawArc(arcBounds, startAngle, cSweep, false, segmentPaint);
        startAngle += cSweep;

        segmentPaint.setColor(fatColor);
        float fSweep = -180f * f;
        canvas.drawArc(arcBounds, startAngle, fSweep, false, segmentPaint);
    }

    private float dpToPx(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }

    private static float clamp01(float v) {
        if (v < 0f) return 0f;
        if (v > 1f) return 1f;
        return v;
    }
}
