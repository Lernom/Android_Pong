package rust.pong.Colliders;

/**
 * Created by Lernom on 12.09.2017.
 */

public interface Collider {

    ColliderType GetColliderType();

    CollisionTestResult CheckCollision(Collider other);

}

