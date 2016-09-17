package com.example.saar.locationalert.objects;

import android.view.View;
import android.view.animation.Animation;

/**
 * Created by Saar on 17/09/2016.
 */
public class AnimationUtil {
    final Thread animator;
    View view;
    Animation animation;

    public AnimationUtil(final View view, final Animation animation){
        this.view = view;
        this.animation = animation;

        animator = new Thread(new Runnable(){
            @Override
            public void run() {
                view.startAnimation(animation);
            }
        });
    }

    public void startAnimation(){
        animator.run();
    }

    public void awaitCompletion() throws InterruptedException{
        animator.join();
    }

}
