package com.example.diet2.ui.dashboard.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.example.diet2.R;

public class BarChartPlaceholderView extends View {

    private final Paint barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int barCount = 7;

    @ColorInt
    private int barColor;

    public BarChartPlaceholderView(Context context) {
        super(context);
        init(context, null);
    }

    public BarChartPlaceholderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BarChartPlaceholderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        barColor = 0xFF60A5FA; // light blue
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BarChartPlaceholderView);
            try {
                barCount = a.getInt(R.styleable.BarChartPlaceholderView_barCount, barCount);
                barColor = a.getColor(R.styleable.BarChartPlaceholderView_barColor, barColor);
            } finally {
                a.recycle();
            }
        }

        barPaint.setStyle(Paint.Style.FILL);
        barPaint.setColor(barColor);

        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(dpToPx(1f));
        gridPaint.setColor(0x33FFFFFF);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float w = getWidth() - getPaddingLeft() - getPaddingRight();
        float h = getHeight() - getPaddingTop() - getPaddingBottom();
        float left = getPaddingLeft();
        float top = getPaddingTop();

        // Simple grid lines (placeholder)
        for (int i = 1; i <= 3; i++) {
            float y = top + (h * i / 4f);
            canvas.drawLine(left, y, left + w, y, gridPaint);
        }

        if (barCount <= 0) return;

        float gap = dpToPx(6f);
        float totalGap = gap * (barCount - 1);
        float barWidth = (w - totalGap) / barCount;
        barWidth = Math.max(dpToPx(6f), barWidth);

        // Mock bar heights: deterministic wave-like pattern
        for (int i = 0; i < barCount; i++) {
            float t = (float) i / (float) Math.max(1, barCount - 1);
            float factor = 0.35f + 0.55f * (float) Math.sin((t * Math.PI) + (Math.PI / 6f));
            factor = clamp01(factor);

            float barH = h * factor;
            float x = left + (i * (barWidth + gap));
            float y = top + (h - barH);

            float r = dpToPx(8f);
            canvas.drawRoundRect(x, y, x + barWidth, top + h, r, r, barPaint);
        }
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
