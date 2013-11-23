public abstract class CDatosServicio {

	protected CPosicion m_posicion;
	protected CDatosServidor PanicoServer;
	protected CDatosServidor AlarmaServer;
	protected CDatosServidor PosicionServer;
	protected CDatosServidor TemaServer;
	protected CDatosServidor ConfiguracionServer;
	
	protected String m_IdEquipo="";
	protected String m_IdUsuario="";
	protected long m_NumeroUnico=0;
	

	public CDatosServicio(){
		m_posicion = new CPosicion();
		PanicoServer = new CDatosServidor("PANICO_SERVER");
		AlarmaServer = new CDatosServidor("ALARMA_SERVER");
		PosicionServer = new CDatosServidor("POSICION_SERVER");
		TemaServer = new CDatosServidor("TEMA_SERVER");
		ConfiguracionServer = new CDatosServidor("CONFIGURACION_SERVER");
		GetConfiguracionServicio();
	}

	protected abstract void GetConfiguracionServicio();
	
	public String getIdEquipo() {
		return m_IdEquipo;
	}
	
	public String getIdUsuario() {
		return m_IdUsuario;
	}

	public long getNumeroUnico() {
		m_NumeroUnico++;
		return m_NumeroUnico;
	}
	
	public class CPosicion{
		private String m_Longitud;
		private String m_Latitud;
		
		public CPosicion(){
			m_Longitud="";
			m_Latitud="";
		}
		public String GetLatitud(){
			return m_Latitud; 
		}
		public String GetLongitud(){
			return m_Longitud; 
		}	
		public void SetPosicion(String Latitud, String Longitud){
			m_Longitud=Longitud;
			m_Latitud=Latitud;
		}
	}

	public class CDatosServidor{
		private String m_Server;
		private int m_Port;
		private String m_NombreServidor;
		
		public CDatosServidor(String NombreServidor){
			m_Server = "";
			m_Port= 0;
			m_NombreServidor=NombreServidor;
		}
		public String GetServer(){
			return m_Server;
		}
		public int GetPort(){
			return m_Port; 
		}
		public String GetNombreServidor(){
			return m_NombreServidor;
		}
		public void SetServer(String Server, int Port){
			m_Server=Server;
			m_Port =Port;
		}
	}
}
