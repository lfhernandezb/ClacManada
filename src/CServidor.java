

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class CServidor{

	protected String m_Servidor;
	protected int m_Port;
	private Socket m_Socket;
	private boolean m_RespuestaPendiente;

	// Manejador del Timer para mantener la conexión
	private Timer timerServidor;
	private TimerTask timertaskServidor;
	private Date m_FechaTimeOut;
	private CEvento m_Evento;

	public CServidor( CEvento Evento ){
		m_Servidor="";
		m_Port = 0;
		m_Evento = Evento;
		m_Socket = new Socket();
		m_RespuestaPendiente=false;
		m_FechaTimeOut = new Date();
	}
	
	public boolean SendMessage(String Servidor, int Port, String Message, int TimeOutRespuesta){

		m_Servidor=Servidor;
		m_Port = Port;
		
		if ( !Conectar() ){
			return false;
		}
		if ( !EscribirMensaje(Message) ){
			return false;
		}
		Date Ahora = new Date();
		m_FechaTimeOut.setTime(Ahora.getTime() + TimeOutRespuesta );
		
		m_RespuestaPendiente=true;
		schedule_task(100);
		return true;
	}
	
	private boolean Conectar() {
		m_Socket = new Socket();
		//System.out.println("CServidor: Conectar: Datos Servidor:" + m_Servidor + ":"+String.valueOf(m_Port));
		try {
			m_Socket.connect(new InetSocketAddress(m_Servidor, m_Port), 15000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return m_Socket.isConnected();
	}

	private boolean EscribirMensaje(String message) {
		OutputStreamWriter outputstreamAux;
		if ( !m_Socket.isConnected() ){
			//System.out.println("SOCKET NO CONECTADO");
			return false;
		}
		
		try {
			outputstreamAux =new OutputStreamWriter(m_Socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			//System.out.println("Error:" + e.toString());
			return false;
		}
		
		try {
			outputstreamAux.write(message + "\r\n");
			outputstreamAux.flush();
		} catch (IOException e) {
			e.printStackTrace();
			//System.out.println("Error:" + e.toString());
			return false;
		}
		
		//System.out.println("Mensaje Enviado:" + message + "\r\n");
		return true;
	}
	
	private  void schedule_task(long MiliSegundosTimer) {
			timerServidor = new Timer();
			timertaskServidor = new TimerTask(){
	
				@Override
				public void run() {
					timerServidor.cancel();
					String strMessage = LeerMensaje();
					Date Ahora = new Date();
					//System.out.println("TIMER");
					if ( (strMessage != null) || (m_FechaTimeOut.getTime() < Ahora.getTime()) ){
						m_RespuestaPendiente=false;
						m_Evento.NotificaEvento("SERVER", "READ", strMessage);
						Cerrar();
						return;	
					}
					
					schedule_task(500);
				}

			};
			
			//timerServidor.scheduleAtFixedRate(timertaskServidor, MiliSegundosTimer, MiliSegundosTimer);
			timerServidor.schedule(timertaskServidor, MiliSegundosTimer);
	}

	private void Cerrar() {
		try {
			m_Socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String LeerMensaje() {
		BufferedReader Buffer;
		String strAux=null;
		//System.out.println("VOY A LEER");
		try {
			Buffer=new BufferedReader(new InputStreamReader(m_Socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		boolean HayDatos = false;
		try {
			HayDatos = Buffer.ready();
		} catch (IOException e) {
			e.printStackTrace();
			//System.out.println("Error: " + e.toString());
			return null;
		}
		
		if ( !HayDatos ) {
			return null;
		}

		//System.out.println("ENCONTRE DATOS");
		
		try {
			strAux = Buffer.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			//System.out.println("error:" + e.toString());
			return null;
		}
		//System.out.println("strAux");
		strAux=strAux.replace("&quot;", "\"");
		return strAux;
	}
	
	public String getServidor() {
		return m_Servidor;
	}

	public int getPort() {
		return m_Port;
	}
	
	public boolean IsRespuestaPendiente(){
		return m_RespuestaPendiente;
	}
}
