package cymbalki;

import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import static cymbalki.Consts.*;

public class Pets {
	private class Pet {
		Image pet;
		Color sliderColor;
		String [] sounds;
		Vector2f sliderPos, petPos;
		float petScale;
		
		Pet(Image p, Color sc, String [] sn, Vector2f sp,
				Vector2f pp, float ps){
			pet = p;
			sliderColor = sc;
			sounds = sn.clone();
			sliderPos = sp;
			petPos = pp;
			petScale = ps;
		}
		
		void render(Graphics g, float allAlpha){
			Color c;
			c = new Color(sliderColor);
			c.a *= allAlpha;
			slider.draw(sliderPos.x, sliderPos.y, SLIDER_WIDTH,
					SLIDER_HEIGHT, c);
			
			c.a = allAlpha;
			c.r = c.g = c.b = 1.f;
			drawImageByCenter(pet, petPos.x, petPos.y, petScale, c);
			
			if (Consts.drawSoundNames())
			for (int i = 0; i < 4; ++i){
				for (int j = 0; j < sounds[i].length(); ++j){
					String s = "" + sounds[i].charAt(j);
					float q = stringWidth(SOUND_FONT, s);
					
					float x = i*SLIDER_WIDTH/4 + SLIDER_WIDTH/8 - q/2
						+ SLIDER_SOUND_TRANSLATION;
					float y = j*LETTER_HEIGHT*0.8f + SLIDER_HEIGHT/2 - 2*LETTER_HEIGHT;
					
					c = sliderColor.addToCopy(adder2);
					c.a *= allAlpha;
					
					drawString(SOUND_FONT, sliderPos.x + x,	sliderPos.y + y, s,
							c);
				}
			}
		}
	}
	
	private static class PetInfo{
		Image i;
		Color c;
		Vector2f translation;
		float scale;
		PetInfo(Image a, Color b, Vector2f t, float s){
			i = a;
			c = b;
			translation = t;
			scale = s;
		}
	}
	
	private Pet[] pets;
	private Song s;
	private Image slider, bulletHole;
	
	private HashMap<String, PetInfo> petInfos 
		= new HashMap<String, PetInfo>();
	
	
	public Pets() throws SlickException {
		bulletHole = new Image(IMAGE_ROOT_DIR + HOLE_IMAGE);
		slider = new Image(IMAGE_ROOT_DIR + SLIDER_IMAGE);
		
		petInfos.put("kotek", new PetInfo(
				new Image (IMAGE_ROOT_DIR + CAT_IMAGE),
				CAT_COLOR, CAT_TRANSLATION, CAT_SCALE));
		
		petInfos.put("rybka", new PetInfo(
				new Image (IMAGE_ROOT_DIR + FISH_IMAGE),
				FISH_COLOR, FISH_TRANSLATION, FISH_SCALE));
		
		petInfos.put("zolwik", new PetInfo(
				new Image(IMAGE_ROOT_DIR + TURTLE_IMAGE),
				TURTLE_COLOR, TURTLE_TRANSLATION, TURTLE_SCALE));
		
		petInfos.put("ptaszek", new PetInfo(
				new Image(IMAGE_ROOT_DIR + BIRD_IMAGE),
				BIRD_COLOR, BIRD_TRANSLATION, BIRD_SCALE));
		
		petInfos.put("misiu", new PetInfo(
				new Image(IMAGE_ROOT_DIR + BEAR_IMAGE),
				BEAR_COLOR, BEAR_TRANSLATION, BEAR_SCALE));
	}
	
	public void setSong(Song so){
		s = so;
		
		int q = s.numberOfPets();
		pets = new Pet[q];
		
		final float margin = WINDOW_WIDTH*0.01f;
		final float stride = (WINDOW_WIDTH - margin*2)/q;
		
		
		for (int i = 0; i < q; ++i){
			Vector2f sliderPos = new Vector2f(), petPos;
			PetInfo pi = petInfos.get(s.petName(i));
		
			
			sliderPos.x = margin + stride*(i + 0.5f) - SLIDER_WIDTH/2;
			sliderPos.y = ((i&1) != 0) ? PET_LOW_Y : PET_HI_Y;
			
			petPos = sliderPos.copy();
			petPos.add(pi.translation);
			
			pets[i] = new Pet(pi.i, pi.c, s.petSounds(i),
					sliderPos, petPos, pi.scale);
			
		}
		
	}
	
	public void render(Graphics g, float petAlpha){
		Vector2f v = new Vector2f();
		Color c = new Color(1,1,1,1);
		
		for (int i = 0; i < pets.length; ++i){
			pets[i].render(g, petAlpha);
			for (int j = 0; j < 4; ++j){
				lineEnd(i, j, v);
				c.r = pets[i].sliderColor.r;
				c.g = pets[i].sliderColor.g;
				c.b = pets[i].sliderColor.b;
				c.a = pets[i].sliderColor.a*petAlpha;
				bulletHole.draw(v.x - HOLE_SIZE/2,
						v.y - HOLE_SIZE/2, HOLE_SIZE,
						HOLE_SIZE, c);
			}
		}
		
	}
	
	public void lineStart(int pn, int line, Vector2f ret){
		ret.x = pets[pn].sliderPos.x;
		ret.y = pets[pn].sliderPos.y;
		
		ret.x += line*SLIDER_WIDTH/4 + SLIDER_WIDTH/8;
	}
	
	public void lineEnd(int pn, int line, Vector2f ret){
		lineStart(pn, line, ret);
		ret.y += SLIDER_EXPLODE_Y;
	}
	
	public void explosion(int pn, int line, float state){
		//TODO
	}
	
	private Color adder = new Color(0.5f, 0.5f, 0.5f, 0.5f),
	adder2 = new Color(0.6f, 0.6f, 0.6f, 0.6f);
	
	public Color colorOf(int pn, int line, float alpha){
		Color c = pets[pn].sliderColor.addToCopy(adder);
		if (alpha < 1)
			c.a = alpha;
		return c;
	}
}
