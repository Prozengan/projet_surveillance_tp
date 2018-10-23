import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class recorderFFMPEG extends Thread{

	////////
	private int tempsTemporaire;
	private String addresseDestinataire;
	private int portDest;
	private Process ffmpeg;		
	private boolean running;
	private int largeur, hauteur;

	public recorderFFMPEG(int w, int h, int temps, String adresseDest, int port){
		ffmpeg = null;
		largeur = w;
		hauteur = h;
		tempsTemporaire = temps;	//sera supprimé à terme
		addresseDestinataire = adresseDest;
		portDest = port;
	}
	//////////////


	public void run()
	{				
		try {			
			String cmd = "ffmpeg -video_size " + largeur + "x" + hauteur + " -framerate 5 -f x11grab -i :0 -t " + tempsTemporaire +" -f mpegts pipe:1";
			//String cmd = "ffmpeg -framerate 5 -f x11grab -i :0 -t " + tempsTemporaire +" -f mpegts pipe:1";
			
			//Permet de lancer la commande depuis l'application Java
			ProcessBuilder procFF = new ProcessBuilder(cmd.split("\\s+"));
			
			//On envoie tout sur le pipe créé
			procFF.redirectOutput(new File("/tmp/pipeReception"));
					
			try {
				System.out.println("Démarrage de l'enregistrement");
				ffmpeg = procFF.start();
				running = true;
				System.out.println("Enregistrement en cours");
				ffmpeg.waitFor();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Fin de l'enregistrement");
			
		}
	}
	
	/**
	 * ATTENTION ON NE SAIT PAS ENCORE CE QUE FAIT CETTE FONCTION, A REVOIR DONC
	 * C'EST UN TEST POUR PLUS TARD
	 */
	public void stopRecord()
	{
		if (ffmpeg != null)
		{
			BufferedOutputStream bos = new BufferedOutputStream(ffmpeg.getOutputStream());
			try
			{
				System.out.println("Envoie de la commande d'arrêt (q) de stream à ffmpeg...");
				bos.write(new String("q").getBytes());
				bos.flush();
				
				bos.close();
				System.out.println("ffmpeg s'est correctement arrêté");
			}
			catch (UnsupportedEncodingException e){
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				running = false;
			}
			
		}
	}

}

