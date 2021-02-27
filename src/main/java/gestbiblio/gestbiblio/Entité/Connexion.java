/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestbiblio.gestbiblio.Entité;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author hp
 */
@Entity
@Table(name = "connexion")
@NamedQueries({
    @NamedQuery(name = "Connexion.findAll", query = "SELECT c FROM Connexion c"),
    @NamedQuery(name = "Connexion.findByIdLog", query = "SELECT c FROM Connexion c WHERE c.idLog = :idLog"),
    @NamedQuery(name = "Connexion.findByUtilisateur", query = "SELECT c FROM Connexion c WHERE c.utilisateur = :utilisateur"),
    @NamedQuery(name = "Connexion.findByLogin", query = "SELECT c FROM Connexion c WHERE c.login = :login"),
    @NamedQuery(name = "Connexion.findByMotpass", query = "SELECT c FROM Connexion c WHERE c.motpass = :motpass")})
public class Connexion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idLog")
    private Integer idLog;
    @Column(name = "utilisateur")
    private String utilisateur;
    @Column(name = "login")
    private String login;
    @Column(name = "motpass")
    private String motpass;

    public Connexion() {
    }

    public Connexion(Integer idLog) {
        this.idLog = idLog;
    }

    public Integer getIdLog() {
        return idLog;
    }

    public void setIdLog(Integer idLog) {
        this.idLog = idLog;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMotpass() {
        return motpass;
    }

    public void setMotpass(String motpass) {
        this.motpass = motpass;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLog != null ? idLog.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Connexion)) {
            return false;
        }
        Connexion other = (Connexion) object;
        if ((this.idLog == null && other.idLog != null) || (this.idLog != null && !this.idLog.equals(other.idLog))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gestbiblio.gestbiblio.Entit\u00e9.Connexion[ idLog=" + idLog + " ]";
    }
    
}
