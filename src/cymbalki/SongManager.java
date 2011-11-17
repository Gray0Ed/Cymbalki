package cymbalki;

import java.io.File;
import java.io.FileFilter;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import static cymbalki.Consts.*;

public class SongManager {
	private class Start implements Menu.DoSth{
		public void doSth(){
			allAlpha = ALL_ALPHA_PLAY;
			sp.start();
		}
	}
	
	private class Stop implements Menu.DoSth{
		public void doSth(){
			allAlpha = ALL_ALPHA_IDLE;
			sp.stop();
		}
	}
	
	private class Next implements Menu.DoSth{
		public void doSth(){
			ss.goNext();
			allAlpha = ALL_ALPHA_IDLE;
			updateSong();
		}
	}
	
	private class Prev implements Menu.DoSth{
		public void doSth(){
			ss.goPrev();
			allAlpha = ALL_ALPHA_IDLE;
			updateSong();
		}
	}
	

	private static class SongSelector {
		private File songfiles[];
		private int actSongId = 0;
		private Song actSong;
		SongSelector(File[] sf) throws Exception{
			songfiles = sf;
			if (sf == null || sf.length == 0)
				throw new Exception("niema plikow z melodiami");
		}
		Song actSong(){
			if (actSong == null) {
				actSong =  Song.load(songfiles[actSongId]);
			}
			return actSong;
		}
		String actSongFilename(){
			return songfiles[actSongId].getName(); 
		}
		void goNext(){
			++actSongId;
			if (actSongId == songfiles.length)
				actSongId = 0;
			actSong = null;
		}
		void goPrev(){
			--actSongId;
			if (actSongId == -1)
				actSongId = songfiles.length - 1;
			actSong = null;
		}
	}
	
	private SongPlayer sp;
	private SongSelector ss;
	private Pets pets;
	private float allAlpha = ALL_ALPHA_IDLE;
	
	public SongManager(BulletProvider bp) throws Exception{
		File f = new File(MUSIC_ROOT_DIR);
		
		ss = new SongSelector(f.listFiles(new FileFilter() {
			public boolean accept(File fi){
				return fi.getName().endsWith(MUSIC_FILE_SUFIX);
			}
		}));
		

		sp = new SongPlayer(bp);
		
		pets = new Pets();
		updateSong();
	}
	
	public String actSongFilename(){
		return ss.actSongFilename();
	}
	public void stopPlaying(){
		sp.stop();
	}
	public void allAlpha(float a){
		allAlpha = a;
	}
	
	public Menu.DoSth startDoSth(){
		return new Start();
	}
	
	public Menu.DoSth stopDoSth(){
		return new Stop();
	}
	
	public Menu.DoSth nextDoSth(){
		return new Next();
	}
	
	public Menu.DoSth prevDoSth(){
		return new Prev();
	}
	
	
	public void update(GameContainer c){
		sp.update();
	}
	
	public void render(GameContainer c, Graphics g){
		pets.render(g, allAlpha);
	}
	
	public Pets pets(){
		return pets;
	}
	
	private void updateSong(){
		sp.stop();
		sp.setSong(ss.actSong());
		pets.setSong(ss.actSong());
	}
	

}
