package cymbalki;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SongPlayer {
	private class BInfo{
		Bullet b;
		int startTime;
		BInfo(int p, int l, int st){
			b = bprovider.newBullet(p,l);
			startTime = st;
		}
		void state(float x){
			b.state(x);
		}
		void destroy(){
			b.destroy();
		}
	}
	
	private BulletProvider bprovider;
	
	private List<BInfo> bullets = new LinkedList<BInfo>();
	
	private Song song;
	
	private int startTime = -1, length, lastIndex;
	
	public SongPlayer(BulletProvider bp){
		bprovider = bp;
	}
	
	public void setSong(Song s){
		song = s;
		length = song.length();
		stop();
	}
	
	public void update(){
		if (startTime == -1)
			return;
		
		final int actTime = Consts.time() - startTime,
			tempo = song.milsecPerBeat();
		
		int limit = Math.min(1 + actTime/tempo, length);
		
		for (int i = lastIndex +1; i < limit; ++i){
			for (int j = 0; j < song.numberOfPets(); ++j)
				for (int k = 0; k < Song.LINES_PER_PET; ++k) if (song.petScore(j, k, i)){
					bullets.add(new BInfo(j, k, actTime));
				}
		}
		
		
		for (Iterator<BInfo> ib = bullets.iterator(); ib.hasNext();){
			BInfo b = ib.next();
			
			int time = actTime - b.startTime;
			float q = time/(3*Consts.BULLET_FLY_TIME);
			
			if (q > 1){
				b.destroy();
				ib.remove();
			}
			else 
				b.state(q);
		}
		
		if (lastIndex + 1 != limit){ 
			lastIndex = limit-1;
		}
	}
	
	public void start(){
		startTime = Consts.time();
		lastIndex = -1;
	}
	
	public void stop(){
		startTime = -1;
		for (BInfo b : bullets){
			b.destroy();
		}
		bullets.clear();
	}
	
}
