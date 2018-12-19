package fr.gouv.beta.fabnum.kelrisks.facade.frontoffice.avis.impl;

import fr.gouv.beta.fabnum.commun.facade.AbstractFacade;
import fr.gouv.beta.fabnum.kelrisks.facade.avis.AvisDTO;
import fr.gouv.beta.fabnum.kelrisks.facade.dto.referentiel.ParcelleDTO;
import fr.gouv.beta.fabnum.kelrisks.facade.dto.referentiel.SiteSolPolueDTO;
import fr.gouv.beta.fabnum.kelrisks.facade.frontoffice.avis.IGestionAvisFacade;
import fr.gouv.beta.fabnum.kelrisks.facade.frontoffice.referentiel.IGestionAdresseFacade;
import fr.gouv.beta.fabnum.kelrisks.facade.frontoffice.referentiel.IGestionInstallationClasseeFacade;
import fr.gouv.beta.fabnum.kelrisks.facade.frontoffice.referentiel.IGestionParcelleFacade;
import fr.gouv.beta.fabnum.kelrisks.facade.frontoffice.referentiel.IGestionSiteIndustrielBasiasFacade;
import fr.gouv.beta.fabnum.kelrisks.facade.frontoffice.referentiel.IGestionSiteIndustrielBasolFacade;
import fr.gouv.beta.fabnum.kelrisks.facade.frontoffice.referentiel.IGestionSiteSolPolueFacade;
import fr.gouv.beta.fabnum.kelrisks.transverse.referentiel.qo.ParcelleQO;

import java.util.List;

import org.geolatte.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GestionAvisFacade extends AbstractFacade implements IGestionAvisFacade {
    
    @Autowired
    IGestionSiteSolPolueFacade         gestionSiteSolPolueFacade;
    @Autowired
    IGestionSiteIndustrielBasiasFacade gestionSiteIndustrielBasiasFacade;
    @Autowired
    IGestionSiteIndustrielBasolFacade  gestionSiteIndustrielBasolFacade;
    @Autowired
    IGestionInstallationClasseeFacade  gestionInstallationClasseeFacade;
    @Autowired
    IGestionAdresseFacade              gestionAdresseFacade;
    @Autowired
    IGestionParcelleFacade             gestionParcelleFacade;
    
    @Override
    public AvisDTO rendreAvis(String codeParcelle, String codeINSEE, String rue, String idBAN, String nomProprietaire) {
        
        AvisDTO avisDTO = new AvisDTO();
        
        // Recherche d'une parcelle à partir de l'adresse si aucune n'a été fournie
        ParcelleDTO parcelleDTO;
        if (codeParcelle == null || codeParcelle.equals("")) {
            parcelleDTO = gestionParcelleFacade.rechercherParcelleAvecIdBan(idBAN);
            codeParcelle = parcelleDTO.getCode();
        }
        else {
            ParcelleQO parcelleQO = new ParcelleQO();
            parcelleQO.setCode(codeParcelle);
            List<ParcelleDTO> parcelleDTOS = gestionParcelleFacade.rechercherAvecCritere(parcelleQO);
        
            if (parcelleDTOS.isEmpty()) {return null;} //TODO
            if (parcelleDTOS.size() > 1) {return null;} // TODO
            parcelleDTO = parcelleDTOS.get(0);
        }
        
        // Recherche d'une éventuelle zone poluée contenant la parcelle
        Geometry        geometry;
        SiteSolPolueDTO siteSolPolueDTO = gestionSiteSolPolueFacade.rechercherZoneContenantParcelle(codeParcelle);
        if (siteSolPolueDTO != null) { geometry = siteSolPolueDTO.getMultiPolygon(); }
        else { geometry = parcelleDTO.getMultiPolygon(); }
        
        avisDTO.setSiteIndustrielBasiasSurParcelleDTOs(gestionSiteIndustrielBasiasFacade.rechercherSitesDansPolygon(geometry));
        avisDTO.setSiteIndustrielBasiasAutourParcelleDTOs(gestionSiteIndustrielBasiasFacade.rechercherSiteDansRayonCentroideParcelle(codeParcelle, 100D));
        if (!nomProprietaire.equals("")) {
            avisDTO.setSiteIndustrielBasiasParRaisonSocialeDTOs(gestionSiteIndustrielBasiasFacade.rechercherParNomProprietaireDansRayonGeometry(geometry, nomProprietaire, 200D));
        }
        avisDTO.getSiteIndustrielBasiasAutourParcelleDTOs().removeAll(avisDTO.getSiteIndustrielBasiasSurParcelleDTOs());
        avisDTO.getSiteIndustrielBasiasParRaisonSocialeDTOs().removeAll(avisDTO.getSiteIndustrielBasiasSurParcelleDTOs());
        avisDTO.getSiteIndustrielBasiasParRaisonSocialeDTOs().removeAll(avisDTO.getSiteIndustrielBasiasAutourParcelleDTOs());
        
        avisDTO.setSiteIndustrielBasolSurParcelleDTOs(gestionSiteIndustrielBasolFacade.rechercherSitesDansPolygon(geometry));
        avisDTO.setSiteIndustrielBasolAutourParcelleDTOs(gestionSiteIndustrielBasolFacade.rechercherSiteDansRayonCentroideParcelle(codeParcelle, 100D));
        avisDTO.getSiteIndustrielBasolAutourParcelleDTOs().removeAll(avisDTO.getSiteIndustrielBasolSurParcelleDTOs());
        
        avisDTO.setInstallationClasseeSurParcelleDTOs(gestionInstallationClasseeFacade.rechercherInstallationsDansPolygon(geometry));
        avisDTO.setInstallationClasseeAutourParcelleDTOs(gestionInstallationClasseeFacade.rechercherInstallationsDansRayonCentroideParcelle(codeParcelle, 100D));
        avisDTO.getInstallationClasseeAutourParcelleDTOs().removeAll(avisDTO.getInstallationClasseeSurParcelleDTOs());
        avisDTO.setInstallationClasseeNonGeorerenceesDTOs(gestionInstallationClasseeFacade.rechercherInstallationsAuCentroideCommune(codeINSEE));
        
        return avisDTO;
    }
}
