package ec.edu.espe.mstallersoap.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "matricula" })
@XmlRootElement(name = "ConsultarVehiculoRequest", namespace = "http://espe.edu.ec/mstallersoap")
public class ConsultarVehiculoRequest {
    @XmlElement(required = true, namespace = "http://espe.edu.ec/mstallersoap")
    protected String matricula;
    public String getMatricula() { return matricula; }
    public void setMatricula(String value) { this.matricula = value; }
}
