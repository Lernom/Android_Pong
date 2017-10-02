package rust.pong;

import android.graphics.Canvas;

/**
 * Created by Lernom on 10.09.2017.
 */

public abstract class APhysicsElement {

    protected Vector _direction;

    public  Vector GetDirection(){
        return  _direction;
    }

    public void SetDirection(Vector newDirection){
        _direction = newDirection.Normalize();
    }

    public float Speed;

    public abstract void  Draw(Canvas canvas);

}
