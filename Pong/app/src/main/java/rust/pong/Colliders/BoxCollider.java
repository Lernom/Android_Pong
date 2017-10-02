package rust.pong.Colliders;

/**
 * Created by Lernom on 12.09.2017.
 */

public class BoxCollider implements Collider {

    public double X,Y,W,H;

    public  BoxCollider(double x,double y,double w,double h) {
        X = x;
        Y = y;
        W = w;
        H = h;
    }

    @Override
    public ColliderType GetColliderType() {
        return  ColliderType.Box;
    }

    @Override
    public CollisionTestResult CheckCollision(Collider other) {
        switch (other.GetColliderType()) {
            case Box: {
                return CollisionChecker.BoxBoxCollision((BoxCollider) other, this);
            }
            case Circle: {
                return CollisionChecker.BoxCircleCollision(this, (CircleCollider) other);
            }
        }
        return null;
    }
}
