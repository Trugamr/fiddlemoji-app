package com.tru.fiddlemoji;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Date;
import java.util.Random;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    String emojis[] = {"😀", "😁", "😂", "🤣", "😃", "😄", "😅", "😆", "😗", "🥰", "😘", "😍", "😎", "😋", "😊", "😉", "😙", "😚", "☺", "🙂", "🤗", "🤩",
        "🤔", "🤨", "😮", "😥", "😣", "😏", "🙄", "😶", "😑", "😐", "🤐", "😯", "😪", "😫", "🥱", "😴", "😌", "😛", "😜", "😝", "🤤", "😓", "😒", "😔", "😕",
        "🙃", "😤", "😟", "😞", "😖", "🙁", "😲", "🤑", "😢", "😭", "😦", "😧", "😨", "😩", "🤯", "😬", "😰", "😱", "🥵", "🥶", "😳", "🤪", "😵", "🥴",
        "🤮", "🤢", "🤕", "🤒", "😷", "🤬", "😡", "😠", "🤧", "😇", "🥳", "🥺", "🤠", "🤡", "🤥", "🤫", "🤭", "🧐", "🤓", "😈", "👿", "👹", "👺", "💀",
        "😸", "😺", "💩", "🤖", "👾", "👽", "👻", "🙈", "🙉", "🙊", "🐶", "🐵", "🦝", "🐮", "🐷", "🐗", "🐭", "🐹", "🐰", "🐻", "🐲", "🐔", "🦄", "🐴",
        "🦓", "🐸", "🐼", "🐨", "👀", "👁", "🍕", "🍔", "♥", "💙", "💚", "💛", "💜", "🧡", "❤", "🖤", "💔", "😍", "🥰", "💪", "🦵", "🦶", "👂", "🦻", "👃",
        "🤏", "👈", "👉", "☝", "👆", "👇", "✌", "🤞", "🖖", "🤘", "🤙", "🖐", "🤜", "🤛", "👊", "✊", "👎", "👍", "👌", "✋", "🤚", "👋", "🤟", "👏",
        "👐", "🙌", "🤲", "💅", "🤝", "🙏", "🎈"};

    ConstraintLayout layout = null;
    boolean emojiTrail = false;
    float emojiSize = 60f;
    SeekBar seekBar = null;
    Switch trailSwitch = null;
    Random rand = new Random();

    Date date = new Date();
    long time = date.getTime();

    public void changeEmojiSize(float val) {
        emojiSize = 20f + val;
    }

    public void toggleEmojiTrail(boolean val) {
        emojiTrail = val;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void spawnEmoji(View v, float x, float y) {

        float emojiTextSize = emojiSize;
        TextView emoji = new TextView(MainActivity.this);
        emoji.setText(emojis[rand.nextInt(emojis.length)]);
        emoji.setTextColor(0xff000000);
        emoji.setTextSize(emojiTextSize);
        emoji.setAlpha(0f);
        emoji.setX(x - emojiTextSize * 2);
        emoji.setY(y - emojiTextSize * 2);
        layout.addView(emoji);
        bounceEmoji(emoji);
    }

    public void bounceEmoji(final View v) {
        // alpha up
        final SpringAnimation alphaUp = new SpringAnimation(v, DynamicAnimation.ALPHA, 1f);
        alphaUp.getSpring().setStiffness(SpringForce.STIFFNESS_LOW);
        alphaUp.start();

        // scaling down
        final SpringAnimation animScaleDownX = new SpringAnimation(v, DynamicAnimation.SCALE_X, 0.5f);
        final SpringAnimation animScaleDownY = new SpringAnimation(v, DynamicAnimation.SCALE_Y, 0.5f);
        animScaleDownX.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY).setStiffness(SpringForce.STIFFNESS_MEDIUM);
        animScaleDownY.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY).setStiffness(SpringForce.STIFFNESS_MEDIUM);
        animScaleDownX.start();
        animScaleDownY.start();

        // scale up
        final SpringAnimation animScaleUpX = new SpringAnimation(v, DynamicAnimation.SCALE_X, 1f);
        final SpringAnimation animScaleUpY = new SpringAnimation(v, DynamicAnimation.SCALE_Y, 1f);
        animScaleUpX.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY).setStiffness(SpringForce.STIFFNESS_LOW);
        animScaleUpY.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY).setStiffness(SpringForce.STIFFNESS_LOW);

        // scaling down event listener
        animScaleDownX.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                animScaleUpX.start();
            }
        });
        animScaleDownY.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                animScaleUpY.start();
            }
        });

        // fading out
        animScaleUpX.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                v.animate().scaleX(0f).scaleY(0f).alpha(0f).setDuration(200).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        // deleting view
                        layout.removeView(v);
                    }
                });
            }
        });

    }

    public int getRandomNumber(int limit) {
        return (int) Math.floor(Math.random() * limit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.constraintLayout);
        seekBar = findViewById(R.id.seekBar);
        trailSwitch = findViewById(R.id.trailSwitch);

        layout.setOnTouchListener(new View.OnTouchListener(){
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {
//                Log.i("XD", String.format("X: %f, Y: %f, Action: %s", event.getX(), event.getY(), event.getAction()));
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        spawnEmoji(view, event.getX(), event.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(emojiTrail && new Date().getTime() - time > 40) {
                            spawnEmoji(view, event.getX(), event.getY());
                            time = new Date().getTime();
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        trailSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleEmojiTrail(isChecked);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                changeEmojiSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // meh
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // meh
            }
        });
    }
}
