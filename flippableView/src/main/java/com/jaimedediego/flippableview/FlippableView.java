package com.jaimedediego.flippableview;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class FlippableView extends FrameLayout {

    private final String FLIPPABLEVIEW_TAG = "FlippableView";
    private final int RESID_NOTFOUND = -1;
    private final long DEFAULT_ANIMATION_DURATION = 1000;
    private final long DEFAULT_CAMERA_DISTANCE = 8000;

    private View frontFace;
    private View backFace;

    private Animation inAnimation;
    private Animation outAnimation;

    private boolean isBackVisible = false;
    private long duration;

    private long cameraDistance;

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

            backFace = a.getResourceId(R.styleable.FlippableView_backFace, RESID_NOTFOUND)!=RESID_NOTFOUND ?
                    inflater.inflate(a.getResourceId(R.styleable.FlippableView_backFace, RESID_NOTFOUND), this, false)
                    : null;

            frontFace = a.getResourceId(R.styleable.FlippableView_frontFace, RESID_NOTFOUND)!=RESID_NOTFOUND ?
                    inflater.inflate(a.getResourceId(R.styleable.FlippableView_frontFace, RESID_NOTFOUND), this, false)
                    : null;

            setCameraDistance(a.getInteger(R.styleable.FlippableView_cameraDistance, RESID_NOTFOUND)!=RESID_NOTFOUND ?
                    a.getInteger(R.styleable.FlippableView_cameraDistance, RESID_NOTFOUND)
                    : DEFAULT_CAMERA_DISTANCE);

            if(a.getResourceId(R.styleable.FlippableView_inAnimation, RESID_NOTFOUND)!=RESID_NOTFOUND){
                inAnimation = new Animation(
                        (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), a.getResourceId(R.styleable.FlippableView_inAnimation, RESID_NOTFOUND)),
                        a.getResourceId(R.styleable.FlippableView_inAnimation, RESID_NOTFOUND)
                );
            } else {
                inAnimation = new Animation(
                        (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_in),
                        R.animator.card_flip_in
                );
                duration = DEFAULT_ANIMATION_DURATION;
            }

            if(a.getResourceId(R.styleable.FlippableView_inAnimation, RESID_NOTFOUND)!=RESID_NOTFOUND){
                outAnimation = new Animation(
                        (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), a.getResourceId(R.styleable.FlippableView_outAnimation, RESID_NOTFOUND)),
                        a.getResourceId(R.styleable.FlippableView_outAnimation, RESID_NOTFOUND)
                );
            } else {
                outAnimation = new Animation(
                        (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_out),
                        R.animator.card_flip_out
                );
                duration = DEFAULT_ANIMATION_DURATION;
            }

            addView(backFace);
            addView(frontFace);
        } finally {
            a.recycle();
        }
    }

    public void setCameraDistance(long cameraDistance) {
        this.cameraDistance = cameraDistance;
        float scale = getResources().getDisplayMetrics().density * cameraDistance;
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

    public void setAnimations(AnimatorSet inAnimation, AnimatorSet outAnimation) {
        this.inAnimation.setAnimation(inAnimation);
        this.outAnimation.setAnimation(outAnimation);
    }

    public void setFlipDuration(long duration){
        if(inAnimation.getAnimationResId()==null || inAnimation.getAnimationResId()!=R.animator.card_flip_in ||
                outAnimation.getAnimationResId()==null || outAnimation.getAnimationResId()!=R.animator.card_flip_out){
            Log.e(FLIPPABLEVIEW_TAG, "setFlipDuration -> can only be called if animations are by default");
        } else {
            for(Animator animation : inAnimation.getAnimation().getChildAnimations()){
                if(animation.getDuration()==this.duration) animation.setDuration(duration);
                else if(animation.getStartDelay()==this.duration/2) animation.setStartDelay(duration/2);
            }
            for(Animator animation : outAnimation.getAnimation().getChildAnimations()){
                if(animation.getDuration()==this.duration) animation.setDuration(duration);
                else if(animation.getStartDelay()==this.duration/2) animation.setStartDelay(duration/2);
            }
            this.duration = duration;
        }
    }

    public void flip() {
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
    }
}
