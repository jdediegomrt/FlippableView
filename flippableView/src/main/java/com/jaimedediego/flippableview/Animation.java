package com.jaimedediego.flippableview;

import android.animation.AnimatorSet;

public class Animation {

    private AnimatorSet animation;
    private Integer animationResId;

    Animation(AnimatorSet animation, int animationResId) {
        this.animation = animation;
        this.animationResId = animationResId;
    }

    AnimatorSet getAnimation() {
        return animation;
    }

    void setAnimation(AnimatorSet animation) {
        this.animation = animation;
        animationResId = null;
    }

    public Integer getAnimationResId() {
        return animationResId;
    }

    public void setAnimationResId(int animationResId) {
        this.animationResId = animationResId;
    }
}
