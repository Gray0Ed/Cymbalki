package cymbalki;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Vector2f;

public class Consts {
	static final String
		IMAGE_ROOT_DIR = "gfx/",
		MUSIC_FILE_SUFIX = ".txt",
		MUSIC_ROOT_DIR = "piosenki/",
		INFO_TEXT_IMAGE = "infotext.png",
		BACKGROUND_IMAGE = "background.png",
		START_BUTTON_IMAGE = "start.png",
		STOP_BUTTON_IMAGE = "stop.png",
		NEXT_BUTTON_IMAGE = "next.png",
		PREV_BUTTON_IMAGE = "prev.png",
		EXPLOSION_SPRITES = "explosion.png",
		INFO_BUTTON_IMAGE = "infobutton.png",
		BULLET_IMAGE = "bullet.png",
		SLIDER_IMAGE = "frame.png",
		HOLE_IMAGE = "hole.png",
		SLIDER_FONT = "font.ttf",
		CAT_IMAGE = "cat.png",
		FISH_IMAGE = "fish.png",
		BEAR_IMAGE = "bear.png",
		TURTLE_IMAGE = "turtle.png",
		BIRD_IMAGE = "bird.png",
		CONFIGURATION_FILE = "conf.txt";
	
	static final boolean
		FULLSCREEN = false;
	
	static final int
		HUD_DISAPPEARANCE_THRESHOLD = 2500;
	
	static final Color
		BUTTON_ACTIVE_COLOR = new Color (1f, 1f, 1f, 0.9f),
		BUTTON_UNACTIVE_COLOR = new Color(1f,1f,1f, 0.7f),
		TURTLE_COLOR = new Color(0x47ff00),
		BIRD_COLOR = new Color(0x009ff1),
		FISH_COLOR = new Color(0xffd800),
		CAT_COLOR = new Color(0xfe0000),
		BEAR_COLOR = new Color(0xff7701);
		
		
	
	static final float 
		BULLET_FLY_TIME = 3000.f,
		BUTTON_ACTIVE_SCALE = 1.3f,
		BUTTON_SIZE = 80f,
		WINDOW_HEIGHT = 600,
		WINDOW_WIDTH = 800,
		PET_LOW_Y = 60,
		PET_HI_Y = 200,
		PET_HEIGHT = 400,
		SLIDER_WIDTH = 120,
		SLIDER_HEIGHT = 360,
		SLIDER_EXPLODE_Y = SLIDER_HEIGHT*0.9f,
		BULLET_SIZE = SLIDER_WIDTH/4,
		PET_SCALE = 80,
		EXPLOSION_SPRITE_SIZE = 105.f,
		EXPLOSION_SIZE = 2.5f * SLIDER_WIDTH/4.f,
		EXPLOSION_FRAME_LENGTH = 25.f,
		HOLE_SIZE = SLIDER_WIDTH/4,
		SOUND_LETTER_HEIGHT = SLIDER_WIDTH/4,
		LETTER_HEIGHT = 35, 
		SLIDER_SOUND_TRANSLATION = 0.f,
		ALL_ALPHA_IDLE = 0.5f,
		ALL_ALPHA_PLAY = 1.f,
		ALL_ALPHA_INFO = 0.1f;
	
	static final float
		CAT_SCALE = 150,
		FISH_SCALE = 130,
		TURTLE_SCALE = 150,
		BIRD_SCALE = 150,
		BEAR_SCALE = 115;
		
	
	static final Vector2f
		START_BUTTON_POS = new Vector2f(WINDOW_WIDTH/2 - 70, WINDOW_HEIGHT - 70),
		STOP_BUTTON_POS = new Vector2f(WINDOW_WIDTH/2 + 70, WINDOW_HEIGHT - 70),
		NEXT_BUTTON_POS = new Vector2f(WINDOW_WIDTH/2 + 170, WINDOW_HEIGHT - 140),
		PREV_BUTTON_POS = new Vector2f(WINDOW_WIDTH/2 - 170, WINDOW_HEIGHT - 140),
		SONG_NAME_POS = new Vector2f(WINDOW_WIDTH/2 - 70, WINDOW_HEIGHT - 180),
		INFO_BUTTON_POS = new Vector2f(50, WINDOW_HEIGHT-50),
		CAT_TRANSLATION = new Vector2f(50, 0),
		BIRD_TRANSLATION = new Vector2f(50, 0),
		FISH_TRANSLATION = new Vector2f(30, 0),
		BEAR_TRANSLATION = new Vector2f(30, 5),
		TURTLE_TRANSLATION = new Vector2f(40, -20),
		INFO_IMAGE_POS = new Vector2f(75, 20);
	
	static final int SOUND_FONT = 0,
		NORMAL_FONT = 1;

	
	private static UnicodeFont font[] = new UnicodeFont[2];
	
	private static long timeStart;
	//scale 1 means image will be drawn with width 1
	static void drawImageByCenter(Image i, float x, float y, float s){
		float scale = s* 1.f/i.getWidth();
		float 
			tx = x-(i.getWidth()*scale)/2.f,
			ty = y-(i.getHeight()*scale)/2.f;
		
		i.draw(tx, ty, scale);
	}
	static void drawImageByCenter(Image i, float x, float y, float s, Color c){
		float scale = s* 1.f/i.getWidth();
		float 
			tx = x-(i.getWidth()*scale)/2.f,
			ty = y-(i.getHeight()*scale)/2.f;
		i.draw(tx, ty, scale, c);
	}
	
	static void drawString(int n, float x, float y, String s){
		font[n].drawString(x, y, s);
	}
	static void drawString(int n, float x, float y, String s, Color c){
		font[n].drawString(x, y, s, c);
	}
	static float stringWidth(int n, String s){
		return font[n].getWidth(s);
	}
		
	static int time(){
		return (int)((System.nanoTime() - timeStart)/1000000);
	}
	
	@SuppressWarnings("unchecked")
	static void init() throws Exception{
		int [] h = new int[2];
		String s[] = new String[2];
		s[SOUND_FONT] = "font1.ttf";
		s[NORMAL_FONT] = "font2.ttf";
		h[SOUND_FONT] = (int)SOUND_LETTER_HEIGHT;
		h[NORMAL_FONT] = (int)LETTER_HEIGHT;
		for (int i = 0; i < 2; ++i){
			font[i] = new UnicodeFont(IMAGE_ROOT_DIR + s[i], h[i],  false, false);
			font[i].addAsciiGlyphs();  
			font[i].addGlyphs(400, 600);
			font[i].getEffects().add(new ColorEffect(java.awt.Color.WHITE));
			font[i].loadGlyphs();
		}
		timeStart = System.nanoTime();
	}
	
	static boolean dsn; 
	static boolean drawSoundNames(){
		return dsn;
	}
}
