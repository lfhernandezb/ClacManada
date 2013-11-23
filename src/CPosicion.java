import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;


public abstract class CPosicion extends CObjetoServidor{

	protected Date m_FechaUltimaPosicion;
	protected Date m_FechaUltimoMensajePosicion;
	
	// Manejador del Timer para mantener la conexión
	private Timer timer;
	private TimerTask timertask;
	private int m_secIntervaloEnvioPosicion; 
	protected String m_UltimoMensajeEnviado;
	protected String m_UltimoMensajeRecibido;

		
	public CPosicion(String Nombre, String Descripcion,
			CDatosServicio DatosServicio, int secIntervaloEnvioPosicion, CEvento ManejadorEvento) {
		super(Nombre, Descripcion, DatosServicio, ManejadorEvento);
		Date Ahora = new Date();
		
		m_FechaUltimaPosicion=new Date(Ahora.getTime()-24*60*60*1000);
		m_FechaUltimoMensajePosicion =new Date(Ahora.getTime()-24*60*60*1000);
		
		m_secIntervaloEnvioPosicion = secIntervaloEnvioPosicion;
		m_UltimoMensajeEnviado=null;
		m_UltimoMensajeRecibido = null;
		schedule_task(10000); // Timer de 10 segundos
	}
	
	private  void schedule_task(long MiliSegundosTimer) {
		timer = new Timer();
		timertask = new TimerTask(){

			@Override
			public void run() {
				// ATENCION DEL TIMER
				AtiendeTimer();
			}
		};
		
		timer.scheduleAtFixedRate(timertask, 0, MiliSegundosTimer); //Tarea que se ejecuta cada 1 minuto
	}
	
	private void AtiendeTimer(){
		//System.out.println("CPosicion : AtiendeTimer");
		Date Ahora = new Date();
		if (m_FechaUltimaPosicion.getTime() + 25000 < Ahora.getTime()){ //Cada 30 segundos actualiza posición
			if ( ActualizaPosicion() ){
				m_FechaUltimaPosicion = Ahora;
				if ( this.m_ManejadorEvento != null ){
					m_ManejadorEvento.NotificaEvento("POSICION", "POSICION_ACTUALIZADA_LOCALMENTE", String.valueOf(this.m_ID));
				}
			}
		}
		if ( (m_FechaUltimoMensajePosicion.getTime() + m_secIntervaloEnvioPosicion*1000) < Ahora.getTime()){
			if ( EnviaMensajePosicion() ){
				m_FechaUltimoMensajePosicion =  Ahora;
			}
		}
	}

	@Override
	public void NotificaEvento(String Origen, String Evento, String Message) {
		// TODO Auto-generated method stub
		this.m_UltimoMensajeRecibido = Message;
		System.out.println("CPOSICION:NOtificaEvento - " + Origen + ":"+Evento+ ":" + Message);
		if ( this.m_ManejadorEvento != null ){
			if ( Message != null ){
				PosicionActualizadaEnServidor();
				m_ManejadorEvento.NotificaEvento("POSICION", "POSICION_ACTUALIZADA_SERVIDOR", String.valueOf(this.m_ID));
			}
			else{
				m_ManejadorEvento.NotificaEvento("POSICION", "POSICION_NO_ACTUALIZADA_SERVIDOR", String.valueOf(this.m_ID));
			}
		}
	}

	private boolean EnviaMensajePosicion() {
		String strAux;
		strAux = GetMensajePosicion();
		if ( strAux != null ){
			if ( m_Servidor.SendMessage(m_DatosServicio.PosicionServer.GetServer(), m_DatosServicio.PosicionServer.GetPort(), strAux, 10000) ){
				m_UltimoMensajeEnviado=strAux;
				return true;
			}
			m_UltimoMensajeEnviado=null;
		}
		return false;
	}

	private String GetMensajePosicion() {
		// TODO Auto-generated method stub
		JSONObject jsonAux1 =new JSONObject();
		JSONObject jsonAux2=new JSONObject();
		Date Ahora = new Date();
		
		try{
			jsonAux1.put("token", String.valueOf(this.m_ID));
			jsonAux1.put("id_usuario",this.m_DatosServicio.getIdUsuario());
			jsonAux1.put("latitud",this.m_DatosServicio.m_posicion.GetLatitud());
			jsonAux1.put("longitud",this.m_DatosServicio.m_posicion.GetLongitud());
			jsonAux1.put("fecha",this.GetStringFechaHora(Ahora)); // CORREGIR ESTO PARA SACAR LA FECHA DEL SISTEMA
			//jsonAux1.put("fecha","10-02-2013 12:00:00"); // CORREGIR ESTO PARA SACAR LA FECHA DEL SISTEMA
				 
			jsonAux2.put("tipo","MSG_POSICION");
			
			jsonAux2.put("data",jsonAux1);
			
		}catch(JSONException e){
			InformarErrorAUsuario("CPosicion", "GetMensajePosicion", 1, "No se puso crear el mensaje de posición");
			return null;
		}
		
		return jsonAux2.toString();

	}
	
	protected abstract boolean ActualizaPosicion();

	@Override
	public boolean IsMensajeRespondido() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean IsMensajeObsoleto() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean IsObjetoObsoleto() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void Show() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void Do() {
		// TODO Auto-generated method stub		
	}
	protected abstract void PosicionActualizadaEnServidor(); //Debe ser reescrita si se desea hacer algo una vez que la posicion se actualizó en servidor
}
