package rust.pong;

/**
 * Created by Lernom on 10.09.2017.
 */

public class Vector {

    public double X;

    public double Y;

    public Vector(double _x, double _y){
        X = _x;
        Y = _y;
    }

    public  Vector()
    {}

    public Vector Add(Vector v){
        return  new Vector(X + v.X, Y + v.Y);
    }

    public Vector Minus(Vector v)
    {
        return new Vector(X - v.X, Y - v.Y);
    }

    public Vector Multiply(double mult){
        return  new Vector(X * mult, Y * mult);
    }

    public  Vector Normalize(){
        double k = 1/Magnitude();
        return Multiply(k);
    }

    public double Magnitude(){
        return  Math.sqrt(X * X + Y * Y);
    }

    public double Cross(Vector v)
    {
        return X * v.Y - Y * v.X;
    }

    public double Dot(Vector v){
        return X * v.X + Y * v.Y;
    }
}
