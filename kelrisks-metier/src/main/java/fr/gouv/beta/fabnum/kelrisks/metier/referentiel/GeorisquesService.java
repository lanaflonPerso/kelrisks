package fr.gouv.beta.fabnum.kelrisks.metier.referentiel;

import fr.gouv.beta.fabnum.kelrisks.metier.referentiel.interfaces.IGeorisquesService;
import fr.gouv.beta.fabnum.kelrisks.transverse.apiclient.GeorisquePaginatedRadon;
import fr.gouv.beta.fabnum.kelrisks.transverse.apiclient.GeorisquePaginatedSismique;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service("georisquesService")
public class GeorisquesService implements IGeorisquesService {
    
    private static final String GEORISQUE_BASE_URL = "http://www.georisques.gouv.fr/api/v1";
    private static final String RADON_URL          = GEORISQUE_BASE_URL + "/radon?code_insee=PARAM_INSEE&page=1&page_size=10";
    private static final String SISMIQUE_URL       = GEORISQUE_BASE_URL + "/zonage_sismique?&code_insee=PARAM_INSEE&page=1&page_size=10";
    
    public GeorisquePaginatedRadon rechercherRadonCommune(String codeINSEE) {
        
        String uri = RADON_URL.replace("PARAM_INSEE", codeINSEE);
        
        WebClient webClient = WebClient.create();
        
        return webClient.get()
                       .uri(uri)
                       .accept(MediaType.APPLICATION_JSON)
                       .exchange()
                       .flatMap(clientResponse -> clientResponse.bodyToMono(GeorisquePaginatedRadon.class))
                       .block(Duration.ofSeconds(10L));
    }
    
    public GeorisquePaginatedSismique rechercherSismiciteCommune(String codeINSEE) {
        
        String uri = SISMIQUE_URL.replace("PARAM_INSEE", codeINSEE);
        
        WebClient webClient = WebClient.create();
        
        return webClient.get()
                       .uri(uri)
                       .accept(MediaType.APPLICATION_JSON)
                       .exchange()
                       .flatMap(clientResponse -> clientResponse.bodyToMono(GeorisquePaginatedSismique.class))
                       .block(Duration.ofSeconds(10L));
    }
}
