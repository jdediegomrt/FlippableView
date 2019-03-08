FlippableView
=============

Custom Android view that provides some simple methods to make easier a flip.

This view contains primarly:

 * frontFace - Layout front face
 * backFace - Layout back face
 * avoidClickOnFlip - boolean to prevent click events with view is flipping
 * duration - integer flip duration
 
You can use your own animations using (this overwrites default animations): 
 * inAnimation
 * outAnimation

```xml
	<com.jaimedediego.flippableview.FlippableView
        android:id="@+id/flippable_view"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_centerInParent="true"
        app:backFace="@layout/<<your back face layout>>"
        app:frontFace="@layout/<<your front face layout>>"
        app:flipDuration="<<integer>>"/>
```
 
```java
class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		...
		
        final FlippableView flippableView = findViewById(R.id.flippable_view);
        flippableView.avoidClickOnFlip(true);

        flippableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flippableView.flip();
            }
        });
		...
    }
}
```

Download
--------

In Module gradle

```groovy
dependencies {
	...
	implementation 'com.github.jdediegomrt:flippableview:0.7'
}
```

In Project gradle
```groovy
allprojects {
    repositories {
		...
        maven { url 'https://jitpack.io' }
    }
}
```

