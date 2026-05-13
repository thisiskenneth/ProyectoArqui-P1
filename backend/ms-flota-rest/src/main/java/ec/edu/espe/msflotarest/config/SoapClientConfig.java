package ec.edu.espe.msflotarest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

@Configuration
public class SoapClientConfig {

    @Bean
    public Jaxb2Marshaller maintenanceMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("ec.edu.espe.msflotarest.soap.model");
        return marshaller;
    }

    @Bean
    public WebServiceTemplate maintenanceWebServiceTemplate(
            Jaxb2Marshaller maintenanceMarshaller,
            @Value("${taller.soap.url}") String defaultUri) {
        WebServiceTemplate template = new WebServiceTemplate();
        template.setMarshaller(maintenanceMarshaller);
        template.setUnmarshaller(maintenanceMarshaller);
        template.setDefaultUri(defaultUri);
        return template;
    }
}
