package rust.pong;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import rust.pong.AI.AAIController;
import rust.pong.AI.DeductiveAI;
import rust.pong.Colliders.CircleCollider;
import rust.pong.Colliders.CollisionTestResult;

public class GameActivity extends AFullscreenActivity {

    SurfaceView _gameView;
    SurfaceHolder _gameViewHolder;

    TextView _playerScoreText, _opponentScoreText;

    GameState _state;
    Thread _mainGameThread;

    APhysicsElement[] _balls;
    IPhysicsObstacle[] _obstacles;

    PlayerBatObstacle _playerBat, _opponentBat;

    int _playerScore, _opponentScore;

    int _playerBatWidth = 100;
    int _playerBatHeight = 20;
    int _playerBatPosition = 10;
    int _playerBatMargin = 10;

    int _height;
    int _width;

    float _previousX = 0;

    int _minBatPosition, _maxBatPosition;

    long _oldTime;
    Paint _batPaint;

    AAIController _opponent;
    //AAIController _demo;


    double _easyModifier = 1;
    double _mediumModifier = 2;
    double _hardModifier = 2.5;

    double _currentModifier;

    float _startingBallSpeed = 200;
    float _maxBallSpeed = 600;
    double _ballSpeedIncrement = 0.02;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        _playerScoreText = (TextView)findViewById(R.id.ScorePlayer);
        _opponentScoreText = (TextView)findViewById(R.id.ScoreOpponent);
        _gameView = (SurfaceView)findViewById(R.id.GameView);
        _gameViewHolder = _gameView.getHolder();

