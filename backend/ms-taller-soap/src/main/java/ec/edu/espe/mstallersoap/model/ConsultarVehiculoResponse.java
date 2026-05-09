package ec.edu.espe.mstallersoap.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "matricula", "estado", "ultimoMantenimiento", "observaciones" })
@XmlRootElement(name = "ConsultarVehiculoResponse", namespace = "http://espe.edu.ec/mstallersoap")
public class ConsultarVehiculoResponse {
    @XmlElement(required = true, namespace = "http://espe.edu.ec/mstallersoap")
    protected String matricula;
    @XmlElement(required = true, namespace = "http://espe.edu.ec/mstallersoap")
    protected String estado;
    @XmlElement(required = true, namespace = "http://espe.edu.ec/mstallersoap")
    protected String ultimoMantenimiento;
    @XmlElement(required = true, namespace = "http://espe.edu.ec/mstallersoap")
    protected String observaciones;

    public String getMatricula() { return matricula; }
    public void setMatricula(String value) { this.matricula = value; }
    public String getEstado() { return estado; }
    public void setEstado(String value) { this.estado = value; }
    public String getUltimoMantenimiento() { return ultimoMantenimiento; }
    public void setUltimoMantenimiento(String value) { this.ultimoMantenimiento = value; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String value) { this.observaciones = value; }
}
