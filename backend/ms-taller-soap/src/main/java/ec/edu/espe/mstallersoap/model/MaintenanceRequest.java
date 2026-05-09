package ec.edu.espe.mstallersoap.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "vehiclePlate",
    "description",
    "priority"
})
@XmlRootElement(name = "MaintenanceRequest", namespace = "http://espe.edu.ec/mstallersoap")
public class MaintenanceRequest {

    @XmlElement(required = true, namespace = "http://espe.edu.ec/mstallersoap")
    protected String vehiclePlate;
    @XmlElement(required = true, namespace = "http://espe.edu.ec/mstallersoap")
    protected String description;
    @XmlElement(required = true, namespace = "http://espe.edu.ec/mstallersoap")
    protected String priority;

    public String getVehiclePlate() { return vehiclePlate; }
    public void setVehiclePlate(String value) { this.vehiclePlate = value; }
    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }
    public String getPriority() { return priority; }
    public void setPriority(String value) { this.priority = value; }
}
