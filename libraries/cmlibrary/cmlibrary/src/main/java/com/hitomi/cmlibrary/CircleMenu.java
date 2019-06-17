package com.hitomi.cmlibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by hitomi on 2016/9/28.
 * <p>
 * github : https://github.com/Hitomis <br/>
 * <p>
 * email : 196425254@qq.com
 */
public class CircleMenu extends View {

    private static final int STATUS_MENU_OPEN = 1;

    private static final int STATUS_MENU_OPENED = 1 << 1;

    private static final int STATUS_MENU_CLOSE = 1 << 2;

    private static final int STATUS_MENU_CLOSE_CLEAR = 1 << 3;

    private static final int STATUS_MENU_CLOSED = 1 << 4;

    private static final int STATUS_MENU_CANCEL = 1 << 5;

    private static final int MAX_SUBMENU_NUM = 10;

    private final int shadowRadius = 3;

    private int partSize;

    private int iconSize;

    private float circleMenuRadius;

    private int itemNum;

    private float itemMenuRadius;

    private float fraction, rFraction;

    private float pathLength;

    private int mainMenuColor;
    private int subMenuColor;

    private Drawable openMenuIcon, closeMenuIcon;

    private List<Integer> subMenuColorList;

    private List<Drawable> subMenuDrawableList;

    private List<RectF> menuRectFList;

    private int centerX, centerY;

    private int clickIndex;

    private int rotateAngle;

    private int itemIconSize;

    private int pressedColor;

    private int status;

    private boolean pressed;

    private Paint oPaint, cPaint, sPaint;

    private PathMeasure pathMeasure;

    private Path path, dstPath;

    private OnMenuSelectedListener onMenuSelectedListener;

    private OnMenuStatusChangeListener onMenuStatusChangeListener;

    public CircleMenu(Context context) {
        this(context, null);
    }

