package com.nptel.courses.online;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import com.squareup.picasso.Picasso;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class SmartImageView extends AppCompatImageView {

    private static final int DEF_PRESS_HIGHLIGHT_COLOR = 0x32000000;
    private Shader bitmapShader;
    private Matrix bitmapMatrix;
    private RectF bitmapDrawBounds;
    private RectF strokeBounds;
    private Bitmap bitmap;
    private Paint bitmapPaint;
    private Paint strokePaint;
    private Paint pressedPaint;
    private boolean initialized;
    private boolean pressed;
    private boolean highlightEnable;
    private String imageUrl;
    private int imagePlaceholder;
    @Shape
    private int shape;

    public SmartImageView(Context context) {
        this(context, null);
    }

    public SmartImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int strokeColor = Color.TRANSPARENT;
        float strokeWidth = 0;
        int highlightColor = DEF_PRESS_HIGHLIGHT_COLOR;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SmartImageView, 0, 0);

            strokeColor = a.getColor(R.styleable.SmartImageView_strokeColor, Color.TRANSPARENT);
            strokeWidth = a.getDimensionPixelSize(R.styleable.SmartImageView_strokeWidth, 0);
            highlightEnable = a.getBoolean(R.styleable.SmartImageView_highlightEnabled, true);
            highlightColor = a.getColor(R.styleable.SmartImageView_highlightColor, DEF_PRESS_HIGHLIGHT_COLOR);
            imageUrl = a.getString(R.styleable.SmartImageView_imageUrl);
            imagePlaceholder = a.getResourceId(R.styleable.SmartImageView_imagePlaceholder, 0);
            shape = a.getInt(R.styleable.SmartImageView_shape, Shape.RECTANGLE);
            a.recycle();
        }
        bitmapMatrix = new Matrix();
        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokeBounds = new RectF();
        bitmapDrawBounds = new RectF();
        strokePaint.setColor(strokeColor);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(strokeWidth);

        pressedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pressedPaint.setColor(highlightColor);
        pressedPaint.setStyle(Paint.Style.FILL);

        initialized = true;
        setImageResource(imagePlaceholder);
        if (!isInEditMode())
            if (imagePlaceholder == 0)
                Picasso.get().load(imageUrl).into(this);
            else Picasso.get().load(imageUrl).placeholder(imagePlaceholder).into(this);
        setupBitmap();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        setupBitmap();
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        setupBitmap();
    }

    @Override
    public void setImageBitmap(@Nullable Bitmap bm) {
        super.setImageBitmap(bm);
        setupBitmap();
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        super.setImageURI(uri);
        Picasso.get().load(uri).into(this);
        setupBitmap();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        if (imagePlaceholder == 0)
            Picasso.get().load(imageUrl).into(this);
        else Picasso.get().load(imageUrl).placeholder(imagePlaceholder).into(this);
    }

    @Override
    protected void onSizeChanged(int currentWidth, int currentHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(currentWidth, currentHeight, oldWidth, oldHeight);
        float halfStrokeWidth = strokePaint.getStrokeWidth() / 2f;
        updateBitmapDrawBounds();
        strokeBounds.set(bitmapDrawBounds);
        strokeBounds.inset(halfStrokeWidth, halfStrokeWidth);
        updateBitmapSize();
        setOutlineProvider(new CircleImageViewOutlineProvider(strokeBounds));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBitmap(canvas);
        drawStroke(canvas);
        drawHighlight(canvas);
    }

    public boolean isHighlightEnable() {
        return highlightEnable;
    }

    public void setHighlightEnable(boolean enable) {
        highlightEnable = enable;
        invalidate();
    }

    @ColorInt
    public int getHighlightColor() {
        return pressedPaint.getColor();
    }

    public void setHighlightColor(@ColorInt int color) {
        pressedPaint.setColor(color);
        invalidate();
    }

    @ColorInt
    public int getStrokeColor() {
        return strokePaint.getColor();
    }

    public void setStrokeColor(@ColorInt int color) {
        strokePaint.setColor(color);
        invalidate();
    }

    @Dimension
    public float getStrokeWidth() {
        return strokePaint.getStrokeWidth();
    }

    public void setStrokeWidth(@Dimension float width) {
        strokePaint.setStrokeWidth(width);
        invalidate();
    }

    protected void drawHighlight(Canvas canvas) {
        if (highlightEnable && pressed) {
            canvas.drawOval(bitmapDrawBounds, pressedPaint);
        }
    }

    protected void drawStroke(Canvas canvas) {
        if (strokePaint.getStrokeWidth() > 0f) {
            if (shape == Shape.CIRCLE)
                canvas.drawOval(strokeBounds, strokePaint);
            else canvas.drawRect(strokeBounds, strokePaint);
        }
    }

    protected void drawBitmap(Canvas canvas) {
        if (shape == Shape.CIRCLE)
            canvas.drawOval(bitmapDrawBounds, bitmapPaint);
        else
            canvas.drawRect(bitmapDrawBounds, bitmapPaint);
    }

    protected void updateBitmapDrawBounds() {
        float contentWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        float contentHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        float left = getPaddingLeft();
        float top = getPaddingTop();
        float right = left + contentWidth;
        float bottom = top + contentHeight;
        if (shape == Shape.SQUARE || shape == Shape.CIRCLE) {
            if (contentWidth > contentHeight) left += (contentWidth - contentHeight) / 2f;
            else top += (contentHeight - contentWidth) / 2f;
            float diameter = Math.min(contentWidth, contentHeight);
            right = left + diameter;
            bottom = top + diameter;
        }

        bitmapDrawBounds.set(left, top, right, bottom);
    }

    private void setupBitmap() {
        if (!initialized) {
            return;
        }
        bitmap = getBitmapFromDrawable(getDrawable());
        if (bitmap == null) {
            return;
        }

        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        bitmapPaint.setShader(bitmapShader);
        updateBitmapSize();
    }

    private void updateBitmapSize() {
        if (bitmap == null) return;

        float dx;
        float dy;
        float scale;

        // scale up/down with respect to this view size and maintain aspect ratio
        // translate bitmap position with dx/dy to the center of the image
        if (bitmap.getWidth() < bitmap.getHeight()) {
            scale = bitmapDrawBounds.width() / (float) bitmap.getWidth();
            dx = bitmapDrawBounds.left;
            dy = bitmapDrawBounds.top - (bitmap.getHeight() * scale / 2f) + (bitmapDrawBounds.width() / 2f);
        } else {
            scale = bitmapDrawBounds.height() / (float) bitmap.getHeight();
            dx = bitmapDrawBounds.left - (bitmap.getWidth() * scale / 2f) + (bitmapDrawBounds.width() / 2f);
            dy = bitmapDrawBounds.top;
        }
        bitmapMatrix.setScale(scale, scale);
        bitmapMatrix.postTranslate(dx, dy);
        bitmapShader.setLocalMatrix(bitmapMatrix);
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private boolean isInCircle(float x, float y) {
        // find the distance between center of the view and x,y point
        double distance = Math.sqrt(
                Math.pow(bitmapDrawBounds.centerX() - x, 2) + Math.pow(bitmapDrawBounds.centerY() - y, 2)
        );
        return distance <= (bitmapDrawBounds.width() / 2);
    }

    public void setShape(@Shape int shape) {
        this.shape = shape;
    }

    @IntDef(value = {Shape.SQUARE, Shape.RECTANGLE, Shape.CIRCLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Shape {
        int SQUARE = 1;
        int RECTANGLE = 2;
        int CIRCLE = 3;
    }

    @IntDef(value = {Mode.IMAGE, Mode.INITIAL})
    @Retention(RetentionPolicy.SOURCE)
    @interface Mode {
        int IMAGE = 1;
        int INITIAL = 2;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public class CircleImageViewOutlineProvider extends ViewOutlineProvider {

        private Rect mRect;

        CircleImageViewOutlineProvider(RectF rect) {
            mRect = new Rect(
                    (int) rect.left,
                    (int) rect.top,
                    (int) rect.right,
                    (int) rect.bottom
            );
        }

        @Override
        public void getOutline(View view, @NonNull Outline outline) {
            outline.setOval(mRect);
        }

    }
}