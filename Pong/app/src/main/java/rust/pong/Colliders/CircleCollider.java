package rust.pong.Colliders;

import rust.pong.Vector;

/**
 * Created by Lernom on 12.09.2017.
 */

public class CircleCollider implements Collider {

    public Vector P;

    public double R;

    public CircleCollider(Vector p, double r){
        P = p;
        R = r;
    }

    @Override
    public ColliderType GetColliderType() {
        return  ColliderType.Circle;
    }

    @Override
    public CollisionTestResult CheckCollision(Collider other) {
        switch (other.GetColliderType()){
            case Box:{
                return CollisionChecker.BoxCircleCollision((BoxCollider)other, this);
            }
            case Circle:{
                return CollisionChecker.CircleCircleCollision(this, (CircleCollider)other);
            }
        }
        return  null;
    }
}
