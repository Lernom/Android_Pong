package rust.pong;

import android.graphics.Canvas;
import android.graphics.Paint;

import rust.pong.Colliders.BoxCollider;
import rust.pong.Colliders.Collider;
import rust.pong.Colliders.CollisionTestResult;

/**
 * Created by Lernom on 10.09.2017.
 */

public class BoxObstacle implements IPhysicsObstacle {

    public BoxCollider Collider;

    public Vector Normal;

    public ICollisionCallback OnCollision;

    public BoxObstacle(double x1, double y1, double w, double h, Vector normal){

        Collider = new BoxCollider(x1,y1,w,h);
        Normal = normal;
    }

    @Override
    public CollisionTestResult CheckObstruction(Collider other) {
        CollisionTestResult result = Collider.CheckCollision(other);
        if(result != null && result.Collided && OnCollision != null)
            OnCollision.OnCollision();
        return  result;
    }

    @Override
    public Vector GetReflectionVector(Vector point, Vector normal, Vector direction) {
        return direction.Minus(Normal.Multiply(2 * (direction.Dot(Normal))));
    }

    @Override
    public void Draw(Canvas c, Paint p) {
        c.drawRect((float) Collider.X, (float) Collider.Y, (float) Collider.X + (float) Collider.W, (float) Collider.Y + (float) Collider.H, p);
    }
}
