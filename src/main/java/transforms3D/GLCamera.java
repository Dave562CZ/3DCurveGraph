package transforms3D;

import com.jogamp.opengl.glu.GLU;

public class GLCamera {
	public float azimuth, radius, zenith;

	boolean first_person, valid; // true -> 1st person, 0 -> 3rd person

	private Vec3D eye, eye_vector, up, pos, centre;

	void compute_matrix() {
		eye_vector = new Vec3D(
				(float) (Math.sin(azimuth) * Math.cos(zenith)),
				(float) Math.sin(zenith),
				(float) -(Math.cos(azimuth) * Math.cos(zenith))
				);
		up = new Vec3D(
				(float) (Math.sin(azimuth) * Math.cos(zenith + Math.PI/ 2)), 
				(float) Math.sin(zenith + Math.PI / 2),
				(float) -(Math.cos(azimuth) * Math.cos(zenith + Math.PI/ 2)) );
		if (first_person) {
			eye = new Vec3D(pos);
			centre = eye.add(eye_vector.mul(radius));
		} else {
			eye = pos.add(eye_vector.mul(-1 * radius));
			centre = new Vec3D(pos);
		}
		valid = true;
	}

	public GLCamera() {
		azimuth = zenith = 0.0f;
		radius = 1.0f;
		pos = new Vec3D(0.0f, 0.0f, 0.0f);
		first_person = true;
		valid = false;
	}

	public void add_azimuth(float ang) {
		azimuth += ang;
		valid = false;
	}

	public void add_radius(float dist) {
		if (radius + dist < 0.1f)
			return;
		radius += dist;
		valid = false;
	}

	public void mul_radius(float scale) {
		if (radius * scale < 0.1f)
			return;
		radius *= scale;
		valid = false;
	}

	public void add_zenith(float ang) {
		if (Math.abs(zenith + ang) <= Math.PI / 2) {
			zenith += ang;
			valid = false;
		}
	}

	public void set_azimuth(float ang) {
		azimuth = ang;
		valid = false;
	}

	public float get_azimuth() {
		return azimuth;
	}

	public void set_radius(float dist) {
		radius = dist;
		valid = false;
	}

	public void set_zenith(float ang) {
		zenith = ang;
		valid = false;
	}

	public float get_zenith() {
		return zenith;
	}

	public void backward(float speed) {
		forward((-1) * speed);
	}

	public void forward(float speed) {
		pos = pos.add(new Vec3D(
				(float) (Math.sin(azimuth) * Math.cos(zenith)), 
				(float) Math.sin(zenith),
				(float) -(Math.cos(azimuth) * Math.cos(zenith))
				).mul(speed));
		valid = false;
	}

	public void left(float speed) {
		right((-1) * speed);
	}

	public void right(float speed) {
		pos = pos.add(new Vec3D(
				(float) -Math.sin(azimuth - Math.PI / 2), 
				0.0f,
				(float) +Math.cos(azimuth - Math.PI / 2)
				).mul(speed));
		valid = false;
	}

	public void down(float speed) {
		pos.y -= speed;
		valid = false;
	}

	public void up(float speed) {
		pos.y += speed;
		valid = false;
	}

	public void move(Vec3D dir) {
		pos = pos.add(dir);
		valid = false;
	}

	public void set_position(Vec3D apos) {
		pos = new Vec3D(apos);
		valid = false;
	}

	public boolean get_first_person() {
		return first_person;
	}

	public void set_first_person(boolean fp) {
		first_person = fp;
		valid = false;
	}

	public Vec3D get_eye() {
		if (!valid)
			compute_matrix();
		return eye;
	}

	public Vec3D get_eye_vector() {
		if (!valid)
			compute_matrix();
		return eye_vector;
	}

	public Vec3D get_position() {
		if (!valid)
			compute_matrix();
		return pos;
	}

	public void set_matrix(GLU glu) {
		if (!valid)
			compute_matrix();
		glu.gluLookAt(eye.x, eye.y, eye.z, centre.x, centre.y, centre.z, up.x,
				up.y, up.z);
	}

}