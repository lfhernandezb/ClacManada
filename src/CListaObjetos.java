import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;



public abstract class CListaObjetos extends CEvento{
	/*
	 * Esta clase maneja todos los objetos que mantendrá la aplicación. Panico/Alarma/Chat
	 * A través del método Show desplegará el Activity con el cual interactuará con el Usuario para mostrar 
	 * los Panicos/Alarmas/Chat que el usuario tiene a su disposición.
	 */
	protected ArrayList<CObjetoServidor> m_ListaObjetos;
	protected int m_MaximoObjetos;
	protected CDatosServicio m_DatosServicio;

	
	// Manejador del Timer para mantener la conexión
	private Timer timerListaObjetos;
	private TimerTask timertaskListaObjetos;
	private int m_SegundosTimer; 
		
	public CListaObjetos(CDatosServicio DatosServicio, int SegundosTimer) {
		super();
		m_DatosServicio = DatosServicio;
		m_ListaObjetos = new ArrayList<CObjetoServidor>();
		m_SegundosTimer = SegundosTimer; 
		m_MaximoObjetos = 5;
	}

	private  void schedule_task(long MiliSegundosTimer) {
		timerListaObjetos = new Timer();
		timertaskListaObjetos = new TimerTask(){

			@Override
			public void run() {
				// ATENCION DEL TIMER
				EjecutaDoObjetos();
			}
		};
		
		timerListaObjetos.scheduleAtFixedRate(timertaskListaObjetos, 0, m_SegundosTimer * 1000); //Tarea que se ejecuta cada 1 minuto
	}
	
	private void EjecutaDoObjetos(){
		for ( int i=0; i < m_ListaObjetos.size() ; i++ ) {
			m_ListaObjetos.get(i).Do();
		}
	}
	
	protected boolean AgregaObjeto(CObjetoServidor ObjetoServidor){
		if ( m_ListaObjetos.size() >= m_MaximoObjetos ){
			return false;
		}
		m_ListaObjetos.add(ObjetoServidor);
		return true;
	}
	
	protected CObjetoServidor GetObjetoServidor(String IdObjetoServidor){
		CObjetoServidor ObjetoServidor;	
		long ObjetoId = Long.parseLong(IdObjetoServidor);
		for (int i=0; i<this.m_ListaObjetos.size(); i++){
			ObjetoServidor = this.m_ListaObjetos.get(i);
			if ( ObjetoServidor.m_ID == ObjetoId ){
				return ObjetoServidor;
			}
		}
		return null;
	}
	
	public abstract boolean CreaObjetoPanico(); // Debe tener cuidado de no crear el objeto si se alcanzó el máximo

	public abstract boolean CreaObjetoAlarma(String MensajeAlarma);
	
	public abstract boolean CreaObjetoTema();
	
	public abstract boolean CreaObjetoPosicion();
	
	public abstract void Show();
	
	protected abstract void InformarErrorAUsuario(String Clase, String Metodo, int Error, String MensajeError);
}

