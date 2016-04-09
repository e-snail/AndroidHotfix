package roff.androidhotfix.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import roff.androidhotfix.R;

public class MainActivity extends AppCompatActivity {

    final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 查看应用加载的所有的classloader及其类型
         */
        int i = 0;
        ClassLoader classLoader = getClassLoader();
        if (classLoader != null){
            Log.i(TAG, "[onCreate] classLoader " + i + " : " + classLoader.toString());
            while (classLoader.getParent()!=null){
                classLoader = classLoader.getParent();
                Log.i(TAG,"[onCreate] classLoader " + i + " : " + classLoader.toString());
            }
        }

        setContentView(R.layout.activity_main);

        ShowText showText = new ShowText();

        TextView textView = (TextView) findViewById(R.id.text_view);
        textView.setText(showText.getText());
    }
}
