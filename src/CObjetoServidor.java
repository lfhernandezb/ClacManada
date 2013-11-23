import java.util.Calendar;
import java.util.Date;

public abstract class CObjetoServidor extends CEvento{

	/*
	 * VARIABLES QUE DESCRIBEN EL OBJETO
	 */
	protected long m_ID;
	protected String m_NombreObjeto;
	protected String m_Descripcion;
	protected CDatosServicio m_DatosServicio;

	/*
	 * Variables que mantienen información de Creación y de los últimos mensajes
	 */
	protected String m_MensajeSalida="";
	private Date m_FechaCreacion;
	private Date m_FechaUltimoMensajeOut;
	
	/*
	 * VARIABLE DE RED
	 */
	protected CServidor m_Servidor;
	
	protected CEvento m_ManejadorEvento;
	
	public CObjetoServidor(String Nombre, String Descripcion, CDatosServicio DatosServicio, CEvento ManejadorEvento){
		m_ID = DatosServicio.getNumeroUnico();
		m_NombreObjeto=Nombre;
		m_Descripcion=Descripcion;
		m_DatosServicio=DatosServicio;
		m_ManejadorEvento = ManejadorEvento;
		m_FechaCreacion = new Date();
		m_FechaUltimoMensajeOut = new Date();
		m_Servidor = new CServidor(this);
	}
	
	public abstract boolean IsMensajeRespondido(); // Indica si existen mensajes pendientes de respuesta por parte del servidor
	
	public abstract boolean IsMensajeObsoleto(); // Indica si el objeto está obsoleto
	
	public abstract boolean IsObjetoObsoleto(); // Indica si el objeto debe ser eliminado de la lista
	
	public abstract boolean HayAccionesPendientes();

	public abstract void Show();
	
	public abstract void Do(); // El objeto será llamado para que ejecute lo que estime conveniente 
	
	@SuppressWarnings("deprecation")
	protected void InformarErrorAUsuario(String Clase, String Metodo, int Error, String MensajeError){
		Date Ahora = new Date();
		String strAux = "";
		strAux = strAux +String.valueOf(Ahora.getHours()) + ":";
		strAux = strAux +String.valueOf(Ahora.getMinutes()) + ":";
		strAux = strAux +String.valueOf(Ahora.getSeconds());
		System.out.println(strAux + "-" + Clase + ":" + Metodo + ":" + MensajeError);
	}

	@SuppressWarnings("deprecation")
	protected String GetStringFechaHora(Date Fecha){
		
		Calendar cal = Calendar.getInstance();
		
		
		String strFecha = "";
		strFecha= String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + "-";
		strFecha= strFecha + String.valueOf(cal.get(Calendar.MONTH)+1) + "-";
		strFecha= strFecha + String.valueOf(cal.get(Calendar.YEAR)) + " ";
		strFecha= strFecha + String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + ":";
		strFecha= strFecha + String.valueOf(cal.get(Calendar.MINUTE)) + ":";
		strFecha= strFecha + String.valueOf(cal.get(Calendar.SECOND));
		
		return strFecha;
	}
	public long getID() {
		return m_ID;
	}

	public String getNombreObjeto() {
		return m_NombreObjeto;
	}

	public String getDescripcion() {
		return m_Descripcion;
	}

	public CDatosServicio getDatosServicio() {
		return m_DatosServicio;
	}
	
	public String getMensajeSalida() {
		return m_MensajeSalida;
	}

	public Date getFechaCreacion() {
		return m_FechaCreacion;
	}

	public Date getFechaUltimoMensajeOut() {
		return m_FechaUltimoMensajeOut;
	}	

}
