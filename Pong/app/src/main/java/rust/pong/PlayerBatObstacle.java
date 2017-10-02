package rust.pong;

import android.graphics.Canvas;
import android.graphics.Paint;

import rust.pong.Colliders.BoxCollider;
import rust.pong.Colliders.Collider;
import rust.pong.Colliders.CollisionTestResult;

/**
 * Created by Lernom on 11.09.2017.
 */

public class PlayerBatObstacle implements IPhysicsObstacle {

    public BoxCollider Collider;

    private  double h = 20;

    public  int Normal = -1;

    public  PlayerBatObstacle(double positionX, double positionY, double width){

        Collider = new BoxCollider(positionX,positionY,width,h);
    }

    @Override
    public CollisionTestResult CheckObstruction(Collider other) {

        return  Collider.CheckCollision(other);
    }

    @Override
    public Vector GetReflectionVector(Vector point, Vector normal, Vector direction) {
        Vector ChangedNormal = new Vector((point.X - Collider.X - Collider.W / 2)/(Collider.W / 2), Normal);
        return ChangedNormal;
    }

    @Override
    public void Draw(Canvas c, Paint p) {
        c.drawRect((float) Collider.X, (float) Collider.Y, (float) Collider.X + (float) Collider.W, (float) Collider.Y + (float) Collider.H, p);
    }

}
