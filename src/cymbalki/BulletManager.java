package cymbalki;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

import static cymbalki.Consts.*;

public class BulletManager implements BulletProvider {
	
	private class Bullet implements cymbalki.Bullet {
		int id;
		int pet, line;
		float state;
		Animation explosion = null;
		Bullet(int p, int l){
			state = 0;
			pet = p;
			line = l;
			id = bullets.size();
			bullets.add(this);
		}
		
		@Override
		public void state(float x) {
			state = x;
		}

		@Override
		public void destroy() {
			int q = bullets.size() -1;
			Bullet x = bullets.get(q);
			x.id = id;
			bullets.set(id, bullets.get(q));
			bullets.remove(q);
		}
		
	}
	ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	public Bullet newBullet(int pet, int line) {
		return new Bullet(pet, line);
	}
	
	
	private SpriteSheet expSprites;
	private Image bImage;
	private Pets pets;
	private Vector2f va = new Vector2f(), vb = new Vector2f();
	
		
	BulletManager() throws Exception{
		bImage = new Image(IMAGE_ROOT_DIR + BULLET_IMAGE);
		
		expSprites = new SpriteSheet(new Image(IMAGE_ROOT_DIR + EXPLOSION_SPRITES),
				(int)EXPLOSION_SPRITE_SIZE, (int)EXPLOSION_SPRITE_SIZE);
		
	}
	
	public void setPets(Pets p){
		pets = p;
	}
	
	public void render(GameContainer c, Graphics g){
		for (Bullet b : bullets){
			if (b.state > 0.3 && b.state < 0.7){
				final float q = (b.state - 0.3f)/0.4f;
				pets.lineStart(b.pet, b.line, va);
				pets.lineEnd(b.pet, b.line, vb);
				vb.sub(va).scale(q);
				va.add(vb);
				
				drawImageByCenter(bImage, va.x, va.y, BULLET_SIZE,
						pets.colorOf(b.pet, b.line, q*30));
			} else if (b.state >= 0.7){
				final float q = (b.state - 0.7f)/0.3f;
				explode (b, q);
			}
		}
	}
	
	private void explode(Bullet b, float state){
		pets.explosion(b.pet, b.line, state);
		if (b.explosion == null) {
			b.explosion = new Animation(expSprites, (int)EXPLOSION_FRAME_LENGTH);
			b.explosion.setAutoUpdate(true);
			b.explosion.start();
			b.explosion.setLooping(false);
		}
		
		pets.lineEnd(b.pet, b.line, va);
		if (state < 0.5 && !b.explosion.isStopped()){
			b.explosion.draw(va.x-EXPLOSION_SIZE/2.f, va.y-EXPLOSION_SIZE/2.f,
					EXPLOSION_SIZE, EXPLOSION_SIZE);
		}
	}
}
