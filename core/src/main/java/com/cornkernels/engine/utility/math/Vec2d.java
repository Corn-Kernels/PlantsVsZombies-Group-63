package com.cornkernels.engine.utility.math;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Vec2d {
    private float x;
    private float y;

    // Constructor
    public Vec2d(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Copy constructor
    @Contract(pure = true)
    public Vec2d(@NotNull Vec2d other) {
        this.x = other.x;
        this.y = other.y;
    }

    // Linear interpolation between two vectors
    @Contract("_, _, _ -> new")
    public static @NotNull Vec2d lerp(@NotNull Vec2d a, @NotNull Vec2d b, float t) {
        t = Math.clamp(t, 0, 1); // Clamp t between 0 and 1
        return new Vec2d(
            a.x + (b.x - a.x) * t,
            a.y + (b.y - a.y) * t
        );
    }

    // Zero vector
    @Contract(value = " -> new", pure = true)
    public static @NotNull Vec2d zero() {
        return new Vec2d(0, 0);
    }

    // Unit vectors
    @Contract(value = " -> new", pure = true)
    public static @NotNull Vec2d unitX() {
        return new Vec2d(1, 0);
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Vec2d unitY() {
        return new Vec2d(0, 1);
    }

    // Getters
    public float getX() {
        return x;
    }

    // Setters
    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Vector Addition: this + other
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Vec2d add(@NotNull Vec2d other) {
        return new Vec2d(this.x + other.x, this.y + other.y);
    }

    // Add in-place
    @Contract(mutates = "this")
    public void addInPlace(@NotNull Vec2d other) {
        this.x += other.x;
        this.y += other.y;
    }

    // Vector Subtraction: this - other
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Vec2d subtract(@NotNull Vec2d other) {
        return new Vec2d(this.x - other.x, this.y - other.y);
    }

    // Subtract in-place
    @Contract(mutates = "this")
    public void subtractInPlace(@NotNull Vec2d other) {
        this.x -= other.x;
        this.y -= other.y;
    }

    // Scalar Multiplication: this * scalar
    @Contract(value = "_ -> new", pure = true)
    public @NotNull Vec2d multiply(float scalar) {
        return new Vec2d(this.x * scalar, this.y * scalar);
    }

    // Multiply in-place
    public void multiplyInPlace(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    // Scalar Division: this / scalar
    @Contract("_ -> new")
    public @NotNull Vec2d divide(float scalar) {
        if (scalar == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return new Vec2d(this.x / scalar, this.y / scalar);
    }

    // Divide in-place
    public void divideInPlace(float scalar) {
        if (scalar == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        this.x /= scalar;
        this.y /= scalar;
    }

    // Dot Product: this · other
    @Contract(pure = true)
    public float dot(@NotNull Vec2d other) {
        return this.x * other.x + this.y * other.y;
    }

    // Cross Product (2D returns scalar): this × other
    @Contract(pure = true)
    public float cross(@NotNull Vec2d other) {
        return this.x * other.y - this.y * other.x;
    }

    // Magnitude (length): ||this||
    public float magnitude() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    // Squared Magnitude (for optimization when exact distance not needed)
    public float magnitudeSquared() {
        return this.x * this.x + this.y * this.y;
    }

    // Normalize: returns unit vector in same direction
    public @NotNull Vec2d normalize() {
        float mag = magnitude();
        if (mag == 0) {
            return new Vec2d(0, 0);
        }
        return this.divide(mag);
    }

    // Normalize in-place
    public void normalizeInPlace() {
        float mag = magnitude();
        if (mag != 0) {
            this.divideInPlace(mag);
        }
    }

    // Distance between two points
    @Contract(pure = true)
    public float distance(@NotNull Vec2d other) {
        float dx = this.x - other.x;
        float dy = this.y - other.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    // Squared distance (for optimization)
    @Contract(pure = true)
    public float distanceSquared(@NotNull Vec2d other) {
        float dx = this.x - other.x;
        float dy = this.y - other.y;
        return dx * dx + dy * dy;
    }

    // Angle between two vectors (in radians)
    public float angleBetween(Vec2d other) {
        float dot = this.dot(other);
        float magProduct = this.magnitude() * other.magnitude();
        if (magProduct == 0) {
            return 0;
        }
        return (float) Math.acos(Math.clamp(dot / magProduct, -1, 1));
    }

    // Angle of this vector from x-axis (in radians)
    public float angle() {
        return (float) Math.atan2(this.y, this.x);
    }

    // Rotate by angle (in radians)
    @Contract("_ -> new")
    public @NotNull Vec2d rotate(float angleRadians) {
        float cos = (float) Math.cos(angleRadians);
        float sin = (float) Math.sin(angleRadians);
        float newX = this.x * cos - this.y * sin;
        float newY = this.x * sin + this.y * cos;
        return new Vec2d(newX, newY);
    }

    // Rotate in-place
    public void rotateInPlace(float angleRadians) {
        float cos = (float) Math.cos(angleRadians);
        float sin = (float) Math.sin(angleRadians);
        float newX = this.x * cos - this.y * sin;
        float newY = this.x * sin + this.y * cos;
        this.x = newX;
        this.y = newY;
    }

    // Perpendicular vector (rotate 90 degrees counter-clockwise)
    @Contract(value = " -> new", pure = true)
    public @NotNull Vec2d perpendicular() {
        return new Vec2d(-this.y, this.x);
    }

    // Project this vector onto another vector
    public @NotNull Vec2d project(Vec2d other) {
        float scalar = this.dot(other) / other.magnitudeSquared();
        return other.multiply(scalar);
    }

    // Reflect this vector across a normal
    public @NotNull Vec2d reflect(@NotNull Vec2d normal) {
        Vec2d normalizedNormal = normal.normalize();
        float dotProduct = this.dot(normalizedNormal);
        return this.subtract(normalizedNormal.multiply(2 * dotProduct));
    }

    // Clamp components between min and max
    public @NotNull Vec2d clamp(float min, float max) {
        float clampedX = Math.clamp(this.x, min, max);
        float clampedY = Math.clamp(this.y, min, max);
        return new Vec2d(clampedX, clampedY);
    }

    // Clamp in-place
    public void clampInPlace(float min, float max) {
        this.x = Math.clamp(this.x, min, max);
        this.y = Math.clamp(this.y, min, max);
    }

    // Negate vector
    @Contract(value = " -> new", pure = true)
    public @NotNull Vec2d negate() {
        return new Vec2d(-this.x, -this.y);
    }

    // Negate in-place
    public void negateInPlace() {
        this.x = -this.x;
        this.y = -this.y;
    }

    @Override
    public String toString() {
        return String.format("Vec2d(%.2f, %.2f)", x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vec2d other = (Vec2d) obj;
        return Float.compare(this.x, other.x) == 0 &&
            Float.compare(this.y, other.y) == 0;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(x) * 31 + Float.hashCode(y);
    }
}
