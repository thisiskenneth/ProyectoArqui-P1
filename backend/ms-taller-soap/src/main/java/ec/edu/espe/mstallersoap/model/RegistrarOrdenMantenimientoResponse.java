package ec.edu.espe.mstallersoap.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "codigoOrden", "fechaIngreso", "mensaje" })
@XmlRootElement(name = "RegistrarOrdenMantenimientoResponse", namespace = "http://espe.edu.ec/mstallersoap")
public class RegistrarOrdenMantenimientoResponse {
    @XmlElement(required = true, namespace = "http://espe.edu.ec/mstallersoap")
    protected String codigoOrden;
    @XmlElement(required = true, namespace = "http://espe.edu.ec/mstallersoap")
    protected String fechaIngreso;
    @XmlElement(required = true, namespace = "http://espe.edu.ec/mstallersoap")
    protected String mensaje;

    public String getCodigoOrden() { return codigoOrden; }
    public void setCodigoOrden(String value) { this.codigoOrden = value; }
    public String getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(String value) { this.fechaIngreso = value; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String value) { this.mensaje = value; }
}
