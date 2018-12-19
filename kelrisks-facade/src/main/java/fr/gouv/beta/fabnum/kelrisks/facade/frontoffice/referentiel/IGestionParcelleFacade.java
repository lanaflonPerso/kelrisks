package fr.gouv.beta.fabnum.kelrisks.facade.frontoffice.referentiel;

import fr.gouv.beta.fabnum.kelrisks.facade.dto.referentiel.ParcelleDTO;
import fr.gouv.beta.fabnum.kelrisks.transverse.referentiel.qo.ParcelleQO;

import java.util.List;

public interface IGestionParcelleFacade {
    
    ParcelleDTO rechercherParcelleAvecAdresse(String commune, String rue, String numero);
    
    ParcelleDTO rechercherResultatUniqueAvecCritere(ParcelleQO parcelleQO);
    
    List<ParcelleDTO> rechercherAvecCritere(ParcelleQO parcelleQO);
    
    ParcelleDTO rechercherParcelleAvecIdBan(String idBAN);
}