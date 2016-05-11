package xiaofei.library.comparatorgeneratortest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Test01.main(null);
        Test02.main(null);
        Test03.main(null);
        Test04.main(null);
    }
}
