
public class CTestPosicion extends CPosicion{

	public CTestPosicion(String Nombre, String Descripcion,
			CDatosServicio DatosServicio, int secIntervaloEnvioPosicion, CEvento ManejadorEvento) {
		super(Nombre, Descripcion, DatosServicio, secIntervaloEnvioPosicion, ManejadorEvento);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean ActualizaPosicion() {
		// TODO Auto-generated method stub
		this.m_DatosServicio.m_posicion.SetPosicion("-33.453518", "-70.609959");
		//this.InformarErrorAUsuario("CTestPosicion", "ActualizaPosicion", 0, "Posición Actualizada");
		return true;
	}

	@Override
	public void Show() {
		// TODO Auto-generated method stub
		super.Show();
		this.InformarErrorAUsuario("CTestPosicion", "Show", 0, "ID=" + String.valueOf(this.getID()));
		this.InformarErrorAUsuario("CTestPosicion", "Show", 0, "Latitud=" + this.m_DatosServicio.m_posicion.GetLatitud());
		this.InformarErrorAUsuario("CTestPosicion", "Show", 0, "Longitud=" + this.m_DatosServicio.m_posicion.GetLongitud());
		this.InformarErrorAUsuario("CTestPosicion", "Show", 0, "Ultimo Mensaje Enviado" + this.m_UltimoMensajeEnviado);
		this.InformarErrorAUsuario("CTestPosicion", "Show", 0, "Ultimo Mensaje Recibido" + this.m_UltimoMensajeRecibido);
	}

	@Override
	public void Do() {
		// TODO Auto-generated method stub
		super.Do();
	}

	@Override
	public boolean HayAccionesPendientes() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void PosicionActualizadaEnServidor() {
		// TODO Auto-generated method stub
		
	}

}
