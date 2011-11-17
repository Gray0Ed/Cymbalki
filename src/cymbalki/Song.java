package cymbalki;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class Song {
	static final int MAX_NUMBER_OF_PETS = 5;
	static final int LINES_PER_PET = 4;
	static final HashMap<String, Integer> PET_NAMES = new HashMap<String, Integer>();
	
	private int tempo;// milsec per beat
	private String [] petNames;
	private String [][] petSounds;
	private boolean score[][][];
	private int petsNumber;

	public String petName(int i){
		return petNames[i];
	}
	
	public String[] petSounds(int i){
		return petSounds[i].clone();
	}
	
	public boolean petScore(int pn, int line, int place){
		return score[pn][line][place];
	}
	
	public int length(){
		return score[0][0].length;
	}
	
	public int numberOfPets(){
		return petsNumber;
	}
	
	public int milsecPerBeat(){
		return tempo;
	}
	
	static public Song load(File file) {
		Song s = new Song();
		String [][] petSounds = new String[5][4];
		try {
			Scanner sc = new Scanner(file);
			if (!sc.hasNextInt()){
				throw new Exception("na poczatku pliku powinna byc dodatnia liczba - tempo");
			}
			s.tempo = sc.nextInt();
			
			
			s.tempo = 60000/s.tempo; //milsec per beat
				
				
			if (s.tempo <= 0)
				throw new Exception("tempo <= 0");

			boolean [][][] score = new boolean [MAX_NUMBER_OF_PETS][][];
			s.petNames = new String[MAX_NUMBER_OF_PETS];
			int len = -1;
			
			int i = 0;
			
			for (; i < MAX_NUMBER_OF_PETS && sc.hasNext() && !sc.hasNext("koniec"); ++i) {
				String petName = sc.next();
				
				if (!PET_NAMES.containsKey(petName))
					throw new Exception("nie znam nazwy zwierza: " + petName);
				
				s.petNames[i] = petName;
				
				for (int j = 0; j < 4; ++j){
					petSounds[i][j] = sc.next();
					
					char x = petSounds[i][j].length() > 0 ? petSounds[i][j].charAt(0) : ' ';
					if ((x >= '0' && x <= '9') || x == ' '){
						throw new Exception("blad w nazwach dziwiekow zwierzaka: " + petName);
					}
				}

				String[] lines = new String[LINES_PER_PET];
				for (int a = 0; a < LINES_PER_PET; ++a)
					lines[a] = new String();
				
				while (true){
					for (int j = 0; j < LINES_PER_PET; ++j)
						lines[j] += sc.next();
					if (!sc.hasNext("&"))
						break;
					else
							sc.next();
				}

				if (len == -1){
					len = lines[0].length();
				} 

				for (int j = 0; j < LINES_PER_PET; ++j)
					if (lines[j].length() != len){
						throw new Exception("linie melodyczne roznych dlugosci");
					}
				
				score[i] = petLines(lines);	
			}
			
			s.petSounds = petSounds;
			s.petsNumber = i;
			s.score = score;
			

		} catch (Exception e) {
			System.out.println("Blad podczas wczytywania pliku: " + file.getName()
					+ ", tresc bledu: " + e);
			return null; // ugly
		}

		return s;
	}
	
	static void main(String args[]){
		System.out.println("test Song.java");
		
		while (true){
			System.out.println("podaj nazwe pliku:\n");
			Scanner sc = new Scanner(System.in);
			
			Song s = Song.load(new File(sc.next()));
			
			if (s != null){
				System.out.println("wczytano plik, jego zawartosc: \n");
				
				System.out.println("tempo: " + s.tempo + ", petsNumber: " + s.petsNumber + ", score:\n");
				
				for (int i = 0; i < s.petsNumber; ++i){
					System.out.println("pet: " + s.petNames[i]);
					
					for (int j = 0; j < LINES_PER_PET; ++j){
						for (boolean b : s.score[i][j]){
							if (b == true)
								System.out.print(1);
							else
								System.out.print(0);
						}
						System.out.println("");
					}
					System.out.println("");
					
				}
			}
			
		}
	}
	
	static boolean[][] petLines(String [] lines) throws Exception{
		int len = lines[0].length();
		boolean ret[][] = new boolean [LINES_PER_PET][len];
		
		for (int i = 0; i < LINES_PER_PET; ++i){
			for (int j = 0; j < len; ++j){
				if (lines[i].charAt(j) != '0' && lines[i].charAt(j) != '1')
					throw new Exception("w ktorejs lini melodycznej wystapilo cos innego niz 0 lub 1");
				
				ret[i][j] = lines[i].charAt(j) == '0' ? false : true;
			}
		}
		return ret;
	}
	
	

	static {
		PET_NAMES.put("kotek", 0);
		PET_NAMES.put("rybka", 1);
		PET_NAMES.put("zolwik", 2);
		PET_NAMES.put("ptaszek", 3);
		PET_NAMES.put("misiu", 4);
	}
}
