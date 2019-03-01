package com.jaimedediego.flippableview;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FlippableView extends FrameLayout {

    private final String FLIPPABLEVIEW_TAG = "FlippableView";
    private final int DEFAULT_ANIMATION_DURATION = 1000;
    private final int RESID_NOTFOUND = -1;

    private View frontFace;
    private View backFace;

    private Animation inAnimation;
    private Animation outAnimation;
    private Animator.AnimatorListener avoidClickOnFlipListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            setClickable(false);
            setFocusable(false);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            setClickable(true);
            setFocusable(true);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    private boolean isBackVisible = false;
    private long cameraFieldOfView;
    private long flipDuration = DEFAULT_ANIMATION_DURATION;

    public FlippableView(@NonNull Context context) {
        super(context);
        setClickable(true);
        setFocusable(true);
    }

    public FlippableView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setAttributes(context, attrs);
        setClickable(true);
        setFocusable(true);
    }

    public FlippableView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttributes(context, attrs);
        setClickable(true);
        setFocusable(true);
    }

    private void setAttributes(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FlippableView, 0, 0);
        try {
            backFace = a.getResourceId(R.styleable.FlippableView_backFace, RESID_NOTFOUND) != RESID_NOTFOUND ?
                    inflater.inflate(a.getResourceId(R.styleable.FlippableView_backFace, RESID_NOTFOUND), this, false)
                    : new View(context);

            frontFace = a.getResourceId(R.styleable.FlippableView_frontFace, RESID_NOTFOUND) != RESID_NOTFOUND ?
                    inflater.inflate(a.getResourceId(R.styleable.FlippableView_frontFace, RESID_NOTFOUND), this, false)
                    : new View(context);

            long DEFAULT_CAMERA_DISTANCE = 8000;
            setCameraFieldOfView(a.getInteger(R.styleable.FlippableView_cameraFieldOfView, RESID_NOTFOUND) != RESID_NOTFOUND ?
                    a.getInteger(R.styleable.FlippableView_cameraFieldOfView, RESID_NOTFOUND)
                    : DEFAULT_CAMERA_DISTANCE);

            if (a.getResourceId(R.styleable.FlippableView_inAnimation, RESID_NOTFOUND) != RESID_NOTFOUND ||
                    a.getResourceId(R.styleable.FlippableView_outAnimation, RESID_NOTFOUND) != RESID_NOTFOUND) {
                inAnimation = new Animation(
                        (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), a.getResourceId(R.styleable.FlippableView_inAnimation, RESID_NOTFOUND)),
                        a.getResourceId(R.styleable.FlippableView_inAnimation, RESID_NOTFOUND)
                );
                outAnimation = new Animation(
                        (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), a.getResourceId(R.styleable.FlippableView_outAnimation, RESID_NOTFOUND)),
                        a.getResourceId(R.styleable.FlippableView_outAnimation, RESID_NOTFOUND)
                );
            } else {
                inAnimation = new Animation(
                        (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_in),
                        R.animator.card_flip_in
                );
                outAnimation = new Animation(
                        (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_out),
                        R.animator.card_flip_out
                );
                Log.i(FLIPPABLEVIEW_TAG, "Flip animations not provided. Setted defaults");
            }

            setFlipDuration(a.getInteger(R.styleable.FlippableView_flipDuration, DEFAULT_ANIMATION_DURATION));

            avoidClickOnFlip(a.getBoolean(R.styleable.FlippableView_avoidClickOnFlip, false));

            addView(backFace);
            addView(frontFace);
        } finally {
            a.recycle();
        }
    }

    public boolean isBackVisible() {
        return isBackVisible;
    }

    public boolean isFrontVisible() {
        return !isBackVisible;
    }

    public long getCameraFieldOfView() {
        return cameraFieldOfView;
    }

    public void setCameraFieldOfView(long cameraFieldOfView) {
        float scale = getResources().getDisplayMetrics().density * cameraFieldOfView;
        frontFace.setCameraDistance(scale);
        backFace.setCameraDistance(scale);
        this.cameraFieldOfView = cameraFieldOfView;
    }

    public void setFaces(View frontFace, View backFace) {
        this.frontFace = frontFace;
        this.backFace = backFace;
    }

    public View getFrontFace() {
        return frontFace;
    }

    public void setFrontFace(View frontFace) {
        this.frontFace = frontFace;
    }

    public View getBackFace() {
        return backFace;
    }

    public void setBackFace(View backFace) {
        this.backFace = backFace;
    }

    public long getFlipDuration() {
        return flipDuration;
    }

    public void setFlipDuration(long duration) {
        if (inAnimation.getAnimationResId() == null || inAnimation.getAnimationResId() != R.animator.card_flip_in ||
                outAnimation.getAnimationResId() == null || outAnimation.getAnimationResId() != R.animator.card_flip_out) {
            Log.e(FLIPPABLEVIEW_TAG, "setFlipDuration() -> can only be called if all animations are by default");
            throw new RuntimeException("setFlipDuration() -> can only be called if all animations are by default");
        } else {
            for (Animator animation : inAnimation.getAnimation().getChildAnimations()) {
                if (animation.getDuration() == this.flipDuration) animation.setDuration(duration);
                else if (animation.getStartDelay() == this.flipDuration / 2)
                    animation.setStartDelay(duration / 2);
            }
            for (Animator animation : outAnimation.getAnimation().getChildAnimations()) {
                if (animation.getDuration() == this.flipDuration) animation.setDuration(duration);
                else if (animation.getStartDelay() == this.flipDuration / 2)
                    animation.setStartDelay(duration / 2);
            }
            flipDuration = duration;
        }
    }

    public AnimatorSet getInAnimation() {
        return inAnimation.getAnimation();
    }

    public void setInAnimation(AnimatorSet inAnimation) {
        this.inAnimation.setAnimation(inAnimation);
    }

    public void setInAnimation(Integer inAnimationResId) {
        inAnimation = inAnimationResId != null ?
                new Animation(
                        (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), inAnimationResId),
                        inAnimationResId
                )
                : null;
    }

    public AnimatorSet getOutAnimation() {
        return outAnimation.getAnimation();
    }

    public void setOutAnimation(AnimatorSet outAnimation) {
        this.outAnimation.setAnimation(outAnimation);
    }

    public void setOutAnimation(Integer outAnimationResId) {
        outAnimation = outAnimationResId != null ?
                new Animation(
                        (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), outAnimationResId),
                        outAnimationResId
                )
                : null;
    }

    public void setAnimations(AnimatorSet inAnimation, AnimatorSet outAnimation) {
        this.inAnimation.setAnimation(inAnimation);
        this.outAnimation.setAnimation(outAnimation);
    }

    public void setAnimations(Integer inAnimationResId, Integer outAnimationResId) {
        inAnimation = inAnimationResId != null ?
                new Animation(
                        (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), inAnimationResId),
                        inAnimationResId
                )
                : null;

        outAnimation = outAnimationResId != null ?
                new Animation(
                        (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), outAnimationResId),
                        outAnimationResId
                )
                : null;
    }

    public void flip() {
        if (inAnimation != null && outAnimation != null) {
            if (!isBackVisible) {
                outAnimation.getAnimation().setTarget(frontFace);
                inAnimation.getAnimation().setTarget(backFace);
                outAnimation.getAnimation().start();
                inAnimation.getAnimation().start();
                isBackVisible = true;
            } else {
                outAnimation.getAnimation().setTarget(backFace);
                inAnimation.getAnimation().setTarget(frontFace);
                outAnimation.getAnimation().start();
                inAnimation.getAnimation().start();
                isBackVisible = false;
            }
        } else {
            throw new RuntimeException("flip() -> all animations must be setted in order to make a flip");
        }
    }

    public void avoidClickOnFlip(boolean avoid){
        if (avoid){
            inAnimation.getAnimation().addListener(avoidClickOnFlipListener);
            outAnimation.getAnimation().addListener(avoidClickOnFlipListener);
        } else {
            inAnimation.getAnimation().removeListener(avoidClickOnFlipListener);
            outAnimation.getAnimation().removeListener(avoidClickOnFlipListener);
        }
    }
}