        _gameViewHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                InitGameData();
                CreateMainLoop();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
    }

    private  void  RefreshScoreBoard(){
        _gameView.post(new Runnable() {
            public void run() {
                _playerScoreText.setText(Integer.toString(_playerScore));
                _opponentScoreText.setText(Integer.toString(_opponentScore));
            }
        });
    }

    private  void  InitGameData(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        ChangeGameState(GameState.Paused);
        _height =  metrics.heightPixels;
        _width = metrics.widthPixels;
        _minBatPosition = 0;
        _maxBatPosition = _width - _playerBatWidth;

        _batPaint = new Paint();
        _batPaint.setColor(Color.WHITE);
        _playerBat = new PlayerBatObstacle(_playerBatPosition, _height - _playerBatHeight - _playerBatMargin * 2, _playerBatWidth);
        _opponentBat = new PlayerBatObstacle(_width / 2, _playerBatHeight + _playerBatMargin, _playerBatWidth);
        _opponentBat.Normal = 1;

        _currentModifier = _mediumModifier;

        _opponent = new DeductiveAI(80f * _currentModifier, _opponentBat);
        //_demo = new DeductiveAI(80f * _currentModifier, _playerBat);
        SetupPlayingField();
        RefreshBatPosition();
    }

    private  void  RefreshBatPosition(){
        if(_playerBatPosition < _minBatPosition)
            _playerBatPosition = _minBatPosition;
        if(_playerBatPosition > _maxBatPosition)
            _playerBatPosition = _maxBatPosition;
        _playerBat.Collider.X = _playerBatPosition;
    }

    private  void  RefreshOpponentBatPosition(){
        if(_opponentBat.Collider.X < _minBatPosition)
            _opponentBat.Collider.X = _minBatPosition;
        if(_opponentBat.Collider.X > _maxBatPosition)
            _opponentBat.Collider.X = _maxBatPosition;
    }


    private void  SetupPlayingField() {
        _balls = new APhysicsElement[1];
        Paint _ballPaint = new Paint();
        _ballPaint.setColor(Color.RED);
        _balls[0] = new Ball(_width / 2, _height / 2, 5, _ballPaint);
        _balls[0].Speed = _startingBallSpeed;
        _balls[0].SetDirection(new Vector(Math.random() * 2 - 1, Math.random() * 6 - 1));


        int offset = 50;

        _obstacles = new IPhysicsObstacle[6];
        _obstacles[0] = new BoxObstacle(-offset, -offset, 10 + offset, _height + offset * 2, new Vector(1, 0));

        BoxObstacle upper = new BoxObstacle(-offset, _height - 10, _width + offset * 2, 10 + offset, new Vector(0, -1));
        upper.OnCollision = new ICollisionCallback() {
            @Override
            public void OnCollision() {
                Score(false);
            }
        };
        _obstacles[1] = upper;
        _obstacles[2] = new BoxObstacle(_width - 10, -offset, offset, _height + offset * 2, new Vector(-1, 0));

        BoxObstacle lower = new BoxObstacle(-offset, -offset, _width + offset * 2, 10 + offset, new Vector(0, 1));
        lower.OnCollision = new ICollisionCallback() {
            @Override
            public void OnCollision() {
                Score(true);
            }
        };
        _obstacles[3] = lower;
        _obstacles[4] = _playerBat;
        _obstacles[5] = _opponentBat;
    }

    public void Score(boolean isPlayerScore) {
        if (isPlayerScore) {
            _playerScore++;
            RefreshScoreBoard();
        } else {
            _opponentScore++;
            RefreshScoreBoard();
        }
    }


    private  void  PhysicsStep(){
        long time = SystemClock.elapsedRealtime();
        double deltaTime = (time - _oldTime)/ 1000.0;
        _oldTime = time;
        for (int i = 0; i < _balls.length; i++)
        {
            MovePhysicalObject(_balls[i], deltaTime);
        }
        _opponent.CalculateStep((Ball)_balls[0], deltaTime);
        //_demo.CalculateStep((Ball)_balls[0], deltaTime);
        RefreshOpponentBatPosition();
        //TO-DO - Check Win Conditions
    }

    private  void  MovePhysicalObject(APhysicsElement p, double deltaTime){

        double speedDelta = deltaTime * p.Speed;
        Vector offset = p.GetDirection().Multiply(speedDelta);

        Ball tempBall = (Ball)p;

        Vector targetPoint = tempBall.Collider.P.Add(offset);

        CircleCollider newPositionCollider = new CircleCollider(targetPoint,tempBall.Collider.R);

        for (int i = 0; i<_obstacles.length;i++){
            CollisionTestResult collision = _obstacles[i].CheckObstruction(newPositionCollider);
            if(collision.Collided)
            {
                Vector newDirection = _obstacles[i].GetReflectionVector(collision.Point, collision.Normal, p.GetDirection());
                p.SetDirection(newDirection);
                offset = p.GetDirection().Multiply(speedDelta);
                targetPoint = tempBall.Collider.P.Add(offset);
                if(p.Speed < _maxBallSpeed)
                    p.Speed += p.Speed * (_ballSpeedIncrement * _currentModifier);
            }
        }
        tempBall.Collider.P = targetPoint;
    }

    private  void  CreateMainLoop(){
        _mainGameThread = new Thread(new Runnable() {
            @Override
            public void run() {
                GameLoop();
            }
        });
        _mainGameThread.start();

        ChangeGameState(GameState.Playing);
    }

    @WorkerThread
    private  void  GameLoop(){
        while (_state != GameState.Stopped) {
            if(_state == GameState.Paused)
                continue;
            PhysicsStep();
            GameStep();
        }
    }

    private  void  GameStep(){
        Canvas canvas =  _gameViewHolder.lockCanvas();
        if(canvas == null)
            return;
        canvas.drawColor(Color.BLACK);

        for (int i = 0; i < _obstacles.length; i++){
            _obstacles[i].Draw(canvas, _batPaint);
        }

        for (int i = 0; i < _balls.length; i++)
        {
           _balls[i].Draw(canvas);
        }
        _gameViewHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - _previousX;
                _playerBatPosition += dx;
                RefreshBatPosition();
        }
        _previousX = x;
        return true;
    }

    @Override
    protected void onUserLeaveHint()
    {
        ChangeGameState(GameState.Paused);
        super.onUserLeaveHint();
    }

    @Override
    protected void onRestart()
    {
        ChangeGameState(GameState.Playing);
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        ChangeGameState(GameState.Paused);
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle("Menu")
                .setMessage("Quit?")
                .setCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        ChangeGameState(GameState.Playing);
                    }
                })
                .setNegativeButton("NO",   new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private  void  ChangeGameState(GameState NewState){
        _state = NewState;
        switch (NewState){
            case Playing:{
                _oldTime =  SystemClock.elapsedRealtime();
            }
        }
    }
}

enum  GameState{
    Playing,
    Paused,
    Stopped
}
