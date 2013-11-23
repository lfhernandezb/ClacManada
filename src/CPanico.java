import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;


public abstract class CPanico extends CObjetoServidor{

	protected int m_EstadoPanico; //0: NO se ha enviado pánico; 1:Mensaje enviado; 2: Se completó el ciclo de Pánico
	protected String m_RespuestaAPanico;
	protected String m_MensajePanico;
	Date m_TimeOutMensajePanico;
	
	// Manejador del Timer para asegurar envío de Mensaje a servidor
	private Timer timer;
	private TimerTask timertask;

	public CPanico(String Nombre, String Descripcion,
			CDatosServicio DatosServicio, CEvento ManejadorEvento) {
		
		super(Nombre, Descripcion, DatosServicio, ManejadorEvento);
		
		m_EstadoPanico=0;
		m_RespuestaAPanico="";
		m_MensajePanico = "";
		m_TimeOutMensajePanico = new Date(); 
	}
	
	public void DoPanico(){
		
		SendMSS();
		
		CreaChatPanico();
		
		SendPanicoToServer(10); // El mensaje de Pánico expirará en 10 minutos
		
	}
		
	private void CreaChatPanico() {
		// TODO Auto-generated method stub
		
	}

	private void SendMSS() {
		// TODO Auto-generated method stub
		
	}

	private void SendPanicoToServer(int minsTimeOut) {
		
		Date Ahora = new Date();
		m_MensajePanico = GetMensajePanico("1", "0");
		m_TimeOutMensajePanico.setTime(Ahora.getTime() + minsTimeOut * 60 * 1000);
		
		if ( m_Servidor.SendMessage(this.m_DatosServicio.PanicoServer.GetServer(), this.m_DatosServicio.PanicoServer.GetPort(), m_MensajePanico, 15000) ){
			m_EstadoPanico=1;
		}
		
		schedule_task(60*1000); //El Timer se activará cada un minuto
		
	}

	private  void schedule_task(long MiliSegundosTimer) {
		timer = new Timer();
		timertask = new TimerTask(){

			@Override
			public void run() {
				// ATENCION DEL TIMER
				Date Ahora = new Date();

				if (m_Servidor.IsRespuestaPendiente()) {
					return;
				}
				if ( m_TimeOutMensajePanico.before(Ahora) ) {
					m_EstadoPanico=0;
					timer.cancel();
					if ( m_ManejadorEvento != null ){
						m_ManejadorEvento.NotificaEvento("PANICO", "TIMEOUT", String.valueOf(m_ID));
					}	
					return;
				}

				if ( m_Servidor.SendMessage(m_DatosServicio.PanicoServer.GetServer(), m_DatosServicio.PanicoServer.GetPort(), m_MensajePanico, 15000) ){
					m_EstadoPanico=1;
				}
			}
		};
		
		timer.scheduleAtFixedRate(timertask, MiliSegundosTimer, MiliSegundosTimer); //Tarea que se ejecuta cada 1 minuto
	}

	@Override
	public void NotificaEvento(String Origen, String Evento, String Message) {
		String strEvento = "";
		System.out.println(Origen + ":" + Evento + ":" + Message);
		m_RespuestaAPanico=Message;
		if ( Message == null ) {
			m_EstadoPanico=0;
			strEvento = "RESPUESTA_NO_RECIBIDA";
		}
		else{
			m_EstadoPanico=2;
			timer.cancel();
			strEvento = "RESPUESTA_RECIBIDA";
		}
		if ( m_ManejadorEvento != null ){
			m_ManejadorEvento.NotificaEvento("PANICO", strEvento, String.valueOf(this.m_ID));
		}
	}

	@Override
	public boolean IsObjetoObsoleto() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean HayAccionesPendientes() {
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

	private String GetMensajePanico( String TipoIncidente, String TipoAlarma){
		JSONObject jsonAux1 = new JSONObject();
		JSONObject jsonAux2 = new JSONObject();
		
		try {
			jsonAux1.put("token", String.valueOf(this.m_ID));
			jsonAux1.put("id_usuario", m_DatosServicio.m_IdUsuario);
			jsonAux1.put("latitud",m_DatosServicio.m_posicion.GetLatitud());
			jsonAux1.put("longitud",m_DatosServicio.m_posicion.GetLongitud());
			jsonAux1.put("tipo_incidente",TipoIncidente);
			jsonAux1.put("tipo_alarma",TipoAlarma);
			
			jsonAux2.put("tipo", "MSG_PANICO_ALARMA");
			jsonAux2.put("id_objeto", this.m_ID);
			jsonAux2.put("data", jsonAux1);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			InformarErrorAUsuario("CPANICO", "GetMensajePanico", 1, "Error JSON al crear el mensaje");
			return null;
		}
		
		return jsonAux2.toString();
	}
	
	@Override
	public boolean IsMensajeRespondido() {
		if ( m_EstadoPanico == 2 ){
			return true;
		}
		return false;
	}

	@Override
	public boolean IsMensajeObsoleto() {
		if (m_EstadoPanico == 1){
			Date Ahora = new Date();
			if ( (this.getFechaUltimoMensajeOut().getTime() + 60000) > Ahora.getTime() ){
				return true;
			}
		}
		return false;
	}


}

