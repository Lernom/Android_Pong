package rust.pong;

import android.graphics.Canvas;
import android.graphics.Paint;

import rust.pong.Colliders.CircleCollider;

/**
 * Created by Lernom on 10.09.2017.
 */

public class Ball extends APhysicsElement {

    public Paint BallPaint;

    public CircleCollider Collider;

    public Ball(double x, double y, int size, Paint ballPaint){
        BallPaint = ballPaint;
        Collider = new CircleCollider(new Vector(x,y), size);
    }

    @Override
    public void Draw(Canvas canvas){
        canvas.drawCircle((float)Collider.P.X, (float)Collider.P.Y, (float)Collider.R, BallPaint);
    }

}
