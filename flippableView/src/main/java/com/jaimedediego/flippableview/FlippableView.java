package com.jaimedediego.flippableview;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

public class FlippableView extends FrameLayout {

    private View frontFace;
    private View backFace;

    private AnimatorSet inAnimation;
    private AnimatorSet outAnimation;

    private boolean isBackVisible = false;

    public FlippableView(Context context) {
        super(context);
        setClickable(true);
        setFocusable(true);
    }

    public FlippableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(context, attrs);
        setClickable(true);
        setFocusable(true);
    }

    public FlippableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttributes(context, attrs);
        setClickable(true);
        setFocusable(true);
    }

    private void setAttributes(Context context, AttributeSet attrs){
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FlippableView, 0, 0);
        try {
            LayoutInflater inflater = LayoutInflater.from(context);

            backFace = a.getResourceId(R.styleable.FlippableView_backFace, 0)!=0 ?
                    inflater.inflate(a.getResourceId(R.styleable.FlippableView_backFace, 0), this, false) :
                    null;
            frontFace = a.getResourceId(R.styleable.FlippableView_frontFace, 0)!=0 ?
                    inflater.inflate(a.getResourceId(R.styleable.FlippableView_frontFace, 0), this, false) :
                    null;
            inAnimation = a.getResourceId(R.styleable.FlippableView_inAnimation, 0)!=0 ?
                    (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), a.getResourceId(R.styleable.FlippableView_inAnimation, 0)) :
                    null;
            outAnimation = a.getResourceId(R.styleable.FlippableView_outAnimation, 0)!=0 ?
                    (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), a.getResourceId(R.styleable.FlippableView_outAnimation, 0)) :
                    null;

            addView(backFace);
            addView(frontFace);
            changeCameraDistance();

        } finally {
            a.recycle();
        }
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        frontFace.setCameraDistance(scale);
        backFace.setCameraDistance(scale);
    }

    public void setFaces(View frontFace, View backFace) {
        this.frontFace = frontFace;
        this.backFace = backFace;
    }

    public void setFrontFace(View frontFace) {
        this.frontFace = frontFace;
    }

    public void setBackFace(View backFace) {
        this.backFace = backFace;
    }

    public void setAnimations(Integer inAnimationResId, Integer outAnimationResId) {
        inAnimation = inAnimationResId != null ? (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), inAnimationResId) : null;
        outAnimation = outAnimationResId != null ? (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), outAnimationResId) : null;
    }

    public void setAnimations(ObjectAnimator inAnimation, ObjectAnimator outAnimation) {
        this.inAnimation = inAnimation != null ? new AnimatorSet() : null;
        if (this.inAnimation != null) this.inAnimation.play(inAnimation);
        this.outAnimation = outAnimation != null ? new AnimatorSet() : null;
        if (this.outAnimation != null) this.outAnimation.play(outAnimation);
    }

    public void flip() {
        if (!isBackVisible) {
            outAnimation.setTarget(frontFace);
            inAnimation.setTarget(backFace);
            outAnimation.start();
            inAnimation.start();
            isBackVisible = true;
        } else {
            outAnimation.setTarget(backFace);
            inAnimation.setTarget(frontFace);
            outAnimation.start();
            inAnimation.start();
            isBackVisible = false;
        }
    }
}
