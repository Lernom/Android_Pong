package rust.pong;

import android.graphics.Canvas;
import android.graphics.Paint;

import rust.pong.Colliders.Collider;
import rust.pong.Colliders.CollisionTestResult;

/**
 * Created by Lernom on 10.09.2017.
 */

public interface IPhysicsObstacle {

    CollisionTestResult CheckObstruction(Collider object);

    Vector GetReflectionVector(Vector point, Vector normal, Vector direction);

    void Draw(Canvas c, Paint p);
}
