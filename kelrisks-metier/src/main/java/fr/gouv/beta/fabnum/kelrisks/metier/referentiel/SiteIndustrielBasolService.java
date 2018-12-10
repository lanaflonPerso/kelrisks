package fr.gouv.beta.fabnum.kelrisks.metier.referentiel;

import fr.gouv.beta.fabnum.commun.metier.impl.AbstractCRUDService;
import fr.gouv.beta.fabnum.kelrisks.metier.referentiel.interfaces.ISiteIndustrielBasolService;
import fr.gouv.beta.fabnum.kelrisks.persistance.referentiel.ISiteIndustrielBasolDAO;
import fr.gouv.beta.fabnum.kelrisks.persistance.referentiel.impl.SiteIndustrielBasolDAO;
import fr.gouv.beta.fabnum.kelrisks.transverse.referentiel.entities.SiteIndustrielBasol;

import java.util.List;

import org.geolatte.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Service métier de gestion de l'entité SiteIndustrielBasol
 */

@Service("siteIndustrielBasolService")
public class SiteIndustrielBasolService extends AbstractCRUDService<SiteIndustrielBasol> implements ISiteIndustrielBasolService {
    
    private static final long serialVersionUID = 1L;
    
    /*
     * Le dao spécifique à ce service
     */
    private ISiteIndustrielBasolDAO dao;
    
    @Autowired
    public SiteIndustrielBasolService(@Qualifier("siteIndustrielBasolDAO") final SiteIndustrielBasolDAO fdao) {
        
        this.setFdao(fdao);
        dao = fdao;
    }
    
    @Override
    public List<SiteIndustrielBasol> rechercherSiteSurParcelle(String codeParcelle) {
        
        return dao.rechercherSiteSurParcelle(codeParcelle);
    }
    
    @Override
    public List<SiteIndustrielBasol> rechercherSiteDansRayonCentroideParcelle(String codeParcelle, Double distance) {
        
        return dao.rechercherSiteDansRayonCentroideParcelle(codeParcelle, distance);
    }
    
    @Override
    public List<SiteIndustrielBasol> rechercherSitesDansPolygon(Geometry multiPolygon) {
        
        return dao.rechercherSitesDansPolygon(multiPolygon);
    }
}
  