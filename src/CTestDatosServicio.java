
public class CTestDatosServicio extends CDatosServicio{

	@Override
	protected void GetConfiguracionServicio() {
		// TODO Auto-generated method stub
		this.m_IdUsuario = "1";
		this.m_IdEquipo = "1";
		this.m_posicion.SetPosicion("0", "0");
		this.PanicoServer.SetServer("servidor1.manadachile.cl", 6000);
		this.PosicionServer.SetServer("servidor1.manadachile.cl", 6000);
		this.AlarmaServer.SetServer("servidor1.manadachile.cl", 6000);
		this.TemaServer.SetServer("servidor1.manadachile.cl", 6000);
	}

}
