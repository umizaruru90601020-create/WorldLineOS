package com.worldline.os.causal;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.worldline.os.core.model.Correction;
import com.worldline.os.core.model.Worldline;

import java.util.List;
import java.util.ArrayList;

public class WorldlineCausalView extends View {

    private Worldline worldline;

    private Paint nodePaint;
    private Paint textPaint;
    private Paint linePaint;

    private float animProgress = 0f;

    private static class Node {
        String label;
        float x, y;
        float width, height;
        int color;
        float scale = 0.6f;
        float alpha = 0f;
    }

    private ArrayList<Node> nodes = new ArrayList<>();

    public WorldlineCausalView(Context ctx) {
        super(ctx);
        init();
    }

    public WorldlineCausalView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        init();
    }

    public WorldlineCausalView(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);
        init();
    }

    private void init() {
        nodePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nodePaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(32f);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.DKGRAY);
        linePaint.setStrokeWidth(6f);
        linePaint.setStyle(Paint.Style.STROKE);
    }

    public void setWorldline(Worldline wl) {
        this.worldline = wl;
        startAnimation();
    }

    private void startAnimation() {
        animProgress = 0f;

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(1400);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(a -> {
            animProgress = (float) a.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (worldline == null) return;

        nodes.clear();

        float startX = 150f;
        float startY = 150f;
        float gapX = 350f;
        float gapY = 250f;

        float x = startX;
        float y = startY;

        Node physical = makeNode("展示ST", x, y, Color.parseColor("#3A7AFE"));
        nodes.add(physical);

        x += gapX;
        Node attack = makeNode("攻撃可能性", x, y, Color.parseColor("#AA44FF"));
        nodes.add(attack);

        // 修正①：List に変更
        List<Correction> logs = worldline.logs;

        float logX = x + gapX;
        float logY = y;

        int count = 0;
        for (Correction c : logs) {

            // 修正②：float キャスト
            float sizeFactor = 1f + (float) Math.min(Math.abs(c.value) / 5f, 1.5f);

            Node logNode = makeNode(
                    c.name,
                    logX,
                    logY,
                    Color.parseColor("#FF8800"),
                    sizeFactor
            );

            nodes.add(logNode);

            count++;
            logX += gapX;

            if (count % 3 == 0) {
                logX = x + gapX;
                logY += gapY;
            }
        }

        logY += gapY;
        Node typeNode = makeNode(
                worldline.type,
                startX + gapX * 3,
                logY,
                Color.parseColor("#FF4444")
        );
        nodes.add(typeNode);

        StringBuilder orderStr = new StringBuilder();
        for (int i = 0; i < worldline.order.size(); i++) {
            orderStr.append(worldline.order.get(i));
            if (i < worldline.order.size() - 1) orderStr.append("-");
        }

        Node orderNode = makeNode(
                orderStr.toString(),
                startX + gapX * 4,
                logY,
                Color.parseColor("#00AA55")
        );
        nodes.add(orderNode);

        for (Node n : nodes) {
            drawNode(canvas, n);
        }

        for (int i = 0; i < nodes.size() - 1; i++) {
            drawArrow(canvas, nodes.get(i), nodes.get(i + 1));
        }
    }

    private Node makeNode(String label, float x, float y, int color) {
        return makeNode(label, x, y, color, 1f);
    }

    private Node makeNode(String label, float x, float y, int color, float scale) {
        Node n = new Node();
        n.label = label;
        n.x = x;
        n.y = y;
        n.color = color;

        float baseW = 200f * scale;
        float baseH = 100f * scale;

        n.width = baseW;
        n.height = baseH;

        return n;
    }

    private void drawNode(Canvas canvas, Node n) {

        float scale = 0.6f + 0.4f * animProgress;
        float alpha = animProgress;

        nodePaint.setColor(n.color);
        nodePaint.setAlpha((int) (255 * alpha));

        float w = n.width * scale;
        float h = n.height * scale;

        float left = n.x - w / 2;
        float top = n.y - h / 2;
        float right = n.x + w / 2;
        float bottom = n.y + h / 2;

        canvas.drawRoundRect(left, top, right, bottom, 30f, 30f, nodePaint);

        textPaint.setAlpha((int) (255 * alpha));
        float textWidth = textPaint.measureText(n.label);
        float textX = n.x - textWidth / 2;
        float textY = n.y + (textPaint.getTextSize() / 3);

        canvas.drawText(n.label, textX, textY, textPaint);
    }

    private void drawArrow(Canvas canvas, Node from, Node to) {

        float startX = from.x + from.width / 2;
        float startY = from.y;

        float endX = to.x - to.width / 2;
        float endY = to.y;

        float controlX = (startX + endX) / 2;
        float controlY = startY - 120;

        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo(controlX, controlY, endX, endY);

        Path partial = new Path();
        pathMeasure.setPath(path, false);
        float length = pathMeasure.getLength();
        float stop = length * animProgress;

        pathMeasure.getSegment(0, stop, partial, true);

        canvas.drawPath(partial, linePaint);
    }

    private android.graphics.PathMeasure pathMeasure = new android.graphics.PathMeasure();
}
