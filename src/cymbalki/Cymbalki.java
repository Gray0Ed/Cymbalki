package cymbalki;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;

public class Cymbalki extends BasicGame {

	SongManager songManager;
	BulletManager bulletManager;
	static HashMap<String, Integer> settings = new HashMap<String, Integer>();
	private Image background;
	private Menu menu;
	
	private static void defaultSettings(){
		settings.put("width", (int)Consts.WINDOW_WIDTH);
		settings.put("height", (int)Consts.WINDOW_HEIGHT);
		settings.put("fullscreen", 0);
		settings.put("background", 0);
		settings.put("soundNames", 0);
	}
	
	private static void loadConfig() 
		throws Exception{
		defaultSettings();
		File f = new File (Consts.CONFIGURATION_FILE);
		Scanner sc = new Scanner(f);
		
		final float raito = Consts.WINDOW_HEIGHT/Consts.WINDOW_WIDTH;
		
		while (sc.hasNext()){
			String s = sc.next();
			
			Integer i = settings.get(s);
			if (i == null){
				throw new Exception("blad w pliku konfiguracyjnym, nieznana nazwa: " + s);
			} else {
				try {
					settings.put(s, sc.nextInt());
				} catch (Exception e) {
					throw new Exception("nie udalo sie wczytac wartosci \"" + s + 
							"\" z pliku konfiguracyjnego, po napisie \""+s + 
							"\" powinna sie znajdowac liczba calkowita");
				}
			}
		}
		
		if (settings.get("width") < 200 || settings.get("width") > 1600){
			throw new Exception("zla wartosc width w pliku konfiguracyjnym" +
					"(ma byc w przedizale <200; 1600>, lub moze byc niepodana");
		}
		settings.put("height", (int)(settings.get("width")*raito));
		
		Consts.dsn = settings.get("soundNames") != 0;
	}
	
	public static void main(String[] args) {
        try {

        	loadConfig();
            AppGameContainer app = new AppGameContainer(new ScalableGame(new Cymbalki(),
            		(int)Consts.WINDOW_WIDTH, (int)Consts.WINDOW_HEIGHT));
            app.setDisplayMode(settings.get("width"),
            		settings.get("height"), settings.get("fullscreen") != 0);
            app.setShowFPS(false);
            app.start();
            
        } catch (SlickException e) {
            e.printStackTrace();
        } catch (Exception e){
        	e.printStackTrace();
        }
    }
	
	Cymbalki(){
		super("Cymbalki");
	}
	
	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		if (background != null)
			background.draw(0, 0, Consts.WINDOW_WIDTH, Consts.WINDOW_HEIGHT);
		
		songManager.render(container, g);
		bulletManager.render(container, g);
		menu.render(container, g);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		try {

			Consts.init();
			bulletManager = new BulletManager();
			songManager = new SongManager(bulletManager);
			menu = new Menu(songManager, container.getInput());
			bulletManager.setPets(songManager.pets());
			if (settings.get("background") != 0)
				background = new Image(Consts.IMAGE_ROOT_DIR +
					Consts.BACKGROUND_IMAGE);
			else
				background = null;
			
		}
		catch (Exception e){
			throw new SlickException(e.toString());
		}

	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		songManager.update(container);
		menu.update(container);
	}

}
