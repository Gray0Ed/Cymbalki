package cymbalki;

import static cymbalki.Consts.*;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.geom.Vector2f;

public class Menu {
	public static interface DoSth{
		void doSth();
	}
	
	private class InfoAction implements DoSth{

		@Override
		public void doSth() {
			sm.stopPlaying();
			if (infoDisplay){
				infoDisplay = false;
				sm.allAlpha(Consts.ALL_ALPHA_IDLE);
			} else {
				infoDisplay = true;
				sm.allAlpha(ALL_ALPHA_INFO);
			}
		}
		
	}
	
	private class Button{
		Image i;
		Vector2f center;
		float scale;
		boolean mouseOver = false;
		DoSth doSth;
		
		Button(Image im, Vector2f pos, float scal, DoSth callback){
			i = im;
			center = pos;
			scale = scal;
			doSth = callback;
		}
		
		void checkMouse(float x, float y, boolean pressed){
			final float s2 = scale/2f;
			if (x >= center.x - s2 && x <= center.x + s2 &&
					y >= center.y - s2 && y <= center.y + s2){
				mouseOver = true;
			} else {
				mouseOver = false;
			}
			if (mouseOver && pressed){
				if (this != buttons[4] &&infoDisplay){
					buttons[4].doSth.doSth();
				}
					
				doSth.doSth();
			}
		}
		
		void draw(Graphics g){
			if (mouseOver) {
				drawImageByCenter(i, center.x, center.y, 
						scale*BUTTON_ACTIVE_SCALE, BUTTON_ACTIVE_COLOR);
			} else {
				drawImageByCenter(i, center.x, center.y,
						scale, BUTTON_UNACTIVE_COLOR);
			}
		}
	}
	
	
	private class MouseInput implements MouseListener{
		public void setInput(Input input) {}
		public boolean isAcceptingInput() { return true;}	
		public void inputEnded() {}
		public void inputStarted(){}
		public void mouseWheelMoved(int change) {}
		public void mouseClicked(int button, int x, int y, int clickCount) {
			for (Button b: buttons){
				b.checkMouse(x, y, button == Input.MOUSE_LEFT_BUTTON);
			}
		}
		
		public void mousePressed(int button, int x, int y) {}
		public void mouseReleased(int button, int x, int y) {}
		public void mouseMoved(int oldx, int oldy, int newx, int newy) {
			lmMove = time();
		}
		
		public void mouseDragged(int oldx, int oldy, int newx, int newy) {}
		
	}
	
	private final static int N_BUTTONS = 5;
	
	private int lmMove;
	private Button buttons[] = new Button[N_BUTTONS];
	private SongManager sm;
	private boolean infoDisplay = false;
	private Image infoImage;
	
	public Menu(SongManager ssm, Input inp) throws Exception{
		sm = ssm;
		prepareButtons();
		inp.addMouseListener(new MouseInput());
		
		lmMove = 0;
		infoImage = new Image (IMAGE_ROOT_DIR + INFO_TEXT_IMAGE);
	}
	
	public void update(GameContainer c){
		int mx, my;
		mx = c.getInput().getMouseX();
		my = c.getInput().getMouseY();
		
		for(Button b: buttons){
			b.checkMouse(mx, my, false);
		}
	}
	
	public void render(GameContainer c, Graphics g){
		if (time() - lmMove < HUD_DISAPPEARANCE_THRESHOLD){
			for (Button b: buttons)
				b.draw(g);
			
			drawString(NORMAL_FONT, SONG_NAME_POS.x, SONG_NAME_POS.y, sm.actSongFilename());
		}
		
		if (infoDisplay){
			infoImage.draw(INFO_IMAGE_POS.x, INFO_IMAGE_POS.y);
		}
	}
	
	private void prepareButtons() throws Exception{
		buttons[0] = new Button(
				new Image(IMAGE_ROOT_DIR + START_BUTTON_IMAGE),
				START_BUTTON_POS,
				BUTTON_SIZE,
				sm.startDoSth());
		
		buttons[1] = new Button(
				new Image(IMAGE_ROOT_DIR + STOP_BUTTON_IMAGE),
				STOP_BUTTON_POS,
				BUTTON_SIZE,
				sm.stopDoSth());
		
		buttons[2] = new Button(
				new Image(IMAGE_ROOT_DIR + NEXT_BUTTON_IMAGE),
				NEXT_BUTTON_POS,
				BUTTON_SIZE,
				sm.nextDoSth());
		
		buttons[3] = new Button(
				new Image(IMAGE_ROOT_DIR + PREV_BUTTON_IMAGE),
				PREV_BUTTON_POS,
				BUTTON_SIZE,
				sm.prevDoSth());
		
		buttons[4] = new Button(
				new Image(IMAGE_ROOT_DIR + INFO_BUTTON_IMAGE),
				INFO_BUTTON_POS,
				BUTTON_SIZE,
				new InfoAction());
		
				
	}
}