    public CircleMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        status = STATUS_MENU_CLOSED;
        init();
    }

    private void init() {
        initTool();

        mainMenuColor = Color.parseColor("#FFFFFF");

        openMenuIcon = new GradientDrawable();
        closeMenuIcon = new GradientDrawable();

        subMenuColorList = new ArrayList<>();
        subMenuDrawableList = new ArrayList<>();
        menuRectFList = new ArrayList<>();
    }

    private void initTool() {
        oPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        oPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        cPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cPaint.setStyle(Paint.Style.STROKE);
        cPaint.setStrokeCap(Paint.Cap.ROUND);

        sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sPaint.setStyle(Paint.Style.FILL);

        path = new Path();
        dstPath = new Path();
        pathMeasure = new PathMeasure();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int measureWidthSize = width, measureHeightSize = height;

        if (widthMode == MeasureSpec.AT_MOST) {
            measureWidthSize = dip2px(20) * 10;
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            measureHeightSize = dip2px(20) * 10;
        }
        setMeasuredDimension(measureWidthSize, measureHeightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int minSize = Math.min(getMeasuredWidth(), getMeasuredHeight());

        partSize = minSize / 10;
        iconSize = partSize * 5 / 5;
        circleMenuRadius = partSize*7/2 ;

        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;
        resetMainDrawableBounds();

        path.addCircle(centerX, centerY, circleMenuRadius, Path.Direction.CW);
        pathMeasure.setPath(path, true);
        pathLength = pathMeasure.getLength();

        RectF mainMenuRectF = new RectF(centerX - partSize, centerY - partSize, centerX + partSize, centerY + partSize);
        menuRectFList.add(mainMenuRectF);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (status) {
            case STATUS_MENU_CLOSED:
                drawMainMenu(canvas);
                break;
            case STATUS_MENU_OPEN:
                drawMainMenu(canvas);
                drawSubMenu(canvas);
                break;
            case STATUS_MENU_OPENED:
                drawMainMenu(canvas);
                drawSubMenu(canvas);
                break;
            case STATUS_MENU_CLOSE:
                drawMainMenu(canvas);
                drawSubMenu(canvas);
                drawCircleMenu(canvas);
                break;
            case STATUS_MENU_CLOSE_CLEAR:
                drawMainMenu(canvas);
                drawCircleMenu(canvas);
                break;
            case STATUS_MENU_CANCEL:
                drawMainMenu(canvas);
                drawSubMenu(canvas);
                break;
        }
    }

    /**
           * Draw a circular path around the surrounding submenu
           *
           * @param canvas
           */
    private void drawCircleMenu(Canvas canvas) {
        if (status == STATUS_MENU_CLOSE) {
            drawCirclePath(canvas);
            drawCircleIcon(canvas);
        } else {
            cPaint.setStrokeWidth(partSize * 1 + partSize * .5f * fraction);
            cPaint.setColor(calcAlphaColor(getClickMenuColor(), false));
            canvas.drawCircle(centerX, centerY, circleMenuRadius + partSize * .5f * fraction, cPaint);
        }
    }

    private int getClickMenuColor() {
        return clickIndex == 0 ? mainMenuColor : subMenuColorList.get(clickIndex - 1);
    }

    /**
           * Draw the icon when the submenu is rotated
           *
           * @param canvas
           */
    private void drawCircleIcon(Canvas canvas) {
        canvas.save();
        Drawable selDrawable = subMenuDrawableList.get(clickIndex - 1);
        if (selDrawable == null) return;
        int startAngle = (clickIndex - 1) * (360 / itemNum);
        int endAngle = 360 + startAngle;
        int itemX = (int) (centerX + Math.sin(Math.toRadians((endAngle - startAngle) * fraction + startAngle)) * circleMenuRadius);
        int itemY = (int) (centerY - Math.cos(Math.toRadians((endAngle - startAngle) * fraction + startAngle)) * circleMenuRadius);
        canvas.rotate(360 * fraction, itemX, itemY);
        selDrawable.setBounds(itemX - iconSize / 1, itemY - iconSize / 1, itemX + iconSize / 1, itemY + iconSize / 1);
        selDrawable.draw(canvas);
        canvas.restore();
    }

    /**
           * Draw the path of the track when the submenu item is rotated
           *
           * @param canvas
           */
    private void drawCirclePath(Canvas canvas) {
        canvas.save();
        canvas.rotate(rotateAngle, centerX, centerY);
        dstPath.reset();
        dstPath.lineTo(0, 0);
        pathMeasure.getSegment(0, pathLength * fraction, dstPath, true);
        cPaint.setStrokeWidth(partSize * 2);
        cPaint.setColor(getClickMenuColor());
        canvas.drawPath(dstPath, cPaint);
        canvas.restore();
    }

    /**
           * Draw surrounding submenu item buttons
           *
           * @param canvas
           */
    private void drawSubMenu(Canvas canvas) {
        int itemX, itemY, angle;
        final float offsetRadius = .5f;
        RectF menuRectF;
        for (int i = 0; i < itemNum; i++) {
            angle = i * (360 / itemNum);
            if (status == STATUS_MENU_OPEN) {
                itemX = (int) (centerX + Math.sin(Math.toRadians(angle)) * (circleMenuRadius - (1 - fraction) * partSize * offsetRadius));
                itemY = (int) (centerY - Math.cos(Math.toRadians(angle)) * (circleMenuRadius - (1 - fraction) * partSize * offsetRadius));
                oPaint.setColor(calcAlphaColor(subMenuColorList.get(i), false));
                sPaint.setColor(calcAlphaColor(subMenuColorList.get(i), false));
            } else if (status == STATUS_MENU_CANCEL) {
                itemX = (int) (centerX + Math.sin(Math.toRadians(angle)) * (circleMenuRadius - fraction * partSize * offsetRadius));
                itemY = (int) (centerY - Math.cos(Math.toRadians(angle)) * (circleMenuRadius - fraction * partSize * offsetRadius));
                oPaint.setColor(calcAlphaColor(subMenuColorList.get(i), true));
                sPaint.setColor(calcAlphaColor(subMenuColorList.get(i), true));
            } else {
                itemX = (int) (centerX + Math.sin(Math.toRadians(angle)) * circleMenuRadius);
                itemY = (int) (centerY - Math.cos(Math.toRadians(angle)) * circleMenuRadius);
                oPaint.setColor(subMenuColorList.get(i));
                sPaint.setColor(subMenuColorList.get(i));
            }
            if (pressed && clickIndex - 1 == i) {
                oPaint.setColor(pressedColor);
            }
            drawMenuShadow(canvas, itemX, itemY, itemMenuRadius);
            canvas.drawCircle(itemX, itemY, itemMenuRadius, oPaint);
            drawSubMenuIcon(canvas, itemX, itemY, i);
            menuRectF = new RectF(itemX - partSize, itemY - partSize, itemX + partSize, itemY + partSize);
            if (menuRectFList.size() - 1 > i) {
                menuRectFList.remove(i + 1);
            }
            menuRectFList.add(i + 1, menuRectF);
        }
    }

    /**
           * Draw submenu item icons
           *
           * @param canvas
           * @param centerX
           * @param centerY
           * @param index
           */
    private void drawSubMenuIcon(Canvas canvas, int centerX, int centerY, int index) {
        int diff;
        if (status == STATUS_MENU_OPEN || status == STATUS_MENU_CANCEL) {
            diff = itemIconSize/1;
        } else {
            diff = iconSize/1 ;
        }
        resetBoundsAndDrawIcon(canvas, subMenuDrawableList.get(index), centerX, centerY, diff);
    }

    private void resetBoundsAndDrawIcon(Canvas canvas, Drawable drawable, int centerX, int centerY, int diff) {
        if (drawable == null) return;
        drawable.setBounds(centerX - diff, centerY - diff, centerX + diff, centerY + diff);
        drawable.draw(canvas);
    }

    /**
           * Draw the middle menu switch button
           *
           * @param canvas
           */
    private void drawMainMenu(Canvas canvas) {
        float centerMenuRadius, realFraction;
        if (status == STATUS_MENU_CLOSE) {
            // The center main menu button zooms out at twice the speed
            realFraction = (1 - fraction * 2) == 0 ? 0 : (1 - fraction * 2);
            centerMenuRadius = partSize * realFraction;
        } else if (status == STATUS_MENU_CLOSE_CLEAR) {
            // Center main menu button expands at quadruple speed
            realFraction = fraction * 4 >= 1 ? 1 : fraction * 4;
            centerMenuRadius = partSize * realFraction;
        } else if (status == STATUS_MENU_CLOSED || status == STATUS_MENU_CANCEL) {
            centerMenuRadius = partSize;
        } else {
            centerMenuRadius = partSize;
        }
        if (status == STATUS_MENU_OPEN || status == STATUS_MENU_OPENED || status == STATUS_MENU_CLOSE) {
            oPaint.setColor(calcPressedEffectColor(0, 0));
        } else if (pressed && clickIndex == 0) {
            oPaint.setColor(pressedColor);
        } else {
            oPaint.setColor(mainMenuColor);
            sPaint.setColor(mainMenuColor);
        }
        drawMenuShadow(canvas, centerX, centerY, centerMenuRadius);
        canvas.drawCircle(centerX, centerY, centerMenuRadius, oPaint);
        drawMainMenuIcon(canvas);
    }

    private void drawMainMenuIcon(Canvas canvas) {
        canvas.save();
        switch (status) {
            case STATUS_MENU_CLOSED:
                if (openMenuIcon != null)
                    openMenuIcon.draw(canvas);
                break;
            case STATUS_MENU_OPEN:
                canvas.rotate(45 * (fraction - 1), centerX, centerY);
                resetBoundsAndDrawIcon(canvas, closeMenuIcon, centerX, centerY, iconSize*  3/2);
                break;
            case STATUS_MENU_OPENED:
                resetBoundsAndDrawIcon(canvas, closeMenuIcon, centerX, centerY, iconSize * 3/2);
                break;
            case STATUS_MENU_CLOSE:
                resetBoundsAndDrawIcon(canvas, closeMenuIcon, centerX, centerY, itemIconSize *3/2);
                break;
            case STATUS_MENU_CLOSE_CLEAR:
                canvas.rotate(90 * (rFraction - 1), centerX, centerY);
                resetBoundsAndDrawIcon(canvas, openMenuIcon, centerX, centerY, itemIconSize *3/2);
                break;
            case STATUS_MENU_CANCEL:
                canvas.rotate(-45 * fraction, centerX, centerY);
                if (closeMenuIcon != null)
                    closeMenuIcon.draw(canvas);
                break;
        }
        canvas.restore();
    }

    /**
           * Draw menu button shadows
           *
           * @param canvas
           * @param centerX
           * @param centerY
           */
    private void drawMenuShadow(Canvas canvas, int centerX, int centerY, float radius) {
        if (radius + shadowRadius > 0) {
            sPaint.setShader(new RadialGradient(centerX, centerY, radius + shadowRadius,
                    Color.WHITE, Color.TRANSPARENT, Shader.TileMode.MIRROR));
            canvas.drawCircle(centerX, centerY, radius + shadowRadius, sPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (status == STATUS_MENU_CLOSE || status == STATUS_MENU_CLOSE_CLEAR) return true;
        int index = clickWhichRectF(event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressed = true;
                if (index != -1) {
                    clickIndex = index;
                    updatePressEffect(index, pressed);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (index == -1) {
                    pressed = false;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                pressed = false;
                if (index != -1) {
                    clickIndex = index;
                    updatePressEffect(index, pressed);
                }
                if (index == 0) { // click on the button in the middle
                    if (status == STATUS_MENU_CLOSED) {
                        status = STATUS_MENU_OPEN;
                        startOpenMenuAnima();
                    } else if (status == STATUS_MENU_OPENED) {
                        status = STATUS_MENU_CANCEL;
                        startCancelMenuAnima();
                    }
                } else { // click on the surrounding submenu item button
                    if (status == STATUS_MENU_OPENED && index != -1) {
                        status = STATUS_MENU_CLOSE;
                        if (onMenuSelectedListener != null)
                            onMenuSelectedListener.onMenuSelected(index - 1);
                        rotateAngle = clickIndex * (360 / itemNum) - (360 / itemNum) - 90;
                        startCloseMeunAnima();
                    }
                }
                break;
        }
        return true;
    }
    /**
           * Update button status
           *
           * @param menuIndex
           * @param press
           */
    private void updatePressEffect(int menuIndex, boolean press) {
        if (press) {
            pressedColor = calcPressedEffectColor(menuIndex, .15f);
        }
        invalidate();

    }

    /**
           * Get the color the button was pressed
           *
           * @param menuIndex
           * @param depth takes the range [0, 1]. The larger the value, the darker the color
           * @return
           */
    private int calcPressedEffectColor(int menuIndex, float depth) {
        int color = menuIndex == 0 ? mainMenuColor : subMenuColorList.get(menuIndex - 1);
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= (1.f - depth);
        return Color.HSVToColor(hsv);
    }

    /**
           * An animation effect for completing the gradual diffusion of the ring in the View
           * <p>
           * Adjust the alpha value of color according to fraction
           *
           * @param color is adjusted to the color of the alpha value
           * @param reverse true : adjusted from opaque to transparent, otherwise reversed
           * @return
           */
    private int calcAlphaColor(int color, boolean reverse) {
        int alpha;
        if (reverse) { // ???????
            alpha = (int) (255 * (1.f - fraction));
        } else { // ???????
            alpha = (int) (255 * fraction);
        }
        if (alpha >= 255) alpha = 255;
        if (alpha <= 0) alpha = 0;
        return ColorUtils.setAlphaComponent(color, alpha);
    }

    /**
           * Launch open menu animation
           */
    private void startOpenMenuAnima() {
        ValueAnimator openAnima = ValueAnimator.ofFloat(1.f, 100.f);
        openAnima.setDuration(500);
        openAnima.setInterpolator(new OvershootInterpolator());
        openAnima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                fraction = valueAnimator.getAnimatedFraction();
                itemMenuRadius = fraction * partSize;
                itemIconSize = (int) (fraction * iconSize);
                invalidate();
            }
        });
        openAnima.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                status = STATUS_MENU_OPENED;
                if (onMenuStatusChangeListener != null)
                    onMenuStatusChangeListener.onMenuOpened();
            }
        });
        openAnima.start();
    }

    /**
           * Start cancel animation
           */
    private void startCancelMenuAnima() {
        ValueAnimator cancelAnima = ValueAnimator.ofFloat(1.f, 100.f);
        cancelAnima.setDuration(500);
        cancelAnima.setInterpolator(new AnticipateInterpolator());
        cancelAnima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                fraction = valueAnimator.getAnimatedFraction();
                itemMenuRadius = (1 - fraction) * partSize;
                itemIconSize = (int) ((1 - fraction) * iconSize);
                invalidate();
            }
        });
        cancelAnima.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                status = STATUS_MENU_CLOSED;
                if (onMenuStatusChangeListener != null)
                    onMenuStatusChangeListener.onMenuClosed();
            }
        });
        cancelAnima.start();
    }

    /**
           * Turn on the close menu animation </br>
           * <p>Close menu animation is divided into three sections</p>
           * <ur>
           * <li>Select the menu item to turn one week</li>
           * <li>The loop trajectory disappears</li>
           * <li>Main menu button rotation</li>
           * </ur>
           */
    private void startCloseMeunAnima() {
        // Select the menu item to rotate one week to drive the animation
        ValueAnimator aroundAnima = ValueAnimator.ofFloat(1.f, 100.f);
        aroundAnima.setDuration(600);
        aroundAnima.setInterpolator(new AccelerateDecelerateInterpolator());
        aroundAnima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                fraction = valueAnimator.getAnimatedFraction();
                // The center main menu icon shrinks at twice the speed
                float animaFraction = fraction * 2 >= 1 ? 1 : fraction * 2;
                itemIconSize = (int) ((1 - animaFraction) * iconSize);
                invalidate();
            }
        });
        aroundAnima.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                status = STATUS_MENU_CLOSE_CLEAR;
            }
        });

        // Ring track diffusion disappears animation drive
        ValueAnimator spreadAnima = ValueAnimator.ofFloat(1.f, 100.f);
        spreadAnima.setInterpolator(new LinearInterpolator());
        spreadAnima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                fraction = valueAnimator.getAnimatedFraction();
            }
        });

        // Main menu rotates the animation driver
        ValueAnimator rotateAnima = ValueAnimator.ofFloat(1.f, 100.f);
        rotateAnima.setInterpolator(new OvershootInterpolator());
        rotateAnima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                rFraction = valueAnimator.getAnimatedFraction();
                itemIconSize = (int) (rFraction * iconSize);
                invalidate();
            }
        });

        AnimatorSet closeAnimaSet = new AnimatorSet();
        closeAnimaSet.setDuration(500);
        closeAnimaSet.play(spreadAnima).with(rotateAnima);
        closeAnimaSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                status = STATUS_MENU_CLOSED;
                if (onMenuStatusChangeListener != null)
                    onMenuStatusChangeListener.onMenuClosed();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(aroundAnima).before(closeAnimaSet);
        animatorSet.start();
    }

    /**
           * Get which menu button is currently clicked
           * The center menu is subscripted with 0, and the surrounding menu is counted clockwise from the top 1~5
           *
           * @param x
           * @param y
           * @return
           */
    private int clickWhichRectF(float x, float y) {
        int which = -1;
        for (RectF rectF : menuRectFList) {
            if (rectF.contains(x, y)) {
                which = menuRectFList.indexOf(rectF);
                break;
            }
        }
        return which;
    }

    private Drawable convertDrawable(int iconRes) {
        return getResources().getDrawable(iconRes);
    }

    private Drawable convertBitmap(Bitmap bitmap) {
        return new BitmapDrawable(getResources(), bitmap);
    }

    private void resetMainDrawableBounds() {
        openMenuIcon.setBounds(centerX - iconSize *3/2, centerY - iconSize *3/2,
                centerX + iconSize *3/2, centerY + iconSize *3/2);
        closeMenuIcon.setBounds(centerX - iconSize *3/2, centerY - iconSize *3/2,
                centerX + iconSize *3/2, centerY + iconSize *3/2);
    }

    /**
           * Set the background color of the main menu, as well as the icon that is turned on/off
           *
           * @param mainMenuColor main menu background color
           * @param openMenuRes menu open icon, Resource format
           * @param closeMenuRes menu close icon, Resource format
           * @return
           */
    public CircleMenu setMainMenu(int mainMenuColor, int openMenuRes, int closeMenuRes) {
        openMenuIcon = convertDrawable(openMenuRes);
        closeMenuIcon = convertDrawable(closeMenuRes);
        this.mainMenuColor = mainMenuColor;
        return this;
    }

    /**
           * Set the background color of the main menu, as well as the icon that is turned on/off
           *
           * @param mainMenuColor main menu background color
           * @param openMenuBitmap menu open icon, Bitmap format
           * @param closeMenuBitmap menu close icon, Bitmap format
           * @return
           */
    public CircleMenu setMainMenu(int mainMenuColor, Bitmap openMenuBitmap,  Bitmap closeMenuBitmap) {
        openMenuIcon = convertBitmap(openMenuBitmap);
        closeMenuIcon = convertBitmap(closeMenuBitmap);
        this.mainMenuColor = mainMenuColor;

        return this;
    }

    /**
           * Set the background color of the main menu, as well as the icon that is turned on/off
           *
           * @param mainMenuColor main menu background color
           * @param openMenuDrawable menu open icon, Drawable format
           * @param closeMenuDrawable menu close icon, Drawable format
           * @return
           */
    public CircleMenu setMainMenu(int mainMenuColor, Drawable openMenuDrawable, Drawable closeMenuDrawable) {
        openMenuIcon = openMenuDrawable;
        closeMenuIcon = closeMenuDrawable;
        this.mainMenuColor = mainMenuColor;
        return this;
    }

    /**
           * Add a submenu item, including the background color of the submenu and the icon
           *
           * @param menuColor background color of the submenu
           * @param menuRes submenu icon, Resource format
           * @return
           */
    public CircleMenu addSubMenu(int menuColor, int menuRes) {
        if (subMenuColorList.size() < MAX_SUBMENU_NUM && subMenuDrawableList.size() < MAX_SUBMENU_NUM) {
            subMenuColorList.add(menuColor);
            subMenuDrawableList.add(convertDrawable(menuRes));
            itemNum = Math.min(subMenuColorList.size(), subMenuDrawableList.size());
        }
        return this;
    }

    /**
           * Add a submenu item, including the background color of the submenu and the icon
           *
           * @param menuColor background color of the submenu
           * @param menuBitmap submenu icon, Bitmap format
           * @return
           */
    public CircleMenu addSubMenu(int menuColor, Bitmap menuBitmap) {
        if (subMenuColorList.size() < MAX_SUBMENU_NUM && subMenuDrawableList.size() < MAX_SUBMENU_NUM) {
            subMenuColorList.add(menuColor);
            subMenuDrawableList.add(convertBitmap(menuBitmap));
            itemNum = Math.min(subMenuColorList.size(), subMenuDrawableList.size());
        }
        return this;
    }

    /**
           * Add a submenu item, including the background color of the submenu and the icon
           *
           * @param menuColor background color of the submenu
           * @param menuDrawable submenu icon, Drawable format
           * @return
           */
    public CircleMenu addSubMenu(int menuColor, Drawable menuDrawable) {
        if (subMenuColorList.size() < MAX_SUBMENU_NUM && subMenuDrawableList.size() < MAX_SUBMENU_NUM) {
            subMenuColorList.add(menuColor);
            subMenuDrawableList.add(menuDrawable);
            itemNum = Math.min(subMenuColorList.size(), subMenuDrawableList.size());
        }
        return this;
    }

    /**
           * Open the menu
           * Open the CircleMenu
           */
    public void openMenu() {
        if (status == STATUS_MENU_CLOSED) {
            status = STATUS_MENU_OPEN;
            startOpenMenuAnima();
        }
    }

    /**
           * Close menu
           * Close the CircleMenu
           */
    public void closeMenu() {
        if (status == STATUS_MENU_OPENED) {
            status = STATUS_MENU_CANCEL;
            //startCancelMenuAnima();
        }
    }

    /**
           * Is the menu closed?
           * Returns whether the menu is alread open
           *
           * @return
           */
    public boolean isOpened() {
        return status == STATUS_MENU_OPENED;
    }

    public CircleMenu setOnMenuSelectedListener(OnMenuSelectedListener listener) {
        this.onMenuSelectedListener = listener;
        return this;
    }

    public CircleMenu setOnMenuStatusChangeListener(OnMenuStatusChangeListener listener) {
        this.onMenuStatusChangeListener = listener;
        return this;
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
