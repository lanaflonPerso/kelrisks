package fr.gouv.beta.fabnum.kelrisks.facade.frontoffice.referentiel;

import fr.gouv.beta.fabnum.kelrisks.facade.dto.referentiel.SiteIndustrielBasolDTO;

import java.util.List;

import org.geolatte.geom.Geometry;

public interface IGestionSiteIndustrielBasolFacade {
    
    List<SiteIndustrielBasolDTO> rechercherSitesSurParcelle(String codeParcelle);
    
    List<SiteIndustrielBasolDTO> rechercherSiteDansRayonCentroideParcelle(String codeParcelle, Double distance);
    
    List<SiteIndustrielBasolDTO> rechercherSitesDansPolygon(Geometry multiPolygon);
}