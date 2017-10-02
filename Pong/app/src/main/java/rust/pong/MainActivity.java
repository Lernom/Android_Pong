package rust.pong;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AFullscreenActivity {


    Button StartButton;
    Button ExitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartButton = (Button) findViewById(R.id.btnStart);
        ExitButton = (Button)findViewById(R.id.btnExit);

        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartGame();
            }
        });

        ExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExitGame();
            }
        });
    }

    private  void  StartGame(){
        Intent gameIntent = new Intent(this, GameActivity.class);
        startActivity(gameIntent);
    }

    private  void  ExitGame(){
        System.exit(1);
    }
}
